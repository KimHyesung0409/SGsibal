package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_petlist extends RecyclerView.ViewHolder {

    TextView textView_pet_name;
    TextView textView_pet_species;
    TextView textview_pet_age;

    public ViewHolder_petlist(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        textView_pet_name = itemView.findViewById(R.id.textview_pet_name);
        textView_pet_species = itemView.findViewById(R.id.textview_pet_species);
        textview_pet_age = itemView.findViewById(R.id.textview_pet_age);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                {
                    listener.onItemClick(v, position);
                }
            }
        });
    }
}
