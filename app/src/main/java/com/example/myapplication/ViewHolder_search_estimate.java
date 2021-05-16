package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_search_estimate extends RecyclerView.ViewHolder {

    TextView textView_estimate_address;
    TextView textView_estimate_species;
    TextView textView_estimate_species_detail;
    TextView textView_estimate_pet_age;
    TextView textView_estimate_price;

    public ViewHolder_search_estimate(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        textView_estimate_address = (TextView)itemView.findViewById(R.id.textview_estimate_address);
        textView_estimate_species = (TextView)itemView.findViewById(R.id.textview_estimate_species);
        textView_estimate_species_detail = (TextView)itemView.findViewById(R.id.textview_estimate_species_detail);
        textView_estimate_pet_age = (TextView)itemView.findViewById(R.id.textview_estimate_pet_age);
        textView_estimate_price = (TextView)itemView.findViewById(R.id.textview_estimate_price);

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
