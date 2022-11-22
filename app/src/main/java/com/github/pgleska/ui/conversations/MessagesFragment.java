package com.github.pgleska.ui.conversations;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pgleska.R;
import com.github.pgleska.adapters.MessagesAdapter;
import com.github.pgleska.cryptography.CryptographyUtils;
import com.github.pgleska.databinding.FragmentMessagesBinding;
import com.github.pgleska.dtos.MessageDTO;
import com.github.pgleska.dtos.ResponseDTO;
import com.github.pgleska.retrofit.RetrofitClient;
import com.github.pgleska.retrofit.interfaces.MessageInterface;
import com.github.pgleska.ui.viewModels.UniversalViewModel;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends Fragment {
    private static final String TAG = MessagesFragment.class.getName();
    private View root;
    private FragmentMessagesBinding binding;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MessagesAdapter messagesAdapter;

    private EditText messageET;
    private Button sendMsgBtn;
    private TextView name;

    private List<MessageDTO> messages;

    private MessageInterface messageInterface;
    private UniversalViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        initComponents();
        initListeners();

        return root;
    }

    private void initComponents() {
        viewModel = new ViewModelProvider(getActivity()).get(UniversalViewModel.class);
        messageET = binding.singleMessageEdittext;
        sendMsgBtn = binding.chatSendMessage;
        name = binding.singleMessageName;
        recyclerView = binding.singleMessageRecyclerView;
        layoutManager = new LinearLayoutManager(getContext());
        messages = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(messages, getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(messagesAdapter);
        messageInterface = RetrofitClient.getMessageInterface(getString(R.string.server_address));

        downloadMessages();
    }

    private void initListeners() {
        sendMsgBtn.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(root.getWindowToken(), 0);

            try {
                SecretKey sharedKey = CryptographyUtils.generateSharedKey();
                String content = messageET.getText().toString();
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setReceiverId(viewModel.getOtherUser().getId());
                messageDTO.setContent(encryptMessage(content, sharedKey));
                messageDTO.setSharedKeyEncryptedWithAuthorPKey(encryptSharedKey(sharedKey, viewModel.getUser().getPublicKey()));
                messageDTO.setSharedKeyEncryptedWithReceiverPKey(encryptSharedKey(sharedKey, viewModel.getOtherUser().getPublicKey()));
                sendMessage(messageDTO);
            } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
                e.printStackTrace();
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if(!messages.isEmpty()) {
                    recyclerView.smoothScrollToPosition(messages.size() - 1);
                }
            }
        });

    }

    private String encryptMessage(String content, SecretKey key) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return CryptographyUtils.encryptSymmetric(content, key);
    }

    private String encryptSharedKey(SecretKey secretKey, String publicKey) {

        return "";
    }

    private void downloadMessages() {
        Call<ResponseDTO<List<MessageDTO>>> call = messageInterface.downloadMessages(viewModel.getToken(),
                viewModel.getOtherUser().getId());
        call.enqueue(new Callback<ResponseDTO<List<MessageDTO>>>() {
            @Override
            public void onResponse(Call<ResponseDTO<List<MessageDTO>>> call, Response<ResponseDTO<List<MessageDTO>>> response) {
                if(response.isSuccessful()) {
                    List<MessageDTO> messages = response.body().getPayload();
                    Collections.reverse(messages);
                    messagesAdapter.updateMessage(messages);
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<List<MessageDTO>>> call, Throwable t) {

            }
        });
    }

    private void sendMessage(MessageDTO messageDTO) {
        Call<ResponseDTO<MessageDTO>> call = messageInterface.sendMessage(viewModel.getToken(), messageDTO);
        call.enqueue(new Callback<ResponseDTO<MessageDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<MessageDTO>> call, Response<ResponseDTO<MessageDTO>> response) {
                if(response.isSuccessful()) {
                    downloadMessages();
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<MessageDTO>> call, Throwable t) {

            }
        });
    }
}
