package com.example.demobtl.model;


public class User {

    private String uid;
    private String email;
    private String fullName;
    private String studentId;

    // BẮT BUỘC: Constructor rỗng cho Firebase Realtime Database
    public User() {
    }

    public User(String uid, String email, String fullName, String studentId) {
        this.uid = uid;
        this.email = email;
        this.fullName = fullName;
        this.studentId = studentId;
    }

    // --- Getters và Setters ---

    public String getUid() {
        return uid;
    }

    // Không cần Setter cho UID nếu không muốn thay đổi nó sau khi tạo

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}