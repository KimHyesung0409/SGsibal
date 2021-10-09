package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.example.myapplication.Fragment.fragment_chat_sitter;
import com.example.myapplication.Fragment.fragment_home_sitter;
import com.example.myapplication.Fragment.fragment_profile_sitter;
import com.example.myapplication.Fragment.fragment_reserve_sitter;
import com.example.myapplication.Fragment.fragment_sitter_story;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_main_sitter extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private com.example.myapplication.Fragment.fragment_home_sitter fragment_home_sitter = new fragment_home_sitter();
    private com.example.myapplication.Fragment.fragment_chat_sitter fragment_chat_sitter = new fragment_chat_sitter();
    private com.example.myapplication.Fragment.fragment_profile_sitter fragment_profile_sitter = new fragment_profile_sitter();
    private com.example.myapplication.Fragment.fragment_reserve_sitter fragment_reserve_sitter = new fragment_reserve_sitter();

    private BottomNavigationView bottomNavigationView_sitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sitter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        bottomNavigationView_sitter = findViewById(R.id.bottom_navigation_view_sitter);

        fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, fragment_home_sitter).commitAllowingStateLoss();

        // 바텀네이게이션바의 아이템 클릭 리스너.
        bottomNavigationView_sitter.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            // 아이템이 선택되면 해당 아이템에 맞는 프래그먼트로 전환.
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    // 홈 아이템을 선택한 경우
                    // 홈 프래그먼트로 전환
                    case R.id.navigation_menu_home_sitter:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, fragment_home_sitter).commitAllowingStateLoss();
                        break;
                    }
                    // 예약 현황 아이템을 선택한 경우
                    // 예약 현황 프래그먼트로 전환
                    case R.id.navigation_menu_reserve_sitter:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, fragment_reserve_sitter).commitAllowingStateLoss();
                        break;
                    }
                    // 채팅 아이템을 선택한 경우
                    // 채팅방 프래그먼트로 전환
                    case R.id.navigation_menu_chat_sitter:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, fragment_chat_sitter).commitAllowingStateLoss();
                        break;
                    }
                    // 프로필 아이템을 클릭한경우
                    // 프로필 프래그먼트로 전환
                    case R.id.navigation_menu_profile_sitter:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, fragment_profile_sitter).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
        // 펫시터 화면이므로 스위치를 true로 설정.(고객 화면은 false)
        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setChecked(true);

        // 스위치를 클릭하면 고객화면으로 전환.
        switch_change_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                {

                }
                else
                {
                    Intent intent = new Intent(activity_main_sitter.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    // 스토리를 작성, 삭제, 수정 시 리사이클러뷰를 재호출 하기위한 목적으로 프래그먼트를 재호출한다.
    // 해당 액티비티에서 fragment_sitter_story에 대한 객체나 참조를 가지고 있지 않기 때문이 이런 방식으로 리사이클러뷰를 재호출 했다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, new fragment_sitter_story()).commitAllowingStateLoss();
        }

    }
}