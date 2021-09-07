package com.gw.zph.ui.home.list;

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
import com.gw.zph.base.db.dao.ProBean;
import com.gw.zph.utils.JSDateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdpProList extends RecyclerView.Adapter<AdpProList.RecycleViewHolder>{
    private Context mContext;
    private List<ProBean> mListData = new ArrayList<>();
    private LayoutInflater mInflater;
    public AdpProList(Context mContext, List<ProBean> mListData) {
        this.mContext = mContext;
        this.mListData.clear();
        this.mListData.addAll(mListData);
        this.mInflater = LayoutInflater.from(mContext);
    }
    public void setList(List<ProBean> mbean) {

        this.mListData.clear();
        this.mListData.addAll(mbean);
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_recyc_item_pro, parent, false);
        RecycleViewHolder holder = new RecycleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdpProList.RecycleViewHolder holder, int position) {
        if (mListData.size() == 0) {
            return;
        }
        ProBean item=mListData.get(position);
        try {
            holder.tv1.setText(JSDateUtil.getDataStringByObj(item.getContent()));
            holder.tv2.setText(JSDateUtil.getDataStringByObj(item.getPer()));
            holder.tv3.setText(JSDateUtil.getDataStringByObj(item.getTime()));
            holder.itemView.setOnClickListener(view -> OnItemClickListener.onVillProjectClickListener( item));
            if(TextUtils.isEmpty(item.getState())){
                holder.imgSH.setVisibility(View.INVISIBLE);
            }else{
                holder.imgSH.setVisibility(View.VISIBLE);
            }
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
        ImageView imgSH;
        public RecycleViewHolder(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
            tv3=itemView.findViewById(R.id.tv3);
            imgSH=itemView.findViewById(R.id.imgSH);
        }
    }

    public interface ProItemClickListener {
        void onVillProjectClickListener(ProBean item);//0点击查看，1删除
    }

    private ProItemClickListener OnItemClickListener;

    public void setOnItemClickListener(ProItemClickListener onItemClickListener) {
        OnItemClickListener = onItemClickListener;
    }
}
