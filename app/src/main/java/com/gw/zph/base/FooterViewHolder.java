package com.gw.zph.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gw.zph.R;

public class FooterViewHolder extends RecyclerView.ViewHolder {
    public TextView tvFooterTips;
    private FooterViewHolder(@NonNull View root) {
        super(root);
        tvFooterTips = root.findViewById(R.id.tvFooterTip);
    }
    public static FooterViewHolder create(@NonNull ViewGroup parent){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
        return new FooterViewHolder(root);
    }
}
