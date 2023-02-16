package com.miiarms.miitool.util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author miiarms
 * @version 1.0
 * @date 2021/9/2 18:10
 */
public class DateTimeHelper {


    /**
     * 获取输入日期所在周的周一0点
     * @author miiarms
     * @date 2021/9/2 18:11
     **/
    public static Date getFirstDateTimeOfWeek(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        dateTime = dateTime.with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.getValue());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getFirstDateTimeOfWeek(LocalDate localDate){
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        dateTime = dateTime.with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.getValue());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取输入日期所在周的周日23:59:59
     * @author miiarms
     * @date 2021/9/2 18:11
     **/
    public static Date getLastDateTimeOfWeek(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        dateTime = dateTime.with(ChronoField.DAY_OF_WEEK, DayOfWeek.SUNDAY.getValue());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getLastDateTimeOfWeek(LocalDate localDate){
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        dateTime = dateTime.with(ChronoField.DAY_OF_WEEK, DayOfWeek.SUNDAY.getValue());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime getLastDateTimeOfWeek(){
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        dateTime = dateTime.with(ChronoField.DAY_OF_WEEK, DayOfWeek.SUNDAY.getValue());
        return dateTime;
    }
    public static LocalDateTime getFirstDateTimeOfWeek(){
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        dateTime = dateTime.with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.getValue());
        return dateTime;
    }

    /**
     * 获取一天中的开始时间
     * @author miiarms
     * @date 2021/9/2 18:45
     **/
    public static Date getFirstDateTimeOfDay(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getFirstDateTimeOfDay(LocalDate localDate){
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取一天中的结束时间
     * @author miiarms
     * @date 2021/9/2 18:45
     **/
    public static Date getLastDateTimeOfDay(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getLastDateTimeOfDay(LocalDate localDate){
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX).withNano(0);
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime getLastLocalDateTimeOfDay(){
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }
    public static Date getLastDateTimeOfDay(){
        return getLastDateTimeOfDay(LocalDate.now());
    }

    public static Date getLastDateTimeOfMonth(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX)
                .with(TemporalAdjusters.lastDayOfMonth());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getLastDateTimeOfMonth(LocalDate localDate){
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX)
                .with(TemporalAdjusters.lastDayOfMonth());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getFirstDateTimeOfMonth(LocalDate localDate){
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN)
                .with(TemporalAdjusters.firstDayOfMonth());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getFirstDateTimeOfMonth(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN)
                .with(TemporalAdjusters.firstDayOfMonth());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getFirstDateTimeOfYear(LocalDate localDate){
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN)
                .with(TemporalAdjusters.firstDayOfYear());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getFirstDateTimeOfYear(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN)
                .with(TemporalAdjusters.firstDayOfYear());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getLastDateTimeOfYear(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX)
                .with(TemporalAdjusters.lastDayOfYear());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getLastDateTimeOfYear(LocalDate localDate){
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX)
                .with(TemporalAdjusters.lastDayOfYear());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static LocalDateTime date2LocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static long getMinutesFromNowToTomorrow(){
        Duration duration = Duration.between(LocalDateTime.now(), DateTimeHelper.getLastLocalDateTimeOfDay());
        return duration.toMinutes() > 0 ? duration.toMinutes(): 1;
    }
    public static long getMinutesFromNowToWeekend(){
        Duration duration = Duration.between(LocalDateTime.now(), DateTimeHelper.getLastDateTimeOfWeek());
        return duration.toMinutes() > 0 ? duration.toMinutes(): 1;
    }
}
