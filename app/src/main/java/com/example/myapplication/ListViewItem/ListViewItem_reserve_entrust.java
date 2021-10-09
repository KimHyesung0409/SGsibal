package com.example.myapplication.ListViewItem;

import androidx.viewpager.widget.ViewPager;

/*
 * 위탁 요소 ListViewItem 클래스
 * 이미지를 보여줄 뷰페이지
 * 이미지 id
 * 위탁 주소
 * 펫시터 이름
 * 가격
 * 위탁 id
 * 펫시터 id
 */

public class  ListViewItem_reserve_entrust extends ListViewItem {

    private ViewPager viewPager;
    private String images_num;
    private String address;
    private String address_detail;
    private String user_name;
    private String price;
    private String entrust_id;
    private String user_id;
    //private String caution;
    //private String intro;

    public void setViewPager(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setEntrust_id(String entrust_id)
    {
        this.entrust_id = entrust_id;
    }

    public void setImages_num(String images_num) {
        this.images_num = images_num;
    }

    public void setUser_id(String user_id) { this.user_id = user_id; }
    //public void setCaution(String caution) { this.caution = caution; }

    //public void setIntro(String intro) { this.intro = intro; }

    public ViewPager getViewPager() {
        return this.viewPager ;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPrice() {
        return price;
    }

    public String getEntrust_id()
    {
        return this.entrust_id;
    }

    public String getImages_num() {
        return images_num;
    }

    public String getUser_id() { return user_id; }
    //public String getCaution() { return caution; }

    //public String getIntro() { return intro; }

}
