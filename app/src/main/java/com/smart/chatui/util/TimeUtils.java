package com.smart.chatui.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gbh on 2018/5/9  15:53.
 *
 * @describe
 */

public class TimeUtils {

    public static String HisContactTimeFormat(String time) {
        if (TextUtils.isEmpty(time)) return "";
        Date date = StringTimeFormateDate1(time);
        if (null == date) return "";
        int type = oneDayDifference(date);
        if (type == 0) return longTimeFormatToDay(date);
        else if (type == 1) return "昨天";
        else
            return longTimeFormatToMoneyAndDay(date);
    }

    public static String stringTimeFormat(String time) {
        if (TextUtils.isEmpty(time)) return "";
        Date date = StringTimeFormateDate(time);
        if (null == date) return "";
        int type = oneDayDifference(date);
        if (type == 0) return longTimeFormatToDay(date);
        else if (type == 1) return "昨天";
        else
            return longTimeFormatToMoneyAndDay(date);
    }

    public static String longTimeFormat(String longTime) {
        if (TextUtils.isEmpty(longTime)) return "";
        Date date = longTimeFormatDate(longTime);
        int type = oneDayDifference(date);
        if (type == 0) return longTimeFormatToDay(date);
        else if (type == 1) return "昨天";
        else
            return longTimeFormatToMoneyAndDay(date);
    }

    public static String longTimeFormatItem(String longTime) {
        if (TextUtils.isEmpty(longTime)) return "";
        Date date = longTimeFormatDate(longTime);
        int type = oneDayDifference(date);
        if (type == 0) return longTimeFormatToDay(date);
        else if (type == 1) return "昨天" + longTimeFormatToDay(date);
        else
            return longTimeFormatToMoneyDay(date);
    }

    //今天
    public static String longTimeFormatToDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String sDateTime = sdf.format(date);  //得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    //具体日期
    public static String longTimeFormatToMoneyAndDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        String sDateTime = sdf.format(date);  //得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    //具体日期
    public static String longTimeFormatToMoneyDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        String sDateTime = sdf.format(date);  //得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    public static Date longTimeFormatDate(String longTime) {
        Date dt = new Date(Long.parseLong(longTime));
        return dt;
    }

    public static Date StringTimeFormateDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt2 = null;
        try {
            dt2 = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt2;
    }

    public static Date StringTimeFormateDate1(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt2 = null;
        try {
            dt2 = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt2;
    }

    /**
     * 判断是否是昨天
     *
     * @param oldDate
     * @return
     */
    public static int oneDayDifference(Date oldDate) {
        Calendar oldCal = Calendar.getInstance();
        Calendar newCal = Calendar.getInstance();

        oldCal.setTime(oldDate);
        newCal.setTime(new Date());
        return Math.abs(newCal.get(Calendar.DAY_OF_YEAR) - oldCal.get(Calendar.DAY_OF_YEAR));
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String sDateTime = sdf.format(new Date());  //得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }
}
