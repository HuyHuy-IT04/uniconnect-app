package com.example.demobtl.GiangVien.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demobtl.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fab;
    private TextView txtEmpty;
    private List<Group> list = new ArrayList<>();
    private GroupAdapter adapter;
    private DatabaseReference db;
    private String teacherId;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_group_list);

        rv = findViewById(R.id.rvGroups);
        fab = findViewById(R.id.fabAddGroup);
        txtEmpty = findViewById(R.id.txtEmpty);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupAdapter(this, list, g -> {
            // mở chat room
            Intent i = new Intent(this, ChatRoomActivity.class);
            i.putExtra("groupId", g.groupId);
            i.putExtra("groupName", g.name);
            startActivity(i);
        });
        rv.setAdapter(adapter);

        db = FirebaseDatabase.getInstance().getReference();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) teacherId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        else teacherId = "teacher_demo";

        loadGroups();

        fab.setOnClickListener(v -> startActivity(new Intent(this, CreateGroupActivity.class)));
    }

    private void loadGroups(){
        // load groups where ownerId == teacherId
        db.child("GroupChats").orderByChild("info/ownerId").equalTo(teacherId)
                .addValueEventListener(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot s : snapshot.getChildren()){
                            DataSnapshot info = s.child("info");
                            Group g = info.getValue(Group.class);
                            if (g != null) {
                                g.groupId = s.getKey();
                                // memberCount stored in info.memberCount
                                list.add(g);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        txtEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                    }

                    @Override public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(GroupListActivity.this, "Lỗi tải nhóm", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
