package com.example.myapplication;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_chat extends RecyclerView.ViewHolder {

    TextView textView_chat_name;
    TextView textView_chat_text;
    TextView textView_chat_timestamp;
    LinearLayout linearlayout_chat;

    public ViewHolder_chat(View itemView)
    {
        super(itemView);

        textView_chat_name = (TextView)itemView.findViewById(R.id.textview_chat_name);
        textView_chat_text = (TextView)itemView.findViewById(R.id.textview_chat_text);
        textView_chat_timestamp = (TextView)itemView.findViewById(R.id.textview_chat_timestamp);
        linearlayout_chat = (LinearLayout)itemView.findViewById(R.id.linearlayout_chat);
    }

}
