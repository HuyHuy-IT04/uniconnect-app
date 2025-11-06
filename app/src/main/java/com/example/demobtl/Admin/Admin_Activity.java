package com.example.demobtl.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demobtl.Admin.ThongBao.DanhSachThongBaoActivity;
import com.example.demobtl.Admin.ThongBao.GuiThongBaoActivity;

import com.example.demobtl.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_Activity extends AppCompatActivity {

    private EditText searchInput;
    private LinearLayout idThongBao;
    private BottomNavigationView admin_navigation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        idThongBao=findViewById(R.id.idThongBao);
        admin_navigation = findViewById(R.id.admin_navigation);


        idThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Activity.this, DanhSachThongBaoActivity.class);
                startActivity(intent);
            }
        });

        admin_navigation.setOnItemSelectedListener(item -> {
            scaleSelectedIcon(item);

            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                Intent intent = new Intent(Admin_Activity.this, Admin_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                return true;
            } else if (id == R.id.navigation_settings) {
                Intent intent = new Intent(Admin_Activity.this, Setting_admin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_fade_out);
                return true;
            }else if (id == R.id.nav_contacts) {
                Intent intent = new Intent(Admin_Activity.this, GuiThongBaoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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