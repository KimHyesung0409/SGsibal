package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class activity_reserve_detail extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private boolean callFrom;
    private String reserve_id;
    private String user_id;
    private String pet_id;

    private TextView textview_reserve_detail_client_name;
    private TextView textview_reserve_detail_client_gender;
    private TextView textview_reserve_detail_client_age;
    private TextView textview_reserve_detail_client_phone;
    private TextView textview_reserve_detail_client_email;
    private TextView textview_reserve_detail_sitter_name;
    private TextView textview_reserve_detail_sitter_gender;
    private TextView textview_reserve_detail_sitter_age;
    private TextView textview_reserve_detail_sitter_phone;
    private TextView textview_reserve_detail_sitter_email;

    private TextView textview_reserve_detail_pet_name;
    private TextView textview_reserve_detail_pet_gender;
    private TextView textview_reserve_detail_pet_age;
    private TextView textview_reserve_detail_pet_species;
    private TextView textview_reserve_detail_pet_species_detail;
    private TextView textview_reserve_detail_pet_mbti;

    private TextView textview_reserve_detail_datetime;
    private TextView textview_reserve_detail_address;
    private TextView textview_reserve_detail_address_detail;
    private TextView textview_reserve_detail_info;
    private TextView textview_reserve_detail_price;

    private Button button_reserve_detail_1;
    private Button button_reserve_detail_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_detail);

        Intent intent = getIntent();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        callFrom = intent.getBooleanExtra("callFrom", false);
        reserve_id = intent.getStringExtra("reserve_id");
        user_id = intent.getStringExtra("user_id");
        pet_id = intent.getStringExtra("pet_id");

        textview_reserve_detail_client_name = (TextView)findViewById(R.id.textview_reserve_detail_client_name);
        textview_reserve_detail_client_gender = (TextView)findViewById(R.id.textview_reserve_detail_client_gender);
        textview_reserve_detail_client_age = (TextView)findViewById(R.id.textview_reserve_detail_client_age);
        textview_reserve_detail_client_phone = (TextView)findViewById(R.id.textview_reserve_detail_client_phone);
        textview_reserve_detail_client_email = (TextView)findViewById(R.id.textview_reserve_detail_client_email);
        textview_reserve_detail_sitter_name = (TextView)findViewById(R.id.textview_reserve_detail_sitter_name);
        textview_reserve_detail_sitter_gender = (TextView)findViewById(R.id.textview_reserve_detail_sitter_gender);
        textview_reserve_detail_sitter_age = (TextView)findViewById(R.id.textview_reserve_detail_sitter_age);
        textview_reserve_detail_sitter_phone = (TextView)findViewById(R.id.textview_reserve_detail_sitter_phone);
        textview_reserve_detail_sitter_email = (TextView)findViewById(R.id.textview_reserve_detail_sitter_email);

        textview_reserve_detail_pet_name = (TextView)findViewById(R.id.textview_reserve_detail_pet_name);
        textview_reserve_detail_pet_gender = (TextView)findViewById(R.id.textview_reserve_detail_pet_gender);
        textview_reserve_detail_pet_age = (TextView)findViewById(R.id.textview_reserve_detail_pet_age);
        textview_reserve_detail_pet_species = (TextView)findViewById(R.id.textview_reserve_detail_pet_species);
        textview_reserve_detail_pet_species_detail = (TextView)findViewById(R.id.textview_reserve_detail_pet_species_detail);
        textview_reserve_detail_pet_mbti = (TextView)findViewById(R.id.textview_reserve_detail_pet_mbti);

        textview_reserve_detail_datetime = (TextView)findViewById(R.id.textview_reserve_detail_datetime);
        textview_reserve_detail_address = (TextView)findViewById(R.id.textview_reserve_detail_address);
        textview_reserve_detail_address_detail = (TextView)findViewById(R.id.textview_reserve_auto_address_detail);
        textview_reserve_detail_info = (TextView)findViewById(R.id.textview_reserve_detail_info);
        textview_reserve_detail_price = (TextView)findViewById(R.id.textview_reserve_detail_price);

        button_reserve_detail_1 = (Button)findViewById(R.id.button_reserve_detail_1);
        button_reserve_detail_2 = (Button)findViewById(R.id.button_reserve_detail_2);

        getMyData();
        getReserveData();
        getUserData();
        getPetData();
    }

    public void onClickReserveDetail_1(View view)
    {
        Intent intent;

        // 유저 예약현황
        if(callFrom)
        {
            String user_name = textview_reserve_detail_sitter_name.getText().toString();

            // 후기 작성
            intent = new Intent(this, activity_upload_review.class);
            intent.putExtra("user_id", user_id);
            intent.putExtra("user_name", user_name);

            startActivity(intent);
        }
        else // 펫시터 예약현황
        {
            // 스토리 작성
            intent = new Intent(this, activity_upload_story.class);
            intent.putExtra("reserve_id", reserve_id);

            startActivity(intent);
        }
    }

    public void onClickReserveDetail_2(View view)
    {

    }

    private void getMyData()
    {
        // 유저 예약현황
        if(callFrom)
        {
            textview_reserve_detail_client_name.setText(LoginUserData.getUser_name());

            button_reserve_detail_1.setText("후기 작성");
            button_reserve_detail_2.setText("스토리 보기");
        }
        else // 펫시터 예약현황
        {
            textview_reserve_detail_sitter_name.setText(LoginUserData.getUser_name());

            button_reserve_detail_1.setText("스토리 작성");
            button_reserve_detail_2.setVisibility(View.GONE);
        }
    }

    private void getReserveData()
    {
        db.collection("reserve").document(reserve_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                             if(task.isSuccessful())
                             {

                                 DocumentSnapshot document = task.getResult();

                                 if(document.exists())
                                 {
                                     Date datetime = document.getDate("datetime");
                                     String info = document.getString("info");
                                     String address = document.getString("address");
                                     String price = document.getString("price");

                                     textview_reserve_detail_datetime.setText(DateString.DateToString(datetime));
                                     textview_reserve_detail_address.setText(address);
                                     textview_reserve_detail_info.setText(info);
                                     textview_reserve_detail_price.setText(price);
                                 }
                                 else
                                 {

                                 }

                             }
                    }
                });
    }

    private void getUserData()
    {
        db.collection("users").document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {

                            DocumentSnapshot document = task.getResult();

                            if(document.exists())
                            {
                                String name = document.getString("name");
                                // String gender
                                // String age
                                // String phone
                                // String email

                                // callFrom 이 true 면 고객 예약현황이다.
                                // 고객 입장에서의 예약현황이고 로그인시 고객의 정보는 LoginUserData에 저장되기 때문에
                                // 펫시터의 정보를 가져와야한다.
                                if(callFrom)
                                {
                                    textview_reserve_detail_sitter_name.setText(name);
                                }
                                else
                                {
                                    textview_reserve_detail_client_name.setText(name);
                                }
                            }
                            else
                            {

                            }

                        }
                    }
                });
    }

    private void getPetData()
    {
        DocumentReference ref;
        //펫시터의 id를 user_id로 받아온다.
        // 고객 입장에서의 예약 현황이므로 본인의 uid를 사용.
        if(callFrom)
        {
            ref = db.collection("users").document(auth.getUid());
        }
        else // 펫시터 입장에서의 예약 현황이므로 받아온 user_id를 사용해야함.
        {
            ref = db.collection("users").document(user_id);
        }

        ref.collection("pet_list").document(pet_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {

                            DocumentSnapshot document = task.getResult();

                            if(document.exists())
                            {
                                String name = document.getString("name");
                                String age = document.getString("age");
                                String mbti = document.getString("mbti");
                                String species = document.getString("species");
                                String species_detail = document.getString("detail_species");
                                //String gender

                                textview_reserve_detail_pet_name.setText(name);
                                textview_reserve_detail_pet_age.setText(age);
                                textview_reserve_detail_pet_species.setText(species);
                                textview_reserve_detail_pet_species_detail.setText(species_detail);
                                textview_reserve_detail_pet_mbti.setText(mbti);
                            }
                            else
                            {

                            }

                        }
                    }
                });
    }

}