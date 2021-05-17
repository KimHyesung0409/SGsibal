package com.example.myapplication;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_reserve_favorites extends RecyclerView.ViewHolder {
    public ViewHolder_reserve_favorites(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {

                    }
                }
            }
        });
    }

    public ViewHolder_reserve_favorites(@NonNull View itemView) {
        super(itemView);
    }
}
