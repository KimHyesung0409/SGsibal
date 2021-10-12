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

// 이용방법을 표시하기 위한 액티비티이므로 별도의 코드가 없다.
public class activity_instruction_sitter extends AppCompatActivity {
    private ViewPager2 mPager2;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 6;
    private CircleIndicator3 mIndicator;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_sitter);

        mPager2 = findViewById(R.id.viewpager_sitter);

        pagerAdapter = new MyAdapter_client_instruction(this, num_page);
        mPager2.setAdapter(pagerAdapter);

        mIndicator = findViewById(R.id.indicator_sitter);
        mIndicator.setViewPager(mPager2);
        mIndicator.createIndicators(num_page, 0);

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

            public void onPageSelected(int position){
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
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
