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
import com.github.pgleska.dtos.UserDTO;
import com.github.pgleska.ui.viewModels.UniversalViewModel;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ConversationViewHolder> {

    private List<UserDTO> users;
    private UniversalViewModel universalViewModel;
    private View rootView;

    public UsersAdapter(List<UserDTO> users, UniversalViewModel universalViewModel,
                        View rootView) {
        this.users = users;
        this.universalViewModel = universalViewModel;
        this.rootView = rootView;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);

        UsersAdapter.ConversationViewHolder nvh = new UsersAdapter.ConversationViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        UserDTO user = users.get(position);
        holder.name.setText(user.getName());
        holder.layout.setOnClickListener(v -> {
            universalViewModel.setOtherUser(user);
            Navigation.findNavController(rootView).navigate(R.id.action_nav_conversations_to_nav_messages);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void update(List<UserDTO> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date, lastMessage;
        public LinearLayout layout;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView.findViewById(R.id.item_conversation);
            this.name = itemView.findViewById(R.id.item_conversation_name);
        }
    }
}
