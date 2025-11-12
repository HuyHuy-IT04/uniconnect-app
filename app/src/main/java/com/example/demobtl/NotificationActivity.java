package com.example.demobtl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.demobtl.adapter.NotificationAdapter;
import com.example.demobtl.model.Notification;
import java.util.ArrayList;
import java.util.List;

// *Để dễ dàng thử nghiệm, chúng ta sẽ tạo một Activity độc lập*

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // 1. Ánh xạ View và Thiết lập RecyclerView
        recyclerView = findViewById(R.id.recyclerViewNotifications);

        // Cấu hình LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo Adapter
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        // 2. Tải dữ liệu (Sử dụng Fake Data trước)
        loadFakeNotifications();
    }

    /**
     * Phương thức tạo dữ liệu giả lập (Fake Data)
     * TODO: Thay thế bằng Firebase Realtime DB (Repository) sau.
     */
    private void loadFakeNotifications() {
        Log.d("NotificationActivity", "Đang tạo dữ liệu giả lập...");

        // Xóa dữ liệu cũ (nếu có)
        notificationList.clear();

        // Thêm các thông báo giả lập (Fake Data)
        notificationList.add(new Notification(
                "N001",
                "Lịch Thi Cuối Kỳ Học Phần Lập Trình Di Động",
                "Các bạn sinh viên kiểm tra lịch thi chính thức trên cổng thông tin học vụ. Chi tiết xem tại đường link kèm theo.",
                System.currentTimeMillis() - 86400000L * 2 // 2 ngày trước
        ));

        notificationList.add(new Notification(
                "N002",
                "Học bổng Samsung 2025: Mở đơn đăng ký",
                "Sinh viên có GPA từ 3.2 trở lên có thể nộp đơn xin học bổng. Hạn cuối: 30/11/2025.",
                System.currentTimeMillis() - 86400000L * 5 // 5 ngày trước
        ));

        notificationList.add(new Notification(
                "N003",
                "Cảnh báo: Đóng cổng đăng ký môn học",
                "Cổng đăng ký môn học sẽ đóng vào 23:59 hôm nay. Vui lòng hoàn tất đăng ký của bạn.",
                System.currentTimeMillis() - 86400000L * 10 // 10 ngày trước
        ));

        // Thông báo cho Adapter biết dữ liệu đã thay đổi để cập nhật UI
        adapter.setNotificationList(notificationList);

        Log.d("NotificationActivity", "Đã tải thành công " + notificationList.size() + " thông báo giả lập.");
        Toast.makeText(this, "Đã tải dữ liệu thông báo giả lập thành công!", Toast.LENGTH_SHORT).show();
    }
}