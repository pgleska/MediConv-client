package com.github.pgleska.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.github.pgleska.R;
import com.github.pgleska.ui.viewModels.UniversalViewModel;

public class LogoutFragment extends Fragment {
    private static final String TAG = "LogOutFragment";

    private View root;
    private UniversalViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(getActivity()).get(UniversalViewModel.class);
        root = inflater.inflate(R.layout.fragment_login, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        clearData(view);
    }

    private void clearData(View view) {
        //TODO: it clears all view models except the one used in current class
        getActivity().getViewModelStore().clear();
        viewModel.clear();
        Navigation.findNavController(view).navigate(R.id.nav_login);
    }

}
