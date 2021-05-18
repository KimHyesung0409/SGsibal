package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_storylist extends RecyclerView.ViewHolder {

    TextView textView_story_title;
    ImageView imageView_story;
    TextView textView_story_content;

    public ViewHolder_storylist(View itemView, OnCustomClickListener listener){
        super(itemView);

        textView_story_title = itemView.findViewById(R.id.textview_story_title);
        imageView_story = itemView.findViewById(R.id.imageview_story);
        textView_story_content = itemView.findViewById(R.id.textview_story_content);

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
