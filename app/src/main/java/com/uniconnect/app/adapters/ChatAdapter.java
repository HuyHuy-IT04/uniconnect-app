package com.uniconnect.app.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.uniconnect.app.R;
import com.uniconnect.app.models.Message;
import com.uniconnect.app.utils.FirebaseUtil;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Message> messages;
    private String currentUserId;

    public ChatAdapter(List<Message> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        boolean isSentByMe = message.getSenderId().equals(currentUserId);

        holder.tvMessage.setText(message.getContent());
        holder.tvTimestamp.setText(FirebaseUtil.formatTimestamp(message.getTimestamp()));

        // Configure message bubble alignment and style
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams();

        if (isSentByMe) {
            // Sent message - align right, green background
            params.gravity = Gravity.END;
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_sent);
            holder.tvSenderName.setVisibility(View.GONE);
            holder.tvTimestamp.setGravity(Gravity.END);
        } else {
            // Received message - align left, white background, show sender name
            params.gravity = Gravity.START;
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_received);
            holder.tvSenderName.setVisibility(View.VISIBLE);
            holder.tvSenderName.setText(message.getSenderName());
            holder.tvTimestamp.setGravity(Gravity.START);
        }

        holder.messageContainer.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTimestamp, tvSenderName;
        LinearLayout messageContainer;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvSenderName = itemView.findViewById(R.id.tvSenderName);
            messageContainer = itemView.findViewById(R.id.messageContainer);
        }
    }
}