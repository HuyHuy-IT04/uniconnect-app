package com.uniconnect.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.*;
import com.uniconnect.app.R;
import com.uniconnect.app.adapters.UserAdapter;
import com.uniconnect.app.models.User;
import com.uniconnect.app.utils.FirebaseUtil;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";

    private TextView tvUserName, tvUserRole;
    private CardView cardGroupChat;
    private RecyclerView rvOnlineUsers;
    private UserAdapter userAdapter;
    private List<User> onlineUsersList;

    private String currentUserId;
    private String currentUserName;
    private String currentUserRole;

    private DatabaseReference usersRef;
    private ChildEventListener usersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Log.d(TAG, "DashboardActivity started");

        // Get user info from intent
        currentUserId = getIntent().getStringExtra("USER_ID");
        currentUserName = getIntent().getStringExtra("USER_NAME");
        currentUserRole = getIntent().getStringExtra("USER_ROLE");

        Log.d(TAG, "User ID: " + currentUserId);
        Log.d(TAG, "User Name: " + currentUserName);
        Log.d(TAG, "User Role: " + currentUserRole);

        if (currentUserId == null || currentUserName == null || currentUserRole == null) {
            Toast.makeText(this, "Lỗi: Thiếu thông tin người dùng!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        usersRef = FirebaseUtil.getUsersRef();

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize views
        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        cardGroupChat = findViewById(R.id.cardGroupChat);
        rvOnlineUsers = findViewById(R.id.rvOnlineUsers);

        // Set user info
        tvUserName.setText(currentUserName);
        String roleText = currentUserRole.equals("teacher") ? "Giảng viên" : "Sinh viên";
        tvUserRole.setText(roleText);

        Log.d(TAG, "UI initialized");

        // Setup RecyclerView for online users
        onlineUsersList = new ArrayList<>();
        userAdapter = new UserAdapter(onlineUsersList);
        rvOnlineUsers.setLayoutManager(new LinearLayoutManager(this));
        rvOnlineUsers.setAdapter(userAdapter);

        // Navigate to group chat
        cardGroupChat.setOnClickListener(v -> {
            Log.d(TAG, "Group chat clicked");
            Intent intent = new Intent(DashboardActivity.this, GroupChatActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            intent.putExtra("USER_NAME", currentUserName);
            intent.putExtra("GROUP_ID", "uniconnect_main");
            startActivity(intent);
        });

        // Listen for online users
        listenForOnlineUsers();

        Toast.makeText(this, "Chào mừng " + currentUserName + "!", Toast.LENGTH_SHORT).show();
    }

    private void listenForOnlineUsers() {
        Log.d(TAG, "Setting up online users listener");

        usersListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                User user = snapshot.getValue(User.class);
                Log.d(TAG, "User added: " + (user != null ? user.getName() : "null"));

                if (user != null && !user.getUserId().equals(currentUserId)) {
                    // Check if user already exists in list
                    boolean exists = false;
                    for (User u : onlineUsersList) {
                        if (u.getUserId().equals(user.getUserId())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        onlineUsersList.add(user);
                        userAdapter.notifyItemInserted(onlineUsersList.size() - 1);
                        Log.d(TAG, "User added to list: " + user.getName());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                User updatedUser = snapshot.getValue(User.class);
                Log.d(TAG, "User changed: " + (updatedUser != null ? updatedUser.getName() : "null"));

                if (updatedUser != null) {
                    for (int i = 0; i < onlineUsersList.size(); i++) {
                        if (onlineUsersList.get(i).getUserId().equals(updatedUser.getUserId())) {
                            onlineUsersList.set(i, updatedUser);
                            userAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                String userId = snapshot.getKey();
                Log.d(TAG, "User removed: " + userId);

                for (int i = 0; i < onlineUsersList.size(); i++) {
                    if (onlineUsersList.get(i).getUserId().equals(userId)) {
                        onlineUsersList.remove(i);
                        userAdapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                Toast.makeText(DashboardActivity.this, "Lỗi tải danh sách: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        usersRef.addChildEventListener(usersListener);
        Log.d(TAG, "Listener attached");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Log.d(TAG, "Logging out");

        // Set user offline
        if (usersRef != null && currentUserId != null) {
            usersRef.child(currentUserId).child("isOnline").setValue(false);
            usersRef.child(currentUserId).child("lastSeen").setValue(System.currentTimeMillis());
        }

        // Navigate to login
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (usersListener != null && usersRef != null) {
            usersRef.removeEventListener(usersListener);
            Log.d(TAG, "Listener removed");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Set user offline when leaving
        if (usersRef != null && currentUserId != null) {
            usersRef.child(currentUserId).child("isOnline").setValue(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set user online when returning
        if (usersRef != null && currentUserId != null) {
            usersRef.child(currentUserId).child("isOnline").setValue(true);
        }
    }
}