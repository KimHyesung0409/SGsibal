package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class fragment_reserve_search extends Fragment {

    ViewGroup viewGroup;
    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private RecyclerView.ViewHolder viewHolder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_search, container, false);

        return viewGroup;
    }
}