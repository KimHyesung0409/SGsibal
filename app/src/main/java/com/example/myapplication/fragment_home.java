package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class fragment_home extends Fragment implements View.OnClickListener {

    ViewGroup viewGroup;
    Button button_reserve_visit, button_reserve_entrust, button_instruction, button_guide;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        button_reserve_visit = (Button)viewGroup.findViewById(R.id.button_reserve_visit);
        button_reserve_visit.setOnClickListener(this);
        //button_reserve_entrust = (Button)viewGroup.findViewById(R.id.button_reserve_entrust);
        //button_reserve_entrust.setOnClickListener(this);
        button_instruction = (Button)viewGroup.findViewById(R.id.button_instruction);
        button_instruction.setOnClickListener(this);
        button_guide = (Button)viewGroup.findViewById(R.id.button_guide);
        button_guide.setOnClickListener(this);

        return viewGroup;
    }

    // 버튼 클릭 메소드
    @Override
    public void onClick(View v) {

        MainActivity mainActivity = (MainActivity) getActivity();
        Intent intent;

        switch (v.getId())
        {
            // 예약 버튼을 클릭한 경우
            // 예약 프로세스 액티비티를 호출한다.
            case R.id.button_reserve_visit :
                intent = new Intent(mainActivity,activity_reserve_visit.class);
                startActivity(intent);
                break;
            // 이용방법 버튼을 클릭한 경우
            // 이용방법 액티비티를 호출한다.
            case R.id.button_instruction :
                intent = new Intent(mainActivity,activity_instruction.class);
                startActivity(intent);
                break;
            // 서비스 안내 버튼을 클릭한 경우
            // 서비스 안내 액티비티를 호출한다.
            case R.id.button_guide :
                intent = new Intent(mainActivity, activity_guide.class);
                startActivity(intent);
                break;
        }

    }

}