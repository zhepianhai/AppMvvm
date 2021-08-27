package com.gw.zph.core.util.number;

import android.text.TextUtils;

public class NumeralUtil {


    public static long parseLong(String value, long defaultValue){
        if (TextUtils.isEmpty(value)) return defaultValue;
        try {
            if (isWholeNumber(value) || checkNumber(value)) return Long.parseLong(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
        return defaultValue;
    }

    public static int parseInt(String value, int defaultValue){
        if (TextUtils.isEmpty(value)) return defaultValue;
        try {
            if (isWholeNumber(value) || checkNumber(value)) return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
        return defaultValue;
    }

    public static double parseDouble(String value, double defaultValue){
        if (TextUtils.isEmpty(value)) return defaultValue;
        try {
            if (isDecimal(value) || checkNumber(value)) return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
        return defaultValue;
    }

    public static float parseFloat(String value, float defaultValue){
        if (TextUtils.isEmpty(value)) return defaultValue;
        try {
            if (isDecimal(value) || checkNumber(value)) return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
        return defaultValue;
    }

    /**
     * 是否数字
     * @param orginal 文本
     * @return true or false
     */
    public static boolean checkNumber(String orginal){
        if (TextUtils.isEmpty(orginal)) return false;
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return orginal.matches(regex);
    }
    /**
     * 是否正整数
     * @param orginal 文本
     * @return true or false
     */
    public static boolean isPositiveInteger(String orginal) {
        if (TextUtils.isEmpty(orginal)) return false;
        return orginal.matches("^\\+{0,1}[1-9]\\d*");
    }

    /**
     * 是否负整数
     * @param orginal 文本
     * @return true or false
     */
    public static boolean isNegativeInteger(String orginal) {
        if (TextUtils.isEmpty(orginal)) return false;
        return orginal.matches("^-[1-9]\\d*");
    }


    /**
     * 是否整数
     * @param orginal 文本
     * @return true or false
     */
    public static boolean isWholeNumber(String orginal) {
        if (TextUtils.isEmpty(orginal)) return false;
        return orginal.matches("[+-]{0,1}0") || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    /**
     * 是否正小数
     * @param orginal 文本
     * @return true or false
     */
    public static boolean isPositiveDecimal(String orginal){
        if (TextUtils.isEmpty(orginal)) return false;
        return orginal.matches("(\\+{0,1}[0]\\.[1-9]*)|(\\+{0,1}[1-9]\\d*\\.\\d*)");
    }

    /**
     * 是否负小数
     * @param orginal 文本
     * @return true or false
     */
    public static boolean isNegativeDecimal(String orginal){
        if (TextUtils.isEmpty(orginal)) return false;
        return orginal.matches("^((-[0]\\.[1-9]*)|(-[1-9]\\d*\\.\\d*))");
    }


    /**
     * 是否小数
     * @param orginal 文本
     * @return true or false
     */
    public static boolean isDecimal(String orginal){
        if (TextUtils.isEmpty(orginal)) return false;
        return orginal.matches("([-+]{0,1}\\d+\\.\\d*)|([-+]{0,1}\\d*\\.\\d+)");
    }

    public static boolean isRealNumber(String orginal){
        if (TextUtils.isEmpty(orginal)) return false;
        return isWholeNumber(orginal) || isDecimal(orginal);
    }
}
