package com.example.myapplication;

import android.util.Patterns;

import java.util.regex.Pattern;

public class VerifyString {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 이메일 유효성 검사
    public static boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    public static boolean isValidPasswd(String password) {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호와 비밀번호re가 일치하는지 체크
    public static boolean isValidPasswd_re(String password, String password_re) {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (password.equals(password_re)) {
            // 비밀번호 일치
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidname(String name)
    {
        if (name.isEmpty())
        {
            return false;
        } else if (name.length() < 3)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean isEmptyAndNull(String str)
    {
        if(str.isEmpty())
        {
            return false;
        }
        if(str == null)
        {
            return false;
        }

        return true;
    }

}
