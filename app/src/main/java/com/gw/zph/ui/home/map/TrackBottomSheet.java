package com.gw.zph.ui.home.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.gw.zph.R;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;
import com.gw.zph.databinding.TrackedDialogBinding;
import com.gw.zph.ui.home.map.adapter.AdpTracked;
import com.gw.zph.view.BaseFullBottomSheetFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TrackBottomSheet extends BaseFullBottomSheetFragment {
    View view;
    private TrackedDialogBinding binding;
    private List<OffLineLatLngInfo> list=new ArrayList<>();
    private AdpTracked adpTracked;
    public TrackBottomSheet(List<OffLineLatLngInfo> list){
        this.list.clear();
        this.list.addAll(list);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_tracked_dialog, container, false);
        binding=DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLis();
        initRecycle();
    }
    private void initLis(){
        binding.layClose.setOnClickListener(v->{
            dismiss();
        });

    }
    private void initRecycle(){
        adpTracked=new AdpTracked(getContext(),list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recycleTrack.setAdapter(adpTracked);
        binding.recycleTrack.setLayoutManager(linearLayoutManager);
        if(list.size()==0){
            binding.layEmpty.setVisibility(View.VISIBLE);
        }else{
            binding.layEmpty.setVisibility(View.GONE);
        }
    };
}
