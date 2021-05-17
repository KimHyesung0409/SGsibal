package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_reviewlist extends RecyclerView.ViewHolder {

    TextView textView_my_review;
    EditText editText_review_title;
    EditText editText_review_content;
    Button button_review_upload;

    public ViewHolder_reviewlist(View itemView, OnCustomClickListener listener)
    {
        super(itemView);

        textView_my_review = (TextView)itemView.findViewById(R.id.textView_my_review);

        itemView.setOnClickListener(new View);

    }


}
