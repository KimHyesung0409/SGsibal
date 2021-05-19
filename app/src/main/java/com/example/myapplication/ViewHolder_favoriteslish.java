package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_favoriteslish extends RecyclerView.ViewHolder {
    TextView    textView_favorite_name;
    TextView    textView_favorite_age;
    
    public ViewHolder_favoriteslish(@NonNull View itemView) 
    {
        super(itemView);
        
        textView_favorite_age=itemView.findViewById(R.id.favorite_age);
        textView_favorite_name=itemView.findViewById(R.id.favorite_name);
        
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                {
                    try
                    {
                        fragment_reserve_search listener = null;
                        View view = null;
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
                        fragment_reserve_search listener = null;
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

