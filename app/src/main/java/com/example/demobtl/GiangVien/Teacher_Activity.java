package com.example.demobtl.GiangVien;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.GiangVien.QLLop.LopActivity;
import com.example.studentmanagement.GiangVien.QLMonHoc_Diem.MonHocActivity;
import com.example.studentmanagement.GiangVien.XemThongBao.XemThongBaoActivity;
import com.example.studentmanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teacher_Activity extends AppCompatActivity {

    private EditText searchInput;
    private LinearLayout idLopHoc, idMonHoc, idThongBao;
    private BottomNavigationView teacherNavigation;

    private TextView userGreeting;
    private String maGiaoVien;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        // 1. Lấy mã giáo viên từ Intent và kiểm tra null
        maGiaoVien = getIntent().getStringExtra("maGiaoVien");
        if (maGiaoVien == null || maGiaoVien.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã giáo viên!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Ánh xạ tất cả view
        userGreeting    = findViewById(R.id.userGreeting);
        idLopHoc        = findViewById(R.id.idLopHoc);
        idMonHoc        = findViewById(R.id.idMonHoc);
        idThongBao      = findViewById(R.id.idThongBao);
        teacherNavigation = findViewById(R.id.teacher_navigation);

        // 3. Thiết lập click chuyển màn hình, truyền maGiaoVien tiếp
        idMonHoc.setOnClickListener(v -> {
            startActivity(new Intent(this, MonHocActivity.class)
                    .putExtra("maGiaoVien", maGiaoVien));
        });
        idLopHoc.setOnClickListener(v -> {
            startActivity(new Intent(this, LopActivity.class)
                    .putExtra("maGiaoVien", maGiaoVien));
        });
        idThongBao.setOnClickListener(v -> {
            startActivity(new Intent(this, XemThongBaoActivity.class)
                    .putExtra("maGiaoVien", maGiaoVien));
        });

        // 4. Cập nhật greeting từ Firebase
        DatabaseReference gvRef = FirebaseDatabase.getInstance()
                .getReference("GiaoVien")
                .child(maGiaoVien);

        gvRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String tenGV = snapshot.child("tenGV").getValue(String.class);
                if (tenGV != null) {
                    userGreeting.setText("Hi Giáo Viên, " + tenGV + " 👋");
                } else {
                    Toast.makeText(Teacher_Activity.this,
                            "Không tìm thấy tên giáo viên", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Teacher_Activity.this,
                        "Lỗi khi lấy dữ liệu: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Thiết lập BottomNavigationView
        teacherNavigation.setSelectedItemId(R.id.action_home);
        teacherNavigation.setOnItemSelectedListener(item -> {
            scaleSelectedIcon(item);

            int id = item.getItemId();
            if (id == R.id.action_home) {
                Intent intent = new Intent(Teacher_Activity.this, Teacher_Activity.class);
                intent.putExtra("maGiaoVien", maGiaoVien);  // thêm dòng này
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                return true;
            } else if (id == R.id.action_settings) {
                Intent intent = new Intent(Teacher_Activity.this, SettingTeacherActivity.class);
                intent.putExtra("maGiaoVien", maGiaoVien);  // thêm dòng này
                startActivity(intent);
                overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_fade_out);
                return true;
            } else if (id == R.id.action_account) {
                Intent intent = new Intent(Teacher_Activity.this, ThongTinCaNhanGiangVien.class);
                intent.putExtra("maGiaoVien", maGiaoVien);  // thêm dòng này
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            return false;
        });
    }
    private void scaleSelectedIcon(MenuItem selectedItem) {
        for (int i = 0; i < teacherNavigation.getMenu().size(); i++) {
            MenuItem item = teacherNavigation.getMenu().getItem(i);
            View iconView = teacherNavigation.findViewById(item.getItemId());
            if (iconView != null) {
                iconView.animate().scaleX(item == selectedItem ? 1.2f : 1f)
                        .scaleY(item == selectedItem ? 1.2f : 1f)
                        .setDuration(200)
                        .start();
            }
        }
    }
}
