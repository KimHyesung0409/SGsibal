package com.example.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateString {

    public static String DateToString(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return dateFormat.format(date);
    }

    public static Date StringToDate(String date) {

        try
        {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            return dateFormat.parse(date);
        }
        catch (Exception e)
        {

        }

        return null;
    }

}
