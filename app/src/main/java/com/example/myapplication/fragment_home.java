package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class fragment_home extends Fragment implements View.OnClickListener {

    ViewGroup viewGroup;
    Button button_reserve_visit, button_reserve_entrust;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        button_reserve_visit = (Button)viewGroup.findViewById(R.id.button_reserve_visit);
        button_reserve_visit.setOnClickListener(this);
        button_reserve_entrust = (Button)viewGroup.findViewById(R.id.button_reserve_entrust);
        button_reserve_entrust.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {

        MainActivity mainActivity = (MainActivity) getActivity();
        Intent intent;

        switch (v.getId())
        {
            case R.id.button_reserve_visit :
                intent = new Intent(mainActivity,activity_reserve_visit.class);
                startActivity(intent);
                break;

            case R.id.button_reserve_entrust :
                intent = new Intent(mainActivity,activity_reserve_entrust.class);
                startActivity(intent);
                break;
        }

    }
}