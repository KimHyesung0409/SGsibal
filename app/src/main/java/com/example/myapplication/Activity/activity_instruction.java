package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.MyAdapter_client_instruction;
import com.example.myapplication.R;

import me.relex.circleindicator.CircleIndicator3;

public class activity_instruction extends AppCompatActivity {
    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 6;
    private CircleIndicator3 mIndicator;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        mPager = findViewById(R.id.viewpager);

        pagerAdapter = new MyAdapter_client_instruction(this, num_page);
        mPager.setAdapter(pagerAdapter);

        mIndicator = findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page, 0);

        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(6);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(positionOffsetPixels == 0){
                    mPager.setCurrentItem(position);
                }
            }

            public void onPageSelected(int position){
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
            }
        });

        Button return_main_client;
        return_main_client = (Button)findViewById(R.id.return_main_client);
        return_main_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
