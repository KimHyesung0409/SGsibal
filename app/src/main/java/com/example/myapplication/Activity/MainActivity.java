package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.LoginUserData;
import com.example.myapplication.NotificationMessaging;
import com.example.myapplication.R;
import com.example.myapplication.Fragment.fragment_chat;
import com.example.myapplication.Fragment.fragment_client_pet_info;
import com.example.myapplication.Fragment.fragment_client_review;
import com.example.myapplication.Fragment.fragment_home;
import com.example.myapplication.Fragment.fragment_profile_client;
import com.example.myapplication.Fragment.fragment_reserve;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_CODE_2 = 1;
    private static final int REQUEST_CODE_3 = 2;
    private static final int REQUEST_CODE_4 = 3;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private com.example.myapplication.Fragment.fragment_home fragment_home = new fragment_home();
    private com.example.myapplication.Fragment.fragment_chat fragment_chat = new fragment_chat();
    private fragment_profile_client fragment_profile = new fragment_profile_client();
    private com.example.myapplication.Fragment.fragment_reserve fragment_reserve = new fragment_reserve();

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        // 첫화면에 출력할 프래그먼트 지정
        fragmentManager.beginTransaction().replace(R.id.layout_main_frame, fragment_home).commitAllowingStateLoss();
        // 바텀 네비게이션 뷰 안에 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    // 홈 아이템을 선택한 경우
                    // 홈 프래그먼트로 전환
                    case R.id.navigation_menu_home:
                    {
                        replaceFragment(fragment_home);
                        break;
                    }
                    // 예약 현황 아이템을 선택한 경우
                    // 예약 현황 프래그먼트로 전환
                    case R.id.navigation_menu_reserve:
                    {
                        replaceFragment(fragment_reserve);
                        break;
                    }
                    // 채팅 아이템을 선택한 경우
                    // 채팅방 프래그먼트로 전환
                    case R.id.navigation_menu_chat:
                    {
                        replaceFragment(fragment_chat);
                        break;
                    }
                    // 프로필 아이템을 클릭한경우
                    // 프로필 프래그먼트로 전환
                    case R.id.navigation_menu_profile:
                    {
                        replaceFragment(fragment_profile);
                        break;
                    }
                }

                return true;
            }
        });
        // 고객 화면이므로 스위치를 false로 설정.(펫시터 화면은 true)
        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        // 스위치를 클릭하면 펫시터화면으로 전환.
        switch_change_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                {
                    Intent intent = new Intent(MainActivity.this, activity_main_sitter.class);
                    startActivity(intent);
                    finish();
                }
                else
                {

                }

            }
        });


        // fcm 메시지로 인해 해당 앱이 실행되었고
        // fcm 메시지의 user_id와 로그인한 user_id가 동일한 경우
        // fcm 메시지의 data에 따라서 이벤트를 발생시킨다.
        // 현재는 테스트 수준이므로 예약현황 까지만 보여준다.
        Intent intent = getIntent();

        boolean callFrom = intent.getBooleanExtra("callFrom", false);

        if(callFrom)
        {
            String fcm_user_id = intent.getStringExtra("fcm_user_id");
            int type = intent.getIntExtra("type", 0);

            // if문과 혼동할 수 있으므로 switch case로 작성

            switch (type)
            {

                case NotificationMessaging.FCM_RESERVE :

                    bottomNavigationView.setSelectedItemId(R.id.navigation_menu_reserve);
                    break;

            }

        }

        LoginUserData.printLoginUserData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 반려동물 정보 수정, 삭제로 인해 반려동물 추가 액티비티가 종료되면
        // 변경된 반려동물 목록을 적용하기 위해 프래그먼트 재호출
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            replaceFragment(new fragment_client_pet_info());
        }
        // 후기 정보 수정, 삭제로 인해 후기 액티비티가 종료되면
        // 변경된 후기 목록을 적용하기 위해 프래그먼트 재호출
        if (requestCode == REQUEST_CODE_2 && resultCode == RESULT_OK) {
            replaceFragment(new fragment_client_review());
        }

        // 예약 현황에서 후기를 작성하면 해당 예약이 db에서 삭제되는데
        // 삭제된 내용을 예약 현황 목록에 적용하기 위해 해당 프래그먼트의 리사이클러뷰 refresh() 메소드를 호출해준다.
        if (requestCode == REQUEST_CODE_3 && resultCode == RESULT_OK) {
            fragment_reserve.refresh();
        }
        // 예약 현황에서 견적서의 역제안을 수락하면 해당 견적서가 db에서 삭제되고 예약이 db에 추가되는데
        // 해당 내용을 예약 현황 목록, 견적서 목록에 적용하기 위해 해당 프래그먼트의 리사이클러뷰 refresh() 메소드를 호출해준다.
        if (requestCode == REQUEST_CODE_4 && resultCode == RESULT_OK) {
            fragment_reserve.refresh();
        }

    }

    // 프래그먼트 전환 메소드.
    public void replaceFragment(Fragment fragment)
    {
        fragmentManager.beginTransaction().replace(R.id.layout_main_frame, fragment).commitAllowingStateLoss();
    }


}