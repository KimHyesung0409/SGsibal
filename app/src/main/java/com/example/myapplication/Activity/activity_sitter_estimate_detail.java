package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.myapplication.NotificationMessaging;
import com.example.myapplication.R;
import com.example.myapplication.ViewPagerAdapterFragment;
import com.example.myapplication.Fragment.fragment_reserve_detail_client;
import com.example.myapplication.Fragment.fragment_reserve_detail_pet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class activity_sitter_estimate_detail extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private String estimate_id;
    private String user_id;
    private String pet_id;
    private String info;
    private String to;
    private String price;
    private int callFrom;
    private Date datetime;

    private String address;
    private String address_detail;

    private String offer_id;
    private String appeal;
    private String offer_user_id;

    /*
    private TextView textView_estimate_detail_pet_name;
    private TextView textView_estimate_detail_pet_gender;
    private TextView textView_estimate_detail_pet_age;
    private TextView textView_estimate_detail_pet_species;
    private TextView textView_estimate_detail_pet_species_detail;
     */

    /*
    private TextView textView_estimate_detail_user_name;
    private TextView textView_estimate_detail_user_gender;
    private TextView textView_estimate_detail_user_birth;
    private TextView textView_estimate_detail_user_phone;
    private TextView textView_estimate_detail_user_email;
     */

    private TextView textView_estimate_detail_datetime;
    private TextView textView_estimate_detail_address;
    //private TextView textView_estimate_detail_address_detail;
    private TextView textView_estimate_detail_info;
    private TextView textView_estimate_detail_price;

    private EditText editText_estimate_detail_price;
    private EditText editText_estimate_detail_appeal;
    private TextView textView_estimate_detail_appeal;

    private Button button_estimate_detail_1;
    private Button button_estimate_detail_2;

    private int load_count = 0;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapterFragment viewPagerAdapterFragment;

    private fragment_reserve_detail_pet fragment_reserve_detail_pet;
    private fragment_reserve_detail_client fragment_reserve_detail_client;

    private String client_name, client_gender, client_birth, client_phone, client_email;
    private String pet_name, pet_gender, pet_age, pet_species, pet_species_detail, pet_mbti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_estimate_detail);

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

        // intent로 견적서 기본 정보와 어디서 call 했는지에 대한 정보를 전달받는다.
        // 액티비티 재사용을 위해 견적서 세부정보와 , 역제안서에 사용한다.

        callFrom = intent.getIntExtra("callFrom",0);

        estimate_id = intent.getStringExtra("estimate_id");
        user_id = intent.getStringExtra("user_id");
        pet_id = intent.getStringExtra("pet_id");
        info = intent.getStringExtra("info");
        price = intent.getStringExtra("price");
        String date = intent.getStringExtra("datetime");
        datetime = DateString.StringToDate(date);

        /*
        textView_estimate_detail_pet_name = (TextView)findViewById(R.id.textview_estimate_detail_pet_name);
        textView_estimate_detail_pet_gender = (TextView)findViewById(R.id.textview_estimate_detail_pet_gender);
        textView_estimate_detail_pet_age = (TextView)findViewById(R.id.textview_estimate_detail_pet_age);
        textView_estimate_detail_pet_species = (TextView)findViewById(R.id.textview_estimate_detail_pet_species);
        textView_estimate_detail_pet_species_detail = (TextView)findViewById(R.id.textview_estimate_detail_pet_species_detail);

        textView_estimate_detail_user_name = (TextView)findViewById(R.id.textview_estimate_detail_user_name);
        textView_estimate_detail_user_gender = (TextView)findViewById(R.id.textview_estimate_detail_user_gender);
        textView_estimate_detail_user_birth = (TextView)findViewById(R.id.textview_estimate_detail_user_birth);
        textView_estimate_detail_user_phone = (TextView)findViewById(R.id.textview_estimate_detail_user_phone);
        textView_estimate_detail_user_email = (TextView)findViewById(R.id.textview_estimate_detail_user_email);
         */

        textView_estimate_detail_datetime = (TextView)findViewById(R.id.textview_estimate_detail_datetime);
        textView_estimate_detail_address = (TextView)findViewById(R.id.textview_estimate_detail_address);
        //textView_estimate_detail_address_detail = (TextView)findViewById(R.id.textview_estimate_detail_address_detail);
        textView_estimate_detail_info = (TextView)findViewById(R.id.textview_estimate_detail_info);
        textView_estimate_detail_price = (TextView)findViewById(R.id.textview_estimate_detail_price);

        editText_estimate_detail_price = (EditText)findViewById(R.id.edit_estimate_detail_price);
        editText_estimate_detail_appeal = (EditText)findViewById(R.id.edit_estimate_detail_appeal);
        textView_estimate_detail_appeal = (TextView)findViewById(R.id.textview_estimate_detail_appeal);

        button_estimate_detail_1 = (Button)findViewById(R.id.button_estimate_detail_1);
        button_estimate_detail_2 = (Button)findViewById(R.id.button_estimate_detail_2);

        textView_estimate_detail_datetime.setText(date);
        textView_estimate_detail_info.setText(info);
        textView_estimate_detail_price.setText(price);

        // callFrom이 0이면 견적서 세부정보를 의미하므로 역제안 가격을 표시하는 요소와
        // 펫시터 어필을 표시하는 요소를 지운다.
        if(callFrom == 0)
        {
            editText_estimate_detail_price.setVisibility(View.GONE);
            editText_estimate_detail_appeal.setVisibility(View.GONE);
            textView_estimate_detail_appeal.setVisibility(View.GONE);
            getUserData(user_id);
        }
        // callFrom이 1이면 펫시터가 역제안서를 작성하는 액티비티로서 사용되므로
        // 견적서 가격 요소와 수락하기 버튼을 지운다.
        else if(callFrom == 1)
        {
            textView_estimate_detail_price.setVisibility(View.GONE);
            button_estimate_detail_1.setVisibility(View.GONE);
            getUserData(user_id);
        }
        // callFrom이 2이면 고객이 펫시터의 역제안서를 확인하는 액티비티로서 사용되므로
        // intent로 제안한 펫시터의 user_id, 제안 가격, 제안 어필 정보를 전달받아 출력한다.
        else
        {
            textView_estimate_detail_price.setVisibility(View.VISIBLE);
            offer_id = intent.getStringExtra("offer_id");
            offer_user_id = intent.getStringExtra("offer_user_id");
            appeal = intent.getStringExtra("appeal");

            textView_estimate_detail_price.setVisibility(View.GONE);
            button_estimate_detail_1.setVisibility(View.GONE);
            button_estimate_detail_2.setText("수락하기");
            editText_estimate_detail_price.setText(price);
            editText_estimate_detail_appeal.setText(appeal);

            editText_estimate_detail_price.setClickable(false);
            editText_estimate_detail_price.setFocusable(false);

            editText_estimate_detail_appeal.setClickable(false);
            editText_estimate_detail_appeal.setFocusable(false);

            getUserData(offer_user_id);
        }

        tabLayout = (TabLayout)findViewById(R.id.Tablayout_estimate_detail);
        viewPager = (ViewPager)findViewById(R.id.Viewpager_estimate_detail);

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapterFragment = new ViewPagerAdapterFragment(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        getPetData();
    }

    // 채팅방 삽입 - 채팅방 삽입이 완료되면 자동으로 예약 정보를 삽입한다. 먼저 채팅방을 삽입하는 이유는 예약 정보에서 해당 채팅방에 대한 정보를 가지고 있어야 하기 때문에
    // 먼저 채팅방을 만들고 해당 채팅방의 id를 예약 정보에 등록한다.

    /* db에 예약 정보를 등록해야한다.
     * reserve 라는 테이블에 unique id의 문서 즉 예약 정보를 만든다.
     * 예약 정보는 예약이 db에 등록된 시간(TimeStamp), 예약 시간, 고객id, 시터id, 고객name, 시터name, 반려동물id, 가격, 주의사항, 채팅id
     * 채팅 정보까지 만들어야 합니다.
     * 수락된 해당 견적서를 db에서 삭제한다.
     */

    public void onClickAcceptEstimate(View view)
    {
        if(callFrom == 0)
        {
            String title = "모두의 집사";
            String body = "등록된 견적서가 수락되었습니다.";

            // 채팅방을 생성하여 db에 등록하고 예약 진행
            createChatroom();
            // fcm 메시지를 전송
            NotificationMessaging messaging = new NotificationMessaging(to, title, body, user_id, NotificationMessaging.FCM_RESERVE, this);

            messaging.start();
        }

    }

    public void onClickCreateEstimate(View view)
    {

        if(callFrom == 0)
        {
            // 역제안서를 작성하기 위해 callFrom을 변경하여 재호출하는 메소드
            call_Estimate_detail();
        }
        else if(callFrom == 1)
        {
            // 역제안서를 db에 등록하기 위한 메소드
            uploadEstimateOffer();
        }
        else // 역제안서를 보고 고객이 수락한 경우.
        {

            // fcm 메시지를 전송하고
            // 채팅방을 생성 하고 db에 등록 후 예약을 진행.
            String title = "모두의 집사";
            String body = "제시한 견적서가 수락되었습니다.";

            createChatroom();

            NotificationMessaging messaging = new NotificationMessaging(to, title, body, user_id, NotificationMessaging.FCM_RESERVE, this);

            messaging.start();
        }

    }
    // 반려동물 정보를 조회하는 메소드
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
                        String name = document.getString("name");
                        String gender = Gender.getGenderPet(document.getBoolean("gender"));
                        String age = document.getString("age");
                        String species = document.getString("species");
                        String species_detail = document.getString("detail_species");
                        String mbti = document.getString("mbti");

                        /*
                        // 조회한 반려동물 정보를 textview에 출력한다.
                        textView_estimate_detail_pet_name.setText(pet_name);
                        textView_estimate_detail_pet_gender.setText(pet_gender);
                        textView_estimate_detail_pet_age.setText(pet_age);
                        textView_estimate_detail_pet_species.setText(pet_species);
                        textView_estimate_detail_pet_species_detail.setText(pet_species_detail);
                         */

                        pet_name = name;
                        pet_age = age;
                        pet_species = species;
                        pet_species_detail = species_detail;
                        pet_mbti = mbti;
                        pet_gender = gender;

                        load_count++;
                        isComplete();
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

    // 견적서를 작성한 유저 정보를 조회하는 메소드
    private void getUserData(String user_id)
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

                        String name = document.getString("name");
                        String birth = document.getString("birth");
                        String phone = document.getString("phone");
                        String email = document.getString("email");
                        String gender = Gender.getGender(document.getBoolean("gender"));

                        String address = document.getString("address");
                        address_detail = document.getString("address_detail");


                        // 조회한 유저 정보를 textview에 출력한다.
                        /*
                        textView_estimate_detail_user_name.setText(user_name);
                        textView_estimate_detail_user_gender.setText(user_gender);
                        textView_estimate_detail_user_birth.setText(user_birth);
                        textView_estimate_detail_user_phone.setText(user_phone);
                        textView_estimate_detail_user_email.setText(user_email);
                         */
                        textView_estimate_detail_address.setText(address);
                        //textView_estimate_detail_address_detail.setText(address_detail);

                        client_name = name;
                        client_gender = gender;
                        client_birth = birth;
                        client_phone = phone;
                        client_email = email;


                        load_count++;
                        isComplete();
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

    // 채팅방을 생성하고 db에 등록하는 메소드
    private void createChatroom()
    {
        Map<String, Object> chatroom = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입

        // 고객이 역제안서를 수락한 경우.
        if(callFrom == 2)
        {
            chatroom.put("client_id", user_id);
            chatroom.put("sitter_id", offer_user_id); // 역제안서를 보낸 user_id
        }
        else
        {
            chatroom.put("client_id", auth.getUid());
            chatroom.put("sitter_id", user_id);
        }

        // db에 업로드
        DocumentReference ref = db.collection("chatroom").document();
        ref.set(chatroom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        createReserve(ref.getId());

                        Log.d("결과 : ", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("결과 : ", "Error writing document", e);
                    }
                });

    }

    // 예약 정보를 db에 등록하는 메소드
    // 역시 callFrom에 따라 입력 정보가 조금씩 변한다.
    private void createReserve(String chatroom)
    {
        // Key와 Value를 가지는 맵
        Map<String, Object> reserve = new HashMap<>();

        String address = textView_estimate_detail_address.getText().toString();
        //String address_detail = textView_estimate_detail_address_detail.getText().toString();

        if(callFrom == 2)
        {
            reserve.put("client_id", user_id);
            reserve.put("sitter_id", offer_user_id);
            reserve.put("client_name", LoginUserData.getUser_name());
            reserve.put("sitter_name", client_name);
        }
        else
        {
            reserve.put("client_id", user_id);
            reserve.put("sitter_id", auth.getUid());
            reserve.put("client_name", client_name);
            reserve.put("sitter_name", LoginUserData.getUser_name());
        }

        // 위에서 만든 맵(user) 변수에 데이터 삽입
        reserve.put("chatroom", chatroom);

        reserve.put("timestamp", new Timestamp(new Date()));
        reserve.put("datetime", datetime);
        reserve.put("pet_id", pet_id);
        reserve.put("pet_name", pet_name);
        reserve.put("price", price);
        reserve.put("address", address);
        reserve.put("address_detail", address_detail);
        reserve.put("info", info);



        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        DocumentReference ref = db.collection("reserve").document();
            ref.set(reserve)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 예약이 수락되면 해당 견적서에 대한 모든 역제안서를 db에서 제거하고
                        // 견적서 정보를 db에서 제거해야한다.
                        deleteOffer();

                        Log.d("결과 : ", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("결과 : ", "Error writing document", e);
                    }
                });
    }

    // 견적서를 db에서 제거하는 메소드
    private void deleteEstimate()
    {
        db.collection("estimate").document(estimate_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 견적서 제거가 완료되면 종료한다.
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        Log.d("", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error deleting document", e);
                    }
                });
    }
    // 컬렉션 내부의 모든 문서를 지워야 해당 컬렉션이 지워진다.
    // 따라서 컬렉션 내부의 모든 문서를 탐색하고 하나하나 지우는 작업을 해야한다.
    // 안드로이드에서는 권장하지 않는다고하는데 방법이 없다.
    private void deleteOffer()
    {
      Task task = db.collection("estimate").document(estimate_id).collection("offer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                // offer 리스트 삭제
                                db.collection("estimate").document(estimate_id).collection("offer")
                                        .document(document.getId()).delete();
                            }

                        }
                        else
                        {

                        }
                    }
                });
      // offer 리스트가 삭제되면 견적서 삭제 함수 호출
      task.continueWith(Task::isComplete).addOnCompleteListener(new OnCompleteListener()
      {
          @Override
          public void onComplete(@NonNull Task task)
          {
              deleteEstimate();
          }
      });

    }

    // 역제안서 작성 액티비티로서 재호출
    private void call_Estimate_detail()
    {
        Intent intent = new Intent(this, activity_sitter_estimate_detail.class);

        intent.putExtra("callFrom", 1);

        intent.putExtra("estimate_id", estimate_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("pet_id", pet_id);
        intent.putExtra("info", info);
        intent.putExtra("price", price);
        intent.putExtra("datetime", DateString.DateToString(datetime));

        startActivityForResult(intent, REQUEST_CODE);
    }

    // 역제안서를 db에 등록하는 메소드
    private void uploadEstimateOffer()
    {
        Context context = this;

        String price = editText_estimate_detail_price.getText().toString().trim();
        String appeal = editText_estimate_detail_appeal.getText().toString().trim();

        // Key와 Value를 가지는 맵
        Map<String, Object> estimate = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        estimate.put("price", price);
        estimate.put("appeal", appeal);
        estimate.put("timestamp", new Timestamp(new Date()));
        estimate.put("user_id", auth.getUid());

        DocumentReference ref = db.collection("estimate").document(estimate_id)
                .collection("offer").document();
        ref.set(estimate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // 역제안서를 작성하고
                        // 고객에서 해당 견적서의 역제안서가 도착했다는
                        // fcm메시지를 전송한다.

                        String title = "모두의 집사";
                        String body = "제안이 들어왔습니다.";

                        NotificationMessaging messaging = new NotificationMessaging(to, title, body, user_id, NotificationMessaging.FCM_RESERVE, context);

                        messaging.start();

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                        Log.d("결과 : ", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("결과 : ", "Error writing document", e);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 역제안서 작성이 완료되면 종료.
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    private void isComplete()
    {
        if(load_count == 2)
        {
            fragment_reserve_detail_pet = new fragment_reserve_detail_pet();
            fragment_reserve_detail_client = new fragment_reserve_detail_client();

            viewPagerAdapterFragment.addFragment(fragment_reserve_detail_pet);
            viewPagerAdapterFragment.addFragment(fragment_reserve_detail_client);
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
        String[] data = {client_name, client_gender, client_birth, client_phone, client_email, user_id};

        return data;
    }

}