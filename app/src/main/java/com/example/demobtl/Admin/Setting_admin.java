package com.example.demobtl.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demobtl.Admin.ThongBao.GuiThongBaoActivity;
import com.example.demobtl.R;
//import com.example.demobtl.User.Login_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Setting_admin extends AppCompatActivity {
    BottomNavigationView admin_navigation;
    LinearLayout getBtnLogin;
    LinearLayout btnLogin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_admin);
        admin_navigation = findViewById(R.id.admin_navigation);
//        btnLogin =findViewById(R.id.btnLogin);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new AlertDialog.Builder(Setting_admin.this)
//                        .setTitle("Thoát ứng dụng")
//                        .setMessage("Bạn Có Chắc Muốn Thoát Ứng Dụng Không")
//                        .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // Chuyển về màn hình đăng nhập
//                                Intent intent = new Intent(Setting_admin.this, Login_Activity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("Hủy", null)
//                        .show();
//            }
//        });

        // Gán tab Setting được chọn
        admin_navigation.setSelectedItemId(R.id.navigation_settings);
        admin_navigation.setOnNavigationItemSelectedListener(item -> {
            scaleSelectedIcon(item);

            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                Intent intent = new Intent(Setting_admin.this, Admin_Activity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.navigation_settings) {
                Intent intent = new Intent(Setting_admin.this, Setting_admin.class);
                startActivity(intent);
                return true;
//            } else if (id == R.id.nav_notes) {
//                Intent intent = new Intent(Setting_admin.this, Account_Admin.class);
//                startActivity(intent);
//                return true;
            } else if (id == R.id.nav_contacts) {
            Intent intent = new Intent(Setting_admin.this, GuiThongBaoActivity.class);
            startActivity(intent);
            return true;
             }

            return false;
        });

    }

    private void scaleSelectedIcon(MenuItem selectedItem) {
        for (int i = 0; i < admin_navigation.getMenu().size(); i++) {
            MenuItem item = admin_navigation.getMenu().getItem(i);
            View iconView = admin_navigation.findViewById(item.getItemId());
            if (iconView != null) {
                iconView.animate().scaleX(item == selectedItem ? 1.2f : 1f)
                        .scaleY(item == selectedItem ? 1.2f : 1f)
                        .setDuration(200)
                        .start();
            }
        }
    }

}