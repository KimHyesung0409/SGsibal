package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/*
 * 반려동물 목록 요소 ViewHolder 클래스
 * 반려동물 이름
 * 반려동물 종류
 * 반려동물 나이
 * 반려동물 세부종류
 */

public class ViewHolder_petlist extends RecyclerView.ViewHolder {

    TextView textView_pet_name;
    TextView textView_pet_species;
    TextView textview_pet_age;
    TextView textview_pet_detail_species;

    public ViewHolder_petlist(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        textView_pet_name = itemView.findViewById(R.id.textview_pet_name);
        textView_pet_species = itemView.findViewById(R.id.textview_pet_species);
        textview_pet_age = itemView.findViewById(R.id.textview_pet_age);
        textview_pet_detail_species = itemView.findViewById(R.id.textview_pet_detail_species);

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
