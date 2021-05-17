package com.example.myapplication;

public class ListViewItem_reserve_auto extends ListViewItem {

    private String user_id;
    private String user_name;
    private String address;
    private String address_detail;
    private String distance;
    private String token_id;

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

    public void setToken_id(String token_id)
    {
        this.token_id = token_id;
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

    public String getToken_id()
    {
        return token_id;
    }
}
