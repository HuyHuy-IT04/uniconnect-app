package com.example.demobtl.students;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.demobtl.R;

import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private final List<Message> messageList = new ArrayList<>();
    private final String currentUserId;
    private final Context context;

    public MessageAdapter(Context context, String currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
    }

    public void addMessage(Message msg) {
        messageList.add(msg);
        notifyItemInserted(messageList.size() - 1);
    }

    public void clear() {
        messageList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message m = messageList.get(position);
        if (m.getSenderId() != null && m.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message m = messageList.get(position);
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(m.getTimestamp()));
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).txtMessage.setText(m.getText());
            ((SentViewHolder) holder).txtTime.setText(time);
        } else {
            ((ReceivedViewHolder) holder).txtSender.setText(m.getSenderName() != null ? m.getSenderName() : "Người lạ");
            ((ReceivedViewHolder) holder).txtMessage.setText(m.getText());
            ((ReceivedViewHolder) holder).txtTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime;
        SentViewHolder(@NonNull View v) {
            super(v);
            txtMessage = v.findViewById(R.id.txt_message_sent);
            txtTime = v.findViewById(R.id.txt_time_sent);
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtSender, txtMessage, txtTime;
        ReceivedViewHolder(@NonNull View v) {
            super(v);
            txtSender = v.findViewById(R.id.txt_sender_received);
            txtMessage = v.findViewById(R.id.txt_message_received);
            txtTime = v.findViewById(R.id.txt_time_received);
        }
    }
}
