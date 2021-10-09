package com.example.myapplication.ListViewItem;

/*
 * 자동매칭 요소 ListViewItem 클래스
 * 펫시터 user_id
 * 펫시터 이름
 * 펫시터 주소
 * 펫시터 주소 상세
 * 펫시터와의 거리
 * 펫시터 평점
 */

public class ListViewItem_reserve_auto extends ListViewItem {

    private String user_id;
    private String user_name;
    private String address;
    private String address_detail;
    private String distance;
    private double rating;

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public String getDistance() {
        return distance;
    }

    public double getRating()
    {
        return rating;
    }
}
