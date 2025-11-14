package com.example.demobtl.GiangVien.Chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demobtl.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.HashMap;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity {
    private TextInputEditText edtName;
    private MaterialButton btnCreate;
    private DatabaseReference db;
    private String teacherId;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_create_group);

        edtName = findViewById(R.id.edtGroupName);
        btnCreate = findViewById(R.id.btnCreate);
        db = FirebaseDatabase.getInstance().getReference();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) teacherId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        else teacherId = "teacher_demo";

        btnCreate.setOnClickListener(v -> {
            String name = edtName.getText() != null ? edtName.getText().toString().trim() : "";
            if (TextUtils.isEmpty(name)) { Toast.makeText(this, "Nhập tên nhóm", Toast.LENGTH_SHORT).show(); return; }
            createGroup(name);
        });
    }

    private void createGroup(String name){
        String groupId = db.child("GroupChats").push().getKey();
        if (groupId == null) { Toast.makeText(this, "Không thể tạo nhóm", Toast.LENGTH_SHORT).show(); return; }
        long now = System.currentTimeMillis();
        Group g = new Group(groupId, name, teacherId, now, 1);

        Map<String, Object> updates = new HashMap<>();
        updates.put("/GroupChats/" + groupId + "/info", g);
        // add owner
        Map<String, Object> owner = new HashMap<>();
        owner.put("userId", teacherId);
        owner.put("role", "teacher");
        updates.put("/GroupChats/" + groupId + "/members/" + teacherId, owner);

        db.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                // index so owner (and any members) can see group quickly
                db.child("StudentGroups").child(teacherId).child(groupId).setValue(true);
                Toast.makeText(this, "Tạo nhóm thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi tạo nhóm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
