package com.example.myapplication;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_reviewlist extends RecyclerView.ViewHolder {

    TextView textView_review_title;
    TextView textView_review_content;
    TextView textView_review_target_name;
    RatingBar ratingBar_review;

    public ViewHolder_reviewlist(View itemView, OnCustomClickListener listener){
        super(itemView);

        textView_review_title = itemView.findViewById(R.id.textview_review_title);
        textView_review_content = itemView.findViewById(R.id.textview_review_content);
        textView_review_target_name = itemView.findViewById(R.id.textview_review_target_name);
        ratingBar_review = itemView.findViewById(R.id.ratingBar_review);

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