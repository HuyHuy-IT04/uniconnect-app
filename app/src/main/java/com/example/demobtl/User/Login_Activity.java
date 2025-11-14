package com.example.demobtl.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.Admin.Admin_Activity;
import com.example.studentmanagement.GiangVien.Teacher_Activity;
import com.example.studentmanagement.R;
import com.example.studentmanagement.SinhVien.Student_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView tvForgot;
    private ProgressBar progressBar;

    private DatabaseReference dbRef;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgot = findViewById(R.id.tvForgot);
        progressBar = findViewById(R.id.progressBar);

        dbRef = FirebaseDatabase.getInstance().getReference("TaiKhoan");

        btnLogin.setOnClickListener(v -> loginUser());

        tvForgot.setOnClickListener(v ->
                Toast.makeText(Login_Activity.this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show()
        );
    }

    private void loginUser() {
        if (!isConnectedToInternet()) {
            Toast.makeText(this, "Vui lòng kiểm tra kết nối Internet!", Toast.LENGTH_LONG).show();
            return;
        }

        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() <= 5) {
            Toast.makeText(this, "Mật khẩu phải lớn hơn 5 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean found = false;

                for (DataSnapshot data : snapshot.getChildren()) {
                    String tenTK = data.child("tenTK").getValue(String.class);
                    String matKhau = data.child("matKhau").getValue(String.class);
                    String loaiNguoiDung = data.child("loaiNguoiDung").getValue(String.class);

                    if (username.equals(tenTK)) {
                        found = true;
                        if (password.equals(matKhau)) {
                            Toast.makeText(Login_Activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            openHomeScreen(loaiNguoiDung, username);
                        } else {
                            Toast.makeText(Login_Activity.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(Login_Activity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Login_Activity.this, "Lỗi kết nối. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
            }
        });
    }

    private void openHomeScreen(String role, String username) {
        Intent intent;
        switch (role) {
            case "Admin":
                intent = new Intent(this, Admin_Activity.class);
                break;
            case "GiaoVien":
                intent = new Intent(this, Teacher_Activity.class);
                intent.putExtra("maGiaoVien", username);
                break;
            case "SinhVien":
                intent = new Intent(this, Student_Activity.class);
                intent.putExtra("maSinhVien", username);
                break;
            default:
                Toast.makeText(this, "Loại người dùng không xác định", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}