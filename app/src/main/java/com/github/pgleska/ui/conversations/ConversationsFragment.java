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
import com.github.pgleska.adapters.ConversationsAdapter;
import com.github.pgleska.databinding.FragmentConversationsBinding;
import com.github.pgleska.dtos.ConversationDTO;
import com.github.pgleska.retrofit.RetrofitClient;
import com.github.pgleska.retrofit.interfaces.ConversationInterface;
import com.github.pgleska.ui.viewModels.CredsViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationsFragment extends Fragment {
    private static final String TAG = ConversationsFragment.class.getName();

    private FragmentConversationsBinding binding;
    private View root;
    private CredsViewModel viewModel;

    private ConversationsAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private List<ConversationDTO> conversations;

    private ConversationInterface conversationInterface;

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
        viewModel = new ViewModelProvider(getActivity()).get(CredsViewModel.class);
        recyclerView = binding.rvConversations;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        conversations = new ArrayList<>();
        adapter = new ConversationsAdapter(conversations, viewModel, root);
        recyclerView.setAdapter(adapter);

        conversationInterface = RetrofitClient.getConversationInterface(getString(R.string.server_address));

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
        Call<List<ConversationDTO>> call = conversationInterface.getConversations(viewModel.getToken());
        call.enqueue(new Callback<List<ConversationDTO>>() {
            @Override
            public void onResponse(Call<List<ConversationDTO>> call, Response<List<ConversationDTO>> response) {
                Log.e(TAG, String.valueOf(response.code()));
                if(response.isSuccessful()) {
                    adapter.update(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ConversationDTO>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void getConversationsWithPhrase(String phrase) {
        Call<List<ConversationDTO>> call = conversationInterface.getConversationsWithQueryParams(
                viewModel.getToken(), phrase);
        call.enqueue(new Callback<List<ConversationDTO>>() {
            @Override
            public void onResponse(Call<List<ConversationDTO>> call, Response<List<ConversationDTO>> response) {
                Log.e(TAG, String.valueOf(response.code()));
                if(response.isSuccessful()) {
                    adapter.update(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ConversationDTO>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
