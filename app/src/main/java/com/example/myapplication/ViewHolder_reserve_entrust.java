package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class ViewHolder_reserve_entrust extends RecyclerView.ViewHolder {

    ViewPager viewPager_reserve_entrust;
    TextView viewPager_indicator;
    TextView textView_reserve_entrust_1;

    public ViewHolder_reserve_entrust(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        viewPager_reserve_entrust = (ViewPager)itemView.findViewById(R.id.viewpager_reserve_entrust);
        viewPager_indicator = (TextView)itemView.findViewById(R.id.viewpager_indicator);
        textView_reserve_entrust_1 = (TextView)itemView.findViewById(R.id.textview_reserve_entrust_1);

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
    }

}
