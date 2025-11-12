package com.example.demobtl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demobtl.R;
import com.example.demobtl.model.Notification;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// *Lưu ý: Đảm bảo bạn đã tạo package adapter*

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    /**
     * Phương thức cập nhật dữ liệu (được gọi từ Activity/ViewModel)
     */
    public void setNotificationList(List<Notification> newNotifications) {
        this.notificationList = newNotifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    /**
     * ViewHolder: Ánh xạ view và bind dữ liệu cho từng item
     */
    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvContent;
        private final TextView tvDate;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvContent = itemView.findViewById(R.id.tvNotificationContent);
            tvDate = itemView.findViewById(R.id.tvNotificationDate);

            // Xử lý sự kiện click vào item (Optional: mở chi tiết thông báo)
            itemView.setOnClickListener(v -> {
                // TODO: Triển khai intent mở NotificationDetailActivity tại đây
            });
        }

        public void bind(Notification notification) {
            tvTitle.setText(notification.getTitle());
            tvContent.setText(notification.getContent());

            // Chuyển timestamp (long) thành chuỗi ngày tháng dễ đọc
            String dateString = dateFormat.format(new Date(notification.getTimestamp()));
            tvDate.setText("Ngày đăng: " + dateString);
        }
    }
}