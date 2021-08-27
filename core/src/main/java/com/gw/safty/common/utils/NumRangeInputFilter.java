package com.gw.safty.common.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * @author smartwangbin@163.com
 * @name ZF_baoshou_android
 * @class name：NumRangeInputFilter
 * @class describe 该类用于editText设置输入数字范围
 * @time 2018/11/7 10:52
 * @Copyright: 2018 本工具类为王彬总结修改，允许公开转载使用，使用情注明出处
 */
public class NumRangeInputFilter implements InputFilter {
    private double min;//最小值
    private double max;//最大值
    private int decimalNumber = 2;//小数点后保留位数

    /**
     * @author smartwangbin wangbin@smartsail.cn
     * @time 2018/11/7  10:01
     * @describe 私有无参构造，不允许调用
     */
    private NumRangeInputFilter() {
    }

    /**
     * @param min           最小值
     * @param max           最大值
     * @param decimalNumber 若取值范围为整数，则 decimalNumber为0
     * @author smartwangbin wangbin@smartsail.cn
     * @time 2018/11/7  10:01
     * @describe 原则上min为最小值，max为最大值，但是程序作了处理输入min和max顺序颠倒也能正常处理
     */
    public NumRangeInputFilter(double min, double max, int decimalNumber) {
        this.min = min >= 0 && max >= 0 ? 0 : (min <= max ? min : max);//如果两个最小值大于0，过滤范围时最小值用0，否则用最小值
        this.max = max <= 0 && min <= 0 ? 0 : (max >= min ? max : min);//如果两个最大值小于0，过滤范围时最大值用0，否则用最大值
        this.decimalNumber = decimalNumber;
    }

    /**
     * @param source 新输入的字符串
     * @param start  新输入的字符串起始下标，一般为0
     * @param end    新输入的字符串终点下标，一般为source长度-1
     * @param dest   之前输入文本框内容
     * @param dstart 在输入时值相同，插入位置，但若在删除内容时，dstart表示删除的开始位置，dend则表示删除前的结束位置；
     * @param dend   在输入时值相同，插入位置，但若在删除内容时，dstart表示删除的开始位置，dend则表示删除前的结束位置；
     * @author smartwangbin wangbin@smartsail.cn
     * @time 2018/11/6  15:50
     * @describe [source, start, end, dest, dstart, dend]
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceContent = source.toString();
        String lastInputContent = dest.toString();
        String resultContent = new StringBuffer(lastInputContent).insert(dstart, sourceContent).toString();
        //验证删除等按键
        if (TextUtils.isEmpty(sourceContent)) {
            //删除内容后数据不符合范围，禁止删除
            if (!isInRange(min, max, new StringBuffer(lastInputContent).delete(dstart, dend).toString())) {
                // 通过添加已删除的内容达到不删除的效果
                return dest.subSequence(dstart, dend);
            }
            //若以"0." 开头的不允许单独删除"0"或者"."
            if (lastInputContent.startsWith("0.")
                    && lastInputContent.length() > 2
                    && ((dstart == 0 && dend == 1) || (dstart == 1 && dend == 2))) {
                return dest.subSequence(dstart, dend);
            }
            return "";
        }
        //以小数点"."开头，默认为设置为“0.”开头
        if (sourceContent.equals(".") && dstart == 0) {
            return "0.";
        }
        //输入“0”，默认设置为以"0."开头
        if (sourceContent.equals("0") && dstart == 0) {
            return "0.";
        }
        //在字符串中间输入或者粘贴带"."的数据
        //小数点位数
        if (resultContent.contains(".")) {
            int index = resultContent.indexOf(".");
            if (resultContent.length() - index - 1 >= decimalNumber + 1) {
                return "";
            }

        }
        //取值范围
        if (!isInRange(min, max, resultContent)) {
            return "";
        }
        return null;
    }

    /**
     * 前后都包含
     *
     * @param a
     * @param b
     * @param s
     * @return
     */
    private boolean isInRange(double a, double b, String s) {
        if (decimalNumber == 0) {
            int c = 0;
            try {
                c = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                c = 0;
            }
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        } else {
            double c = 0;
            try {
                c = Double.parseDouble(s.startsWith(".") ? 0 + s : (s.endsWith(".") ? s + 0 : s));
            } catch (NumberFormatException e) {
                c = 0;
            }
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
