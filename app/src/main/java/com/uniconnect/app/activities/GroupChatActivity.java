package com.uniconnect.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
import com.uniconnect.app.R;
import com.uniconnect.app.adapters.ChatAdapter;
import com.uniconnect.app.models.Message;
import com.uniconnect.app.utils.FirebaseUtil;
import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private FloatingActionButton fabSend;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;

    private String currentUserId;
    private String currentUserName;
    private String groupId;

    private DatabaseReference messagesRef;
    private ChildEventListener messagesListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Get data from intent
        currentUserId = getIntent().getStringExtra("USER_ID");
        currentUserName = getIntent().getStringExtra("USER_NAME");
        groupId = getIntent().getStringExtra("GROUP_ID");

        if (currentUserId == null || currentUserName == null || groupId == null) {
            Toast.makeText(this, "Lỗi: Thiếu thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        messagesRef = FirebaseUtil.getMessagesRef(groupId);

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize views
        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        fabSend = findViewById(R.id.fabSend);

        // Setup RecyclerView
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList, currentUserId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(chatAdapter);

        // Send message
        fabSend.setOnClickListener(v -> sendMessage());

        // Listen for messages
        listenForMessages();
    }

    private void listenForMessages() {
        messagesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    // Check if message already exists
                    boolean exists = false;
                    for (Message m : messageList) {
                        if (m.getMessageId() != null && m.getMessageId().equals(message.getMessageId())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        messageList.add(message);
                        chatAdapter.notifyItemInserted(messageList.size() - 1);
                        rvMessages.scrollToPosition(messageList.size() - 1);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                Message updatedMessage = snapshot.getValue(Message.class);
                String messageId = snapshot.getKey();

                if (updatedMessage != null && messageId != null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        if (messageList.get(i).getMessageId() != null &&
                                messageList.get(i).getMessageId().equals(messageId)) {
                            messageList.set(i, updatedMessage);
                            chatAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                String messageId = snapshot.getKey();
                if (messageId != null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        if (messageList.get(i).getMessageId() != null &&
                                messageList.get(i).getMessageId().equals(messageId)) {
                            messageList.remove(i);
                            chatAdapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(GroupChatActivity.this,
                        "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        messagesRef.addChildEventListener(messagesListener);
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
            return;
        }

        String messageId = messagesRef.push().getKey();
        if (messageId == null) {
            Toast.makeText(this, "Lỗi tạo tin nhắn", Toast.LENGTH_SHORT).show();
            return;
        }

        Message message = new Message(
                messageId,
                currentUserId,
                currentUserName,
                messageText,
                System.currentTimeMillis()
        );

        messagesRef.child(messageId).setValue(message)
                .addOnSuccessListener(aVoid -> {
                    etMessage.setText("");
                    rvMessages.scrollToPosition(messageList.size() - 1);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(GroupChatActivity.this,
                            "Lỗi gửi tin nhắn: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messagesListener != null && messagesRef != null) {
            messagesRef.removeEventListener(messagesListener);
        }
    }
}