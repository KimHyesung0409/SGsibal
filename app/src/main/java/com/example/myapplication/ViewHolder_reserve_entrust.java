package com.example.myapplication;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class ViewHolder_reserve_entrust extends RecyclerView.ViewHolder {

    ViewPager viewPager_reserve_entrust;
    TextView viewPager_indicator;
    TextView textView_reserve_entrust_title;
    TextView textView_reserve_entrust_address;
    TextView textView_reserve_entrust_name;
    TextView textView_reserve_entrust_price;

    boolean isclicked;

    public ViewHolder_reserve_entrust(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        viewPager_reserve_entrust = (ViewPager)itemView.findViewById(R.id.viewpager_reserve_entrust);
        viewPager_indicator = (TextView)itemView.findViewById(R.id.viewpager_indicator);
        textView_reserve_entrust_title = (TextView)itemView.findViewById(R.id.textview_reserve_entrust_title);
        textView_reserve_entrust_address = (TextView)itemView.findViewById(R.id.textview_reserve_entrust_address);
        textView_reserve_entrust_name = (TextView)itemView.findViewById(R.id.textview_reserve_entrust_name);
        textView_reserve_entrust_price = (TextView)itemView.findViewById(R.id.textview_reserve_entrust_price);

        isclicked = false;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                {
                    try
                    {
                        listener.onItemClick(v, position);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        viewPager_reserve_entrust.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int position = getAdapterPosition();

                if(event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    isclicked = false;
                }

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    isclicked = true;
                }

                if(event.getAction() == MotionEvent.ACTION_UP && isclicked == true) {
                    if (position != RecyclerView.NO_POSITION) {
                        try {
                            isclicked = false;
                            listener.onItemClick(v, position);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });

    }

}
