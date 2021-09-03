package com.gw.zph.utils;

public class MapUtils {

    /**
     * 获取政区编码，直辖市的处理方法为取前两位
     * 省级的正常处理
     * 11 北京
     * 12 天津
     * 31 上海
     * 50 重庆
     * 81 香港
     * 82  澳门 特殊处理
     */
    public static final String getCityCode(String rgncd) {
        int adRgncd = getCityAdCode(rgncd);
        if (adRgncd == 11 || adRgncd == 12 || adRgncd == 31 || adRgncd == 50 || adRgncd == 81 || adRgncd == 82) {
            return getRgncd12(adRgncd + "");
        }
        String result = rgncd;
        if (rgncd.length() == 0) {
            return result;
        }
        if (result.length() < 4) {
            return getRgncd12(rgncd);
        }
        return getRgncd12(result);
    }
    /**
     * 取政区前两位
     */
    public static final int getCityAdCode(String rgncd) {
        String result = rgncd;
        if (rgncd.length() == 0) {
            return 0;
        }
        if (result.length() < 2) {
            return JSDateUtil.getDataIntByObj(rgncd);
        }
        result = result.substring(0, 2);
        return JSDateUtil.getDataIntByObj(result);
    }
    /**
     * 获取12位政区编码
     */

    public static final String getRgncd12(String rgncd) {
        if (rgncd == null) {
            return "110000000000";
        }
        String result = rgncd;
        if (rgncd.length() == 0) {
            return result;
        }
        if (rgncd.length() < 12) {
            for (int i = 0; i < 12 - rgncd.length(); ++i) {
                result += "0";
            }
        }
        return result;
    }
}
