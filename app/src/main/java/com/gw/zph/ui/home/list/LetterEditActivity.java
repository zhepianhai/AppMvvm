package com.gw.zph.ui.home.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gw.safty.common.utils.ToastUtil;
import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.base.db.DbHelper;
import com.gw.zph.base.db.dao.ProBean;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.LetterEditActivityBinding;
import com.gw.zph.ui.login.LoginActivity;
import com.gw.zph.ui.login.LoginViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LetterEditActivity extends BaseActivityImpl {
    private LetterEditActivityBinding binding;
    public SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LetterEditActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_letter_edit );
        binding.getRoot().setFitsSystemWindows(true);
        initObserver();
        setupViewAction();
    }
    private void initObserver(){
        binding.llBack.setOnClickListener(v->{
            this.finish();
        });
        binding.btnFabu.setOnClickListener(v->{
            commit();
        });
    }
    private void setupViewAction(){}
    private void commit(){

        if(binding.etContent.getText().toString().trim().isEmpty()){
            QMUITipDialog dialog = new QMUITipDialog.Builder(this).
                    setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord("请输入发布内容").create();
            dialog.show();
            return;
        }
        ProBean proBean=new ProBean();
        proBean.setContent(binding.etContent.getText().toString().trim());
        proBean.setPer(StatusHolder.INSTANCE.getCurUserBean(this).getPhone());
        proBean.setState("1");
        proBean.setTime(sdf1.format(new Date()));
        proBean.setPersId(StatusHolder.INSTANCE.getCurUserBean(this).getPhone());
        boolean insert = DbHelper.getInstance().getProBeanDaoLongDBManager().insert(proBean);
        QMUITipDialog dialog = new QMUITipDialog.Builder(this).
                setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("提交中...").create();
        dialog.show();
        new Handler().postDelayed(() -> {
            dialog.dismiss();
            if(insert){
                ToastUtil.showToast(LetterEditActivity.this,"发布成功！");
                LetterEditActivity.this.finish();
            }else {
                ToastUtil.showToast(LetterEditActivity.this,"发布失败！");
            }
        }, 3000);
    }
}