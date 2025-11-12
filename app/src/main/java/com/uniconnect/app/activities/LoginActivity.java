package com.uniconnect.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.uniconnect.app.R;
import com.uniconnect.app.models.User;
import com.uniconnect.app.utils.FirebaseUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnTestA, btnTestB, btnTestC, btnTestF;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        try {
            usersRef = FirebaseUtil.getUsersRef();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi kết nối Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnTestA = findViewById(R.id.btnTestA);
        btnTestB = findViewById(R.id.btnTestB);
        btnTestC = findViewById(R.id.btnTestC);
        btnTestF = findViewById(R.id.btnTestF);

        // Create test accounts on first launch
        createTestAccounts();

        // Login button
        btnLogin.setOnClickListener(v -> loginUser());

        // Quick login buttons
        btnTestA.setOnClickListener(v -> quickLogin("studentA@uni.com", "123456"));
        btnTestB.setOnClickListener(v -> quickLogin("studentB@uni.com", "123456"));
        btnTestC.setOnClickListener(v -> quickLogin("studentC@uni.com", "123456"));
        btnTestF.setOnClickListener(v -> quickLogin("teacherF@uni.com", "123456"));
    }

    private void createTestAccounts() {
        // Create test users in Firebase (only once)
        User[] testUsers = {
                new User("userA", "Sinh viên A", "studentA@uni.com", "123456", "student"),
                new User("userB", "Sinh viên B", "studentB@uni.com", "123456", "student"),
                new User("userC", "Sinh viên C", "studentC@uni.com", "123456", "student"),
                new User("userF", "Giảng viên F", "teacherF@uni.com", "123456", "teacher")
        };

        for (User user : testUsers) {
            usersRef.child(user.getUserId()).setValue(user)
                    .addOnFailureListener(e -> {
                        // Ignore if already exists
                    });
        }

        // Create default group chat
        DatabaseReference groupRef = FirebaseUtil.getGroupChatRef("uniconnect_main");
        groupRef.child("groupName").setValue("UniConnect Main Chat");
        groupRef.child("createdAt").setValue(System.currentTimeMillis());
    }

    private void quickLogin(String email, String password) {
        etEmail.setText(email);
        etPassword.setText(password);
        loginUser();
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Search for user with matching email
        usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                if (user != null && user.getPassword().equals(password)) {
                                    // Login successful
                                    updateUserStatus(user.getUserId(), true);
                                    navigateToDashboard(user);
                                    return;
                                }
                            }
                            Toast.makeText(LoginActivity.this,
                                    "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Email không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(LoginActivity.this,
                                "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserStatus(String userId, boolean isOnline) {
        usersRef.child(userId).child("isOnline").setValue(isOnline);
        usersRef.child(userId).child("lastSeen").setValue(System.currentTimeMillis());
    }

    private void navigateToDashboard(User user) {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("USER_ID", user.getUserId());
        intent.putExtra("USER_NAME", user.getName());
        intent.putExtra("USER_ROLE", user.getRole());
        startActivity(intent);
        finish();
    }
}