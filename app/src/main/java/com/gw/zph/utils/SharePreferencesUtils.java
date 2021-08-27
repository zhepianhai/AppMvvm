package com.gw.zph.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

import java.util.List;

public class SharePreferencesUtils {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";

    public static final String TOKEN_DATA = "token_data";

    private static final String TOKEN_NAME_DATA = "token_name_data";

    private static final String COOKIE_DATA = "cookie_data";
    private static final String ADD_CODE = "ADD_CODE";
    private static final String KEY_LOGIN_RESPONSE_DATA = "KEY_LOGIN_RESPONSE_DATA";
    public static final String KEY_RULEMAP = "KEY_RULEMAP";

    /**
     * 测站维护管理 总分
     */
    public static final String STATION_STATE_SCORE = "STATION_STATE_SCORE";

    /**
     * 监测数据质量和报到率 总分
     */
    public static final String QUALITY_REPORTRATESTATE_SCORE = "QUALITY_REPORTRATESTATE_SCORE";
    /**
     * 水闸水库的inspGroupId
     */
    public static final String inspGroupId = "INSPGROUPID";
    private static final String NEMO_PWD = "NEMO_PWD";
    private static final String PER_INF_BEAN = "PER_INF_BEAN";
    private static final String NEMO_STATE = "NEMO_STATE";

    /**
     * 资源卫星中心token
     * */
    public static final String GLOBEVIEW_TOKEN="globe_view_token";
    /**
     * 省级平台的一张图的权限数据
     * */
    public static final String POWER_ONEMAP="power_onemap";

    /**
     * 存入一个boolean型的参数
     *
     * @param context 上下文
     * @param key     存入的key
     * @param bool    存入的值
     */
    public static void setBooleanParam(Context context, String key, boolean bool) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, bool);
        editor.commit();
    }

    /**
     * 取出一个boolean型的参数
     *
     * @param context        上下文
     * @param key            取出的key
     * @param defaultBoolean 如果没有对应的值，默认的值
     * @return
     */
    public static boolean getBooleanParam(Context context, String key, boolean defaultBoolean) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        boolean aBoolean = sp.getBoolean(key, defaultBoolean);
        return aBoolean;
    }

    /**
     * 设置sp参数 string 类型的
     * @param key
     * @param defaultStr
     */
    public static void setStringParam(String key, String defaultStr) {
        MMKV.mmkvWithID(FILE_NAME, Context.MODE_PRIVATE).putString(key, defaultStr);
    }

//    /**
//     * 获取sp参数 string 类型的
//     *
//     * @param context
//     * @param key
//     * @param defaultBoolean
//     * @return
//     */
//    public static String getStringParam(Context context, String key, String defaultBoolean) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        String result = sp.getString(key, Constants.BLANK);
//        return result;
//    }
//
//    /**
//     * 设置sp参数 string 类型的
//     */
//    public static void setStringInspGroupId(Context context, String value) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putString(inspGroupId, value);
//        edit.apply();
//    }
//
//    /**
//     * 获取sp参数 string 类型的
//     */
//    public static String getStringInspGroupId(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        String result = sp.getString(inspGroupId, Constants.BLANK);
//        return result;
//    }
//
//    /**
//     * @param context
//     * @param key
//     * @return
//     */
//    public static String getStringParam(Context context, String key) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        String result = sp.getString(key, Constants.BLANK);
//        return result;
//    }
//
//    /**
//     * 测站维护管理 总分
//     *
//     * @param context
//     * @param defaultStr
//     */
//    public static void setStationStateScore(Context context, String defaultStr) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putString(STATION_STATE_SCORE, defaultStr);
//        edit.apply();
//    }
//
//    public static String getStationStateScore(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        String result = sp.getString(STATION_STATE_SCORE, Constants.BLANK);
//        return result;
//    }

