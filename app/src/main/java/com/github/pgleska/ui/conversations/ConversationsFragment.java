package com.github.pgleska.ui.conversations;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pgleska.R;
import com.github.pgleska.adapters.UsersAdapter;
import com.github.pgleska.databinding.FragmentConversationsBinding;
import com.github.pgleska.dtos.MessageDTO;
import com.github.pgleska.dtos.ResponseDTO;
import com.github.pgleska.dtos.UserDTO;
import com.github.pgleska.retrofit.RetrofitClient;
import com.github.pgleska.retrofit.interfaces.MessageInterface;
import com.github.pgleska.retrofit.interfaces.UserInterface;
import com.github.pgleska.ui.viewModels.UniversalViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationsFragment extends Fragment {
    private static final String TAG = ConversationsFragment.class.getName();

    private FragmentConversationsBinding binding;
    private View root;
    private UniversalViewModel viewModel;

    private UsersAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private List<UserDTO> users;

    private UserInterface userInterface;

    private EditText searchBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConversationsBinding.inflate(inflater, container, false);
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
        recyclerView = binding.rvConversations;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        users = new ArrayList<>();
        adapter = new UsersAdapter(users, viewModel, root);
        recyclerView.setAdapter(adapter);
        searchBar = binding.etSearchDoctor;

        userInterface = RetrofitClient.getUserInterface(getString(R.string.server_address));

        getUser();
        getUsers();
    }

    private void initListeners() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty())
                    getUsers();
                else
                    getUsersWithSeq(editable.toString());
            }
        });
    }

    private void getUser() {
        Call<ResponseDTO<UserDTO>> call = userInterface.getUser(viewModel.getToken());
        call.enqueue(new Callback<ResponseDTO<UserDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<UserDTO>> call, Response<ResponseDTO<UserDTO>> response) {
                if(response.isSuccessful()) {
                    viewModel.setUser(response.body().getPayload());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<UserDTO>> call, Throwable t) {

            }
        });
    }

    private void getUsers() {
        getUsersWithSeq("");
    }

    private void getUsersWithSeq(String seq) {
        Call<ResponseDTO<List<UserDTO>>> call = userInterface.searchForUser(viewModel.getToken(), seq);
        call.enqueue(new Callback<ResponseDTO<List<UserDTO>>>() {
            @Override
            public void onResponse(Call<ResponseDTO<List<UserDTO>>> call, Response<ResponseDTO<List<UserDTO>>> response) {
                if(response.isSuccessful()) {
                    adapter.update(response.body().getPayload());
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<List<UserDTO>>> call, Throwable t) {

            }
        });
    }
}
