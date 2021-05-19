package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_CODE_2 = 1;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private fragment_home fragment_home = new fragment_home();
    private fragment_chat fragment_chat = new fragment_chat();
    private fragment_profile_client fragment_profile = new fragment_profile_client();
    private fragment_reserve fragment_reserve = new fragment_reserve();

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
                    case R.id.navigation_menu_home:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame, fragment_home).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_reserve:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame, fragment_reserve).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_chat:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame, fragment_chat).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_profile:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame, fragment_profile).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            fragmentManager.beginTransaction().replace(R.id.layout_main_frame, new fragment_client_pet_info()).commitAllowingStateLoss();
        }
        if(requestCode == REQUEST_CODE_2 && resultCode == RESULT_OK)
        {
            fragmentManager.beginTransaction().replace(R.id.layout_main_frame, new fragment_client_review()).commitAllowingStateLoss();
        }
    }


}