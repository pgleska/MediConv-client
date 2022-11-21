package com.github.pgleska.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.pgleska.R;
import com.github.pgleska.databinding.FragmentPinBinding;

public class PinFragment extends Fragment {
    //generating private and public keys

    private FragmentPinBinding binding;
    private View root;
    private Button ButtonRegister;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPinBinding.inflate(inflater, container, false);
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

        ButtonRegister = binding.btnConfirm;
    }

    private void initListeners() {
        ButtonRegister.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_nav_login_to_nav_pin);
        });
    }
}