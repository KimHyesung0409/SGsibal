package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// 서비스 안내를 표시하기 위한 액티비티이므로 별도의 코드가 없다.
public class activity_guide extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);



    }

    public void onClickOfferEstimate(View view)
    {

    }
}
