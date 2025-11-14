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

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.VH> {

    public interface OnItemClick { void onClick(Group g); }

    private Context ctx;
    private List<Group> groups;
    private OnItemClick listener;

    public GroupAdapter(Context ctx, List<Group> groups, OnItemClick listener){
        this.ctx = ctx; this.groups = groups; this.listener = listener;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_group, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Group g = groups.get(position);
        holder.tvName.setText(g.name);
        holder.tvCount.setText(g.memberCount + " thành viên");
        holder.itemView.setOnClickListener(v -> { if (listener != null) listener.onClick(g); });
    }

    @Override public int getItemCount() { return groups.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvCount;
        VH(@NonNull View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tvGroupName);
            tvCount = itemView.findViewById(R.id.tvMemberCount);
        }
    }
}
