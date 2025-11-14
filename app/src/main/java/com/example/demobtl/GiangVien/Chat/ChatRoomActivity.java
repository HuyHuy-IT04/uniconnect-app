package com.example.demobtl.GiangVien.Chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demobtl.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private RecyclerView rv;
    private EditText edtMsg;
    private ImageButton btnSend;
    private ChatAdapter chatAdapter;
    private List<Message> msgs = new ArrayList<>();
    private DatabaseReference db;
    private String groupId;
    private String myUid;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_chat_room);

        rv = findViewById(R.id.rvMessages);
        edtMsg = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        rv.setLayoutManager(new LinearLayoutManager(this));
        myUid = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : "demo_user";
        chatAdapter = new ChatAdapter(this, msgs, myUid);
        rv.setAdapter(chatAdapter);

        db = FirebaseDatabase.getInstance().getReference();

        groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) { Toast.makeText(this, "Group không hợp lệ", Toast.LENGTH_SHORT).show(); finish(); return; }

        listenMessages();

        btnSend.setOnClickListener(v -> {
            String text = edtMsg.getText() != null ? edtMsg.getText().toString().trim() : "";
            if (TextUtils.isEmpty(text)) return;
            sendMessage(text);
            edtMsg.setText("");
        });
    }

    private void listenMessages(){
        db.child("GroupChats").child(groupId).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                        msgs.clear();
                        for (DataSnapshot s : snapshot.getChildren()){
                            Message m = s.getValue(Message.class);
                            if (m != null) {
                                msgs.add(m);
                            }
                        }
                        chatAdapter.notifyDataSetChanged();
                        rv.scrollToPosition(msgs.size()-1);
                    }
                    @Override public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void sendMessage(String text){
        String msgId = db.child("GroupChats").child(groupId).child("messages").push().getKey();
        if (msgId == null) return;
        long now = System.currentTimeMillis();
        Message m = new Message(msgId, myUid, text, now);
        db.child("GroupChats").child(groupId).child("messages").child(msgId).setValue(m)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) Toast.makeText(this, "Gửi thất bại", Toast.LENGTH_SHORT).show();
                });
    }
}
