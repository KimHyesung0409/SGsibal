package com.example.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateString {

    // Date 형식을 받아와서 String 형식으로 형변환 하여 리턴한다.
    public static String DateToString(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return dateFormat.format(date);
    }
    // String 형식을 받아와서 Date 형식으로 형변환 하여 리턴한다.
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
