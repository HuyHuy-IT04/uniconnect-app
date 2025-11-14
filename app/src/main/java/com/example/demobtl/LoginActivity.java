package com.example.demobtl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demobtl.students.ChatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "✅ Login successful!", Toast.LENGTH_SHORT).show();

                        // ⭐ Redirect to ChatActivity
                        Intent intent = new Intent(LoginActivity.this, ChatActivity.class);

                        String chatId = "chat_student_" + auth.getCurrentUser().getUid();
                        intent.putExtra("chatId", chatId);
                        intent.putExtra("chatName", "Hỗ trợ sinh viên");

                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "❌ Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
