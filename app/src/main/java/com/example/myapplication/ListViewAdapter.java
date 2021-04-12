package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListViewItem_reserve_entrust> listViewItemList = new ArrayList<>();
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private int images[];

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final int pos = i;
        final Context context = viewGroup.getContext();
        final TextView textView;

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.reserve_entrust_listview_items, viewGroup, false);
        }
        ListViewItem_reserve_entrust listViewItem = listViewItemList.get(i);

        TextView titleTextView = (TextView)view.findViewById(R.id.textview_reserve_entrust_1);
        titleTextView.setText(listViewItem.getTitle());

        viewPager = (ViewPager)view.findViewById(R.id.viewpager_reserve_entrust);
        textView = (TextView)view.findViewById(R.id.viewpager_indicator);
        viewPagerAdapter = new ViewPagerAdapter(context, listViewItem.getImages());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.getLayoutParams().width = 500;
        viewPager.getLayoutParams().height = 500;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText((position + 1) + "/5");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    public void addItem(int images[] ,String title)
    {
        ListViewItem_reserve_entrust item = new ListViewItem_reserve_entrust();

        item.setViewPager(viewPager);
        item.setImages(images);
        item.setTitle(title);

        listViewItemList.add(item);
    }

}
