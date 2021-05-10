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
    private Button button_search_estimate, button_regester_sitter, button_upload_entrust;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_home_sitter, container, false);

        button_search_estimate = (Button)viewGroup.findViewById(R.id.button_search_estimate);
        button_search_estimate.setOnClickListener(this);
        button_regester_sitter = (Button)viewGroup.findViewById(R.id.button_regester_sitter);
        button_regester_sitter.setOnClickListener(this);
        button_upload_entrust = (Button)viewGroup.findViewById(R.id.button_upload_entrust);
        button_upload_entrust.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        Activity activity = getActivity();

        switch (v.getId())
        {
            case R.id.button_search_estimate :

                Intent intent_search_estimate = new Intent(activity, activity_sitter_estimate.class);
                startActivity(intent_search_estimate);

                break;
            case R.id.button_regester_sitter :
                Intent intent_regester_sitter = new Intent(activity, activity_sitter_assignment_form.class);
                startActivity(intent_regester_sitter);

                break;

            case R.id.button_upload_entrust :
                Intent intent_upload_entrust = new Intent(activity, activity_upload_entrust.class);
                startActivity(intent_upload_entrust);

                break;
        }

    }
}