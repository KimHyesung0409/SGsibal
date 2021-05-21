package com.example.myapplication;

import android.view.View;
import android.widget.AdapterView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_searchlist extends RecyclerView.ViewHolder {

    TextView textView_searchlist_user_name;
    TextView textView_searchlist_birth;
    TextView textView_searchlist_gender;
    TextView textView_searchlist_datetime;
    RatingBar ratingBar_searchlist;

    public ViewHolder_searchlist(@NonNull View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        textView_searchlist_user_name = itemView.findViewById(R.id.textview_searchlist_user_name);
        textView_searchlist_birth = itemView.findViewById(R.id.textview_searchlist_birth);
        textView_searchlist_gender = itemView.findViewById(R.id.textview_searchlist_gender);
        textView_searchlist_datetime = itemView.findViewById(R.id.textview_searchlist_datetime);
        ratingBar_searchlist = itemView.findViewById(R.id.ratingBar_searchlist);

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
