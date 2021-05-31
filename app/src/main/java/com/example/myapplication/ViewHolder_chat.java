package com.example.myapplication;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/*
 * 채팅 요소 ViewHolder 클래스
 * 채팅 작성자
 * 채팅 텍스트
 * 채팅을 입력한 사람의 이름
 * 채팅을 입력한 시간(왼쪽, 오른쪽)
 */


public class ViewHolder_chat extends RecyclerView.ViewHolder {

    TextView textView_chat_name;
    TextView textView_chat_text;
    TextView textView_chat_timestamp_left;
    TextView textView_chat_timestamp_right;
    LinearLayout linearlayout_chat;

    public ViewHolder_chat(View itemView)
    {
        super(itemView);

        textView_chat_name = (TextView)itemView.findViewById(R.id.textview_chat_name);
        textView_chat_text = (TextView)itemView.findViewById(R.id.textview_chat_text);
        textView_chat_timestamp_left = (TextView)itemView.findViewById(R.id.textview_chat_timestamp_left);
        textView_chat_timestamp_right = (TextView)itemView.findViewById(R.id.textview_chat_timestamp_right);
        linearlayout_chat = (LinearLayout)itemView.findViewById(R.id.linearlayout_chat);
    }

}
