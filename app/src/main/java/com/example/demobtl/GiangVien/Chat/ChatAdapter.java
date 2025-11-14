package com.example.demobtl.GiangVien.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demobtl.R;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LEFT = 0, RIGHT = 1;
    private Context ctx;
    private List<Message> items;
    private String myUid;

    public ChatAdapter(Context ctx, List<Message> items, String myUid){
        this.ctx = ctx; this.items = items; this.myUid = myUid;
    }

    @Override public int getItemViewType(int position) {
        return items.get(position).senderId.equals(myUid) ? RIGHT : LEFT;
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == RIGHT) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_message_right, parent, false);
            return new RightVH(v);
        } else {
            View v = LayoutInflater.from(ctx).inflate(R.layout.item_message_left, parent, false);
            return new LeftVH(v);
        }
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message m = items.get(position);
        if (holder instanceof RightVH) ((RightVH) holder).txt.setText(m.text);
        else ((LeftVH) holder).txt.setText(m.text);
    }

    @Override public int getItemCount() { return items.size(); }

    static class LeftVH extends RecyclerView.ViewHolder {
        TextView txt;
        LeftVH(View v){ super(v); txt = v.findViewById(R.id.txtMessageLeft); }
    }
    static class RightVH extends RecyclerView.ViewHolder {
        TextView txt;
        RightVH(View v){ super(v); txt = v.findViewById(R.id.txtMessageRight); }
    }
}
