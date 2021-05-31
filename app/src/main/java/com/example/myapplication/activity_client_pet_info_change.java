package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class activity_client_pet_info_change extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText change_edit_pet_name;
    private EditText change_edit_pet_species;
    private EditText change_edit_pet_detail_species;
    private EditText change_edit_pet_age;
    private EditText change_edit_pet_mbti;
    private EditText change_edit_pet_info;

    private RadioGroup radioGroup_change_pet_info;
    private boolean gender = false;

    private String pet_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pet_info_change);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 인텐트를 얻음
        // 여기에 정보가 담겨있음
        // 수정할 반려동물의 정보를 intent를 통해 전달받는다.
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String species = intent.getStringExtra("species");
        String detail_species = intent.getStringExtra("detail_species");
        String age = intent.getStringExtra("age");
        String mbti = intent.getStringExtra("mbti");
        String info = intent.getStringExtra("info");
        //pet_id 는 업데이트에 사용하기 위해서 전역 변수.
        pet_id = intent.getStringExtra("pet_id");

        change_edit_pet_name = (EditText) findViewById(R.id.change_edit_pet_name);
        change_edit_pet_species = (EditText) findViewById(R.id.change_edit_pet_species);
        change_edit_pet_detail_species = (EditText) findViewById(R.id.change_edit_pet_detail_species);
        change_edit_pet_age = (EditText) findViewById(R.id.change_edit_pet_age);
        change_edit_pet_mbti = (EditText) findViewById(R.id.change_edit_pet_mbti);
        change_edit_pet_info = (EditText) findViewById(R.id.change_edit_pet_info);

        radioGroup_change_pet_info = (RadioGroup)findViewById(R.id.radioGroup_change_pet_info);
        radioGroup_change_pet_info.setOnCheckedChangeListener(this);

        // intent로 전달받은 반려동물 정보를 출력한다.
        change_edit_pet_name.setText(name);
        change_edit_pet_species.setText(species);
        change_edit_pet_detail_species.setText(detail_species);
        change_edit_pet_age.setText(age);
        change_edit_pet_mbti.setText(mbti);
        change_edit_pet_info.setText(info);
    }
    // 수정버튼 클릭 메소드
    public void onClickPetInfoChange(View view)
    {
        // 모든 반려동물 정보를 불러오고 db에 등록한다.
        String name = change_edit_pet_name.getText().toString().trim();
        String age = change_edit_pet_age.getText().toString().trim();
        String species = change_edit_pet_species.getText().toString().trim();
        String detail_species = change_edit_pet_detail_species.getText().toString().trim();
        String mbti = change_edit_pet_mbti.getText().toString().trim();
        String info = change_edit_pet_info.getText().toString().trim();

        // 불러온 반려동물 정보를 HashMap으로 만들어 db에 등록한다.
        Map<String, Object> pet = new HashMap<>();

        pet.put("name", name);
        pet.put("gender", gender);
        pet.put("age", age);
        pet.put("species", species);
        pet.put("detail_species", detail_species);
        pet.put("mbti", mbti);
        pet.put("info", info);

        // 유저 - 반려동물 목록 컬렉션에 위에서 만든 반려동물 HashMap정보를 등록한다.
        db.collection("users").document(auth.getUid())
                .collection("pet_list").document(pet_id)
                .update(pet)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    // 등록에 성공하면 액티비티를 종료한다.
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
    // 반려동물의 성별을 설정하기 위한 메소드
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {

        switch (checkedId)
        {
            case R.id.radiobutton_change_pet_info_male:

                gender = true;

                break;

            default:

                gender = false;
        }
    }
}
