package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_storylist extends RecyclerView.ViewHolder {
    TextView textView_name_customer;
    TextView textView_name_pet;

    public ViewHolder_storylist(View itemView, OnCustomClickListener listener){
        super(itemView);

        textView_name_customer = itemView.findViewById(R.id.name_customer);
        textView_name_pet = itemView.findViewById(R.id.name_pet);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                {
                    try
                    {
                        listener.onItemClick(view, position);
                    }catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                {
                    try
                    {
                        listener.onItemLongClick(view, position);
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
