package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolder_estimate_offer extends RecyclerView.ViewHolder {

    TextView textView_estimate_offer_price;
    TextView textview_estimate_offer_timestamp;

    public ViewHolder_estimate_offer(@NonNull View itemView, OnCustomClickListener listener) {
        super(itemView);

        textView_estimate_offer_price = (TextView)itemView.findViewById(R.id.textview_estimate_offer_price);
        textview_estimate_offer_timestamp = (TextView)itemView.findViewById(R.id.textview_estimate_offer_timestamp);

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
