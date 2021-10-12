package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.Activity.activity_instruction_sitter;
import com.example.myapplication.Fragment.fragment_sitter_instruction_1;
import com.example.myapplication.Fragment.fragment_sitter_instruction_2;
import com.example.myapplication.Fragment.fragment_sitter_instruction_3;
import com.example.myapplication.Fragment.fragment_sitter_instruction_4;
import com.example.myapplication.Fragment.fragment_sitter_instruction_5;
import com.example.myapplication.Fragment.fragment_sitter_instruction_6;
import com.example.myapplication.Fragment.fragment_sitter_instruction_7;
import com.example.myapplication.Fragment.fragment_sitter_instruction_8;

public class MyAdapter_sitter_instruction extends FragmentStateAdapter {

    public int mCount2;

    public MyAdapter_sitter_instruction(activity_instruction_sitter fa2, int count2){
        super(fa2);
        mCount2 = count2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index2 = getRealPosition(position);

        if(index2 == 0) return new fragment_sitter_instruction_1();
        else if(index2 == 1) return new fragment_sitter_instruction_2();
        else if(index2 == 2) return new fragment_sitter_instruction_3();
        else if(index2 == 3) return new fragment_sitter_instruction_4();
        else if(index2 == 4) return new fragment_sitter_instruction_5();
        else if(index2 == 5) return new fragment_sitter_instruction_6();
        else if(index2 == 6) return new fragment_sitter_instruction_7();
        else return new fragment_sitter_instruction_8();

    }

    private int getRealPosition(int position) { return position % mCount2; }

    @Override
    public int getItemCount() {
        return 8;
    }

}
