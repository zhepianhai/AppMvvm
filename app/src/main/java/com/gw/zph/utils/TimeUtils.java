package com.gw.zph.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class TimeUtils {
    /**
     * 时间转换为时间戳
     *
     * @param timeString 时间
     * @param pattern    样式
     * @return
     */
    public static long getStringToLong(String timeString, String pattern) {
        Timber.i("timeString-->" + timeString);
        Timber.i("timeString-->" + pattern);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = formatter.parse(timeString);
        } catch (Exception e) {
            date = new Date();
        }
        return date.getTime();
    }

    public static Date getStringToDate(String timeString, String pattern) {
        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            date = formatter.parse(timeString);
        } catch (ParseException e) {
            date = new Date();
        }
        return date;
    }


    /**
     * 时间戳转换为 时间
     *
     * @param timeLon 时间戳
     * @param pattern 样式
     * @return
     */
    public static String getTimeToString(long timeLon, String pattern) {
        if (timeLon == 0) {
            timeLon = System.currentTimeMillis();
        }
        Date dateTime = new Date();
        dateTime.setTime(timeLon);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return formatter.format(dateTime);
    }

    /**
     * 水印时间
     *
     * @return 2019-09-16 星期一 11:19
     */
    public static String getWaterMarkTime() {
        return new SimpleDateFormat("yyyy-MM-dd E HH:mm").format(new Date());
    }

}
