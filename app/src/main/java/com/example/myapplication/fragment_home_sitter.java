package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class fragment_home_sitter extends Fragment implements View.OnClickListener {

    ViewGroup viewGroup;
    private Button button_search_estimate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_home_sitter, container, false);

        button_search_estimate = (Button)viewGroup.findViewById(R.id.button_search_estimate);
        button_search_estimate.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_search_estimate :

                Activity activity = getActivity();
                Intent intent = new Intent(activity, activity_sitter_estimate.class);
                startActivity(intent);

                break;
        }

    }
}