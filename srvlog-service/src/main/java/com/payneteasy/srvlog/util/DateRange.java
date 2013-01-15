package com.payneteasy.srvlog.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Date: 15.01.13 Time: 12:51
 */
public class DateRange implements Serializable{
    private Date fromDate;
    private Date toDate;

    public DateRange(Date fromDate, Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("From date: ").append(fromDate);
        buf.append(", to date: ").append(toDate);
        return buf.toString();
    }
    public static DateRange today() {
        Calendar calendar = Calendar.getInstance();
        nullifyTime(calendar);
        Date fromDate = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date toDate = calendar.getTime();
        return new DateRange(fromDate, toDate);
    }

    public static DateRange yesterday() {
        Calendar calendar = Calendar.getInstance();
        nullifyTime(calendar);
        Date toDate = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date fromDate = calendar.getTime();
        return new DateRange(fromDate, toDate);
    }

    public static DateRange thisWeek() {
        Calendar calendar = Calendar.getInstance();
        nullifyTime(calendar);
        resetToWeekStart(calendar);
        Date fromDate = calendar.getTime();
        calendar.add(Calendar.DATE, 7);
        Date toDate = calendar.getTime();
        return new DateRange(fromDate, toDate);
    }

    public static DateRange lastWeek() {
        Calendar calendar = Calendar.getInstance();
        nullifyTime(calendar);
        resetToWeekStart(calendar);
        Date toDate = calendar.getTime();
        calendar.add(Calendar.DATE, -7);
        Date fromDate = calendar.getTime();
        return new DateRange(fromDate, toDate);
    }

    public static DateRange thisMonth() {
        Calendar calendar = Calendar.getInstance();
        nullifyTime(calendar);
        resetToMonthStart(calendar);
        Date fromDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date toDate = calendar.getTime();
        return new DateRange(fromDate, toDate);
    }

    public static DateRange lastMonth() {
        Calendar calendar = Calendar.getInstance();
        nullifyTime(calendar);
        resetToMonthStart(calendar);
        Date toDate = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        Date fromDate = calendar.getTime();
        return new DateRange(fromDate, toDate);
    }


    private static void resetToWeekStart(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }

    private static void resetToMonthStart(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
    }

    private static void nullifyTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        nullifyInHour(calendar);
    }

    private static void nullifyInHour(Calendar calendar) {
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
