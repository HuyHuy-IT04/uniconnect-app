package com.example.demobtl;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demobtl.model.User; // Đã sửa lỗi: Import User Model
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    // UI Components
    private EditText etEmail, etPassword, etFullName, etStudentId;
    private Button btnRegister;
    private ProgressBar progressBar;

    // Firebase Instances
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase; // Tham chiếu đến Realtime DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Ánh xạ View
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        etStudentId = findViewById(R.id.etStudentId);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        // 2. Khởi tạo Firebase (Sử dụng singleton instance)
        mAuth = FirebaseAuth.getInstance();
        // Lấy tham chiếu đến node "users" theo yêu cầu
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // 3. Xử lý sự kiện Đăng ký
        btnRegister.setOnClickListener(v -> registerUser());
    }

    /**
     * Phương thức xử lý toàn bộ quá trình Đăng ký (Auth + DB Save).
     */
    private void registerUser() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String fullName = etFullName.getText().toString().trim();
        final String studentId = etStudentId.getText().toString().trim();

        // 4. Kiểm tra đầu vào (Input Validation)
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(fullName) || TextUtils.isEmpty(studentId)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có tối thiểu 6 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị ProgressBar, ẩn nút để tránh click kép
        showProgress(true);

        // 5. Firebase Auth: Tạo người dùng mới
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Auth Thành công
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            // 6. Lưu thông tin người dùng vào Realtime Database
                            saveUserToDatabase(user.getUid(), email, fullName, studentId);
                        }
                    } else {
                        // Auth Thất bại
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        showProgress(false);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Phương thức lưu thông tin User vào node "users" trong Realtime DB.
     */
    private void saveUserToDatabase(String uid, String email, String fullName, String studentId) {
        // Tạo đối tượng User Model (chứa dữ liệu)
        User user = new User(uid, email, fullName, studentId);

        // Đặt dữ liệu vào node "users/{uid_cua_user}"
        mDatabase.child(uid).setValue(user)
                .addOnCompleteListener(task -> {
                    showProgress(false); // Ẩn ProgressBar
                    if (task.isSuccessful()) {
                        // Lưu DB Thành công
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Chào mừng đến với UniConnect.", Toast.LENGTH_LONG).show();

                        // Chuyển hướng đến màn hình chính
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        finish(); // Kết thúc Activity hiện tại
                    } else {
                        // Lưu DB Thất bại
                        Log.e(TAG, "Lưu thông tin thất bại.", task.getException());
                        Toast.makeText(RegisterActivity.this, "Lỗi lưu thông tin cá nhân. Vui lòng thử lại.", Toast.LENGTH_LONG).show();

                        // *Đảm bảo tính toàn vẹn: Nếu lưu DB thất bại, cần xóa tài khoản Auth vừa tạo*
                        if (mAuth.getCurrentUser() != null) {
                            mAuth.getCurrentUser().delete();
                            Toast.makeText(RegisterActivity.this, "Đã hủy tài khoản Auth để đảm bảo dữ liệu.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Hiển thị/Ẩn ProgressBar và vô hiệu hóa nút Đăng ký.
     */
    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
        }
    }
}