package com.example.myapplication.ViewHolder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.OnCustomClickListener;
import com.example.myapplication.R;

/*
 * 자동매칭 요소 ViewHolder 클래스
 * 펫시터 이름
 * 펫시터 주소
 * 펫시터 주소 상세
 * 펫시터와의 거리
 * 펫시터 평점
 */


public class ViewHolder_reserve_auto extends RecyclerView.ViewHolder {

    TextView textView_reserve_auto_user_name;
    TextView textView_reserve_auto_address;
    TextView textView_reserve_auto_address_detail;
    TextView textView_reserve_auto_distance;
    RatingBar ratingBar_reserve_auto;

    public ViewHolder_reserve_auto(View itemView, OnCustomClickListener listener) {
        super(itemView);

        textView_reserve_auto_user_name = (TextView)itemView.findViewById(R.id.textview_reserve_auto_user_name);
        textView_reserve_auto_address = (TextView)itemView.findViewById(R.id.textview_reserve_auto_address);
        textView_reserve_auto_address_detail = (TextView)itemView.findViewById(R.id.textview_reserve_auto_address_detail);
        textView_reserve_auto_distance = (TextView)itemView.findViewById(R.id.textview_reserve_auto_distance);
        ratingBar_reserve_auto = (RatingBar)itemView.findViewById(R.id.ratingBar_reserve_auto);

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

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                {
                    try
                    {
                        listener.onItemLongClick(v, position);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                return true;
            }
        });

    }

}
