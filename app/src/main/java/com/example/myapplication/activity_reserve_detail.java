package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class activity_reserve_detail extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private boolean callFrom;
    private String reserve_id;
    private String user_id;
    private String pet_id;
    private String user_token;

    private TextView textview_reserve_detail_client_name;
    private TextView textview_reserve_detail_client_gender;
    private TextView textview_reserve_detail_client_birth;
    private TextView textview_reserve_detail_client_phone;
    private TextView textview_reserve_detail_client_email;
    private TextView textview_reserve_detail_sitter_name;
    private TextView textview_reserve_detail_sitter_gender;
    private TextView textview_reserve_detail_sitter_birth;
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

        // intent ??? ??????_id ??? user_id, pet_id ????????? ???????????? ????????? ?????? ??????????????? ????????????????????? ?????? ?????? callFrom ??? ???????????????.
        // ????????? callFrom??? true??? ?????? ???????????? ??????????????? ????????? user_id??? ????????? ??? ???????????? user_id??? ????????????
        // callFrom??? false??? ????????? ???????????? ??????????????? ????????? user_id??? ????????? ??? ????????? user_id??? ????????????.

        callFrom = intent.getBooleanExtra("callFrom", false);
        reserve_id = intent.getStringExtra("reserve_id");
        user_id = intent.getStringExtra("user_id");
        pet_id = intent.getStringExtra("pet_id");

        textview_reserve_detail_client_name = (TextView)findViewById(R.id.textview_reserve_detail_client_name);
        textview_reserve_detail_client_gender = (TextView)findViewById(R.id.textview_reserve_detail_client_gender);
        textview_reserve_detail_client_birth = (TextView)findViewById(R.id.textview_reserve_detail_client_birth);
        textview_reserve_detail_client_phone = (TextView)findViewById(R.id.textview_reserve_detail_client_phone);
        textview_reserve_detail_client_email = (TextView)findViewById(R.id.textview_reserve_detail_client_email);
        textview_reserve_detail_sitter_name = (TextView)findViewById(R.id.textview_reserve_detail_sitter_name);
        textview_reserve_detail_sitter_gender = (TextView)findViewById(R.id.textview_reserve_detail_sitter_gender);
        textview_reserve_detail_sitter_birth = (TextView)findViewById(R.id.textview_reserve_detail_sitter_birth);
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
        textview_reserve_detail_address_detail = (TextView)findViewById(R.id.textview_reserve_detail_address_detail);
        textview_reserve_detail_info = (TextView)findViewById(R.id.textview_reserve_detail_info);
        textview_reserve_detail_price = (TextView)findViewById(R.id.textview_reserve_detail_price);

        button_reserve_detail_1 = (Button)findViewById(R.id.button_reserve_detail_1);
        button_reserve_detail_2 = (Button)findViewById(R.id.button_reserve_detail_2);

        getMyData();
        getReserveData();
        getUserData();
        getPetData();
    }

    // ??????????????? ????????? ???????????? callFrom??? ?????? ????????? ????????? ????????? ????????????.
    // callFrom??? true??? ???????????? false??? ????????? ?????? ??????????????? ????????????.
    public void onClickReserveDetail_1(View view)
    {
        Intent intent;

        // ?????? ????????????
        if(callFrom)
        {
            String user_name = textview_reserve_detail_sitter_name.getText().toString();
            String datetime = textview_reserve_detail_datetime.getText().toString();
            String gender = textview_reserve_detail_sitter_gender.getText().toString();
            String birth = textview_reserve_detail_sitter_birth.getText().toString();

            // ?????? ??????
            intent = new Intent(this, activity_upload_review.class);
            intent.putExtra("user_id", user_id);
            intent.putExtra("user_name", user_name);
            intent.putExtra("reserve_id", reserve_id);
            intent.putExtra("datetime", datetime);
            intent.putExtra("gender", gender);
            intent.putExtra("birth", birth);

            startActivityForResult(intent, REQUEST_CODE);
        }
        else // ????????? ????????????
        {
            // ????????? ??????
            intent = new Intent(this, activity_upload_story.class);
            intent.putExtra("reserve_id", reserve_id);
            intent.putExtra("user_token", user_token);
            intent.putExtra("user_id", user_id);

            startActivity(intent);
        }
    }
    // ????????? ???????????? ????????? ???????????? ???????????? ??????
    // ?????? ????????? ?????? ????????? ????????? ???????????? ??????????????? ???????????? ?????????
    public void onClickReserveDetail_2(View view)
    {
        Intent intent = new Intent(this, activity_story_list.class);

        intent.putExtra("reserve_id", reserve_id);

        startActivity(intent);
    }

    // ????????? ?????? ????????? textview??? ???????????? ?????????
    // callFrom??? true??? ?????? false??? ?????????
    private void getMyData()
    {
        // ?????? ????????????
        if(callFrom)
        {
            textview_reserve_detail_client_name.setText(LoginUserData.getUser_name());
            textview_reserve_detail_client_gender.setText(Gender.getGender(LoginUserData.getGender()));
            textview_reserve_detail_client_birth.setText(LoginUserData.getBirth());
            textview_reserve_detail_client_phone.setText(LoginUserData.getPhone());
            textview_reserve_detail_client_email.setText(auth.getCurrentUser().getEmail());

            button_reserve_detail_1.setText("?????? ??????");
            button_reserve_detail_2.setText("????????? ??????");
        }
        else // ????????? ????????????
        {
            textview_reserve_detail_sitter_name.setText(LoginUserData.getUser_name());
            textview_reserve_detail_sitter_gender.setText(Gender.getGender(LoginUserData.getGender()));
            textview_reserve_detail_sitter_birth.setText(LoginUserData.getBirth());
            textview_reserve_detail_sitter_phone.setText(LoginUserData.getPhone());
            textview_reserve_detail_sitter_email.setText(auth.getCurrentUser().getEmail());

            button_reserve_detail_1.setText("????????? ??????");
            button_reserve_detail_2.setVisibility(View.GONE);
        }
    }
    // ???????????? ??????_id??? ?????? ?????? ?????? ????????? ???????????? ?????????
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
                                     String address_detail = document.getString("address_detail");
                                     String price = document.getString("price");


                                     // ???????????? textview??? ??????.
                                     textview_reserve_detail_datetime.setText(DateString.DateToString(datetime));
                                     textview_reserve_detail_address.setText(address);

                                     textview_reserve_detail_address_detail.setText(address_detail);
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
    // ????????? ????????? ???????????? ?????????
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
                                String gender = Gender.getGender(document.getBoolean("gender"));
                                String birth = document.getString("birth");
                                String phone = document.getString("phone");
                                String email = document.getString("email");

                                // callFrom ??? true ??? ?????? ??????????????????.
                                // ?????? ??????????????? ?????????????????? ???????????? ????????? ????????? LoginUserData??? ???????????? ?????????
                                // ???????????? ????????? ??????????????????.
                                if(callFrom)
                                {
                                    textview_reserve_detail_sitter_name.setText(name);
                                    textview_reserve_detail_sitter_gender.setText(gender);
                                    textview_reserve_detail_sitter_birth.setText(birth);
                                    textview_reserve_detail_sitter_phone.setText(phone);
                                    textview_reserve_detail_sitter_email.setText(email);
                                }
                                else
                                {
                                    textview_reserve_detail_client_name.setText(name);
                                    textview_reserve_detail_client_gender.setText(gender);
                                    textview_reserve_detail_client_birth.setText(birth);
                                    textview_reserve_detail_client_phone.setText(phone);
                                    textview_reserve_detail_client_email.setText(email);
                                    user_token = document.getString("fcm_token");
                                }
                            }
                            else
                            {

                            }

                        }
                    }
                });
    }

    // ??? ????????? ???????????? ?????????
    private void getPetData()
    {
        DocumentReference ref;
        //???????????? id??? user_id??? ????????????.
        // ?????? ??????????????? ?????? ??????????????? ????????? uid??? ??????.
        if(callFrom)
        {
            ref = db.collection("users").document(auth.getUid());
        }
        else // ????????? ??????????????? ?????? ??????????????? ????????? user_id??? ???????????????.
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
                                String gender = Gender.getGenderPet(document.getBoolean("gender"));

                                // ????????? ??? ????????? textview??? ??????
                                textview_reserve_detail_pet_name.setText(name);
                                textview_reserve_detail_pet_age.setText(age);
                                textview_reserve_detail_pet_species.setText(species);
                                textview_reserve_detail_pet_species_detail.setText(species_detail);
                                textview_reserve_detail_pet_mbti.setText(mbti);
                                textview_reserve_detail_pet_gender.setText(gender);
                            }
                            else
                            {

                            }

                        }
                    }
                });
    }
    // ????????? ??????, ?????? ????????? ???????????? ???????????? ?????? ??????????????? ??????.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }


    }

}