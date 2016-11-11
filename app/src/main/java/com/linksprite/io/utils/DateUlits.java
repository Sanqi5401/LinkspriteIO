package com.linksprite.io.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DateUlits {

    public static String getCurTime() {
        SimpleDateFormat s = new SimpleDateFormat("yyyy:MM:dd HH:mm");
        Date date = new Date(System.currentTimeMillis());
        return s.format(date);
    }

    public static int getDay(String time) {
        return Integer.parseInt(time.substring(8, 10));
    }

    public static int getMonth(String time) {
        return Integer.parseInt(time.substring(5, 7));
    }
    public static int getYear(String time) {
        return Integer.parseInt(time.substring(0, 4));
    }

    public static int getHour(String time) {
        return Integer.parseInt(time.substring(11, 13));
    }

}
