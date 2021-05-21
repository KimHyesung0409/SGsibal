package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class activity_reserve_entrust_detail extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    TextView entrust_detail_intro, entrust_detail_caution, entrust_detail_price;

    private FirebaseFirestore db; //파이어스토어 db 객체
    private FirebaseAuth auth; //파이어베이스 인증 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_entrust_detail);

        Intent intent = getIntent();

        String price_str = intent.getStringExtra("price");
        //String entrust_id = intent.getStringExtra("entrsut_id");

        db = FirebaseFirestore.getInstance(); //파이어스토어 db 객체
        auth = FirebaseAuth.getInstance();

        viewPager = (ViewPager)findViewById(R.id.viewpager_reserve_entrust_detail);
        viewPagerAdapter = new ViewPagerAdapter(this, intent.getStringExtra("images"));
        viewPager.setAdapter(viewPagerAdapter);

        entrust_detail_price = (TextView)findViewById(R.id.entrust_detail_price);
        entrust_detail_intro = (TextView)findViewById(R.id.entrust_detail_intro);
        entrust_detail_caution = (TextView)findViewById(R.id.entrust_detail_caution);

        entrust_detail_price.setText(price_str+"원");

        //소개(intro), 주의사항(caution)을 불러오지를 못하겠음
        String intro_str = intent.getStringExtra("intro");
        String caution_str = intent.getStringExtra("caution");

        entrust_detail_intro.setText(intro_str);
        entrust_detail_caution.setText(caution_str);
    }
}