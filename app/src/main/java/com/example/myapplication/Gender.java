package com.example.myapplication;

public class Gender
{
    // boolean 형태로 제공된 성별 정보를 String 으로 변환하여 리턴하는 메소드.
    public static String getGender(boolean bool)
    {
        String gender = "여성";

        if(bool)
        {
            gender = "남성";
        }

        return gender;
    }
    // String 형태로 제공된 성별 정보를 boolean 으로 변환하여 리턴하는 메소드.
    public static boolean getGender(String gender)
    {
        if(gender.equals("남성"))
        {
            return true;
        }

        return false;
    }

    // 반려동물의 경우. 다르게 표현한다.
    public static String getGenderPet(boolean bool)
    {
        String gender = "암컷";

        if(bool)
        {
            gender = "수컷";
        }

        return gender;
    }

}
