package com.example.demobtl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Notice> notices;
    NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = findViewById(R.id.listView);
        Button btnAdd = findViewById(R.id.btnAdd);

        // dữ liệu ảo
        notices = new ArrayList<>();
        notices.add(new Notice("Thông báo 1", "Hôm nay nghỉ học"));
        notices.add(new Notice("Thông báo 2", "Thi giữa kì vào thứ 6"));

        adapter = new NoticeAdapter(this, notices);
        lv.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Nhập nội dung thông báo");

        new AlertDialog.Builder(this)
                .setTitle("Thêm thông báo")
                .setView(input)
                .setPositiveButton("Thêm", (d, w) -> {
                    notices.add(new Notice("Thông báo mới", input.getText().toString()));
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

