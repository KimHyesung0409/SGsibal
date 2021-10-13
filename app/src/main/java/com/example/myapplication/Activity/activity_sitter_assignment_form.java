package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;

import android.widget.Switch;

import android.widget.Button;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.MyAdapter_sitter_assignment;
import com.example.myapplication.R;

import me.relex.circleindicator.CircleIndicator3;

public class activity_sitter_assignment_form extends AppCompatActivity {

    private ViewPager2 mPager3;
    private FragmentStateAdapter pagerAdapter3;
    private int num_page3 = 4;
    private CircleIndicator3 mIndicator3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_asignment_form);

        // --------- 액션바, 스위치 버튼 설정 ----------
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);
        // ---------------------------------------------

        mPager3 = findViewById(R.id.viewpager3);

        pagerAdapter3 = new MyAdapter_sitter_assignment(this, num_page3);
        mPager3.setAdapter(pagerAdapter3);

        mIndicator3 = findViewById(R.id.indicator3);
        mIndicator3.setViewPager(mPager3);
        mIndicator3.createIndicators(num_page3,0);

        mPager3.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        mPager3.setCurrentItem(0);
        mPager3.setOffscreenPageLimit(4);

        mPager3.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(positionOffsetPixels == 0) {
                    mPager3.setCurrentItem(position);
                }
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator3.animatePageSelected(position % num_page3);
            }
        });
        Button return_main_sitter;
        return_main_sitter = (Button)findViewById(R.id.return_main_sitter2);
        return_main_sitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
