package com.gw.safty.common.utils;

import android.text.TextUtils;

public class JsonUtils {
    /**
     * 判断字符串是否可以转化为json对象
     *
     * @param content
     * @return
     */
    public static boolean isJsonObject(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        if (content.startsWith("{")) {
            return true;
        }
        return false;
    }
    public static boolean isJSONArray(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        if (content.startsWith("[")) {
            return true;
        }
        return false;
    }

}
