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

    // 라디오 버튼 클릭 메소드.
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        activity_reserve_visit activity = ((activity_reserve_visit)getActivity());
        RadioButton radioButton = (RadioButton)viewGroup.findViewById(checkedId);

        if(fragment_reserve_visit_1.getSelected_pet() != null)
        {

            //같은 객체인지 확인.
            // 버튼 그룹이 매칭 타입인 경우
            // 즉 자동, 위탁, 선택, 견적서 작성인 경우.
            if (group == radioGroup_match_type) {

                switch (checkedId) {
                    // 자동 매칭 버튼을 선택한 경우
                    // 자동 매칭 프래그먼트로 전환한다.
                    case R.id.radiobutton_match_type_1:

                        activity.replaceFragment(new fragment_reserve_auto(), activity.TAG_AUTO);

                        break;
                    // 견적서 작성 버튼을 선택한 경우
                    // 견적서 작성 프래그먼트로 전환한다.
                    case R.id.radiobutton_match_type_2:

                        activity.replaceFragment(new fragment_reserve_estimate(), activity.TAG_ESTIMATE);

                        break;
                    // 선택 매칭 버튼을 선택한 경우
                    // 선택 매칭의 3가지 방식(검색, 즐겨찾기, 사용기록)을 VISIBLE 한다.
                    case R.id.radiobutton_match_type_3:

                        radioGroup_Expeded.setVisibility(View.VISIBLE);

                        break;
                    // 위탁 버튼을 선택한 경우
                    // 위탁 리스트를 출력하는 프래그먼트로 전환한다.
                    case R.id.radiobutton_match_type_4:
                        activity.replaceFragment(new fragment_reserve_entrust(), activity.TAG_ENTRUST);

                }
                // 버튼 그룹이 선택 매칭의 3가지 방식(검색, 즐겨찾기, 사용기록)인 경우
            } else {
                System.out.println(checkedId);
                switch (checkedId)
                {
                    // 사용기록 버튼을 선택한 경우
                    // 사용기록 프래그먼트로 전환한다.
                    case R.id.radiobutton_expended_1:

                        System.out.println("히스토리");
                        activity.replaceFragment(new fragment_reserve_history(), activity.TAG_HISTORY);

                        break;
                    // 검색 버튼을 선택한 경우
                    // 검색예약 프래그먼트로 전환한다.
                    case R.id.radiobutton_expended_2:

                        System.out.println("검색");
                        activity.replaceFragment(new fragment_reserve_search(), activity.TAG_SEARCH);

                        break;
                    // 즐겨찾기 버튼을 선택한 경우\
                    // 즐겨찾기 프래그먼트로 전환한다.
                    default:

                        System.out.println("즐겨찾기");
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