package com.example.myapplication;

import androidx.viewpager.widget.ViewPager;

public class  ListViewItem_reserve_entrust extends ListViewItem {

    private ViewPager viewPager;
    private String images_num;
    private String address;
    private String user_name;
    private String price;
    private String entrust_id;
    private String caution;
    private String intro;

    public void setViewPager(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public void setCaution(String caution) { this.caution = caution; }

    public void setIntro(String intro) { this.intro = intro; }

    public ViewPager getViewPager() {
        return this.viewPager ;
    }

    public String getAddress() {
        return address;
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

    public String getCaution() { return caution; }

    public String getIntro() { return intro; }

}
