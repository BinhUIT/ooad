package com.example.ooad.utils;

import java.sql.Date;
import java.time.LocalTime;

public class DateTimeUtil {
    public static Date getCurrentDate() {
        java.util.Date currentTime = new java.util.Date();
        return new Date(currentTime.getYear(),currentTime.getMonth(),currentTime.getDate());
    }
    public static boolean isTwoScheduleOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        if(start1.isBefore(start2)&&end1.isAfter(start2)) {
            return true;
        }
        if(start1.isBefore(end2)&&end1.isAfter(end2)) {
            return true;
        }
        return false;
    }
    public static boolean isStartBeforeEnd(LocalTime start, LocalTime end) {
        return start.isBefore(end);
    }
    public static boolean isDateAfterCurrentDate(Date date) {
        Date currentDate=DateTimeUtil.getCurrentDate();
        return date.after(currentDate);
    }
}
