package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_chatroom extends RecyclerView.ViewHolder{

    TextView textview_chatroom_opponent;

    public ViewHolder_chatroom(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        textview_chatroom_opponent = (TextView)itemView.findViewById(R.id.textview_chatroom_opponent);

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
