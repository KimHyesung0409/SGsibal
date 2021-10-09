package com.example.myapplication.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ListViewItem.ListViewItem_petlist;
import com.example.myapplication.LoginUserData;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class fragment_reserve_estimate extends Fragment {

    ViewGroup viewGroup;
    private FirebaseFirestore db; // 파이어베이스 객체
    private FirebaseAuth auth;
    private EditText edit_address;
    private EditText edit_info;
    private EditText edit_price;
    private String uid;
    private Button button_next;
    private ListViewItem_petlist pet_data;

    public static fragment_reserve_auto newInstance() {
        return new fragment_reserve_auto();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_estimate, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();


        edit_price = (EditText)viewGroup.findViewById(R.id.edit_price);
        edit_info = (EditText)viewGroup.findViewById(R.id.edit_info);
        edit_address = (EditText)viewGroup.findViewById(R.id.edit_address);

        // 펫 데이터를 가져온다.
        pet_data = fragment_reserve_visit_1.getSelected_pet();

        if(pet_data != null)
        {
            edit_info.setText(pet_data.getInfo());
        }

        edit_address.setText(LoginUserData.getAddress());

        return viewGroup;
    }

    // 견적서 업로드 메소드
    public void uploadEstimate()
    {
        String price = edit_price.getText().toString();
        String info = edit_info.getText().toString();
        String address = edit_address.getText().toString();

        // Key와 Value를 가지는 맵
        Map<String, Object> estimate = new HashMap<>();
        // 위에서 만든 맵 변수에 데이터 삽입
        estimate.put("uid", uid);
        estimate.put("pet_id", pet_data.getPet_id());
        estimate.put("price", price);
        estimate.put("address", address);
        estimate.put("datetime", fragment_reserve_visit_2.getSelectedTime());
        estimate.put("species", pet_data.getSpecies());
        estimate.put("species_detail", pet_data.getDetail_species());
        estimate.put("pet_name", pet_data.getName());
        estimate.put("pet_age", pet_data.getAge());
        estimate.put("info", info);

        // 파이어 스토어는 자동 증가가 없다 따라서 document명을 지정하지 않고 add메소드로 데이터를 추가한다.
        // 하지만 파이어 스토어는 이렇게 임의로 생성한 문서ID를 정렬하지 못한다고 한다.
        // 구글이 추천하는 방법은 필드에 타임스탬프를 넣어 시간별로 정렬할 수 있도록 만드는 것이 좋다고 한다.
        estimate.put("timestamp", new Timestamp(new Date()));
        // db에 업로드
          DocumentReference ref = db.collection("estimate").document();
                ref.set(estimate)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    // 업로드가 성공적으로 완료되면 예약 액티비티 종료
                    Activity activity = getActivity();
                    activity.finish();

                    Toast.makeText(getContext(), "견적서 업로드 성공", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
    }

}