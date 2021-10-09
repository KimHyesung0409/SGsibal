package com.example.myapplication.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

// 펫시터 인증 양식을 표시하기 위한 액티비티로 별도의 코드는 없다.
public class activity_sitter_assignment_form extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_asignment_form);

    }
}
