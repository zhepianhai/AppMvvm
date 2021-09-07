package com.gw.zph.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.gw.zph.R;

public class BaseFullBottomSheetFragment extends BottomSheetDialogFragment {
    /**
     * 顶部向下偏移量
     */
    private int topOffset = 0;
    private BottomSheetBehavior<FrameLayout> behavior;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        return new BottomSheetDialog(getContext(), R.style.TransparentBottomSheetStyle);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        // 设置软键盘不自动弹出
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = getHeight();
            behavior = BottomSheetBehavior.from(bottomSheet);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.3f;

        window.setAttributes(windowParams);
    }
    /**
     * 获取屏幕高度
     *
     * @return height
     */
    private int getHeight() {
        int height = 1920;
        if (getContext() != null) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            if (wm != null) {
                // 使用Point已经减去了状态栏高度
                wm.getDefaultDisplay().getSize(point);
                height = point.y - getTopOffset();
            }
        }
        return height;
    }
    public int getTopOffset() {
        return topOffset;
    }
    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }
    public BottomSheetBehavior<FrameLayout> getBehavior() {
        return behavior;
    }
}
