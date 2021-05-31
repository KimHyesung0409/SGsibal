package com.example.myapplication;

import java.util.Date;

/*
 * 검색, 즐겨찾기 요소 ListViewItem 클래스
 * 펫시터 user_id
 * 펫시터 이름
 * 펫시터 생년월일
 * 펫시터 성별
 * 최근 예약(사용) 시간
 * 평점
 */

public class ListViewItem_searchlist extends ListViewItem {

    private String user_id;
    private String user_name;
    private String birth;
    private String gender;
    private Date datetime;
    private double rating;


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }


    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getBirth() {
        return birth;
    }

    public String getGender() {
        return gender;
    }

    public Date getDatetime() {
        return datetime;
    }

    public double getRating()
    {
        return rating;
    }

}
