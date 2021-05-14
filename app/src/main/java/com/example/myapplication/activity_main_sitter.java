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

public class activity_main_sitter extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private fragment_home_sitter fragment_home_sitter = new fragment_home_sitter();
    private fragment_chat fragment_chat = new fragment_chat();
    private fragment_profile_sitter fragment_profile_sitter = new fragment_profile_sitter();
    private fragment_reserve_sitter fragment_reserve_sitter = new fragment_reserve_sitter();

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

        bottomNavigationView_sitter.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.navigation_menu_home:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, fragment_home_sitter).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_reserve_sitter:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, fragment_reserve_sitter).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_chat_sitter:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, fragment_chat).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_profile_sitter:
                    {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_frame_sitter, fragment_profile_sitter).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setChecked(true);

        //파이어베이스 스토리지 인스턴스
        //FirebaseStorage storage = FirebaseStorage.getInstance();
        //파이어베이스 스토리지 레퍼런스
        //StorageReference storageRef = storage.getReference();

       // Glide.with(this).load(storageRef.child("images_entrust/entrust_1").getDownloadUrl()).into(imageView);

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
}