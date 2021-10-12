package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.MyAdapter_sitter_instruction;
import com.example.myapplication.R;

import me.relex.circleindicator.CircleIndicator3;

public class activity_instruction_sitter extends AppCompatActivity {

    private ViewPager2 mPager2;
    private FragmentStateAdapter pagerAdapter2;
    private int num_page2 = 8;
    private CircleIndicator3 mIndicator2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_sitter);

        mPager2 = findViewById(R.id.viewpager2);

        pagerAdapter2 = new MyAdapter_sitter_instruction(this, num_page2);
        mPager2.setAdapter(pagerAdapter2);

        mIndicator2 = findViewById(R.id.indicator2);
        mIndicator2.setViewPager(mPager2);
        mIndicator2.createIndicators(num_page2,0);

        mPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        mPager2.setCurrentItem(0);
        mPager2.setOffscreenPageLimit(8);

        mPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(positionOffsetPixels == 0){
                    mPager2.setCurrentItem(position);
                }
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator2.animatePageSelected(position % num_page2);
            }
        });
        Button return_main_sitter;
        return_main_sitter = (Button)findViewById(R.id.return_main_sitter);
        return_main_sitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
