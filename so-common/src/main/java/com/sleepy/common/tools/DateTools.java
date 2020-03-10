package com.sleepy.common.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理类
 *
 * @author gehoubao
 * @create 2019-04-26 10:20
 **/
public class DateTools {
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String FLYWAY_SQL_FILE_NAME_PATTERN = "yyyy.MM.dd_HHmm";
    public static final String CUSTOM_DATETIME_PATTERN = "yyyyMMddHHmmss";

    /**
     * 指定格式，格式化日期
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return "yyyy-MM-dd HH:mm:ss"的字符串
     */
    public static String dateFormat(Date date) {
        return new SimpleDateFormat(DEFAULT_DATETIME_PATTERN).format(date);
    }

    /**
     * 字符串转日期
     *
     * @param dateString
     * @param dateFormat
     * @return
     */
    public static Date toDate(String dateString, String dateFormat) {
        Date date = null;
        try {
            date = new SimpleDateFormat(dateFormat).parse(dateString);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    public static String randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);

            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return dateFormat(new Date(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    public static Date getDateWithCurrent(int amount, int unit) {
        Calendar c = Calendar.getInstance();
        c.add(unit, amount);
        return c.getTime();
    }
}