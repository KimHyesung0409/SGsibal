package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.activity_instruction_sitter;
import com.example.myapplication.Activity.activity_main_sitter;
import com.example.myapplication.Activity.activity_sitter_assignment_form;
import com.example.myapplication.Activity.activity_sitter_estimate;
import com.example.myapplication.Activity.activity_upload_entrust;
import com.example.myapplication.LoginUserData;
import com.example.myapplication.R;

public class fragment_home_sitter extends Fragment implements View.OnClickListener {

    ViewGroup viewGroup;
    private Button button_search_estimate, button_regester_sitter, button_upload_entrust, button_instruction_sitter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_home_sitter, container, false);

        button_search_estimate = (Button)viewGroup.findViewById(R.id.button_search_estimate);
        button_search_estimate.setOnClickListener(this);
        button_regester_sitter = (Button)viewGroup.findViewById(R.id.button_regester_sitter);
        button_regester_sitter.setOnClickListener(this);
        button_upload_entrust = (Button)viewGroup.findViewById(R.id.button_upload_entrust);
        button_upload_entrust.setOnClickListener(this);
        button_instruction_sitter = (Button)viewGroup.findViewById(R.id.button_instruction_sitter);
        button_instruction_sitter.setOnClickListener(this);

        return viewGroup;
    }

    // 버튼 클릭 메소드
    @Override
    public void onClick(View v) {
        activity_main_sitter activity_main_sitter = (activity_main_sitter) getActivity();
        Intent intent;
        switch (v.getId())
        {

            // 견적서 찾기 버튼을 클릭한 경우
            // 견적서 목록 액티비티를 호출한다.
            case R.id.button_search_estimate :

                intent = new Intent(activity_main_sitter, activity_sitter_estimate.class);
                startActivity(intent);

                break;
            // 펫시터 등록 버튼을 클릭한 경우
            // 펫시터 등록 양식 액티비티를 호출한다.
            case R.id.button_regester_sitter :
                intent = new Intent(activity_main_sitter, activity_sitter_assignment_form.class);
                startActivity(intent);

                break;
            // 위탁 등록 버튼을 클릭한 경우
            // 위탁 업로드 액티비티를 호출한다.
            case R.id.button_upload_entrust :

                if(!LoginUserData.getSitter_entrust())
                {
                    intent = new Intent(activity_main_sitter, activity_upload_entrust.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(), "이미 등록되어 있습니다.", Toast.LENGTH_SHORT).show();
                }

                break;
            // 이용 방법 버튼을 클릭한 경우
            // 이용방법 액티비티를 호출한다.
            case R.id.button_instruction_sitter :
                intent = new Intent(activity_main_sitter, activity_instruction_sitter.class);
                startActivity(intent);

                break;
        }

    }
}