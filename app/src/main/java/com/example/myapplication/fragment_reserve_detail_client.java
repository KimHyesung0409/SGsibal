package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Activity.activity_reserve_detail;

public class fragment_reserve_detail_client extends Fragment {

    ViewGroup viewGroup;

    private TextView textview_reserve_detail_client_name;
    private TextView textview_reserve_detail_client_gender;
    private TextView textview_reserve_detail_client_birth;
    private TextView textview_reserve_detail_client_phone;
    private TextView textview_reserve_detail_client_email;
    private String[] clientData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_detail_client, container, false);

        textview_reserve_detail_client_name = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_name);
        textview_reserve_detail_client_gender = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_gender);
        textview_reserve_detail_client_birth = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_birth);
        textview_reserve_detail_client_phone = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_phone);
        textview_reserve_detail_client_email = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_email);

        activity_reserve_detail activity = (activity_reserve_detail) getActivity();
        clientData = activity.getClientFragment();

        setReserveDetailClient();

        return viewGroup;
    }

    public void setReserveDetailClient()
    {
        textview_reserve_detail_client_name.setText(clientData[0]);
        textview_reserve_detail_client_gender.setText(clientData[1]);
        textview_reserve_detail_client_birth.setText(clientData[2]);
        textview_reserve_detail_client_phone.setText(clientData[3]);
        textview_reserve_detail_client_email.setText(clientData[4]);
    }


}