package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class activity_sitter_estimate_detail extends AppCompatActivity {

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

    private TextView textView_estimate_detail_datetime;
    private TextView textView_estimate_detail_address;
    //private TextView textView_estimate_detail_address_detail;
    private TextView textView_estimate_detail_info;
    private TextView textView_estimate_detail_price;

    private EditText editText_estimate_detail_price;
    private EditText editText_estimate_detail_appeal;
    private TextView textView_estimate_detail_appeal;

    private Button button_estimate_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_estimate_detail);

        Intent intent = getIntent();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        callFrom = intent.getIntExtra("callFrom",0);

        estimate_id = intent.getStringExtra("estimate_id");
        user_id = intent.getStringExtra("user_id");
        pet_id = intent.getStringExtra("pet_id");
        info = intent.getStringExtra("info");
        price = intent.getStringExtra("price");
        String date = intent.getStringExtra("datetime");
        datetime = DateString.StringToDate(date);

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

        textView_estimate_detail_datetime = (TextView)findViewById(R.id.textview_estimate_detail_datetime);
        textView_estimate_detail_address = (TextView)findViewById(R.id.textview_estimate_detail_address);
        //textView_estimate_detail_address_detail = (TextView)findViewById(R.id.textview_estimate_detail_address_detail);
        textView_estimate_detail_info = (TextView)findViewById(R.id.textview_estimate_detail_info);
        textView_estimate_detail_price = (TextView)findViewById(R.id.textview_estimate_detail_price);

        editText_estimate_detail_price = (EditText)findViewById(R.id.edit_estimate_detail_price);
        editText_estimate_detail_appeal = (EditText)findViewById(R.id.edit_estimate_detail_appeal);
        textView_estimate_detail_appeal = (TextView)findViewById(R.id.textview_estimate_detail_appeal);

        button_estimate_detail = (Button)findViewById(R.id.button_estimate_detail);

        textView_estimate_detail_datetime.setText(date);
        textView_estimate_detail_info.setText(info);
        textView_estimate_detail_price.setText(price);

        if(callFrom == 0)
        {
            editText_estimate_detail_price.setVisibility(View.GONE);
            editText_estimate_detail_appeal.setVisibility(View.GONE);
            textView_estimate_detail_appeal.setVisibility(View.GONE);
        }
        else if(callFrom == 1)
        {
            textView_estimate_detail_price.setVisibility(View.GONE);
            button_estimate_detail.setVisibility(View.GONE);
        }

        getUserData();
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

            createChatroom();

            NotificationMessaging messaging = new NotificationMessaging(to, title, body, this);

            messaging.start();
        }
        else if(callFrom == 1)
        {

        }

    }

    public void onClickCreateEstimate(View view)
    {

        if(callFrom == 0)
        {
            //String title = "모두의 집사";
            //String body = "역으로 제안된 견적서가 도착했습니다.";

           // NotificationMessaging messaging = new NotificationMessaging(to, title, body, this);

            //messaging.start();

            // 역으로 제한된 견적서를 처리.
            // 기존 estimate db에 추가를 할지
            // 다른 테이블을 만들어서 관리를 할지 고민중.

            call_Estimate_detail();
        }
        else if(callFrom == 1)
        {
            uploadReversedEstimate();
        }

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

                        textView_estimate_detail_pet_name.setText(pet_name);
                        textView_estimate_detail_pet_gender.setText(pet_gender);
                        textView_estimate_detail_pet_age.setText(pet_age);
                        textView_estimate_detail_pet_species.setText(pet_species);
                        textView_estimate_detail_pet_species_detail.setText(pet_species_detail);

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

                        textView_estimate_detail_user_name.setText(user_name);
                        textView_estimate_detail_user_gender.setText(user_gender);
                        textView_estimate_detail_user_age.setText(user_age);
                        textView_estimate_detail_user_phone.setText(user_phone);
                        textView_estimate_detail_user_email.setText(user_email);

                        textView_estimate_detail_address.setText(address);
                        //textView_estimate_detail_address_detail.setText(address_detail);

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
        String user_name = textView_estimate_detail_user_name.getText().toString();
        String address = textView_estimate_detail_address.getText().toString();
        //String address_detail = textView_estimate_detail_address_detail.getText().toString();

        // Key와 Value를 가지는 맵
        Map<String, Object> reserve = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        reserve.put("client_id", user_id);
        reserve.put("sitter_id", auth.getUid());
        reserve.put("client_name", user_name);
        reserve.put("sitter_name", LoginUserData.getUser_name());
        reserve.put("chatroom", chatroom);

        reserve.put("timestamp", new Timestamp(new Date()));
        reserve.put("datetime", datetime);
        reserve.put("pet_id", pet_id);
        reserve.put("price", price);
        reserve.put("address", address);
        //reserve.put("address_detail", address_detail);
        reserve.put("info", info);


        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        DocumentReference ref = db.collection("reserve").document();
            ref.set(reserve)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //updateUserReserve(ref.getId());
                        deleteEstimate();
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
    /*
    private void updateUserReserve(String reserve_id)
    {
        // 고객
        db.collection("users").document(user_id).collection("reserve_list").document(reserve_id)
                .set(new HashMap<String, Object>())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d("결과 : ", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("결과 : ", "Error writing document", e);
                    }
                });
        // 시터
        db.collection("users").document(auth.getUid()).collection("reserve_list").document(reserve_id)
                .set(new HashMap<String, Object>())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteEstimate();
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
     */

    private void deleteEstimate()
    {
        db.collection("estimate").document(estimate_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

        startActivity(intent);
    }

    private void uploadReversedEstimate()
    {
        String price = editText_estimate_detail_price.getText().toString().trim();
        String appeal = editText_estimate_detail_appeal.getText().toString().trim();

        // Key와 Value를 가지는 맵
        Map<String, Object> estimate = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        estimate.put("price", price);
        estimate.put("appeal", appeal);
        estimate.put("timestamp", new Timestamp(new Date()));
        estimate.put("user_id", user_id);

        DocumentReference ref = db.collection("estimate").document(estimate_id)
                .collection("offer").document();
        ref.set(estimate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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