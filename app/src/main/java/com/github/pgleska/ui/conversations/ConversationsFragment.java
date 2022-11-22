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

import com.github.pgleska.adapters.UsersAdapter;
import com.github.pgleska.databinding.FragmentConversationsBinding;
import com.github.pgleska.dtos.MessageDTO;
import com.github.pgleska.retrofit.interfaces.MessageInterface;
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

    private List<MessageDTO> conversations;

    private MessageInterface messageInterface;

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

        conversations = new ArrayList<>();
        adapter = new UsersAdapter(conversations, viewModel, root);
        recyclerView.setAdapter(adapter);

//        messageInterface = RetrofitClient.getConversationInterface(getString(R.string.server_address));

        getConversations();
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
                    getConversations();
                else
                    getConversationsWithPhrase(editable.toString());
            }
        });
    }

    private void getConversations() {
        Call<List<MessageDTO>> call = messageInterface.getConversations(viewModel.getToken());
        call.enqueue(new Callback<List<MessageDTO>>() {
            @Override
            public void onResponse(Call<List<MessageDTO>> call, Response<List<MessageDTO>> response) {
                Log.e(TAG, String.valueOf(response.code()));
                if(response.isSuccessful()) {
                    adapter.update(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MessageDTO>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void getConversationsWithPhrase(String phrase) {
        Call<List<MessageDTO>> call = messageInterface.getConversationsWithQueryParams(
                viewModel.getToken(), phrase);
        call.enqueue(new Callback<List<MessageDTO>>() {
            @Override
            public void onResponse(Call<List<MessageDTO>> call, Response<List<MessageDTO>> response) {
                Log.e(TAG, String.valueOf(response.code()));
                if(response.isSuccessful()) {
                    adapter.update(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MessageDTO>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
