package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_reserve_entrust_detail extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    TextView entrust_detail_intro, entrust_detail_caution, entrust_detail_price;

    Button button_entrust_detail_reserve;

    private FirebaseFirestore db; //파이어스토어 db 객체
    private FirebaseAuth auth; //파이어베이스 인증 객체

    private String entrust_id;
    private String user_token;

    Fragment fragment_home = new fragment_home();

    private String user_id;
    private String user_name;
    private String entrust_price;

    private String address;
    private String address_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_entrust_detail);

        // intent 를 통해 위탁에 대한 기본 정보를 받아온다.
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        user_name = intent.getStringExtra("user_name");
        entrust_price = intent.getStringExtra("price");

        String price_str = intent.getStringExtra("price");
        entrust_id = intent.getStringExtra("entrust_id");

        address = intent.getStringExtra("address");
        address_detail = intent.getStringExtra("address_detail");

        db = FirebaseFirestore.getInstance(); //파이어스토어 db 객체
        auth = FirebaseAuth.getInstance();

        viewPager = (ViewPager)findViewById(R.id.viewpager_reserve_entrust_detail);
        viewPagerAdapter = new ViewPagerAdapter(this, intent.getStringExtra("images"));
        viewPager.setAdapter(viewPagerAdapter);

        entrust_detail_price = (TextView)findViewById(R.id.entrust_detail_price);
        entrust_detail_intro = (TextView)findViewById(R.id.entrust_detail_intro);
        entrust_detail_caution = (TextView)findViewById(R.id.entrust_detail_caution);

        entrust_detail_price.setText(price_str+"원");
        button_entrust_detail_reserve = (Button)findViewById(R.id.button_entrust_detail_reserve);

        // 예약 버튼 클릭
        button_entrust_detail_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 채팅방만 생성하는 것이 아니고
                // 채팅방_id를 예약 정보에 추가해야 하기 때문에 먼저 채팅방을 db에 등록하고
                // 예약 정보를 db에 등록한다.
                createChatroom();

            }
        });



        getEntrustDetail();
    }


    private void createChatroom() {
        Map<String, Object> chatroom = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입

        chatroom.put("client_id", auth.getUid()); // 고객의 user_id
        chatroom.put("sitter_id", user_id); // 펫시터의 user_id


        // db에 업로드
        DocumentReference ref = db.collection("chatroom").document();
        ref.set(chatroom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // 채팅방을 만들고 해당 채팅방_id를 예약 정보에 등록해야 하기 때문에
                        // 해당 채팅방db의 id를 예약 정보를 db에 등록하는 메소드에 파라미터로 첨부하여 실행한다.
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

    private void createReserve(String chatroom) {
        ListViewItem_petlist pet_data = fragment_reserve_visit_1.getSelected_pet();

        // Key와 Value를 가지는 맵
        Map<String, Object> reserve = new HashMap<>();

        //String user_name = textView_popup_user_data_name.getText().toString();

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
        reserve.put("price", entrust_price+"원");
        reserve.put("address", address);
        reserve.put("address_detail", address_detail);
        reserve.put("info", pet_data.getInfo());

        Context context = this;

        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        DocumentReference ref = db.collection("reserve").document();
        ref.set(reserve)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // 예약 정보를 db에 성공적으로 등록하고 나서
                        // 해당 유저에게 fcm 메시지를 보내고 종료한다.

                        String title = "모두의 집사";
                        String body = "예약이 들어왔습니다.";

                        NotificationMessaging messaging = new NotificationMessaging(user_token, title, body, user_id, NotificationMessaging.FCM_RESERVE, context);

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

    // 해당 위탁의 세부 정보를 조회하는 메소드
    private void getEntrustDetail()
    {
        db.collection("entrust_list").document(entrust_id).collection("detail")
                .document("content")
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
                                String intro = document.getString("intro");
                                String caution = document.getString("caution");
                                // 위탁 세부정보를 조회하고 textview에 출력한다.
                                entrust_detail_intro.setText(intro);
                                entrust_detail_caution.setText(caution);
                            }
                        }
                        else
                        {

                        }

                    }
                });
    }
}