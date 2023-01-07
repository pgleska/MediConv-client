package com.github.pgleska.ui.register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.github.pgleska.R;
import com.github.pgleska.cryptography.CryptographyUtils;
import com.github.pgleska.databinding.FragmentRegisterBinding;
import com.github.pgleska.dtos.CredentialsDTO;
import com.github.pgleska.dtos.ResponseDTO;
import com.github.pgleska.dtos.TokenDTO;
import com.github.pgleska.dtos.UserDTO;
import com.github.pgleska.retrofit.RetrofitClient;
import com.github.pgleska.retrofit.interfaces.UserInterface;
import com.github.pgleska.ui.viewModels.UniversalViewModel;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    private static final String TAG = RegisterFragment.class.getName();

    private FragmentRegisterBinding binding;
    private View root;
    private Button ButtonRegister;
    private UniversalViewModel viewModel;
    private EditText emailET, passwordET, nameET;
    private UserInterface userInterface;
    private RadioGroup radioGroup;
    private String role = "USER";

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$";
    private  static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    private Matcher matcher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        initComponents();
        initListeners();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initComponents() {
        viewModel = new ViewModelProvider(getActivity()).get(UniversalViewModel.class);
        ButtonRegister = binding.btnRegister;
        emailET = binding.registerEmail;;
        passwordET = binding.registerPassword;
        nameET = binding.registerName;
        userInterface = RetrofitClient.getUserInterface(getString(R.string.server_address));
        radioGroup = binding.radioGroup;
    }

    private void initListeners() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_user:
                    role = "USER";
                    break;
                case R.id.radio_doctor:
                    role = "DOCTOR";
                    break;
            }
        });

        ButtonRegister.setOnClickListener(v -> {
            if (verifyPassword(passwordET.getText().toString())) {
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(emailET.getText().toString());
                userDTO.setPassword(passwordET.getText().toString());
                userDTO.setName(nameET.getText().toString());
                userDTO.setRole(role);
                Call<ResponseDTO<UserDTO>> call = userInterface.registerUser(userDTO);
                call.enqueue(new Callback<ResponseDTO<UserDTO>>() {
                    @Override
                    public void onResponse(Call<ResponseDTO<UserDTO>> call, Response<ResponseDTO<UserDTO>> response) {
                        if (response.isSuccessful()) {
                            CredentialsDTO credentialsDTO = new CredentialsDTO();
                            credentialsDTO.setEmail(emailET.getText().toString());
                            credentialsDTO.setPassword(passwordET.getText().toString());
                            emailET.clearComposingText();
                            passwordET.clearComposingText();
                            nameET.clearComposingText();
                            Call<TokenDTO> call2 = userInterface.login(credentialsDTO);
                            call2.enqueue(new Callback<TokenDTO>() {
                                @Override
                                public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {
                                    if (response.isSuccessful()) {
                                        viewModel.setToken(response.body().getToken());
                                        Navigation.findNavController(root).navigate(R.id.action_nav_register_to_nav_pin);
                                    }
                                }

                                @Override
                                public void onFailure(Call<TokenDTO> call, Throwable t) {

                                }
                            });

                        } else {
                            Toast.makeText(getContext(), "Incorrect email", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDTO<UserDTO>> call, Throwable t) {

                    }
                });
            } else {
                Toast.makeText(getContext(), "Password must contain at least:\n" +
                        "- 8 characters\n" +
                        "- 1 upper case letter\n" +
                        "- 1 lower case letter\n" +
                        "- 1 digit\n" +
                        "- 1 special character", Toast.LENGTH_LONG).show();
                passwordET.setText("");
            }
        });
    }

    private boolean verifyPassword(String password) {
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}

