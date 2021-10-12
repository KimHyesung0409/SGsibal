package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.Fragment.fragment_client_instruction_1;
import com.example.myapplication.Fragment.fragment_client_instruction_2;
import com.example.myapplication.Fragment.fragment_client_instruction_3;
import com.example.myapplication.Fragment.fragment_client_instruction_4;
import com.example.myapplication.Fragment.fragment_client_instruction_5;
import com.example.myapplication.Fragment.fragment_client_instruction_6;

public class MyAdapter_client_instruction extends FragmentStateAdapter {
    public int mCount;

    public MyAdapter_client_instruction(FragmentActivity fa, int count){
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new fragment_client_instruction_1();
        else if(index==1) return new fragment_client_instruction_2();
        else if(index==2) return new fragment_client_instruction_3();
        else if(index==3) return new fragment_client_instruction_4();
        else if(index==4) return new fragment_client_instruction_5();
        else return new fragment_client_instruction_6();
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public int getRealPosition(int position){ return position % mCount;}
}
