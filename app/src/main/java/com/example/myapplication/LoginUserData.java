package com.example.myapplication;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

/*
 * 로그인한 유저 정보 저장 클래스
 * 유저이름
 * 주소
 * fcm 토큰
 * geoHash
 * 좌표
 * 펫시터 인증 여부
 * 펫시터 위탁 등록 여부
 * 케어 가능한 동물 리스트
 * 성별
 * 생년월일
 * 핸드폰 번호
 */

public class LoginUserData {

    private static String user_name;
    private static String address;
    private static String address_detail;
    private static String fcm_token;
    private static String geoHash;
    private static GeoPoint geoPoint;
    private static boolean sitter_auth;
    private static boolean sitter_entrust;
    private static ArrayList<String> care_list;

    private static String care_list_Str;

    private static boolean gender;
    private static String birth;
    private static String phone;



    public static void setUser_name(String user_name) {
        LoginUserData.user_name = user_name;
    }

    public static void setAddress(String address) {
        LoginUserData.address = address;
    }

    public static void setAddress_detail(String address_detail) { LoginUserData.address_detail = address_detail; }

    public static void setFcm_token(String fcm_token) {
        LoginUserData.fcm_token = fcm_token;
    }

    public static void setGeoHash(String geoHash) {
        LoginUserData.geoHash = geoHash;
    }

    public static void setGeoPoint(GeoPoint geoPoint) {
        LoginUserData.geoPoint = geoPoint;
    }

    public static void setSitter_auth(boolean sitter_auth) { LoginUserData.sitter_auth = sitter_auth; }

    public static void setSitter_entrust(boolean sitter_entrust) { LoginUserData.sitter_entrust = sitter_entrust; }

    public static void setCare_list(ArrayList<String> care_list) { LoginUserData.care_list = care_list; }
    //테스트용 String 형식 care_list
    public static void setCare_list_Str(String care_list_Str){ LoginUserData.care_list_Str = care_list_Str;}

    public static void setGender(boolean gender) {
        LoginUserData.gender = gender;
    }

    public static void setBirth(String birth) {
        LoginUserData.birth = birth;
    }

    public static void setPhone(String phone) {
        LoginUserData.phone = phone;
    }

    public static String getUser_name() {
        return user_name;
    }

    public static String getAddress() {
        return address;
    }

    public static String getAddress_detail() {
        return address_detail;
    }

    public static String getFcm_token() {
        return fcm_token;
    }

    public static String getGeoHash() {
        return geoHash;
    }

    public static GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public static boolean getSitter_auth() {
        return sitter_auth;
    }

    public static boolean getSitter_entrust() {
        return sitter_entrust;
    }

    public static ArrayList<String> getCare_list() {
        return care_list;
    }
    //테스트용 String 형식 care_list
    public static String getCare_list_Str() { return care_list_Str;}

    public static boolean getGender() {
        return gender;
    }

    public static String getBirth() {
        return birth;
    }

    public static String getPhone() {
        return phone;
    }
}
