package com.phototell.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * date utility
 */

public class DateUtility {
    public static Date getDate(String time) {
        try {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(time));
            return cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return Calendar.getInstance().getTime();//get local date
        }
    }
}