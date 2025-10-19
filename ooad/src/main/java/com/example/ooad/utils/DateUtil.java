package com.example.ooad.utils;

import java.sql.Date;

public class DateUtil {
    public static Date getCurrentDate() {
        java.util.Date currentTime = new java.util.Date();
        return new Date(currentTime.getYear(),currentTime.getMonth(),currentTime.getDate());
    }
}
