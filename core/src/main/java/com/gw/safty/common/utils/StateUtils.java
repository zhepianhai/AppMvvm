package com.gw.safty.common.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class StateUtils {
    private static String[] states = {"未督查", "督查中", "已督查"};
    private static String[] fillInStates = {"未填报", "已填报"};
    private static String[] yesOrNo = {"否", "是"};
    private static HashMap<String, String> spinnerEvaluateMap;
    private static ArrayList<String> spinnerEvaluateList;
    private static HashMap<String, String> adminProMap;
    private static ArrayList<String> adminProList;
    private static HashMap<String, String> otherFeeOrgMap;
    private static ArrayList<String> otherFeeOrgList;
    private static HashMap<String, Integer> indexMap;

    public static boolean isInspect(String key) {
        try {
            int index = Integer.parseInt(key) + 1;
            if (index == states.length) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getInspectState(String key) {
        try {
            int index = Integer.parseInt(key);
            if (index < 0 || index >= states.length) {
                return states[0];
            }
            return states[index];
        } catch (NumberFormatException e) {
            return states[0];
        }
    }

    public static String getFillInStates(String key) {
        try {
            int index = Integer.parseInt(key);
            if (index < 0 || index > fillInStates.length) {
                return fillInStates[0];
            }
            return fillInStates[index];
        } catch (NumberFormatException e) {
            return fillInStates[0];
        }

    }

    public static String getYesOrNo(String key) {
        try {
            int index = Integer.parseInt(key);
            if (index < 0 || index > yesOrNo.length) {
                return yesOrNo[0];
            }
            return yesOrNo[index];
        } catch (NumberFormatException e) {
            return yesOrNo[0];
        }
    }

    public static ArrayList<String> getspinnerEvaluate() {
        if (spinnerEvaluateList == null) {
            spinnerEvaluateList = new ArrayList<>();
            spinnerEvaluateList.add("A-能饮用");
            spinnerEvaluateList.add("B-能基本饮用");
            spinnerEvaluateList.add("C-不能饮用");
        }
        return spinnerEvaluateList;
    }

    public static String getspinnerEvaluatKey(String key) {
        if (spinnerEvaluateMap == null) {
            spinnerEvaluateMap = new HashMap<>();
            spinnerEvaluateMap.put("A-能饮用", "A");
            spinnerEvaluateMap.put("B-能基本饮用", "B");
            spinnerEvaluateMap.put("C-不能饮用", "C");
        }
        if (spinnerEvaluateMap.containsKey(key)) {
            return spinnerEvaluateMap.get(key);
        }
        return "A";
    }

    public static ArrayList<String> getadminPro() {
        if (adminProList == null) {
            adminProList = new ArrayList<>();
            adminProList.add("A-专职");
            adminProList.add("B-兼职");
        }
        return adminProList;
    }

    public static String getadminProKey(String key) {
        if (adminProMap == null) {
            adminProMap = new HashMap<>();
            adminProMap.put("A-专职", "A");
            adminProMap.put("B-兼职", "B");
        }
        if (adminProMap.containsKey(key)) {
            return adminProMap.get(key);
        }
        return "A";
    }

    public static ArrayList<String> getotherFeeOrg() {
        if (otherFeeOrgList == null) {
            otherFeeOrgList = new ArrayList<>();
            otherFeeOrgList.add("A-政府");
            otherFeeOrgList.add("B-村委会");
            otherFeeOrgList.add("C-村民自筹");
            otherFeeOrgList.add("D-其他");
        }
        return otherFeeOrgList;
    }

    public static String getotherFeeOrgKey(String key) {
        if (otherFeeOrgMap == null) {
            otherFeeOrgMap = new HashMap<>();
            otherFeeOrgMap.put("A-政府", "A");
            otherFeeOrgMap.put("B-村委会", "B");
            otherFeeOrgMap.put("C-村民自筹", "C");
            otherFeeOrgMap.put("D-其他", "D");
        }
        if (otherFeeOrgMap.containsKey(key)) {
            return otherFeeOrgMap.get(key);
        }
        return "A";
    }

    public static int type2Index(String key) {
        if (indexMap == null) {
            indexMap = new HashMap<>();
            indexMap.put("A", 0);
            indexMap.put("B", 1);
            indexMap.put("C", 2);
            indexMap.put("D", 3);
        }
        if (indexMap.containsKey(key.toUpperCase())) {
            return indexMap.get(key.toUpperCase());
        }
        return 0;
    }

    public static String double2str(double d) {
        try {
            String s = String.valueOf(d);
            return s;
        } catch (Exception e) {
            return "0";
        }
    }

    public static double str2double(String s) {
        try {
            double d = Double.parseDouble(s);
            return d;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int str2int(String s) {
        try {
            int d = Integer.parseInt(s);
            return d;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 类似dart赋值，按顺序返回第一个非空的值
     *
     * @param s
     * @return
     */
    public static String getNotNull(String... s) {
        if (s == null) {
            return "";
        }
        for (int i = 0; i < s.length; i++) {
            if (!TextUtils.isEmpty(s[i])) {
                return s[i];
            }
        }
        return "";
    }
}
