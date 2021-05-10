package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_reservelist extends RecyclerView.ViewHolder{
    TextView textview_sittername;
    TextView textview_datetime;
    TextView textview_petname;

    public ViewHolder_reservelist(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        textview_sittername = itemView.findViewById(R.id.textview_sittername);
        textview_datetime = itemView.findViewById(R.id.textview_datetime);
        textview_petname = itemView.findViewById(R.id.textview_petname);

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