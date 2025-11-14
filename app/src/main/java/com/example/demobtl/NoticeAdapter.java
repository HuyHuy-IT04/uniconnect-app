package com.example.demobtl;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class NoticeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Notice> list;

    public NoticeAdapter(Context context, ArrayList<Notice> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_2, parent, false);

        TextView t1 = convertView.findViewById(android.R.id.text1);
        TextView t2 = convertView.findViewById(android.R.id.text2);

        Notice n = list.get(pos);

        t1.setText(n.title);
        t2.setText(n.content);

        // CLICK -> SỬA
        convertView.setOnClickListener(v -> showEditDialog(pos));

        // LONG CLICK -> HIỆN POPUP XÓA
        convertView.setOnLongClickListener(v -> {
            showDeleteDialog(pos);
            return true;
        });

        return convertView;
    }

    // --- POPUP SỬA THÔNG BÁO ---
    private void showEditDialog(int pos) {
        Notice n = list.get(pos);

        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(n.content);

        new AlertDialog.Builder(context)
                .setTitle("Sửa thông báo")
                .setView(input)
                .setPositiveButton("Lưu", (d, w) -> {
                    n.content = input.getText().toString();
                    notifyDataSetChanged();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // --- POPUP XÓA ---
    private void showDeleteDialog(int pos) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa thông báo?")
                .setMessage("Bạn có chắc muốn xóa thông báo này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    list.remove(pos);
                    notifyDataSetChanged();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

