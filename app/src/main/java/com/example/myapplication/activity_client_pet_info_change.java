package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class activity_client_pet_info_change extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText change_edit_pet_name;
    private EditText change_edit_pet_species;
    private EditText change_edit_pet_detail_species;
    private EditText change_edit_pet_age;
    private EditText change_edit_pet_mbti;
    private EditText change_edit_pet_info;

    private String pet_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pet_info_change);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 인텐트를 얻음
        // 여기에 정보가 담겨있음
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String species = intent.getStringExtra("species");
        String detail_species = intent.getStringExtra("detail_species");
        String age = intent.getStringExtra("age");
        String mbti = intent.getStringExtra("mbti");
        String info = intent.getStringExtra("info");
        //pet_id 는 업데이트에 사용하기 위해서 전역 변수.
        pet_id = intent.getStringExtra("pet_id");


        // intent 를 통해서 정보를 받던지
        // pet_id로 조회해서 정보를 받아야 하는데
        // data = new ListViewItem_petlist(); 이므로 그냥 null임.
        // null 데이터를 가지고 setText를 해봤자 null 이므로 텍스트가 뜨지 않는 것.
        // ListViewItem_petlist data = new ListViewItem_petlist();

        change_edit_pet_name = (EditText) findViewById(R.id.change_edit_pet_name);
        change_edit_pet_species = (EditText) findViewById(R.id.change_edit_pet_species);
        change_edit_pet_detail_species = (EditText) findViewById(R.id.change_edit_pet_detail_species);
        change_edit_pet_age = (EditText) findViewById(R.id.change_edit_pet_age);
        change_edit_pet_mbti = (EditText) findViewById(R.id.change_edit_pet_mbti);
        change_edit_pet_info = (EditText) findViewById(R.id.change_edit_pet_info);

        change_edit_pet_name.setText(name);
        change_edit_pet_species.setText(species);
        change_edit_pet_detail_species.setText(detail_species);
        change_edit_pet_age.setText(age);
        change_edit_pet_mbti.setText(mbti);
        change_edit_pet_info.setText(info);
    }

    public void onClickPetInfoChange(View view)
    {

        String name = change_edit_pet_name.getText().toString().trim();
        String age = change_edit_pet_age.getText().toString().trim();
        String species = change_edit_pet_species.getText().toString().trim();
        String detail_species = change_edit_pet_detail_species.getText().toString().trim();
        String mbti = change_edit_pet_mbti.getText().toString().trim();
        String info = change_edit_pet_info.getText().toString().trim();

        Map<String, Object> pet = new HashMap<>();

        pet.put("name", name);
        pet.put("age", age);
        pet.put("species", species);
        pet.put("detail_species", detail_species);
        pet.put("mbti", mbti);
        pet.put("info", info);

        db.collection("users").document(auth.getUid())
                .collection("pet_list").document(pet_id)
                .update(pet)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });

    }

}
