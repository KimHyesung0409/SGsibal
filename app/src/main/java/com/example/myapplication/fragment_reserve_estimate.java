package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class fragment_reserve_estimate extends Fragment {

    ViewGroup viewGroup;

    public static fragment_reserve_auto newInstance() {
        return new fragment_reserve_auto();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_estimate, container, false);

        return viewGroup;
    }
}