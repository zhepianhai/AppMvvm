package com.gw.safty.common.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
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




    /**
     *
     * @param text  文字的总内容
     * @param indexStr 关键字的内容
     * @param color  关键字的颜色
     * @return
     */
    public static SpannableStringBuilder setColor(String text, String indexStr, int color) {

        //  记录关键字的次数 与他在整个字符中所占的索引位置
        String[] deStr = text.split(indexStr);
        ArrayList<Entity> objects = new ArrayList<>(deStr.length);
        int cycleSize = text.endsWith(indexStr) ? deStr.length : deStr.length - 1;
        for (int i = 0; i < cycleSize; i++) {
            Entity entity = new Entity();
            int index;
            if (i == 0) {
                index = text.indexOf(indexStr);
            } else {
                Entity entity1 = objects.get(i - 1);
                index = text.indexOf(indexStr, entity1.end);
            }
            entity.start = index;
            entity.end = index + indexStr.length();
            objects.add(entity);
        }


        //根据索引集合 来设置关键字 字体颜色
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        for (int i = 0; i < objects.size(); i++) {
            //这个实例设置一次关键字的时候就要重新实例一次
            ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
            builder.setSpan(redSpan, objects.get(i).start, objects.get(i).end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }


    public static class Entity {
        public int start;
        public int end;
        @Override
        public String toString() {
            return "Entity{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }


}
