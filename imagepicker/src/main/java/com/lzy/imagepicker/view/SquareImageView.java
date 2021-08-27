package com.lzy.imagepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.lzy.imagepicker.R;

/**
 * <pre>
 *     author : zhangpengfei
 *     e-mail : 18242312549@163.com
 *     time   : 2019/04/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SquareImageView extends AppCompatImageView {

    private Paint mPaint;

    float width, height;

    private int leftTopRadius;
    private int leftBottomRadius;
    private int rightTopRadius;
    private int rightBottomRadius;

    private int radius;
    private int defaultRadius = 0;

    private Path mPath;
    public SquareImageView(Context context) {
        this(context,null);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs){
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mPaint=new Paint();
        mPath=new Path();

        if (null!=attrs){
            // 读取配置
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Custom_Round_Image_View);
            radius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_radius, defaultRadius);
            leftTopRadius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_left_top_radius, defaultRadius);
            rightTopRadius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_right_top_radius, defaultRadius);
            rightBottomRadius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_right_bottom_radius, defaultRadius);
            leftBottomRadius = array.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_left_bottom_radius, defaultRadius);


            //如果四个角的值没有设置，那么就使用通用的radius的值。
            if (defaultRadius == leftTopRadius) {
                leftTopRadius = radius;
            }
            if (defaultRadius == rightTopRadius) {
                rightTopRadius = radius;
            }
            if (defaultRadius == rightBottomRadius) {
                rightBottomRadius = radius;
            }
            if (defaultRadius == leftBottomRadius) {
                leftBottomRadius = radius;
            }
            array.recycle();
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        /**
         * 设置为正方形
         */
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //这里做下判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
        int maxLeft = Math.max(leftTopRadius, leftBottomRadius);
        int maxRight = Math.max(rightTopRadius, rightBottomRadius);
        int minWidth = maxLeft + maxRight;
        int maxTop = Math.max(leftTopRadius, rightTopRadius);
        int maxBottom = Math.max(leftBottomRadius, rightBottomRadius);
        int minHeight = maxTop + maxBottom;
        if (width >= minWidth && height > minHeight) {
            //四个角：右上，右下，左下，左上
            mPath.moveTo(leftTopRadius, 0);
            mPath.lineTo(width - rightTopRadius, 0);
            mPath.quadTo(width, 0, width, rightTopRadius);

            mPath.lineTo(width, height - rightBottomRadius);
            mPath.quadTo(width, height, width - rightBottomRadius, height);

            mPath.lineTo(leftBottomRadius, height);
            mPath.quadTo(0, height, 0, height - leftBottomRadius);

            mPath.lineTo(0, leftTopRadius);
            mPath.quadTo(0, 0, leftTopRadius, 0);

            canvas.clipPath(mPath);
        }

        super.onDraw(canvas);

    }







}
