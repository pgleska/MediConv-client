package com.github.pgleska.ui.login;

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
import com.github.pgleska.databinding.FragmentLoginBinding;
import com.github.pgleska.dtos.CredentialsDTO;
import com.github.pgleska.dtos.ResponseDTO;
import com.github.pgleska.dtos.TokenDTO;
import com.github.pgleska.retrofit.RetrofitClient;
import com.github.pgleska.retrofit.interfaces.UserInterface;
import com.github.pgleska.ui.viewModels.UniversalViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();

    private FragmentLoginBinding binding;
    private View root;
    private Button ButtonRegister;
    private Button ButtonLogin;
    private UniversalViewModel viewModel;
    private EditText emailET;
    private EditText passwordET;
    private UserInterface userInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
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
        emailET = binding.etEmail;;
        passwordET = binding.etPassword;
        ButtonRegister = binding.btnToRegister;
        ButtonLogin = binding.btnLogin;
        userInterface = RetrofitClient.getUserInterface(getString(R.string.server_address));
    }

    private void initListeners() {
        ButtonRegister.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_nav_login_to_nav_register);
        });
        ButtonLogin.setOnClickListener(v -> {
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            CredentialsDTO credentialsDTO = new CredentialsDTO();
            credentialsDTO.setEmail(email);
            credentialsDTO.setPassword(password);
            Call<TokenDTO> call = userInterface.login(credentialsDTO);
            call.enqueue(new Callback<TokenDTO>() {
                @Override
                public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {
                    if(response.isSuccessful()) {
                        viewModel.setToken(response.body().getToken());
                        emailET.clearComposingText();
                        passwordET.clearComposingText();
                        Navigation.findNavController(root).navigate(R.id.action_nav_login_to_nav_conversations);
                    } else {
                        Log.e(TAG, String.valueOf(response.code()));
                        Toast.makeText(getContext(), "Incorrect email or password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<TokenDTO> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        });
    }
}
