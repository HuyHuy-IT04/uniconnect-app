package com.example.demobtl.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.demobtl.R;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_CHAT_ID = "chatId";
    public static final String EXTRA_CHAT_NAME = "chatName";

    private RecyclerView recyclerView;
    private EditText editMessage;
    private Button btnSend;
    private TextView tvChatName;

    private MessageAdapter adapter;
    private DatabaseReference messagesRef;
    private ChildEventListener messagesListener;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private String chatId;
    private String chatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy chatId và chatName từ Intent
        chatId = getIntent().getStringExtra(EXTRA_CHAT_ID);
        chatName = getIntent().getStringExtra(EXTRA_CHAT_NAME);
        if (chatId == null) {
            // Nếu không truyền chatId, tạo chatId mặc định cho student
            chatId = "chat_student_" + currentUser.getUid();
        }

        tvChatName = findViewById(R.id.tv_chat_name);
        tvChatName.setText(!TextUtils.isEmpty(chatName) ? chatName : "Chat");

        recyclerView = findViewById(R.id.recycler_messages);
        editMessage = findViewById(R.id.edit_message);
        btnSend = findViewById(R.id.btn_send);

        adapter = new MessageAdapter(this, currentUser.getUid());
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

        // Database reference
        messagesRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");

        // Listen new messages
        messagesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                if (msg != null) {
                    adapter.addMessage(msg);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, String s) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String s) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        };
        messagesRef.addChildEventListener(messagesListener);

        btnSend.setOnClickListener(v -> sendMessage());

        // gửi khi nhấn Enter trên soft keyboard
        editMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void sendMessage() {
        String text = editMessage.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        String msgId = UUID.randomUUID().toString();
        Message message = new Message(
                msgId,
                currentUser.getUid(),
                currentUser.getEmail(), // hoặc tên hiển thị nếu bạn có
                text,
                System.currentTimeMillis()
        );

        messagesRef.child(msgId).setValue(message).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                editMessage.setText("");
            } else {
                Toast.makeText(ChatActivity.this, "Gửi thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messagesRef != null && messagesListener != null) {
            messagesRef.removeEventListener(messagesListener);
        }
    }
}
