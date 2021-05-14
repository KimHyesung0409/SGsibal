package com.example.myapplication;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

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

    public static void setUser_name(String user_name) {
        LoginUserData.user_name = user_name;
    }

    public static void setAddress(String address) {
        LoginUserData.address = address;
    }

    public static void setAddress_detail(String address_detail) {
        LoginUserData.address_detail = address_detail;
    }

    public static void setFcm_token(String fcm_token) {
        LoginUserData.fcm_token = fcm_token;
    }

    public static void setGeoHash(String geoHash) {
        LoginUserData.geoHash = geoHash;
    }

    public static void setGeoPoint(GeoPoint geoPoint) {
        LoginUserData.geoPoint = geoPoint;
    }

    public static void setSitter_auth(boolean sitter_auth) {
        LoginUserData.sitter_auth = sitter_auth;
    }

    public static void setSitter_entrust(boolean sitter_entrust) {
        LoginUserData.sitter_entrust = sitter_entrust;
    }

    public static void setCare_list(ArrayList<String> care_list) {
        LoginUserData.care_list = care_list;
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

    public static boolean isSitter_auth() {
        return sitter_auth;
    }

    public static boolean isSitter_entrust() {
        return sitter_entrust;
    }

    public static ArrayList<String> getCare_list() {
        return care_list;
    }
}
