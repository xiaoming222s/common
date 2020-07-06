//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.unity.common.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class SuperviseDates {
    private static final int[] FIELDS = new int[]{1, 2, 5, 11, 12, 13, 14};
    public static String FRM_ymdhms = "yyyy-MM-dd HH:mm:ss";
    public static String TIMEZONE_GTM_8 = "Asia/Taipei";
    public static DateTimeFormatter fmt;
    public static SimpleDateFormat FORMATTER;
    private static Calendar instance;
    private static Locale locale;

    public SuperviseDates() {
    }

    public static String format(Date date) {
        if (date == null) {
            date = new Date();
        }

        return FORMATTER.format(date);
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            date = new Date();
        }

        if (StringUtils.isEmpty(pattern)) {
            pattern = FRM_ymdhms;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
        return formatter.format(date);
    }

    public static Date parse(String str, String... patterns) {
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            if (ArrayUtils.isEmpty(patterns)) {
                patterns = new String[]{FRM_ymdhms};
            }

            try {
                return DateUtils.parseDate(str, patterns);
            } catch (ParseException var3) {
                log.warn("[parse] Could not parse \"{}\" with patterns -> {}", str, patterns);
                return null;
            }
        }
    }

    public static boolean between(Date base, Date... minMax) {
        if (base == null) {
            return false;
        } else {
            int len = ArrayUtils.getLength(minMax);
            if (len == 0) {
                return true;
            } else {
                Date min = minMax[0];
                Date max = len > 1 ? minMax[1] : null;
                boolean between = min == null ? true : base.compareTo(min) >= 0;
                if (max != null) {
                    between &= base.compareTo(max) <= 0;
                }

                return between;
            }
        }
    }

    public static Date min(Date... dates) {
        dates = SuperviseArrays.notEmpty(dates);
        return ArrayUtils.isEmpty(dates) ? null : Arrays.stream(dates).filter(Objects::nonNull).min(Comparator.naturalOrder()).get();
    }

    public static Date max(Date... dates) {
        dates = SuperviseArrays.notEmpty(dates);
        return ArrayUtils.isEmpty(dates) ? null : Arrays.stream(dates).filter(Objects::nonNull).max(Date::compareTo).get();
    }

    public static Date utc(Date date) {
        return date == null ? null : utc(date.getTime());
    }

    public static Date utc(TimeZone tz) {
        return utc(new Date(), tz);
    }

    public static Date utc(Date date, TimeZone tz) {
        return date == null ? null : utc(date.getTime(), tz);
    }

    public static Date utc(String timeZone) {
        DateTime dt = new DateTime();
        dt.withZone(DateTimeZone.forID(timeZone));
        return dt.toDate();
    }

    public static Date utc(long ts, TimeZone tz) {
        return new Date(ts + (long) (tz == null ? TimeZone.getDefault() : tz).getRawOffset());
    }

    public static Date utc(Date ctime, String timezoneGtm8) {
        DateTime dt = new DateTime(ctime);
        dt.withZone(DateTimeZone.forID(timezoneGtm8));
        return dt.toDate();
    }

    public static Date utc(long ts) {
        return (new DateTime(ts, DateTimeZone.forID("UTC"))).toDate();
    }

    public static Date utc() {
        return fmt.withZone(DateTimeZone.getDefault()).parseDateTime(utcStr()).toDate();
    }

    public static String utcStr(String timeZone) {
        DateTime dt = new DateTime();
        return fmt.withZone(DateTimeZone.forID(timeZone)).print(dt);
    }

    public static String utcStr() {
        return utcStr((Date) null);
    }

    public static String utcStr(Date from) {
        DateTime dt = from == null ? new DateTime() : new DateTime(from.getTime());
        return DateTimeFormat.forPattern(FRM_ymdhms).withZone(DateTimeZone.UTC).print(dt);
    }

    public static Date local() {
        return (new DateTime()).toDate();
    }

    public static int minutes(int amount) {
        return seconds(12, amount);
    }

    public static int minutesMilli(int amount) {
        return minutes(amount) * 1000;
    }

    public static int hoursMilli(int amount) {
        return hours(amount) * 1000;
    }

    public static int hours(int amount) {
        return seconds(10, amount);
    }

    public static int days(int amount) {
        return seconds(5, amount);
    }

    public static int distance(Date from, Date to) {
        if (from == null) {
            from = new Date();
        }

        if (to == null) {
            to = new Date();
        }

        return (int) (to.getTime() - from.getTime());
    }

    public static int seconds(int field, int amount) {
        if (amount < 1) {
            return 0;
        } else {
            int ts = amount;
            switch (field) {
                case 5:
                    ts = amount * 24;
                case 10:
                    ts *= 60;
                case 12:
                    ts *= 60;
                default:
                    return ts;
            }
        }
    }

    public static int seconds(int amount, TimeUnit unit) {
        return amount < 1 ? 0 : (int) unit.toSeconds((long) amount);
    }

    public static int ts(int field, int amount) {
        return seconds(field, amount) * 1000;
    }

    public static Date date(Integer... fields) {
        Calendar cal = calendar();

        for (int i = 0; i < FIELDS.length; ++i) {
            int val = (Integer) SuperviseArrays.get(fields, i, 0);
            if (val > 0 && i == 1) {
                --val;
            }

            cal.set(FIELDS[i], val);
        }

        return cal.getTime();
    }

    public static long getTime(Date time) {
        return time == null ? 0L : time.getTime();
    }

    public static boolean isBefore(Date val, Date baseline) {
        long ts = getTime(val);
        return ts > 0L && ts - (baseline == null ? System.currentTimeMillis() : getTime(baseline)) < 0L;
    }

    public static Date setFields(Date date, int... fieldsAndValues) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = calendar();
        cal.setTime(date);

        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            int field = fieldsAndValues[i];
            int val = i + 1 == fieldsAndValues.length ? (field == 2 ? 0 : 1) : fieldsAndValues[i + 1];
            cal.set(field, val);
        }

        return cal.getTime();
    }

    public static Integer[] getFields(Date date, int... fields) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = calendar();
        cal.setTime(date);
        Integer[] vals = new Integer[fields.length];

        for (int i = 0; i < fields.length; ++i) {
            vals[i] = cal.get(fields[i]);
        }

        return vals;
    }

    public static Date purgeTime(Date date) {
        if (date == null) {
            date = new Date();
        }

        return purge(date, 11, 12, 13, 14);
    }

    public static Date firstDayOfWeek(Integer hourSpan) {
        return firstDayOfWeek((Date) null, hourSpan);
    }

    public static Date firstDayOfWeek(Date date) {
        return firstDayOfWeek(date, (Integer) null);
    }

    public static Date firstDayOfWeek(Date date, Integer hourSpan) {
        if (hourSpan == null) {
            hourSpan = 0;
        }

        if (date == null) {
            date = utcDate(hourSpan);
        } else {
            date = add(date, 11, hourSpan);
        }

        return setFields(date, 7, calendar().getFirstDayOfWeek());
    }

    public static Date firstDateOf(Date date, int field) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = calendar();
        cal.setTime(date);
        switch (field) {
            case 5:
                cal.set(5, 1);
                break;
            case 6:
                cal.set(6, 1);
            case 7:
        }

        return purgeTime(cal.getTime());
    }

    public static Date lastDateOf(Date date, int field) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = calendar();
        cal.setTime(date);
        switch (field) {
            case 5:
                cal.add(2, 1);
                cal.set(5, 1);
                cal.add(6, -1);
                break;
            case 6:
                cal.add(1, 1);
                cal.set(6, 1);
                cal.add(6, -1);
                break;
            case 7:
                cal.add(4, 1);
                cal.set(7, 1);
                cal.add(6, -1);
        }

        return purgeTime(cal.getTime());
    }

    public static Date add(Date date, int... fieldsAndValues) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = calendar();
        cal.setTime(date);

        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            int field = fieldsAndValues[i];
            int val = i + 1 == fieldsAndValues.length ? 0 : fieldsAndValues[i + 1];
            cal.add(field, val);
        }

        return cal.getTime();
    }

    public static Calendar calendar() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(2);
        return cal;
    }

    public static int seconds(Date date) {
        return (int) (getTime(date) / 1000L);
    }

    public static Date utcDate(int hourSpan) {
        return utc(purgeTime(add(utc(), 11, hourSpan)));
    }

    public static int utcHourSpan() {
        return TimeZone.getDefault().getRawOffset() / 3600000;
    }

    public static Date utcWithHourOffset(int hourOffset) {
        return utcWithHourOffset((Date) null, 8);
    }

    public static Date utcWithHourOffset(Date date, int hourOffset) {
        date = date == null ? utc() : utc(date);
        return add(date, 11, hourOffset);
    }

    public static Date purge(Date date, int... fields) {
        if (date == null) {
            date = new Date();
        }

        int[] var2 = fields;
        int var3 = fields.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            int field = var2[var4];
            switch (field) {
                case 2:
                    date = setFields(date, 2, 0);
                case 3:
                case 4:
                case 6:
                case 8:
                case 9:
                default:
                    break;
                case 5:
                    date = setFields(date, 5, 1);
                    break;
                case 7:
                    date = setFields(date, 7, 2);
                    break;
                case 10:
                case 11:
                    date = setFields(date, 11, 0);
                    break;
                case 12:
                    date = setFields(date, 12, 0);
                    break;
                case 13:
                    date = setFields(date, 13, 0);
                    break;
                case 14:
                    date = setFields(date, 14, 0);
            }
        }

        return date;
    }

    static {
        locale = Locale.CHINA;
        fmt = DateTimeFormat.forPattern(FRM_ymdhms);
        FORMATTER = new SimpleDateFormat(FRM_ymdhms, locale);
    }

    /**
     * 判断是否是日期格式
     *
     * @param str 传入的字符串
     * @return true/false
     * @author xupeng
     * @since 2018年03月4日 19:19
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", locale);
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException ignored) {
            convertSuccess = false;
        }
        return convertSuccess;
    }


    /**
     * 从日期转成星期几的工具类
     *
     * @param datetime yyyy-MM-dd类型的字符串
     * @return 转换好的字符串
     * @throws ParseException 如果字符串转换异常，则抛出
     * @author Jung
     * @since 2018-03-20 16:25:073
     */
    public static String dateToWeek(String datetime) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", locale);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = f.parse(datetime);
        cal.setTime(datet);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取指定年月的最大天数
     *
     * @param year  需要获取的年份
     * @param month 需要获取的月份 1-12
     * @return 当前年月的最大年月
     * @author Jung
     * @since 2018-03-20 16:36:03
     */
    public static int getMaxDay(int year, int month) {
        if (month < 1 || month > 12) {
            log.error("传入的月份应在[1,12]范围内");
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据传入的年月获得当月的日历
     *
     * @param years 年,month月份
     * @return 这个月的日历
     * 例如：["2018-03-01","2018-03-02"...,"2018-03-31"]
     * @author Jung
     * @since 2018年03月4日 19:19
     */
    public static List<String> getDate(int years, int month) {
        //获取本月的最大天数
        int maxDay = getMaxDay(years, month);
        //获取字符串格式化后的月份
        String monthStr = month < 10 ? "0" + month : String.valueOf(month);

        List<String> calendar = new ArrayList<>(maxDay);
        for (int i = 1; i <= maxDay; i++) {
            //获取字符串类型的日期
            String dayStr = i < 10 ? "0" + i : String.valueOf(i);
            calendar.add(years + "-" + monthStr + "-" + dayStr);
        }
        return calendar;
    }

    /**
     * 传入开始时间结束时间查询中间相隔的月份(包括开始月份跟结束月份)
     *
     * @param startDate 开始月份
     * @param endDate   结束月份
     * @return 集合
     * @author xupeng
     * @since 2018年03月4日 19:19
     */
    public static List<String> getMonthListByStartDate2EndDate(String startDate, String endDate) {

        ArrayList<String> calendarList = new ArrayList<>();
        try {
            //进行日期转换
            Calendar startDateCal = DateUtils.toCalendar(DateUtils.parseDate(startDate, new String[]{"yyyy-MM-dd"}));
            Calendar endDateCal = DateUtils.toCalendar(DateUtils.parseDate(endDate, new String[]{"yyyy-MM-dd"}));

            //选差的月份
            int minusMonth = endDateCal.get(Calendar.YEAR) * 12 + endDateCal.get(Calendar.MONTH) - (startDateCal.get(Calendar.YEAR) * 12 + startDateCal.get(Calendar.MONTH));

            //开始循环每个月的年份和月份
            for (int i = startDateCal.get(Calendar.MONTH) + 1; i <= minusMonth + startDateCal.get(Calendar.MONTH) + 1; i++) {
                String year = String.valueOf(startDateCal.get(Calendar.YEAR) + (startDateCal.get(Calendar.MONTH) + i + 1) / 12);
                String monthStr = i % 12 < 10 ? "0" + i % 12 : String.valueOf(i % 12);
                calendarList.add(year + "-" + monthStr);
            }
        } catch (ParseException e) {
            log.error("在日期转换时发生了异常", e);
            return new ArrayList<>();
        }

        return calendarList;

    }

    /**
     * 从当月开始，最近N个月的倒序值
     *
     * @param date  开始计算的月份
     * @param range 需要倒序的月份
     * @return 倒序后的日历，例如传入 "2017-03",12，则会返回如下列表：
     * ["2017-03","2017-02","2017-01","2016-12","2016-11","2016-10","2016-09","2016-08","2016-07","2016-06","2016-05","2016-04"]
     * @author lixun
     * @since 2018/3/11 14:41
     */
    public static List<String> converseDate(String date, int range) throws ParseException {
        Calendar calendar = DateUtils.toCalendar(DateUtils.parseDate(date, new String[]{"yyyy-MM"}));
        calendar.add(Calendar.YEAR, -1);
        List<String> months = new ArrayList<>();
        for (int i = 0; i < range; i++) {
            calendar.add(Calendar.MONTH, 1);
            int month = calendar.get(Calendar.MONTH) + 1;
            months.add(calendar.get(Calendar.YEAR) + "-" + (month < 10 ? ("0" + month) : month));
        }
        return Lists.reverse(months);
    }
}
