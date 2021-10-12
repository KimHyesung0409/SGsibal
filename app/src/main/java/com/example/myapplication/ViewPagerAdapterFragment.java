package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapterFragment extends FragmentPagerAdapter
{

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public ViewPagerAdapterFragment(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment)
    {
        fragmentArrayList.add(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "반려동물 정보";
            case 1:
                return "고객 정보";
            case 2:
                return "펫시터 정보";
        }
        return null;
    }

}
