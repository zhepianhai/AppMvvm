package com.gw.safty.common.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class AnimUitls {
    public static final int ANIM_DURATION=480;

    /**
     * 动画-从上往下-从下往上
     * */
    public static void animFormTopToBuoom(View mView){
        if(mView.getVisibility()==View.VISIBLE) {
            TranslateAnimation mHiddenAction = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f);
            mHiddenAction.setDuration(ANIM_DURATION);
            Interpolator interpolator = new DecelerateInterpolator();//开始加速再减速
            mHiddenAction.setInterpolator(interpolator);
            mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mView.startAnimation(mHiddenAction);
        }else{
            TranslateAnimation mShowAction = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF,-1f,
                    Animation.RELATIVE_TO_SELF, 0f);
            mShowAction.setDuration(600);
            Interpolator interpolator = new DecelerateInterpolator();//开始加速再减速
            mShowAction.setInterpolator(interpolator);
            mView.setVisibility(View.VISIBLE);
            mView.startAnimation(mShowAction);
        }
    }


    public static void animFormTopToBuoomPadding(View mView,int pading){
        if(mView.getVisibility()==View.VISIBLE) {
            TranslateAnimation mHiddenAction = new TranslateAnimation(
                    0, 0, 0, -36);
            mHiddenAction.setDuration(ANIM_DURATION);
            Interpolator interpolator = new DecelerateInterpolator();//开始加速再减速
            mHiddenAction.setInterpolator(interpolator);
            mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.topMargin=pading;
                    mView.setLayoutParams(params);
//                    mView.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mView.startAnimation(mHiddenAction);
        }else{
            TranslateAnimation mShowAction = new TranslateAnimation(
                    0, 0, -36, 0);
            mShowAction.setDuration(600);
            Interpolator interpolator = new DecelerateInterpolator();//开始加速再减速
            mShowAction.setInterpolator(interpolator);
            mView.setVisibility(View.VISIBLE);
            mView.startAnimation(mShowAction);
        }
    }
}
