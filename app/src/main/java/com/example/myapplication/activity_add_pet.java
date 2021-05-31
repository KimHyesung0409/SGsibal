package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class activity_add_pet extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText edit_pet_name;
    private EditText edit_pet_species;
    private EditText edit_pet_age;
    private EditText edit_pet_detail_species;
    private EditText edit_pet_mbti;
    private EditText edit_pet_info;

    private RadioGroup radioGroup_add_pet;

    private boolean pet_gender = false;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        uid = auth.getUid();

        edit_pet_name = (EditText)findViewById(R.id.edit_pet_name);
        edit_pet_species = (EditText)findViewById(R.id.edit_pet_species);
        edit_pet_age = (EditText)findViewById(R.id.edit_pet_age);
        edit_pet_detail_species = (EditText)findViewById(R.id.edit_pet_detail_species);
        edit_pet_mbti = (EditText)findViewById(R.id.edit_pet_mbti);
        edit_pet_info = (EditText)findViewById(R.id.edit_pet_info);

        radioGroup_add_pet = (RadioGroup)findViewById(R.id.radioGroup_add_pet);
        radioGroup_add_pet.setOnCheckedChangeListener(this);
    }

    // 입력한 반려동물 정보를 DB에 등록하는 메소드
    public void onClickAddPet(View view)
    {

        String pet_name = edit_pet_name.getText().toString().trim();
        String pet_species = edit_pet_species.getText().toString().trim();
        String pet_age = edit_pet_age.getText().toString().trim();
        String pet_detail_species = edit_pet_detail_species.getText().toString().trim();
        String pet_mbti = edit_pet_mbti.getText().toString().trim();
        String pet_info = edit_pet_info.getText().toString().trim();

        // Key와 Value를 가지는 맵
        Map<String, Object> data = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        data.put("name", pet_name);
        data.put("gender", pet_gender);
        data.put("species", pet_species);
        data.put("age", pet_age);
        data.put("detail_species", pet_detail_species);
        data.put("mbti", pet_mbti);
        data.put("info", pet_info);

        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        // db에 등록이 완료되면 종료.
        db.collection("users").document(uid).collection("pet_list")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(activity_add_pet.this, "반려동물 등록 완료", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_add_pet.this, "반려동물 등록 실패", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    // 반려동물의 성별을 설정하기 위한 메소드
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {

        switch (checkedId)
        {
            case R.id.radiobutton_add_pet_male:

                pet_gender = true;

                break;

            default:

                pet_gender = false;
        }
    }

}