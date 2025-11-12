package com.uniconnect.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.uniconnect.app.R;
import com.uniconnect.app.models.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_online, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvUserName.setText(user.getName());

        String roleText = user.getRole().equals("teacher") ? "Giảng viên" : "Sinh viên";
        holder.tvUserRole.setText(roleText);

        // Set status indicator color
        int statusColor = user.isOnline()
                ? ContextCompat.getColor(holder.itemView.getContext(), R.color.online_status)
                : ContextCompat.getColor(holder.itemView.getContext(), R.color.offline_status);
        holder.statusIndicator.setBackgroundColor(statusColor);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserRole;
        View statusIndicator;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
    }
}