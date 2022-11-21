package com.github.pgleska.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pgleska.R;
import com.github.pgleska.dtos.MessageDTO;
import com.github.pgleska.ui.viewModels.CredsViewModel;

import java.util.List;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {

    private List<MessageDTO> conversations;
    private CredsViewModel credsViewModel;
    private View rootView;

    public ConversationsAdapter(List<MessageDTO> conversations, CredsViewModel credsViewModel,
                                View rootView) {
        this.conversations = conversations;
        this.credsViewModel = credsViewModel;
        this.rootView = rootView;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);

        ConversationsAdapter.ConversationViewHolder nvh = new ConversationsAdapter.ConversationViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        MessageDTO conversation = conversations.get(position);
        holder.name.setText(conversation.getRecipient().getName());
        holder.lastMessage.setText(conversation.getPlainLastMessage(credsViewModel.getPrivateKey()));
        holder.date.setText(conversation.getDate());
        holder.layout.setOnClickListener(v -> {
            //TODO: create proper navigation
            Navigation.findNavController(rootView).navigate(-1);
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public void update(List<MessageDTO> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date, lastMessage;
        public LinearLayout layout;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView.findViewById(R.id.item_conversation);
            this.name = itemView.findViewById(R.id.item_conversation_name);
            this.date = itemView.findViewById(R.id.item_conversation_date);
            this.lastMessage = itemView.findViewById(R.id.item_conversation_latest);
        }
    }
}
