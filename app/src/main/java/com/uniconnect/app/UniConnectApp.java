package com.uniconnect.app;

import android.app.Application;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class UniConnectApp extends Application {
    private static final String TAG = "UniConnectApp";

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // Initialize Firebase
            FirebaseApp.initializeApp(this);

            // THAY ĐỔI URL này sau khi bạn tạo database và lấy URL thực
            // URL sẽ có dạng: https://uniconnect-13fe7-default-rtdb.REGION.firebasedatabase.app
            String databaseUrl = "https://uniconnect-13fe7-default-rtdb.asia-southeast1.firebasedatabase.app/";

            // Enable offline persistence với URL cụ thể
            FirebaseDatabase database = FirebaseDatabase.getInstance(databaseUrl);
            database.setPersistenceEnabled(true);

            Log.d(TAG, "Firebase initialized successfully with URL: " + databaseUrl);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage(), e);
        }
    }
}