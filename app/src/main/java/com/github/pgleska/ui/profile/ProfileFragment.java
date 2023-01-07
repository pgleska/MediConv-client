package com.github.pgleska.ui.profile;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.github.pgleska.R;
import com.github.pgleska.databinding.FragmentProfileBinding;
import com.github.pgleska.dtos.ResponseDTO;
import com.github.pgleska.dtos.UserDTO;
import com.github.pgleska.retrofit.RetrofitClient;
import com.github.pgleska.retrofit.interfaces.UserInterface;
import com.github.pgleska.ui.viewModels.UniversalViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private View root;
    private TextView profileEmail;
    private EditText profileET;
    private Button updateBtn;

    private UserInterface userInterface;
    private UniversalViewModel viewModel;

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
        profileEmail = binding.profileEmail;
        profileET = binding.profileET;
        viewModel = new ViewModelProvider(getActivity()).get(UniversalViewModel.class);
        profileEmail.setText(viewModel.getUser().getEmail());
        profileET.setText(viewModel.getUser().getName());
        updateBtn = binding.updateBtn;
        userInterface = RetrofitClient.getUserInterface(getString(R.string.server_address));
    }

    private void initListeners() {
        updateBtn.setOnClickListener(v -> {
            UserDTO dto = new UserDTO();
            dto.setName(profileET.getText().toString());
            Call<ResponseDTO<UserDTO>> call = userInterface.updateUser(viewModel.getToken(), dto);
            call.enqueue(new Callback<ResponseDTO<UserDTO>>() {
                @Override
                public void onResponse(Call<ResponseDTO<UserDTO>> call, Response<ResponseDTO<UserDTO>> response) {
                    if(!response.isSuccessful()) {
                        Toast.makeText(getContext(), "Ooopsie! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseDTO<UserDTO>> call, Throwable t) {

                }
            });
        });
    }

}
