package com.github.pgleska.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.pgleska.R;
import com.github.pgleska.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private View root;
    private Button ButtonRegister;
    private Button ButtonLogin;

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
        ButtonRegister = binding.btnToRegister;
        ButtonLogin = binding.btnLogin;
    }

    private void initListeners() {
        ButtonRegister.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_nav_login_to_nav_register);
        });
        ButtonLogin.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_nav_login_to_nav_pin);
        });
    }
}
