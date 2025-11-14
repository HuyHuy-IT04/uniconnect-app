package com.example.demobtl;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Khai báo các nút UI
    private Button btnNavigateToRegister;
    private Button btnNavigateToLogin;
    private Button btnNavigateToNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- 1. KHỞI TẠO VÀ KIỂM TRA FIREBASE ---
        FirebaseApp.initializeApp(this);

        if (FirebaseApp.getApps(this).size() > 0) {
            Log.d("FirebaseCheck", "✅ Firebase đã khởi tạo thành công!");
        } else {
            Log.e("FirebaseCheck", "❌ Firebase chưa khởi tạo!");
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth != null) {
            Log.d("FirebaseCheck", "✅ FirebaseAuth sẵn sàng!");
        }

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Log.d("FirebaseCheck", "✅ FirebaseDatabase URL: " + db.getReference().toString());
        // ------------------------------------------------------------------


        // 2. Ánh xạ các nút UI (Dùng ID từ XML)
        btnNavigateToRegister = findViewById(R.id.btnNavigateToRegister);
        btnNavigateToLogin = findViewById(R.id.btnNavigateToLogin);
        btnNavigateToNotifications = findViewById(R.id.btnNavigateToNotifications);

        // 3. Thiết lập sự kiện chuyển hướng Đăng ký
        btnNavigateToRegister.setOnClickListener(v -> {
            Log.d(TAG, "Chuyển sang màn hình Đăng ký.");
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // 4. Thiết lập sự kiện chuyển hướng Đăng nhập
        btnNavigateToLogin.setOnClickListener(v -> {
            Log.d(TAG, "Chuyển sang màn hình Đăng nhập.");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // 5. Thiết lập sự kiện chuyển hướng THÔNG BÁO
        btnNavigateToNotifications.setOnClickListener(v -> {
            Log.d(TAG, "Chuyển sang màn hình Thông báo.");
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
    }
}