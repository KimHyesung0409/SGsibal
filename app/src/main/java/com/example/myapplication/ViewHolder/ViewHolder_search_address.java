package com.example.myapplication.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.OnCustomClickListener;
import com.example.myapplication.R;

/*
 * 주소 검색 요소 ViewHolder 클래스
 * 우편번호
 * 도로명 주소
 * 지번 주소
 */

public class ViewHolder_search_address extends RecyclerView.ViewHolder{

    TextView textView_postal_code;
    TextView textView_road_address;
    TextView textView_jibun_address;

    public ViewHolder_search_address(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        textView_postal_code = (TextView) itemView.findViewById(R.id.textview_postal_code);
        textView_road_address = (TextView) itemView.findViewById(R.id.textview_road_address);
        textView_jibun_address = (TextView) itemView.findViewById(R.id.textview_jibun_address);

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
