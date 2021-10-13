package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.DateString;
import com.example.myapplication.Gender;
import com.example.myapplication.LoginUserData;
import com.example.myapplication.R;
import com.example.myapplication.ViewPagerAdapterFragment;
import com.example.myapplication.Fragment.fragment_reserve_detail_client;
import com.example.myapplication.Fragment.fragment_reserve_detail_pet;
import com.example.myapplication.Fragment.fragment_reserve_detail_sitter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
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

    /*
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
     */

    private String client_name, client_gender, client_birth, client_phone, client_email, client_id;
    private String sitter_name, sitter_gender, sitter_birth, sitter_phone, sitter_email, sitter_id;
    private String pet_name, pet_gender, pet_age, pet_species, pet_species_detail, pet_mbti;

    /*
    private TextView textview_reserve_detail_pet_name;
    private TextView textview_reserve_detail_pet_gender;
    private TextView textview_reserve_detail_pet_age;
    private TextView textview_reserve_detail_pet_species;
    private TextView textview_reserve_detail_pet_species_detail;
    private TextView textview_reserve_detail_pet_mbti;
    */

    private TextView textview_reserve_detail_datetime;
    private TextView textview_reserve_detail_address;
    private TextView textview_reserve_detail_address_detail;
    private TextView textview_reserve_detail_info;
    private TextView textview_reserve_detail_price;

    private Button button_reserve_detail_1;
    private Button button_reserve_detail_2;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapterFragment viewPagerAdapterFragment;

    private fragment_reserve_detail_pet fragment_reserve_detail_pet;
    private fragment_reserve_detail_client fragment_reserve_detail_client;
    private fragment_reserve_detail_sitter fragment_reserve_detail_sitter;

    private int load_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_detail);

        // --------- 액션바, 스위치 버튼 설정 ----------
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);
        // ---------------------------------------------

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

        /*
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
         */


        textview_reserve_detail_datetime = (TextView)findViewById(R.id.textview_reserve_detail_datetime);
        textview_reserve_detail_address = (TextView)findViewById(R.id.textview_reserve_detail_address);
        textview_reserve_detail_address_detail = (TextView)findViewById(R.id.textview_reserve_detail_address_detail);
        textview_reserve_detail_info = (TextView)findViewById(R.id.textview_reserve_detail_info);
        textview_reserve_detail_price = (TextView)findViewById(R.id.textview_reserve_detail_price);

        button_reserve_detail_1 = (Button)findViewById(R.id.button_reserve_detail_1);
        button_reserve_detail_2 = (Button)findViewById(R.id.button_reserve_detail_2);

        tabLayout = (TabLayout)findViewById(R.id.Tablayout_reserve_detail);
        viewPager = (ViewPager)findViewById(R.id.Viewpager_reserve_detail);

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapterFragment = new ViewPagerAdapterFragment(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

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
            String user_name = sitter_name;
            String datetime = textview_reserve_detail_datetime.getText().toString();
            String gender = sitter_gender;
            String birth = sitter_birth;

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
            client_name = LoginUserData.getUser_name();
            client_gender = Gender.getGender(LoginUserData.getGender());
            client_birth = LoginUserData.getBirth();
            client_phone = LoginUserData.getPhone();
            client_email = auth.getCurrentUser().getEmail();
            client_id = auth.getUid();

            button_reserve_detail_1.setText("후기 작성");
            button_reserve_detail_2.setText("스토리 보기");
        }
        else // 펫시터 예약현황
        {
            sitter_name = LoginUserData.getUser_name();
            sitter_gender = Gender.getGender(LoginUserData.getGender());
            sitter_birth = LoginUserData.getBirth();
            sitter_phone = LoginUserData.getPhone();
            sitter_email = auth.getCurrentUser().getEmail();
            sitter_id = auth.getUid();

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

                                     load_count++;
                                     isComplete();
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

                                    sitter_name = name;
                                    sitter_gender = gender;
                                    sitter_birth = birth;
                                    sitter_phone = phone;
                                    sitter_email = email;
                                    sitter_id = user_id;

                                }
                                else
                                {

                                    client_name = name;
                                    client_gender = gender;
                                    client_birth = birth;
                                    client_phone = phone;
                                    client_email = email;
                                    client_id = user_id;

                                    user_token = document.getString("fcm_token");
                                }


                                load_count++;
                                isComplete();
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

                                pet_name = name;
                                pet_age = age;
                                pet_species = species;
                                pet_species_detail = species_detail;
                                pet_mbti = mbti;
                                pet_gender = gender;

                                load_count++;
                                isComplete();
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

    private void isComplete()
    {
        if(load_count == 3)
        {
            fragment_reserve_detail_pet = new fragment_reserve_detail_pet();
            fragment_reserve_detail_client = new fragment_reserve_detail_client();
            fragment_reserve_detail_sitter = new fragment_reserve_detail_sitter();

            viewPagerAdapterFragment.addFragment(fragment_reserve_detail_pet);
            viewPagerAdapterFragment.addFragment(fragment_reserve_detail_client);
            viewPagerAdapterFragment.addFragment(fragment_reserve_detail_sitter);
            viewPager.setAdapter(viewPagerAdapterFragment);

        }

    }

    public String[] getPetFragment()
    {
        String[] data = {pet_name, pet_gender, pet_age, pet_species, pet_species_detail, pet_mbti, pet_id};

        return data;
    }

    public String[] getClientFragment()
    {
        String[] data = {client_name, client_gender, client_birth, client_phone, client_email, client_id};

        return data;
    }

    public String[] getSitterFragment()
    {
        String[] data = {sitter_name, sitter_gender, sitter_birth, sitter_phone, sitter_email, sitter_id};

        return data;
    }

}