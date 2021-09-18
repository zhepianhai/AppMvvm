package com.gw.zph.ui.message.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gw.zph.R;
import com.gw.zph.modle.bean.MsgBean;
import com.gw.zph.utils.JSDateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.RecycleViewHolder>{
    private Context mContext;
    private List<MsgBean> mListData = new ArrayList<>();
    private LayoutInflater mInflater;
    public MsgAdapter(Context mContext, List<MsgBean> mListData) {
        this.mContext = mContext;
        this.mListData.clear();
        this.mListData.addAll(mListData);
        this.mInflater = LayoutInflater.from(mContext);
    }
    public void setList(List<MsgBean> mbean) {

        this.mListData.clear();
        this.mListData.addAll(mbean);
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_recyc_item_msg, parent, false);
        RecycleViewHolder holder = new RecycleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecycleViewHolder holder, int position) {
        if (mListData.size() == 0) {
            return;
        }
        MsgBean item=mListData.get(position);
        try {
            holder.tv1.setText(JSDateUtil.getDataStringByObj(item.getContent()));
            holder.tv2.setText("用户："+JSDateUtil.phoneNumber(item.getPhoneNum()));
            holder.tv3.setText("时间："+JSDateUtil.getDataStringByObj(item.getTime()));
            holder.itemView.setOnClickListener(view -> OnItemClickListener.onVillProjectClickListener( item));

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        public RecycleViewHolder(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
            tv3=itemView.findViewById(R.id.tv3);
        }
    }

    public interface ProItemClickListener {
        void onVillProjectClickListener(MsgBean item);//0点击查看，1删除
    }

    private ProItemClickListener OnItemClickListener;

    public void setOnItemClickListener(ProItemClickListener onItemClickListener) {
        OnItemClickListener = onItemClickListener;
    }
}
