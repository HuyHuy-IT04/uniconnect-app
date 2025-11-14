package com.example.demobtl.GiangVien;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.R;
import com.example.studentmanagement.SinhVien.Setting_student;
import com.example.studentmanagement.SinhVien.Student_Activity;
import com.example.studentmanagement.SinhVien.ThongTinCaNhanActivity;
import com.example.studentmanagement.User.Login_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Set;

public class SettingTeacherActivity extends AppCompatActivity {

    private Switch switchTheme;
    private LinearLayout btnLogout;
    private BottomNavigationView bottomNav;
    private String maGiaoVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_teacher); // Tên file XML của bạn

        // 1. Lấy mã giáo viên từ Intent và kiểm tra null
        maGiaoVien = getIntent().getStringExtra("maGiaoVien");
        if (maGiaoVien == null || maGiaoVien.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã giáo viên!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Ánh xạ view
        switchTheme = findViewById(R.id.switchTheme);
        btnLogout   = findViewById(R.id.btnLogin);
        bottomNav   = findViewById(R.id.teacher_navigation);

        // Chuyển đổi theme
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this,
                    isChecked ? "Chế độ tối" : "Chế độ sáng",
                    Toast.LENGTH_SHORT).show();
            // TODO: AppCompatDelegate.setDefaultNightMode(...)
        });
        // Đăng xuất với dialog xác nhận
        btnLogout.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (d, w) -> {
                    Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Login_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show()
        );

        // Điều hướng bottom nav
        bottomNav.setSelectedItemId(R.id.action_settings);
        bottomNav.setOnItemSelectedListener(item -> {
            scaleSelectedIcon(item);

            int id = item.getItemId();
            if (id == R.id.action_home) {
                Intent intent = new Intent(SettingTeacherActivity.this, Teacher_Activity.class);
                intent.putExtra("maGiaoVien", maGiaoVien);  // thêm dòng này
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                return true;
            } else if (id == R.id.action_settings) {
                Intent intent = new Intent(SettingTeacherActivity.this, SettingTeacherActivity.class);
                intent.putExtra("maGiaoVien", maGiaoVien);  // thêm dòng này
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (id == R.id.action_account) {
                Intent intent = new Intent(SettingTeacherActivity.this, ThongTinCaNhanGiangVien.class);
                intent.putExtra("maGiaoVien", maGiaoVien);  // thêm dòng này
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            return false;
        });
    }
    private void scaleSelectedIcon(MenuItem selectedItem) {
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            MenuItem item = bottomNav.getMenu().getItem(i);
            View iconView = bottomNav.findViewById(item.getItemId());
            if (iconView != null) {
                iconView.animate().scaleX(item == selectedItem ? 1.2f : 1f)
                        .scaleY(item == selectedItem ? 1.2f : 1f)
                        .setDuration(200)
                        .start();
            }
        }
    }
}

