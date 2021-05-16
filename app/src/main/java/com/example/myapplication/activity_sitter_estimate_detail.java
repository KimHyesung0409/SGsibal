package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class activity_sitter_estimate_detail extends AppCompatActivity {

    private FirebaseFirestore db;
    private String user_id;
    private String pet_id;
    private String info;
    private String to;

    private TextView textView_estimate_detail_pet_name;
    private TextView textView_estimate_detail_pet_gender;
    private TextView textView_estimate_detail_pet_age;
    private TextView textView_estimate_detail_pet_species;
    private TextView textView_estimate_detail_pet_species_detail;

    private TextView textView_estimate_detail_user_name;
    private TextView textView_estimate_detail_user_gender;
    private TextView textView_estimate_detail_user_age;
    private TextView textView_estimate_detail_user_phone;
    private TextView textView_estimate_detail_user_email;

    private TextView textView_estimate_detail_address;
    private TextView textView_estimate_detail_address_detail;
    private TextView textView_estimate_detail_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_estimate_detail);

        Intent intent = getIntent();

        db = FirebaseFirestore.getInstance();
        user_id = intent.getStringExtra("user_id");
        pet_id = intent.getStringExtra("pet_id");
        info = intent.getStringExtra("info");

        textView_estimate_detail_pet_name = (TextView)findViewById(R.id.textview_estimate_detail_pet_name);
        textView_estimate_detail_pet_gender = (TextView)findViewById(R.id.textview_estimate_detail_pet_gender);
        textView_estimate_detail_pet_age = (TextView)findViewById(R.id.textview_estimate_detail_pet_age);
        textView_estimate_detail_pet_species = (TextView)findViewById(R.id.textview_estimate_detail_pet_species);
        textView_estimate_detail_pet_species_detail = (TextView)findViewById(R.id.textview_estimate_detail_pet_species_detail);

        textView_estimate_detail_user_name = (TextView)findViewById(R.id.textview_estimate_detail_user_name);
        textView_estimate_detail_user_gender = (TextView)findViewById(R.id.textview_estimate_detail_user_gender);
        textView_estimate_detail_user_age = (TextView)findViewById(R.id.textview_estimate_detail_user_age);
        textView_estimate_detail_user_phone = (TextView)findViewById(R.id.textview_estimate_detail_user_phone);
        textView_estimate_detail_user_email = (TextView)findViewById(R.id.textview_estimate_detail_user_email);

        textView_estimate_detail_address = (TextView)findViewById(R.id.textview_estimate_detail_address);
        textView_estimate_detail_address_detail = (TextView)findViewById(R.id.textview_estimate_detail_address_detail);
        textView_estimate_detail_info = (TextView)findViewById(R.id.textview_estimate_detail_info);

        textView_estimate_detail_info.setText(info);

        getUserData();
        getPetData();
    }

    public void onClickAcceptEstimate(View view)
    {
        String title = "모두의 집사";
        String body = "등록된 견적서가 수락되었습니다.";

        NotificationMessaging messaging = new NotificationMessaging( to,  title, body, this);

        messaging.start();

        // db에 예약 정보를 등록해야한다.
    }

    public void onClickOfferEstimate(View view)
    {
        String title = "모두의 집사";
        String body = "역으로 제안된 견적서가 도착했습니다.";

        NotificationMessaging messaging = new NotificationMessaging( to,  title, body, this);

        messaging.start();

        // 역으로 제한된 견적서를 처리.
        // 기존 estimate db에 추가를 할지
        // 다른 테이블을 만들어서 관리를 할지 고민중.
    }

    private void getPetData()
    {
        db.collection("users").document(user_id).collection("pet_list").document(pet_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        String pet_name = document.getString("name");
                        String pet_gender = "설정안함";
                        String pet_age = document.getString("age");
                        String pet_species = document.getString("species");
                        String pet_species_detail = document.getString("detail_species");

                        textView_estimate_detail_pet_name.setText("이름 : " + pet_name);
                        textView_estimate_detail_pet_gender.setText("성별 : " + pet_gender);
                        textView_estimate_detail_pet_age.setText("나이 : " + pet_age);
                        textView_estimate_detail_pet_species.setText("종류 : " + pet_species);
                        textView_estimate_detail_pet_species_detail.setText("세부종류 : " + pet_species_detail);

                        Log.d("", "DocumentSnapshot data: " + document.getData());
                    }
                    else
                    {
                        Log.d("", "No such document");
                    }
                }
                else
                {
                    Log.d("", "get failed with ", task.getException());
                }
            }
        });
    }

    private void getUserData()
    {
        db.collection("users").document(user_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        to = document.getString("fcm_token");

                        String user_name = document.getString("name");
                        String user_gender = "설정안함";
                        String user_age = "설정안함";
                        String user_phone = "설정안함";
                        String user_email = "설정안함";

                        String address = document.getString("address");
                        String address_detail = document.getString("address_detail");

                        textView_estimate_detail_user_name.setText("이름 : " + user_name);
                        textView_estimate_detail_user_gender.setText("성별 : " + user_gender);
                        textView_estimate_detail_user_age.setText("생년월일 : " + user_age);
                        textView_estimate_detail_user_phone.setText("전화번호 : " + user_phone);
                        textView_estimate_detail_user_email.setText("이메일 : " + user_email);

                        textView_estimate_detail_address.setText(address);
                        textView_estimate_detail_address_detail.setText(address_detail);

                        Log.d("", "DocumentSnapshot data: " + document.getData());
                    }
                    else
                    {
                        Log.d("", "No such document");
                    }
                }
                else
                {
                    Log.d("", "get failed with ", task.getException());
                }
            }
        });

    }

}