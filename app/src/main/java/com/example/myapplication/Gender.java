package com.example.myapplication;

public class Gender
{

    public static String getGender(boolean bool)
    {
        String gender = "여성";

        if(bool)
        {
            gender = "남성";
        }

        return gender;
    }

    public static boolean getGender(String gender)
    {
        if(gender.equals("남성"))
        {
            return true;
        }

        return false;
    }

}
