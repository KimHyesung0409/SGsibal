package com.example.myapplication;

import androidx.viewpager.widget.ViewPager;

public class  ListViewItem_reserve_entrust extends ListViewItem {

    private ViewPager viewPager;
    private int images[];

    public void setViewPager(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }

    public void setImages(int images[])
    {
        this.images = images;
    }

    public ViewPager getViewPager() {
        return this.viewPager ;
    }

    public int[] getImages()
    {
        return this.images;
    }

}
