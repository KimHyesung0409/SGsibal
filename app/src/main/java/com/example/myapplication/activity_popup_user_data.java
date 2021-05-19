package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class activity_popup_user_data extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private TextView textView_popup_user_data_name;
    private TextView textView_popup_user_data_address;
    private TextView textView_popup_user_data_address_detail;
    private TextView textView_popup_user_data_age;
    private TextView textView_popup_user_data_phone;
    private TextView textView_popup_user_data_email;
    private TextView textView_popup_user_data_care_list;
    private TextView textView_popup_user_data_can_time;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_user_data);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        textView_popup_user_data_name = (TextView)findViewById(R.id.textview_popup_user_data_name);
        textView_popup_user_data_address = (TextView)findViewById(R.id.textview_popup_user_data_address);
        textView_popup_user_data_address_detail = (TextView)findViewById(R.id.textview_popup_user_data_address_detail);
        textView_popup_user_data_age = (TextView)findViewById(R.id.textview_popup_user_data_age);
        textView_popup_user_data_phone = (TextView)findViewById(R.id.textview_popup_user_data_phone);
        textView_popup_user_data_email = (TextView)findViewById(R.id.textview_popup_user_data_email);
        textView_popup_user_data_care_list = (TextView)findViewById(R.id.textview_popup_user_data_care_list);
        textView_popup_user_data_can_time = (TextView)findViewById(R.id.textview_popup_user_data_can_time);

        getUserData();
    }

    public void onClickReserve(View view)
    {
        createChatroom();
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
                                String address = document.getString("address");
                                String address_detail = document.getString("address_detail");
                                ArrayList<String> list = (ArrayList<String>)document.get("care_list");
                                Iterator<String> iterator = list.iterator();

                                StringBuilder stringBuilder = new StringBuilder();

                                while(iterator.hasNext())
                                {
                                    String care_pet = iterator.next();

                                    stringBuilder.append(care_pet);
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.deleteCharAt(stringBuilder.length() - 1);

                                String care_list = stringBuilder.toString();

                                textView_popup_user_data_name.setText(name);
                                textView_popup_user_data_address.setText(address);
                                textView_popup_user_data_address_detail.setText(address_detail);
                                textView_popup_user_data_care_list.setText(care_list);
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



        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        DocumentReference ref = db.collection("reserve").document();
        ref.set(reserve)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //updateUserReserve(ref.getId());
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

}