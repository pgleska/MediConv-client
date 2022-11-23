package com.github.pgleska.ui.register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    }

    private void initListeners() {
        ButtonRegister.setOnClickListener(v -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(emailET.getText().toString());
            userDTO.setPassword(passwordET.getText().toString());
            userDTO.setName(nameET.getText().toString());
            userDTO.setRole("USER");
            Call<ResponseDTO<UserDTO>> call = userInterface.registerUser(userDTO);
            call.enqueue(new Callback<ResponseDTO<UserDTO>>() {
                @Override
                public void onResponse(Call<ResponseDTO<UserDTO>> call, Response<ResponseDTO<UserDTO>> response) {
                    if(response.isSuccessful()) {
                        genKeysAndLogin(emailET.getText().toString(), passwordET.getText().toString());
                        emailET.clearComposingText();
                        passwordET.clearComposingText();
                        nameET.clearComposingText();

                    } else {
                        Toast.makeText(getContext(), "Incorrect data", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseDTO<UserDTO>> call, Throwable t) {

                }
            });
        });
    }

    private void genKeysAndLogin(String email, String password) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            viewModel.setPrivateKey(privateKey);
            String publicKeyEncoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            CredentialsDTO credentialsDTO = new CredentialsDTO();
            credentialsDTO.setEmail(email);
            credentialsDTO.setPassword(password);
            Call<TokenDTO> call = userInterface.login(credentialsDTO);
            call.enqueue(new Callback<TokenDTO>() {
                @Override
                public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {
                    if(response.isSuccessful()) {
                        viewModel.setToken(response.body().getToken());
                        sendPublicKey(publicKeyEncoded);
                        Navigation.findNavController(root).navigate(R.id.action_nav_register_to_nav_conversations);
                    }
                }

                @Override
                public void onFailure(Call<TokenDTO> call, Throwable t) {

                }
            });

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void sendPublicKey(String publicKey) {
        UserDTO userDTO = new UserDTO();
        userDTO.setPublicKey(publicKey);
        Call<ResponseDTO<UserDTO>> call = userInterface.sendPublicKey(viewModel.getToken(), userDTO);
        call.enqueue(new Callback<ResponseDTO<UserDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<UserDTO>> call, Response<ResponseDTO<UserDTO>> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<UserDTO>> call, Throwable t) {

            }
        });
    }
}

