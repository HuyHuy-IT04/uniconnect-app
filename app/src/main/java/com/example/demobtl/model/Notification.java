package com.example.demobtl.model;

public class Notification {

    private String notificationId;
    private String title;
    private String content;
    private long timestamp; // Thời gian đăng (sử dụng epoch time)

    public Notification() {
        // Constructor rỗng bắt buộc cho Firebase
    }

    public Notification(String notificationId, String title, String content, long timestamp) {
        this.notificationId = notificationId;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    // --- Getters và Setters ---

    public String getNotificationId() {
        return notificationId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters cần thiết cho việc ánh xạ dữ liệu Firebase (Omitted for brevity)

}