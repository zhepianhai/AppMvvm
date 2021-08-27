package cn.baililuohui.dropdownmenu;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 仅做测试使用
 */

public class TestAdapter implements MenuAdapter {
    private List<String> titles = new ArrayList<>();

    @Override
    public int getMenuCount() {
        return titles.size();
    }

    @Override
    public void onMenuTabCreate(@NotNull DrawableTabView tab, int position) {
        tab.getTextView().setText(titles.get(position));
    }

    @Override
    public void onViewShow(@NotNull View view, int position, @NotNull DrawableTabView tab) {

    }

    @Override
    public void onViewDismiss(@NotNull View view, int position, @NotNull DrawableTabView tab) {

    }

    @NotNull
    @Override
    public View onCreateView(ViewGroup parent, int position) {
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        frameLayout.setBackgroundColor(Color.parseColor("#66666666"));
        TextView textView = new TextView(parent.getContext());
        textView.setText(titles.get(position));
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(Color.GREEN);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameLayout.addView(textView, layoutParams);
        return frameLayout;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
}
