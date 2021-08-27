package com.gw.zph.base;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.gw.zph.R;

/**
 * Created by dell on 2016/8/10.
 * <p/>
 * 1、继承这个类，子类布局应该：
 * <android.support.v7.widget.Toolbar
 * android:id="@+id/my_toolbar"
 * style="@style/my_toolbar_style"
 * app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />
 * <p>
 * <p/>
 * 2、子类应该：
 * <p/>
 */
public abstract class BaseToolBarTitleActivity extends BaseActivityImpl {
    //@BindView(R.id.toolbar_title)
    protected TextView mToolBarTitle;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 主要是设置这个值
     * mToolBarTitle
     * R.id.toolbar_title
     */
    protected abstract void setToolbarValue();

    protected void initToolbar(Toolbar toolbar) {
        this.mToolBar = toolbar;
        if (getActionBarMenu() > 0) {
        }
    }

    protected int getActionBarMenu() {
        return 0;//R.menu.base_search;
    }

    /**
     * 处理点击返回按钮逻辑
     *
     * @return
     */
    protected void onBackClick() {
        finish();
    }

    protected boolean onSearchClick() {
        return true;
    }

    protected boolean onCustomActionBarClick() {
        return true;
    }

    protected void setToolbarTitle(String title) {
        if (mToolBar != null) {
            mToolBarTitle.setText(title);
        }
    }

    protected void setToolbarTitle(int titleRes) {
        if (mToolBar != null) {
            mToolBarTitle.setText(titleRes);
        }
    }

    /**
     * 设置返回按钮
     *
     * @param status
     */
    protected void setBackBtnStatus(boolean status) {
        if (mToolBar != null) {
            if (status) {
                mToolBar.setNavigationIcon(R.mipmap.back);
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackClick();
                    }
                });
            }
        }
    }

    protected Toolbar getToolBar() {
        return mToolBar;
    }

}
