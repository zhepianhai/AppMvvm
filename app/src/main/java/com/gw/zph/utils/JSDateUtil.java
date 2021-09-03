package com.gw.zph.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSDateUtil {

    public static final String getDataStringByObj(Object obj){
        String result="";
        if(null==obj){
            return result;
        }
        try{
            result=String.valueOf(obj);
            result=result.trim();
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
    public static final String getDataStringByObjDefault(Object obj){
        String result = "暂无";
        if(null==obj||obj.toString().length()==0){
            return result;
        }
        try{
            result=String.valueOf(obj);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static final double getDatadoubleByObj(Object obj){
        double result=0.0;
        if(null==obj||obj.toString().length()==0){
            return result;
        }
        try{
            result=Double.parseDouble( obj.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
    public static final int getDataIntByObj(Object obj){
        int result=0;
        if(null==obj){
            return result;
        }
        try{
            result=Integer.valueOf( obj.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static final long getDataLongByObj(Object obj){
        long result=0;
        if(null==obj){
            return result;
        }
        try{
            result=Long.valueOf( obj.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static  final String getHtmlTextBlack(String msg){
        return
        "<font color='#000000'>"+getDataStringByObj(msg)+"</font>";
    }
    public static  final String getHtmlNbs(){
        return "&nbsp;&nbsp;&nbsp;&nbsp;";
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
