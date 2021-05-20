package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class activity_reserve_entrust_detail extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    TextView entrust_detail_intro, entrust_detail_caution, entrust_detail_price;

    String entrust_id, entrust_detail_intro_str, entrust_detail_caution_str, entrust_detail_price_str;

    private FirebaseFirestore db; //파이어스토어 db 객체
    private FirebaseAuth auth; //파이어베이스 인증 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_entrust_detail);

        db = FirebaseFirestore.getInstance(); //파이어스토어 db 객체
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        viewPager = (ViewPager)findViewById(R.id.viewpager_reserve_entrust_detail);
        viewPagerAdapter = new ViewPagerAdapter(this, intent.getStringExtra("images"));
        viewPager.setAdapter(viewPagerAdapter);

        entrust_detail_intro = (TextView)findViewById(R.id.entrust_detail_intro);
        entrust_detail_caution = (TextView)findViewById(R.id.entrust_detail_caution);
        entrust_detail_price = (TextView)findViewById(R.id.entrust_detail_price);
        //가격이 이상하게 불려옴
        db.collection("entrust_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            entrust_detail_price_str = document.getString("price");
                            entrust_detail_price.setText(entrust_detail_price_str+"원");

                            //entrust_id = document.getString("");
                        }
                    }else{
                    }
                 }
                });


        //소개(intro), 주의사항(caution)을 불러오지를 못하겠음
        db.collection("entrust_list").document()
                .collection("detail")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                entrust_detail_intro_str = document.getString("intro");
                                entrust_detail_intro.setText(entrust_detail_intro_str);

                                entrust_detail_caution_str = document.getString("caution");
                                entrust_detail_caution.setText(entrust_detail_caution_str);
                            }
                        }else{
                        }
                    }
                });
    }
}