//    /**
//     * 监测数据质量和报到率 总分
//     *
//     * @param context
//     * @param defaultStr
//     */
//    public static void setQualityReportratestateScore(Context context, String defaultStr) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putString(QUALITY_REPORTRATESTATE_SCORE, defaultStr);
//        edit.apply();
//    }
//
//    public static String getQualityReportratestateScore(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        String result = sp.getString(QUALITY_REPORTRATESTATE_SCORE, Constants.BLANK);
//        return result;
//    }

//    /**
//     * 设置了补丁参数值
//     *
//     * @param context
//     */
//    public static void setTokenData(Context context, String value) {
//        setStringParam(context, TOKEN_DATA, value);
//    }

//    /**
//     * 获取补丁参数值
//     *
//     * @param context
//     * @return
//     */
//    public static String getTokenData(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getString(TOKEN_DATA, Constants.BLANK);
//    }
//
//
//    /**
//     * 设置了用户名称
//     *
//     * @param context
//     */
//    public static void setTokenNameData(Context context, String value) {
//        setStringParam(context, TOKEN_NAME_DATA, value);
//    }

//    /**
//     * 获取用户名称
//     *
//     * @param context
//     * @return
//     */
//    public static String getTokenNameData(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getString(TOKEN_NAME_DATA, Constants.BLANK);
//    }
//
//    /**
//     * 设置了补丁参数值
//     *
//     * @param context
//     */
//    public static void setCookieData(Context context, String value) {
//        setStringParam(context, COOKIE_DATA, value);
//    }
//
//    /**
//     * 获取补丁参数值
//     *
//     * @param context
//     * @return
//     */
//    public static String getCookieData(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getString(COOKIE_DATA, Constants.BLANK);
//    }
//
//    /**
//     * 用指定类型的bean取出实体类
//     *
//     * @param context
//     * @param key
//     * @param clazz
//     * @param <T>
//     * @return
//     */
//    public static <T> T getObjFromString(Context context, String key, Class<T> clazz) {
//        String s = getStringParam(context, key, "");
//        if (s.isEmpty()) {
//            return null;
//        } else {
//            Object object = new Gson().fromJson(s, clazz);
//            return Primitives.wrap(clazz).cast(object);
//        }
//    }
//
//    /**
//     * 打分规则
//     *
//     * @param context
//     * @param key
//     * @return
//     */
//    public static Map<String, ArrayList<RuleBean>> getMapScrolRuleFromString(Context context, String key) {
//        String str = getStringParam(context, key, "");
//        if (TextUtils.isEmpty(str)) {
//            return null;
//        } else {
//            java.lang.reflect.Type maptype = new TypeToken<HashMap<String, ArrayList<RuleBean>>>() {
//            }.getType();
//            Map<String, ArrayList<RuleBean>> datMap = new Gson().fromJson(str, maptype);
//            return datMap;
//        }
//
//
//    }
//

    /**
     * 存储已经选择的code
     */
//    public static void putAreaByAddCode(List<AreaByPerBean> list) {
//        StringBuilder s = new StringBuilder();
//
//        if (list == null || list.isEmpty()) {
//            return;
//        }
//        for (AreaByPerBean bean : list) {
//            if (TextUtils.isEmpty(bean.getAD_CODE())) {
//                continue;
//            }
//            s.append(bean.getAD_CODE()).append(",");
//        }
//        String s1 = s.toString().trim();
//        s1 = s1.endsWith(",") ? s1.substring(0, s1.length() - 1) : s1;
//        setStringParam(ADD_CODE, s1);
//    }
//
//    /**
//     * 存储已经选择的code
//     *
//     * @param context
//     */
//    public static void putAreaByAddCodeSql(Context context, List<AddressBean> list) {
//        StringBuilder s = new StringBuilder();
//
//        if (list == null || list.isEmpty()) {
//            return;
//        }
//        for (AddressBean bean : list) {
//            if (TextUtils.isEmpty(bean.getAdCode())) {
//                continue;
//            }
//            s.append(bean.getAdCode()).append(",");
//        }
//        String s1 = s.toString().trim();
//        s1 = s1.endsWith(",") ? s1.substring(0, s1.length() - 1) : s1;
//        setStringParam(context, ADD_CODE, s1);
//    }
//
//
    /**
     * 获取已经选中的code
     *
     * @return
     */
    public static String[] getAreaByAddCode() {
        String s = MMKV.defaultMMKV().getString(ADD_CODE, "");
        if (!TextUtils.isEmpty(s)) {
            return s.split(",");
        }
        return null;
    }
