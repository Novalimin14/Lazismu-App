package com.example.lazismuapp.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String getMonthAndYear(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        Date date = null;
        try {
            date = inputFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date != null ? outputFormat.format(date) : "";
    }
}

