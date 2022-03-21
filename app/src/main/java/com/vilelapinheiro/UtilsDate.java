package com.vilelapinheiro;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class UtilsDate {

    public static String formatDate(Context context, LocalDateTime date){

        if (date == null) return null;

        SimpleDateFormat dateFormat;
        DateTimeFormatter formatador;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MM/dd/yyyy");

            dateFormat = new SimpleDateFormat(pattern);
            formatador = DateTimeFormatter.ofPattern(pattern);

        }else{

            dateFormat = (SimpleDateFormat) DateFormat.getMediumDateFormat(context);
            formatador = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        }

//        return dateFormat.format(date);
        return date.format(formatador);
    }

    public static String formatTime(Context context, Date date){

        SimpleDateFormat dateFormat;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "hh:mm");

            dateFormat = new SimpleDateFormat(pattern);

        }else{

            dateFormat = (SimpleDateFormat) DateFormat.getTimeFormat(context);
        }

        return dateFormat.format(date);
    }
}
