package com.example.myapplication;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_search extends RecyclerView.ViewHolder {

    TextView textView_search_sitter_name;
    TextView textView_search_sitter_age;

    public ViewHolder_search(@NonNull View itemView)
    {
        super(itemView);

        textView_search_sitter_age=itemView.findViewById(R.id.search_sitter_age);
        textView_search_sitter_name=itemView.findViewById(R.id.search_sitter_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION)
                {
                    try
                    {
                        fragment_reserve_search listener = null;
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
