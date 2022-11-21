package com.github.pgleska.ui.profile;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.pgleska.R;
import com.github.pgleska.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private View root;
    private Button ButtonReturn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
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
        ButtonReturn = binding.buttonReturn;
    }

    private void initListeners() {
        ButtonReturn.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_nav_profile_to_nav_login);
        });
    }
}
