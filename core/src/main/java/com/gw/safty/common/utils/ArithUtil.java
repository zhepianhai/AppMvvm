package com.gw.safty.common.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * author : wangbin
 * e-mail : smartwangbin@163.com
 * time   : 2018/01/03
 * desc   : float double 数值不能计算
 * version: 1.0
 */

public class ArithUtil {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    // 这个类不能实例化
    private ArithUtil() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后 10 位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由 scale 参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }

        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");

        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String round1(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return new BigDecimal(v).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String round1(BigDecimal v, int scale) {
        return v.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 两个数比较大小
     *
     * @param v1 第一位
     * @param v2 第二位
     * @return int a = bigdemical.compareTo(bigdemical2)
     * a = -1,表示bigdemical小于bigdemical2；
     * a = 0,表示bigdemical等于bigdemical2；
     * a = 1,表示bigdemical大于bigdemical2；
     */
    public static int compareTo(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        int a = b1.compareTo(b2);
        return a;
    }

    /**
     * 不进行四舍五入，舍去小数末尾的0
     *
     * @param v 需要舍0的数字
     * @return 舍0后的结果
     */
    public static String stripZeros(double v) {
        return new BigDecimal(Double.toString(v)).stripTrailingZeros().toPlainString();
    }

    /**
     * 不进行四舍五入，舍去小数末尾的0
     *
     * @param v 需要舍0的数字
     * @return 舍0后的结果
     */
    public static String stripZeros(Double v) {
        if (v == null) {
            return "0";
        }
        return new BigDecimal(Double.toString(v)).stripTrailingZeros().toPlainString();
    }

    /**
     * 不进行四舍五入，舍去小数末尾的0
     *
     * @param v 需要舍0的数字
     * @return 舍0后的结果
     */
    public static String stripZeros(String v) {
        if (TextUtils.isEmpty(v)) {
            return "0";
        }
        return new BigDecimal(v).stripTrailingZeros().toPlainString();
    }

}
