/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.unity.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * <p>
 * create by zhangxiaogang at 2018/9/4 13:53
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss.SSS","yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss.SSS","yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 获得当前日期，根据传进的分隔符，返回不同的形式
     * getCurrentDate("-");
     * <p>
     * 返回  2007-04-12
     *
     * @param splitFlag
     * @return
     */
    public static String getCurrentDate(String splitFlag) {
        String dt = "";
        String source = "yyyy" + splitFlag + "MM" + splitFlag + "dd";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(source);
            Calendar cal = Calendar.getInstance();
            dt = sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }


    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd）
     */
    public static String formatDateStr(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date 时间
     * @return 过去多长时间
     * @author zhangxiaogang
     * @since 2018/9/4 13:52
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date 时间
     * @return 小时
     * @author zhangxiaogang
     * @since 2018/9/4 13:51
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date 时间
     * @return 时间
     * @author zhangxiaogang
     * @since 2018/9/4 13:50
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis 时间
     * @return 时间
     * @author zhangxiaogang
     * @since 2018/9/4 13:50
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 转换为时间（yyyy 年 MM 月 dd）
     *
     * @param date 时间
     * @return 时间
     * @author zhangxiaogang
     * @since 2018/9/4 13:50
     */
    public static String formatDate(Date date) {
        return DateFormatUtils.format(date, "yyyy  年  MM  月 dd 日");
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before 之前时间
     * @param after  之后时间
     * @return 时间查
     * @author zhangxiaogang
     * @since 2018/9/4 13:49
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 时间戳
     * @return
     */
    public static String timeStamp2Date(Long seconds) {
        return timeStamp2Date(seconds,"");
    }
    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 时间戳
     * @param format
     * @return
     */
    public static String timeStamp2Date(Long seconds, String format) {
        if (seconds == null) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    /**
     * 获取指定日期下个月的第一天
     *
     * @param oldDate 当前时间
     * @return 指定日期下个月的第一天
     * @author zhangxiaogang
     * @since 2018/11/13 9:36
     */
    public static Date getFirstDayOfNextMonth(Date oldDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String formatDate = DateFormatUtils.format(oldDate, "yyyy-MM-dd");
            Date date = sdf.parse(formatDate + " 00:00:00");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 1);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定日期本月的第一天
     *
     * @param now 当前时间
     * @return 指定日期下个月的第一天
     * @author zhangxiaogang
     * @since 2018/11/13 9:36
     */
    public static Date getFirstDayOfNowMonth(Date now) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String formatDate = DateFormatUtils.format(now, "yyyy-MM-dd");
            Date date = sdf.parse(formatDate + " 00:00:00");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            //calendar.add(Calendar.MONTH, 1);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        System.out.println("-----2------lastDay:" + DateUtils.formatDate(DateUtils.getFirstDayOfNowMonth(new Date())));
//    }

    /**
     * @param args
     * @throws ParseException
     */
   /* public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		         //获取前月的第一天
		     *//*    Calendar   cal_1=Calendar.getInstance();//获取当前日期
		         cal_1.add(Calendar.MONTH, -1);
		         cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		         String  firstDay = format.format(cal_1.getTime());
		         System.out.println("-----1------firstDay:"+firstDay);
		         //获取前月的最后一天
		         Calendar cale = Calendar.getInstance();   
		         cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
		         String   lastDay = format.format(cale.getTime());
		         System.out.println("-----2------lastDay:"+lastDay);*//*
       // formatDate(new Date());

        //System.out.println("-----2------lastDay:"+DateUtils.getFirstDayOfNextMonth(new Date()));

		
	}*/
}
