package com.example.myapplication.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.OnCustomClickListener;
import com.example.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 * 채팅방 요소 ViewHolder 클래스
 * 상대방의 이름
 */

public class ViewHolder_chatroom extends RecyclerView.ViewHolder{

    CircleImageView circleimageview_chatroom_image;
    TextView textview_chatroom_opponent;

    public ViewHolder_chatroom(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        circleimageview_chatroom_image = (CircleImageView)itemView.findViewById(R.id.circleimageview_chatroom_image);
        textview_chatroom_opponent = (TextView)itemView.findViewById(R.id.textview_chatroom_opponent);

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