//
    /**
     * 获取已经选中的code
     *
     * @return
     */
    public static void clearAreaByAddCode() {
        setStringParam(ADD_CODE, "");
    }
//
//    /**
//     * 保存小鱼登陆后的账号
//     *
//     * @param context
//     * @param data
//     */
//    public static void putLoginResponseData(Context context, LoginResponseData data) {
//        String json = GsonUtils.GsonString(data);
//        setStringParam(context, KEY_LOGIN_RESPONSE_DATA, json);
//    }
//
//    /**
//     * 保存小鱼登陆后的账号
//     *
//     * @param context
//     */
//    public static LoginResponseData getLoginResponseData(Context context) {
//        LoginResponseData data = getObjFromString(context, KEY_LOGIN_RESPONSE_DATA, LoginResponseData.class);
//        return data == null ? new LoginResponseData(-1, -1, -1, "", "", "") : data;
//    }
//
//    /**
//     * 保存规则
//     *
//     * @param context
//     * @param data
//     */
//    public static void putRuleMap(Context context, Map<String, ArrayList<RuleBean>> data) {
//        String json = GsonUtils.GsonString(data);
//        setStringParam(context, KEY_RULEMAP, json);
//    }
//
//    /**
//     * 保存规则
//     *
//     * @param context
//     * @param json
//     */
//    public static void putRuleMap(Context context, String json) {
//        setStringParam(context, KEY_RULEMAP, json);
//    }
//
//    /**
//     * 获取打分规则
//     *
//     * @param context
//     */
//    public static Map<String, ArrayList<RuleBean>> getRuleMap(Context context) {
//        Map<String, ArrayList<RuleBean>> data = getMapScrolRuleFromString(context, KEY_RULEMAP);
//        return data == null ? new HashMap<String, ArrayList<RuleBean>>() : data;
//    }
//
//    /**
//     * 获取小鱼密码
//     *
//     * @return
//     */
//    public static String getNemoPwd() {
//        return getStringParam(MyApplication.getContext(), NEMO_PWD, "");
//    }
//
//    /**
//     * 设置小鱼密码
//     *
//     * @return
//     */
//    public static void setNemoPwd(String pwd) {
//        setStringParam(MyApplication.getContext(), NEMO_PWD, pwd);
//    }
//
//    /**
//     * 获取小鱼登陆状态
//     *
//     * @return
//     */
//    public static boolean getNemoState() {
//        return getBooleanParam(MyApplication.getContext(), NEMO_STATE, false);
//    }
//
//    /**
//     * 设置小鱼登陆状态
//     *
//     * @return
//     */
//    public static void setNemoState(boolean b) {
//        setBooleanParam(MyApplication.getContext(), NEMO_STATE, b);
//    }
//
//    /**
//     * 保存个人信息
//     *
//     * @param data
//     */
//    public static void setPerInfBean(PerInfBean data) {
//        String json = GsonUtils.GsonString(data);
//        setStringParam(MyApplication.getContext(), PER_INF_BEAN, json);
//    }
//
//    /**
//     * 读取个人信息
//     */
//    public static PerInfBean getPerInfBean() {
//        return getObjFromString(MyApplication.getContext(), PER_INF_BEAN, PerInfBean.class);
//    }
//    public static void clearPowerList(Context context){
//        setStringParam(context,POWER_ONEMAP,"");
//    }

}
