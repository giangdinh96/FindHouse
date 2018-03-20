package com.giangdinh.returnnotfound.findhouse.Utils;

import java.util.Date;

public class DateUtils {
    public static String getDateTimeAgoString(long timeMilliseconds) {
        Date dateCurrent = new Date();
        long timeMillisecondsCurrent = dateCurrent.getTime();
        long milliseconds = timeMillisecondsCurrent - timeMilliseconds;
        if (milliseconds < 0) {
            return "Null";
        }
        if (milliseconds >= 0 && milliseconds < 30000) {
            return "Vừa xong";
        }

        long second = milliseconds / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        long day = hour / 24;
        long month = day / 30;
        long year = month / 12;

        if (year > 0) {
            return year + " năm trước";
        }
        if (month > 0) {
            return month + " tháng trước";
        }
        if (day > 0) {
            return day + " ngày trước";
        }
        if (hour > 0) {
            return hour + " giờ trước";
        }
        if (minute > 0) {
            return minute + " phút trước";
        }
        if (second > 0) {
            return second + " giây trước";
        }
        return "Null";
    }

    public static long getDayAgoFromPubDate(long pubDate) {
        Date dateCurrent = new Date();
        long timeMillisecondsCurrent = dateCurrent.getTime();
        long millisecondsAgo = timeMillisecondsCurrent - pubDate;
        long second = millisecondsAgo / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        long day = hour / 24;
        return day;
    }
}
