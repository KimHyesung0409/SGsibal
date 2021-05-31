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

        // intent 로 예약_id 와 user_id, pet_id 정보를 전달받고 어디서 해당 액티비티를 실행시켰는지에 대한 정보 callFrom 을 전달받는다.
        // 여기서 callFrom이 true면 고객 화면에서 실행했다는 의미로 user_id는 상대방 즉 펫시터의 user_id를 가져오고
        // callFrom이 false면 펫시터 화면에서 실행했다는 의미로 user_id는 상대방 즉 고객의 user_id를 가져온다.

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

    // 액티비티를 재사용 하기위해 callFrom과 자바 코드로 기능을 다르게 수행한다.
    // callFrom이 true면 후기작성 false는 스토리 작성 액티비티를 호출한다.
    public void onClickReserveDetail_1(View view)
    {
        Intent intent;

        // 유저 예약현황
        if(callFrom)
        {
            String user_name = textview_reserve_detail_sitter_name.getText().toString();
            String datetime = textview_reserve_detail_datetime.getText().toString();
            String gender = textview_reserve_detail_sitter_gender.getText().toString();
            String birth = textview_reserve_detail_sitter_birth.getText().toString();

            // 후기 작성
            intent = new Intent(this, activity_upload_review.class);
            intent.putExtra("user_id", user_id);
            intent.putExtra("user_name", user_name);
            intent.putExtra("reserve_id", reserve_id);
            intent.putExtra("datetime", datetime);
            intent.putExtra("gender", gender);
            intent.putExtra("birth", birth);

            startActivityForResult(intent, REQUEST_CODE);
        }
        else // 펫시터 예약현황
        {
            // 스토리 작성
            intent = new Intent(this, activity_upload_story.class);
            intent.putExtra("reserve_id", reserve_id);
            intent.putExtra("user_token", user_token);
            intent.putExtra("user_id", user_id);

            startActivity(intent);
        }
    }
    // 고객이 펫시터가 작성한 스토리를 확인하기 위해
    // 해당 예약에 대한 스토리 목록을 출력하는 액티비티를 호출하는 메소드
    public void onClickReserveDetail_2(View view)
    {
        Intent intent = new Intent(this, activity_story_list.class);

        intent.putExtra("reserve_id", reserve_id);

        startActivity(intent);
    }

    // 본인의 대한 정보를 textview에 출력하는 메소드
    // callFrom이 true면 고객 false면 펫시터
    private void getMyData()
    {
        // 유저 예약현황
        if(callFrom)
        {
            textview_reserve_detail_client_name.setText(LoginUserData.getUser_name());
            textview_reserve_detail_client_gender.setText(Gender.getGender(LoginUserData.getGender()));
            textview_reserve_detail_client_birth.setText(LoginUserData.getBirth());
            textview_reserve_detail_client_phone.setText(LoginUserData.getPhone());
            textview_reserve_detail_client_email.setText(auth.getCurrentUser().getEmail());

            button_reserve_detail_1.setText("후기 작성");
            button_reserve_detail_2.setText("스토리 보기");
        }
        else // 펫시터 예약현황
        {
            textview_reserve_detail_sitter_name.setText(LoginUserData.getUser_name());
            textview_reserve_detail_sitter_gender.setText(Gender.getGender(LoginUserData.getGender()));
            textview_reserve_detail_sitter_birth.setText(LoginUserData.getBirth());
            textview_reserve_detail_sitter_phone.setText(LoginUserData.getPhone());
            textview_reserve_detail_sitter_email.setText(auth.getCurrentUser().getEmail());

            button_reserve_detail_1.setText("스토리 작성");
            button_reserve_detail_2.setVisibility(View.GONE);
        }
    }
    // 전달받은 예약_id를 통해 해당 예약 정보를 조회하는 메소드
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


                                     // 조회하고 textview에 출력.
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
    // 상대방 정보를 조회하는 메소드
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

                                // callFrom 이 true 면 고객 예약현황이다.
                                // 고객 입장에서의 예약현황이고 로그인시 고객의 정보는 LoginUserData에 저장되기 때문에
                                // 펫시터의 정보를 가져와야한다.
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

    // 펫 정보를 조회하는 메소드
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
                                String gender = Gender.getGenderPet(document.getBoolean("gender"));

                                // 조회한 펫 정보를 textview에 출력
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
    // 스토리 작성, 후기 작성이 완료되어 종료되면 해당 액티비티를 종료.
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