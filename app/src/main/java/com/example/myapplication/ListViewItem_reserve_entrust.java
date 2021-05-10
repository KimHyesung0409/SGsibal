package com.example.myapplication;

import androidx.viewpager.widget.ViewPager;

public class  ListViewItem_reserve_entrust extends ListViewItem {

    private ViewPager viewPager;
    private int images[];
    private String address;
    private String user_name;
    private String price;
    private String entrust_id;


    public void setViewPager(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }

    public void setImages(int images[])
    {
        this.images = images;
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

    public ViewPager getViewPager() {
        return this.viewPager ;
    }

    public int[] getImages()
    {
        return this.images;
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

}
