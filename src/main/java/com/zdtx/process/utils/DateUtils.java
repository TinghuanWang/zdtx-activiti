package com.zdtx.process.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/***
 * 日期工具类（基于JDK1.8+）
 */
public class DateUtils {

    /***
     * 把日期格式转换为指定字符串格式
     * @param localDate
     * @param pattern
     * @return
     */
    public static String parseDate(Date date, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return dateTimeFormatter.format(localDateTime);
    }
}
