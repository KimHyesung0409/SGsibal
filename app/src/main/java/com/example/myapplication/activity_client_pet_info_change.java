package com.example.myapplication;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class activity_client_pet_info_change extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pet_info_change);

        EditText change_edit_pet_name, change_edit_pet_species, change_edit_pet_detail_species,
                change_edit_pet_age, change_edit_pet_mbti, change_edit_pet_info;

        String change_str_pet_name = "";
        String change_str_pet_species = "";
        String change_str_pet_detail_species = "";
        String change_str_pet_age = "";
        String change_str_pet_mbti = "";
        String change_str_pet_info = "";

        ListViewItem_petlist data = new ListViewItem_petlist();

        change_edit_pet_name = (EditText)findViewById(R.id.change_edit_pet_name);
        change_edit_pet_species = (EditText)findViewById(R.id.change_edit_pet_species);
        change_edit_pet_detail_species = (EditText)findViewById(R.id.change_edit_pet_detail_species);
        change_edit_pet_age = (EditText)findViewById(R.id.change_edit_pet_age);
        change_edit_pet_mbti = (EditText)findViewById(R.id.change_edit_pet_mbti);
        change_edit_pet_info = (EditText)findViewById(R.id.change_edit_pet_info);

        change_str_pet_name = data.getName();
        change_str_pet_species = data.getSpecies();
        change_str_pet_detail_species = data.getDetail_species();
        change_str_pet_age = data.getAge();
        change_str_pet_mbti = data.getMbti();
        change_str_pet_info = data.getInfo();

        change_edit_pet_name.setText(change_str_pet_name);
        change_edit_pet_species.setText(change_str_pet_species);
        change_edit_pet_detail_species.setText(change_str_pet_detail_species);
        change_edit_pet_age.setText(change_str_pet_age);
        change_edit_pet_mbti.setText(change_str_pet_mbti);
        change_edit_pet_info.setText(change_str_pet_info);
    }
}
