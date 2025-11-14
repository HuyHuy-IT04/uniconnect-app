package com.example.demobtl.GiangVien;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.demobtl.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThongTinCaNhanGiangVien extends AppCompatActivity {

    private ImageView profileImage;
    private TextView fullName, maGV, email, phone, ngaySinh, gioiTinh, queQuan, maKhoa;
    private BottomNavigationView teacherNavigation;
    private String maGiangVien;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan_giaovien);

        profileImage = findViewById(R.id.profile_image);
        fullName     = findViewById(R.id.full_name);
        maGV         = findViewById(R.id.magv);
        email        = findViewById(R.id.email);
        phone        = findViewById(R.id.phone);
        ngaySinh     = findViewById(R.id.ngay_sinh);
        gioiTinh     = findViewById(R.id.gioi_tinh);
        queQuan      = findViewById(R.id.que_quan);
        maKhoa       = findViewById(R.id.ma_khoa);

        maGiangVien = getIntent().getStringExtra("maGiaoVien");
        if (maGiangVien == null || maGiangVien.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã giảng viên!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("GiaoVien")
                .child(maGiangVien);

        loadTeacherInfo();

        teacherNavigation = findViewById(R.id.teacher_navigation);
        teacherNavigation.setSelectedItemId(R.id.action_account);
        teacherNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_home) {
                Intent intent = new Intent(this, Teacher_Activity.class);
                intent.putExtra("maGiaoVien", maGiangVien);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (id == R.id.action_settings) {
                Intent intent = new Intent(this, SettingTeacherActivity.class);
                intent.putExtra("maGiaoVien", maGiangVien);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            return id == R.id.action_account;
        });
    }

    private void loadTeacherInfo() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(ThongTinCaNhanGiangVien.this,
                            "Không tìm thấy thông tin giảng viên", Toast.LENGTH_SHORT).show();
                    return;
                }

                String tenGV     = snapshot.child("tenGV").getValue(String.class);
                String anh       = snapshot.child("anh").getValue(String.class);
                String emailStr  = snapshot.child("email").getValue(String.class);
                String phoneStr  = snapshot.child("phone").getValue(String.class);
                String ngayStr   = snapshot.child("ngaySinh").getValue(String.class);
                String gioiTinhStr = snapshot.child("gioiTinh").getValue(String.class);
                String diaChiStr = snapshot.child("diaChi").getValue(String.class);
                String khoaStr   = snapshot.child("maKhoa").getValue(String.class);

                fullName.setText("Họ tên: " + tenGV);
                maGV.setText("Mã GV: " + maGiangVien);
                email.setText("Email: " + emailStr);
                phone.setText("SĐT: " + phoneStr);
                ngaySinh.setText("Ngày sinh: " + ngayStr);
                gioiTinh.setText("Giới tính: " + gioiTinhStr);
                queQuan.setText("Địa chỉ: " + diaChiStr);
                maKhoa.setText("Mã khoa: " + khoaStr);

                Glide.with(ThongTinCaNhanGiangVien.this)
                        .load(anh)
                        .placeholder(R.drawable.icon_student)
                        .error(R.drawable.icon_student)
                        .into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThongTinCaNhanGiangVien.this,
                        "Lỗi tải dữ liệu: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
