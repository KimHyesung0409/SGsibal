package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.Fragment.fragment_sitter_assignment_1;
import com.example.myapplication.Fragment.fragment_sitter_assignment_2;
import com.example.myapplication.Fragment.fragment_sitter_assignment_3;
import com.example.myapplication.Fragment.fragment_sitter_assignment_4;

public class MyAdapter_sitter_assignment extends FragmentStateAdapter {

    public int mCount3;

    public MyAdapter_sitter_assignment(FragmentActivity fa3, int count3){
        super(fa3);
        mCount3 = count3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index3 = getRealPosition(position);

        if (index3 == 0) return new fragment_sitter_assignment_1();
        else if (index3 == 1) return new fragment_sitter_assignment_2();
        else if (index3 == 2) return new fragment_sitter_assignment_3();
        else return new fragment_sitter_assignment_4();

    }

    private int getRealPosition(int position) { return position % mCount3;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}