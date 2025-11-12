package com.example.demobtl;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase
        FirebaseApp.initializeApp(this);

        // Kiểm tra Firebase hoạt động
        if (FirebaseApp.getApps(this).size() > 0) {
            Log.d("FirebaseCheck", "✅ Firebase đã khởi tạo thành công!");
        } else {
            Log.e("FirebaseCheck", "❌ Firebase chưa khởi tạo!");
        }

        // Kiểm tra Firebase Authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth != null) {
            Log.d("FirebaseCheck", "✅ FirebaseAuth sẵn sàng!");
        }

        // Kiểm tra Firebase Realtime Database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Log.d("FirebaseCheck", "✅ FirebaseDatabase URL: " + db.getReference().toString());
    }
}
