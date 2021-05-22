package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_CODE_2 = 1;
    private static final int REQUEST_CODE_3 = 2;
    private static final int REQUEST_CODE_4 = 3;

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
                        replaceFragment(fragment_home);
                        break;
                    }
                    case R.id.navigation_menu_reserve:
                    {
                        replaceFragment(fragment_reserve);
                        break;
                    }
                    case R.id.navigation_menu_chat:
                    {
                        replaceFragment(fragment_chat);
                        break;
                    }
                    case R.id.navigation_menu_profile:
                    {
                        replaceFragment(fragment_profile);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            replaceFragment(new fragment_client_pet_info());
        }
        if(requestCode == REQUEST_CODE_2 && resultCode == RESULT_OK)
        {
            replaceFragment(new fragment_client_review());
        }
        if(requestCode == REQUEST_CODE_3 && resultCode == RESULT_OK)
        {
            fragment_reserve.refresh();
        }
        if(requestCode == REQUEST_CODE_4 && resultCode == RESULT_OK)
        {
            fragment_reserve.refresh();
        }

    }

    private void replaceFragment(Fragment fragment)
    {
        fragmentManager.beginTransaction().replace(R.id.layout_main_frame, fragment).commitAllowingStateLoss();
    }


}