package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
 * 견적서 요소 ViewHolder 클래스
 * 가격
 * 반려동물 나이
 * 반려동물 종류
 * 반려동물 상세종류
 * 예약 날짜
 * 반려동물 이름
 */


public class ViewHolder_estimate extends RecyclerView.ViewHolder {

    TextView textView_estimate_pet_name;
    TextView textView_estimate_datetime;
    TextView textView_estimate_species;
    TextView textView_estimate_species_detail;
    TextView textView_estimate_pet_age;
    TextView textView_estimate_price;

    public ViewHolder_estimate(@NonNull View itemView,  OnCustomClickListener listener) {
        super(itemView);

        textView_estimate_pet_name = (TextView)itemView.findViewById(R.id. textview_estimate_pet_name);
        textView_estimate_datetime = (TextView)itemView.findViewById(R.id. textview_estimate_datetime);
        textView_estimate_species = (TextView)itemView.findViewById(R.id. textview_estimate_species);
        textView_estimate_species_detail = (TextView)itemView.findViewById(R.id. textview_estimate_species_detail);
        textView_estimate_pet_age = (TextView)itemView.findViewById(R.id. textview_estimate_pet_age);
        textView_estimate_price = (TextView)itemView.findViewById(R.id. textview_estimate_price);

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
