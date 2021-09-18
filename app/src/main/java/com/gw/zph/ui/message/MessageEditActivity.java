package com.gw.zph.ui.message;

import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gw.safty.common.utils.ToastUtil;
import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.MessageEditActivityBinding;
import com.gw.zph.model.login.bean.UserBean;
import com.gw.zph.modle.AllVm;
import com.gw.zph.modle.bean.MsgBean;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageEditActivity extends BaseActivityImpl {
    private MessageEditActivityBinding binding;
    private AllVm viewModel;
    public SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MessageEditActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_edit);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_message_edit );
        binding.getRoot().setFitsSystemWindows(true);
        viewModel = createViewModel(AllVm.class);
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
    private void setupViewAction(){
        setupBaseViewModel(viewModel);
        viewModel.getAddMsgModel().observe(this,item->{
            if(item!=null){
                ToastUtil.showToast(this,"提交成功");
                MessageEditActivity.this.finish();
            }else{
                ToastUtil.showToast(this,"提交失败");
            }
        });
    }
    private void commit() {

        if (binding.etContent.getText().toString().trim().isEmpty()) {
            QMUITipDialog dialog = new QMUITipDialog.Builder(this).
                    setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord("请输入发布内容").create();
            dialog.show();
            return;
        }
        UserBean userBean= StatusHolder.INSTANCE.getCurUserBean(this);
        MsgBean msgBean=new MsgBean();
        msgBean.setContent(binding.etContent.getText().toString().trim());
        msgBean.setTime(sdf1.format(new Date()));
        msgBean.setPhoneNum(userBean.getPhone());
        viewModel.addMsg(msgBean);
    }
}