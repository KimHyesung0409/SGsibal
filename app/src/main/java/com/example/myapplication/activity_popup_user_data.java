package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class activity_popup_user_data extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private TextView textView_popup_user_data_name;
    private TextView textView_popup_user_data_gender;
    private TextView textView_popup_user_data_address;
    private TextView textView_popup_user_data_address_detail;
    private TextView textView_popup_user_data_birth;
    private TextView textView_popup_user_data_phone;
    private TextView textView_popup_user_data_email;
    private TextView textView_popup_user_data_care_list;
    private TextView textView_popup_user_data_can_time;

    private Button button_popup_user_data_favorites;

    private String user_id;
    private String user_token;

    private int callFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_user_data);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        callFrom = intent.getIntExtra("callFrom", 0);

        textView_popup_user_data_name = (TextView)findViewById(R.id.textview_popup_user_data_name);
        textView_popup_user_data_gender = (TextView)findViewById(R.id.textview_popup_user_data_gender);
        textView_popup_user_data_address = (TextView)findViewById(R.id.textview_popup_user_data_address);
        textView_popup_user_data_address_detail = (TextView)findViewById(R.id.textview_popup_user_data_address_detail);
        textView_popup_user_data_birth = (TextView)findViewById(R.id.textview_popup_user_data_birth);
        textView_popup_user_data_phone = (TextView)findViewById(R.id.textview_popup_user_data_phone);
        textView_popup_user_data_email = (TextView)findViewById(R.id.textview_popup_user_data_email);
        textView_popup_user_data_care_list = (TextView)findViewById(R.id.textview_popup_user_data_care_list);
        textView_popup_user_data_can_time = (TextView)findViewById(R.id.textview_popup_user_data_can_time);

        button_popup_user_data_favorites = (Button)findViewById(R.id.button_popup_user_data_favorites);

        initActivity();

        getUserData();
    }

    public void onClickReserve(View view)
    {
        createChatroom();
    }

    public void onClickFavorites(View view)
    {

        if(callFrom == 1) // 즐겨찾기
        {

            checkFavorites();

        }
        else // 즐겨찾기 삭제
        {

            deleteFavorites();

        }

    }

    private void initActivity()
    {

        if(callFrom == 0) // 자동매칭
        {

            // 자동매칭에서는 즐겨찾기 버튼을 삭제해야한다.
            button_popup_user_data_favorites.setVisibility(View.GONE);

        }
        else if(callFrom == 1) // 사용기록, 검색
        {



        }
        else // 즐겨찾기
        {

            button_popup_user_data_favorites.setText("삭제");

        }

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
                                String gender = Gender.getGender(document.getBoolean("gender"));
                                String address = document.getString("address");
                                String address_detail = document.getString("address_detail");
                                String birth = document.getString("birth");
                                String phone = document.getString("phone");
                                String email = document.getString("email");
                                user_token = document.getString("fcm_token");

                                ArrayList<String> list = (ArrayList<String>)document.get("care_list");

                                // 탐색 시 미리 care_list가 없으면 목록에 출력을 하지 않지만
                                // 목록에 출력이 되고 해당 펫시터가 care_list를 삭제했다면
                                // 에러가 출력 될 수 있으므로 예방 차원에서 조건문 추가.
                                if(list != null)
                                {

                                    Iterator<String> iterator = list.iterator();

                                    StringBuilder stringBuilder = new StringBuilder();

                                    while (iterator.hasNext()) {
                                        String care_pet = iterator.next();

                                        stringBuilder.append(care_pet);
                                        stringBuilder.append(",");
                                    }
                                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                                    String care_list = stringBuilder.toString();
                                    textView_popup_user_data_care_list.setText(care_list);
                                }
                                else
                                {
                                    textView_popup_user_data_care_list.setText("없음");
                                }

                                textView_popup_user_data_name.setText(name);
                                textView_popup_user_data_gender.setText(gender);
                                textView_popup_user_data_address.setText(address);
                                textView_popup_user_data_address_detail.setText(address_detail);
                                textView_popup_user_data_birth.setText(birth);
                                textView_popup_user_data_phone.setText(phone);
                                textView_popup_user_data_email.setText(email);

                            }
                            else
                            {

                            }
                        }
                    }
                });
    }

    private void createChatroom()
    {
        Map<String, Object> chatroom = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입

        chatroom.put("client_id", auth.getUid());
        chatroom.put("sitter_id", user_id);


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

    private void createReserve(String chatroom)
    {
        ListViewItem_petlist pet_data = fragment_reserve_visit_1.getSelected_pet();

        // Key와 Value를 가지는 맵
        Map<String, Object> reserve = new HashMap<>();

        String user_name = textView_popup_user_data_name.getText().toString();

        reserve.put("client_id", auth.getUid());
        reserve.put("sitter_id", user_id);
        reserve.put("client_name", LoginUserData.getUser_name());
        reserve.put("sitter_name", user_name);


        // 위에서 만든 맵(user) 변수에 데이터 삽입
        reserve.put("chatroom", chatroom);
        reserve.put("timestamp", new Timestamp(new Date()));
        reserve.put("datetime", fragment_reserve_visit_2.getSelectedTime());
        reserve.put("pet_id", pet_data.getPet_id());
        reserve.put("pet_name", pet_data.getName());
        reserve.put("price", "15000"); // <- 가격은 펫시터 프로필에서 설정하는 가격으로.
        reserve.put("address", LoginUserData.getAddress());
        //reserve.put("address_detail", address_detail);
        reserve.put("info", pet_data.getInfo());

        Context context = this;

        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        DocumentReference ref = db.collection("reserve").document();
        ref.set(reserve)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        String title = "모두의 집사";
                        String body = "예약이 들어왔습니다.";

                        NotificationMessaging messaging = new NotificationMessaging(user_token, title, body, context);

                        messaging.start();

                        setResult(RESULT_OK, new Intent());
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

    // 무작정 추가하면 안된다. 이미 즐겨찾기에 등록이 되어있는 경우가 있을 수 있다.
    // 해당 유저가 즐겨찾기에 없으면 추가해준다.
    private void checkFavorites()
    {
        db.collection("users").document(auth.getUid()).collection("favorites")
                .whereEqualTo("user_id", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            // 결과가 비어있지 않으면 즉 해당 유저가 즐겨찾기에 존재하면.
                            if(!task.getResult().isEmpty())
                            {
                                Toast.makeText(activity_popup_user_data.this, "이미 즐겨찾기에 등록되어 있습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else // 해당 유저가 즐겨찾기에 존재하지 않으면
                            {
                                addFavorites();
                            }

                        }
                        else
                        {

                        }
                    }
                });
    }

    private void addFavorites()
    {

        String user_name = textView_popup_user_data_name.getText().toString();
        String birth = textView_popup_user_data_birth.getText().toString();
        boolean gender = Gender.getGender(textView_popup_user_data_gender.getText().toString());

        Map<String, Object> Favorites = new HashMap<>();

        Favorites.put("user_id", user_id);
        Favorites.put("name", user_name);
        Favorites.put("birth", birth);
        Favorites.put("gender", gender);

        db.collection("users").document(auth.getUid()).collection("favorites")
                .document()
                .set(Favorites)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        Toast.makeText(activity_popup_user_data.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteFavorites()
    {
        db.collection("users").document(auth.getUid()).collection("favorites")
                .whereEqualTo("user_id", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                db.collection("users").document(auth.getUid()).collection("favorites")
                                        .document(document.getId()).delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                setResult(RESULT_OK, new Intent());
                                                finish();
                                            }
                                        });
                            }

                        }
                        else
                        {

                        }
                    }
                });
    }

}