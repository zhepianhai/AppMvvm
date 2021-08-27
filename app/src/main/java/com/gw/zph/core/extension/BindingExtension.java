package com.gw.zph.core.extension;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;

import com.gw.zph.callback.EditTextChangeListener;
import com.gw.zph.callback.RadioChangeListener;


/**
 * date: 2019-11-1
 * 用于Databinding 给控件做自定义属性绑定
 * e.g.
 *  //定义
 * @BindingAdapter("labelDrawable")
 * public static void setLabelDrawable(DrawTextView view, Drawable drawable){
 *      view.drawable = drawable;
 * }
 * <p> //使用
 * labelDrawable="@{@drawable/shape_background_state}"
 *
 * 通过 {@BindingAdapter("")} 绑定属性时, 如果需要在[数据改变]时[自动]更新界面, 三种情况：
 *      1.在xml布局界面通过 <variable >...</variable> 标签定义的变量,DataBinding自动使用LiveData包裹此数据
 *      2.在ViewModel中定义的LiveData可直接使用
 *      3.如果是某一对象的某个字段，需要使用 {@Bindable} 对字段的get方法进行注解，参考 全国项目
 */
public class BindingExtension {



    @BindingAdapter("isSelect")
    public static void setTextViewSelect(View view, boolean select){
        view.setSelected(select);
    }

    @BindingAdapter("isGone") //通用
    public static void setIsGone(View view, boolean isGone){
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("isVisible")//通用
    public static void setIsVisible(View view, boolean isVisible){
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    @BindingAdapter("isVisibleOrInVisible")//通用
    public static void setIsInvisible(View view, boolean isVisible){
        view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
    @BindingAdapter("funcEnable")// Spinner通用
    public static void setFuncEnable(Spinner view, boolean enable){
        view.setEnabled(enable);
    }

    @BindingAdapter("checkChangeFun")// RadioButton通用
    public static void setRadioCheckChange(RadioButton button, RadioChangeListener listener){
        button.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null && isChecked){
                listener.onCheck();
            }
        });
    }


    @BindingAdapter("exAfterChange")//EditText通用
    public static void setAfterChangeCallback(EditText editText, EditTextChangeListener listener){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onChange(editable.toString());
            }
        });
    }

    /**
     * 设置 {@link EditText} 是否可输入
     * @param editText 通用参数，DataBinding传入，表示被设置的控件
     * @param editable DataBinding 绑定的界面中 定义的变量，表示是否可输入
     */
    @BindingAdapter("setEditable")// EditText通用
    public static void setEditTextEditable(EditText editText, boolean editable){
        if (editable){
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setOnClickListener(View::requestFocus);
        } else {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setOnClickListener(null);
        }
    }
}
