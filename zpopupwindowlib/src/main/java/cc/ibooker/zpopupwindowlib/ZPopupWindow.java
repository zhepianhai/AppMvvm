package cc.ibooker.zpopupwindowlib;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * 自定义弹出的PopupWindow，增加半透明蒙层。
 * 实现原理：
 * 在弹出自定义的PopupWindow时，增加一个半透明蒙层view到窗口，并置于PopupWindow下方。
 * 使用方法
 * 继承BottomPushPopupWindow，编写generateCustomView添加自定义的view，调用show方法显示。
 *
 * @author 王彬
 */
public abstract class ZPopupWindow extends PopupWindow implements ViewModelStoreOwner {
    private Context context;
    private WindowManager wm;
    public View maskView;
    private int maskHeight;
    private int maskGravity = Gravity.CENTER;
    private ViewModelStore viewModelStore;




    public ZPopupWindow(Context context) {
        super(context);
        if (viewModelStore == null){
            viewModelStore = new ViewModelStore();
        }
        this.context = context;
        setContentView(generateCustomView(context));
        this.maskHeight = getScreenH(context);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
        setAnimationStyle(R.style.BottomPushPopupWindow);
        // 关闭所有ZPopupWindow
        ZPopupWindowUtil.getInstance().clearZPopupWindow();
        // 注册相应广播
        regReceiver();
    }

    // 抽象方法
    protected abstract View generateCustomView(Context context);

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {

        this.showAtLocation(parent, gravity, x, y,true);
    }
    public void showAtLocation(View parent, int gravity, int x, int y,boolean flag) {
        if(flag) {
            addMaskView(parent.getWindowToken(), maskHeight, maskGravity,true);
        }
        super.showAtLocation(parent, gravity, x, y);
    }
    public void showAtLocation(View parent, int gravity, int x, int y,boolean flag,boolean flag2) {
        if(flag) {
            addMaskView(parent.getWindowToken(), maskHeight, maskGravity,flag2);
        }
        super.showAtLocation(parent, gravity, x, y);
    }
    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {

        this.showAsDropDown(anchor, xoff, yoff,true);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff,boolean flag) {
        if(flag) {
            addMaskView(anchor.getWindowToken(), maskHeight, maskGravity,true);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }
    public void showAsDropDown(View anchor, int xoff, int yoff,boolean flag,boolean flag2) {
        if(flag) {
            addMaskView(anchor.getWindowToken(), maskHeight, maskGravity,flag2);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void setAnimationStyle(int animationStyle) {
        super.setAnimationStyle(animationStyle);
    }

    @Override
    public void dismiss() {
        try {
            if (receiver != null) // 注销广播
                context.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 刷新界面
        removeMaskView();
        getViewModelStore().clear();
        super.dismiss();
    }

    /**
     * 显示在界面的底部
     */
    public void showBottom() {
        if (context != null)
            showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 显示在界面的顶部
     */
    public void showTop() {
        if (context != null)
            showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 显示在指定View的正上方
     *
     * @param view    指定的View
     * @param yOffset Y轴偏移量
     */
    public void showViewTop(View view, int yOffset) {
        if (context != null) {
            this.maskHeight = getScreenH(context) - view.getHeight() - yOffset;
            // 获取需要在其上方显示的控件的位置信息
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            // 获取自身的长宽高
            this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupHeight = this.getContentView().getMeasuredHeight();
            int popupWidth = this.getContentView().getMeasuredWidth();
            //在控件上方显示
            maskGravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            showAtLocation(view, Gravity.NO_GRAVITY, location[0] + popupWidth / 2, location[1] - popupHeight - yOffset);
        }
    }

    /**
     * 显示在指定View的正下方
     *
     * @param view    指定的View
     * @param yOffset Y轴偏移量
     */
    public void showViewBottom(View view, int yOffset) {
//        this.maskHeight = getScreenH(context) - getStatusHeight((Activity) context) - view.getHeight() - yOffset;
//        // 获取需要在其上方显示的控件的位置信息
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        // 获取自身的长宽高
//        this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
////        int popupHeight = this.getContentView().getMeasuredHeight();
//        int popupWidth = this.getContentView().getMeasuredWidth();
//        // 在控件上方显示
//        maskGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//        showAtLocation(view, Gravity.NO_GRAVITY, (location[0]) + popupWidth / 2, location[1] + view.getHeight() + yOffset);
        if (context != null) {
            this.maskHeight = getScreenH(context) - getStatusHeight((Activity) context) - view.getHeight() - yOffset;
            maskGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            showAsDropDown(view, 0, yOffset);
        }
    }

    // 注册广播接收器，接收暗屏广播，锁屏广播
    private void regReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        context.registerReceiver(receiver, filter);
        // 广播开启的同时，添加到管理类
        ZPopupWindowUtil.getInstance().clearZPopupWindow();
        ZPopupWindowUtil.getInstance().addZPopupWindow(this);
    }

    // 定义一个广播接收器，帮助关闭PopuWindow
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action) || Intent.ACTION_USER_PRESENT.equals(action)) {
                dismiss();
            }
        }
    };

    // 添加遮罩层
    private void addMaskView(IBinder token, int maskHeight, int gravity,boolean flag) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = maskHeight;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        params.token = token;
        params.windowAnimations = android.R.style.Animation_Toast;
        params.gravity = gravity;
        maskView = new View(context);
        maskView.setBackgroundColor(Color.parseColor("#66000000"));
        maskView.setFitsSystemWindows(false);
        if(flag) {
            maskView.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                        removeMaskView();
                        return true;
                    }
                    return false;
                }
            });
        }
        wm.addView(maskView, params);
    }

    // 移除遮罩层
    private void removeMaskView() {
        if (maskView != null) {
            wm.removeViewImmediate(maskView);
            maskView = null;
        }
    }

    /**
     * 获取屏幕高度（包括状态栏的高度） - px
     */
    private int getScreenH(Context aty) {
        WindowManager wm = (WindowManager) aty.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.heightPixels+getDaoHangHeight(aty);
    }

    /**
     * 获取状态栏高度
     */
    private int getStatusHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

      /* 获取导航栏高度
     * @param context
     * @return
             */
      public static int getDaoHangHeight(Context context) {
          int result = 0;
          int resourceId=0;
          int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
          if (rid!=0){ resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");

          return context.getResources().getDimensionPixelSize(resourceId);
          }else return 0; }


    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
          if (viewModelStore == null){
              viewModelStore = new ViewModelStore();
          }
        return viewModelStore;
    }

}