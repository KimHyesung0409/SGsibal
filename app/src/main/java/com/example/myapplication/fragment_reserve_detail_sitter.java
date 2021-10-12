package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Activity.activity_reserve_detail;

public class fragment_reserve_detail_sitter extends Fragment {

    ViewGroup viewGroup;

    private TextView textview_reserve_detail_sitter_name;
    private TextView textview_reserve_detail_sitter_gender;
    private TextView textview_reserve_detail_sitter_birth;
    private TextView textview_reserve_detail_sitter_phone;
    private TextView textview_reserve_detail_sitter_email;

    private String[] sitterData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_detail_sitter, container, false);

        textview_reserve_detail_sitter_name = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_name);
        textview_reserve_detail_sitter_gender = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_gender);
        textview_reserve_detail_sitter_birth = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_birth);
        textview_reserve_detail_sitter_phone = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_phone);
        textview_reserve_detail_sitter_email = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_email);

        activity_reserve_detail activity = (activity_reserve_detail) getActivity();
        sitterData = activity.getSitterFragment();

        setReserveDetailSitter();

        return viewGroup;
    }

    public void setReserveDetailSitter()
    {
        textview_reserve_detail_sitter_name.setText(sitterData[0]);
        textview_reserve_detail_sitter_gender.setText(sitterData[1]);
        textview_reserve_detail_sitter_birth.setText(sitterData[2]);
        textview_reserve_detail_sitter_phone.setText(sitterData[3]);
        textview_reserve_detail_sitter_email.setText(sitterData[4]);
    }

}