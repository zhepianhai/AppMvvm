package com.gw.zph.ui.home.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gw.zph.R;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdpTracked  extends RecyclerView.Adapter<AdpTracked.RecycleViewHolder>{
    private Context mContext;
    private List<OffLineLatLngInfo> mListData = new ArrayList<>();
    private LayoutInflater mInflater;

    public AdpTracked(Context mContext, List<OffLineLatLngInfo> mListData) {
        this.mContext = mContext;
        this.mListData.clear();
        this.mListData.addAll(mListData);
        this.mInflater = LayoutInflater.from(mContext);
    }
    public void setList(List<OffLineLatLngInfo> mbean) {

        this.mListData.clear();
        this.mListData.addAll(mbean);
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_tracked_adp, parent, false);
        RecycleViewHolder holder = new RecycleViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecycleViewHolder holder, int position) {
        if (mListData.size() == 0) {
            return;
        }
        OffLineLatLngInfo item=mListData.get(position);
        try {
            holder.tv1.setText("时间："+item.getOperateTime());
            holder.tv2.setText("地点："+item.getAddress());
        }catch (Exception e){}
    }
    @Override
    public int getItemCount() {
        return mListData.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {
        TextView tv1;
        TextView tv2;
        public RecycleViewHolder(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
        }
    }
}
