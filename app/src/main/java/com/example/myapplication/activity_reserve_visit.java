package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class activity_reserve_visit extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private fragment_reserve_visit_1 fragment_reserve_visit_1 = new fragment_reserve_visit_1();
    private fragment_reserve_visit_2 fragment_reserve_visit_2 = new fragment_reserve_visit_2();
    private fragment_reserve_visit_3 fragment_reserve_visit_3 = new fragment_reserve_visit_3();
    private fragment_reserve_visit_4 fragment_reserve_visit_4 = new fragment_reserve_visit_4();
    private fragment_payment fragment_payment = new fragment_payment();
    private Fragment fragment_reserve_list[] = {fragment_reserve_visit_1, fragment_reserve_visit_2,
            fragment_reserve_visit_3, fragment_reserve_visit_4, fragment_payment};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_visit);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);

        replaceFragment(fragment_reserve_visit_1);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);

        ViewGroup layout_progress_visit = (ViewGroup) findViewById(R.id.layout_progress_visit);
        TextView TextView_progress[] = new TextView[5];

        for(int i = 0; i < layout_progress_visit.getChildCount(); i++)
        {
            TextView_progress[i] = (TextView)layout_progress_visit.getChildAt(i);
            TextView_progress[i].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.seekbar_background));
        }

        TextView_progress[0].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.seekbar_progress));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                for(int j = 0; j < TextView_progress.length; j++)
                {
                    if(j == i)
                    {
                        TextView_progress[j].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.seekbar_progress));

                    }
                    else
                    {
                        TextView_progress[j].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.seekbar_background));
                    }
                }
                replaceFragment(fragment_reserve_list[i]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void replaceFragment(Fragment fragment)
    {
        fragmentManager.beginTransaction().replace(R.id.layout_reserve_visit_main, fragment).addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            fragment_reserve_visit_1.refreshListView();
        }

    }

}