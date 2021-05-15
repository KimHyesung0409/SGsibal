package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class fragment_reserve_visit_3 extends Fragment implements RadioGroup.OnCheckedChangeListener {

    ViewGroup viewGroup;
    private RadioGroup radioGroup_match_type;
    private RadioGroup radioGroup_Expeded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_visit_3, container, false);

        radioGroup_match_type = (RadioGroup)viewGroup.findViewById(R.id.radioGroup_match_type);
        radioGroup_Expeded = (RadioGroup)viewGroup.findViewById(R.id.radioGroup_expended);

        radioGroup_match_type.setOnCheckedChangeListener(this);
        radioGroup_Expeded.setOnCheckedChangeListener(this);

        return viewGroup;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        activity_reserve_visit activity = ((activity_reserve_visit)getActivity());
        RadioButton radioButton = (RadioButton)viewGroup.findViewById(checkedId);

        if(activity.getPetInfo() != null)
        {

            //같은 객체인지 확인.
            if (group == radioGroup_match_type) {

                switch (checkedId) {
                    case R.id.radiobutton_match_type_1:

                        activity.replaceFragment(new fragment_reserve_auto(), activity.TAG_AUTO);

                        break;

                    case R.id.radiobutton_match_type_2:

                        activity.replaceFragment(new fragment_reserve_estimate(), activity.TAG_ESTIMATE);

                        break;

                    case R.id.radiobutton_match_type_3:

                        radioGroup_Expeded.setVisibility(View.VISIBLE);

                        break;

                    default:

                        radioGroup_Expeded.setVisibility(View.GONE);

                }

            } else {

                switch (checkedId) {
                    case R.id.radiobutton_match_type_1:

                        activity.replaceFragment(new fragment_reserve_history(), activity.TAG_HISTORY);

                        break;

                    case R.id.radiobutton_match_type_2:

                        activity.replaceFragment(new fragment_reserve_search(), activity.TAG_SEARCH);

                        break;

                    default:

                        activity.replaceFragment(new fragment_reserve_favorites(), activity.TAG_FAVORITES);

                        break;

                }

            }

            radioButton.setChecked(false);
        }
        else
        {
            Toast.makeText(getContext(), "반려동물을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}