package com.example.myapplication;

import androidx.viewpager.widget.ViewPager;

public class ListViewItem_reserve_entrust {

    private ViewPager viewPager;
    private int images[];
    private String title;

    public void setViewPager(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }

    public void setImages(int images[])
    {
        this.images = images;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ViewPager getViewPager() {
        return this.viewPager ;
    }

    public String getTitle() {
        return this.title ;
    }

    public int[] getImages()
    {
        return this.images;
    }

}
