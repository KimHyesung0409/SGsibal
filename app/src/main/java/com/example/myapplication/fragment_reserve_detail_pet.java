package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Activity.activity_reserve_detail;

import org.w3c.dom.Text;

public class fragment_reserve_detail_pet extends Fragment {

    ViewGroup viewGroup;

    private TextView textview_reserve_detail_pet_name;
    private TextView textview_reserve_detail_pet_gender;
    private TextView textview_reserve_detail_pet_age;
    private TextView textview_reserve_detail_pet_species;
    private TextView textview_reserve_detail_pet_species_detail;
    private TextView textview_reserve_detail_pet_mbti;
    private String[] petData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_detail_pet, container, false);

        textview_reserve_detail_pet_name = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_name);
        textview_reserve_detail_pet_gender = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_gender);
        textview_reserve_detail_pet_age = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_age);
        textview_reserve_detail_pet_species = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_species);
        textview_reserve_detail_pet_species_detail = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_species_detail);
        textview_reserve_detail_pet_mbti = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_mbti);

        activity_reserve_detail activity = (activity_reserve_detail) getActivity();
        petData = activity.getPetFragment();

        setReserveDetailPet();

        return viewGroup;
    }

    public void setReserveDetailPet()
    {
        textview_reserve_detail_pet_name.setText(petData[0]);
        textview_reserve_detail_pet_gender.setText(petData[1]);
        textview_reserve_detail_pet_age.setText(petData[2]);
        textview_reserve_detail_pet_species.setText(petData[3]);
        textview_reserve_detail_pet_species_detail.setText(petData[4]);
        textview_reserve_detail_pet_mbti.setText(petData[5]);
    }

}