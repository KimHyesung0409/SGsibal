package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class activity_main_sitter extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private fragment_home_sitter fragment_home_sitter = new fragment_home_sitter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sitter);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        fragmentManager.beginTransaction().replace(R.id.layout_main_sitter_frame, fragment_home_sitter).commitAllowingStateLoss();

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setChecked(true);

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