package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ViewPagerItem> viewPagerItemList = new ArrayList<>();
    private int images[];

    public ViewPagerAdapter(Context context, int images[])
    {
        this.context = context;
        this.images = images;
    }

    public Object instantiateItem(ViewGroup container, int position)
    {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.slide_item, container, false);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.imageview_reserve_entrust);
        imageView.setImageResource(images[position]);
        imageView.getLayoutParams().width = 500;
        imageView.getLayoutParams().height = 500;
        ((ViewPager)container).addView(itemView);
        return itemView;
    }

    public void addItem(int image)
    {
        ViewPagerItem item = new ViewPagerItem();

        item.setImage(image);

        viewPagerItemList.add(item);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }

    @Override
    public int getCount()
    {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View)object);
    }

}
