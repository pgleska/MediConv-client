package com.github.pgleska.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pgleska.R;
import com.github.pgleska.cryptography.CryptographyUtils;
import com.github.pgleska.dtos.MessageDTO;
import com.github.pgleska.ui.viewModels.UniversalViewModel;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private List<MessageDTO> messages;
    private Context context;
    private UniversalViewModel viewModel;

    public MessagesAdapter(List<MessageDTO> messages, Context context, UniversalViewModel viewModel) {
        this.messages = messages;
        this.context = context;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_right, parent, false);
            return new MessagesAdapter.MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false);
            return new MessagesAdapter.MessageViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getAuthorId() == viewModel.getUser().getId()){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageDTO messageDTO = messages.get(position);
        String content = "";
        try {
            if (messageDTO.getAuthorId() == viewModel.getUser().getId()) {
                content = CryptographyUtils.decrypt("RSA", messageDTO.getSharedKeyEncryptedWithAuthorPKey(),
                        viewModel.getPrivateKey());
            } else {
                content = CryptographyUtils.decrypt("AES", messageDTO.getSharedKeyEncryptedWithReceiverPKey(),
                        viewModel.getPrivateKey());
            }
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |InvalidAlgorithmParameterException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        holder.message.setText(content);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateMessage(List<MessageDTO> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.chat_message);
        }
    }
}
