package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

public class fragment_reserve_visit_3 extends Fragment {

    ViewGroup viewGroup;
    RadioGroup radioGroup, radioGroup_Expeded;

    private static final int TAG_AUTO = 1001;
    private static final int TAG_ESTIMATE = 1002;
    private static final int TAG_HISTORY = 1003;
    private static final int TAG_SEARCH = 1004;
    private static final int TAG_FAVORITES = 1005;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_visit_3, container, false);

        radioGroup = (RadioGroup)viewGroup.findViewById(R.id.radioGroup_match_type);
        radioGroup_Expeded = (RadioGroup)viewGroup.findViewById(R.id.radioGroup_expended);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton radioButton= (RadioButton)viewGroup.findViewById(i);

                if(radioButton.getId() == R.id.radiobutton_match_type_1)
                {
                    ((activity_reserve_visit)getActivity()).replaceFragment(new fragment_reserve_auto(), TAG_AUTO);
                    radioButton.setChecked(false);
                }
                else if(radioButton.getId() == R.id.radiobutton_match_type_2)
                {
                    ((activity_reserve_visit)getActivity()).replaceFragment(new fragment_reserve_estimate(), TAG_ESTIMATE);
                    radioButton.setChecked(false);
                }
                else if(radioButton.getId() == R.id.radiobutton_match_type_3)
                {
                    radioGroup_Expeded.setVisibility(View.VISIBLE);
                }
                else
                {
                    radioGroup_Expeded.setVisibility(View.GONE);
                }

            }
        });


        radioGroup_Expeded.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton radioButton= (RadioButton)viewGroup.findViewById(i);

                if(radioButton.getId() == R.id.radiobutton_expended_1)
                {
                    ((activity_reserve_visit)getActivity()).replaceFragment(new fragment_reserve_history(), TAG_HISTORY);
                    radioButton.setChecked(false);
                }
                else if(radioButton.getId() == R.id.radiobutton_expended_2)
                {
                    ((activity_reserve_visit)getActivity()).replaceFragment(new fragment_reserve_search(), TAG_SEARCH);
                    radioButton.setChecked(false);
                }
                else if(radioButton.getId() == R.id.radiobutton_expended_3)
                {
                    ((activity_reserve_visit)getActivity()).replaceFragment(new fragment_reserve_favorites(), TAG_FAVORITES);
                    radioButton.setChecked(false);
                }

            }
        });

        return viewGroup;
    }
}