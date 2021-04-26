package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class fragment_reserve_estimate extends Fragment {

    ViewGroup viewGroup;
    private FirebaseFirestore db; // 파이어베이스 객체
    private FirebaseAuth auth;
    private EditText edit_address;
    private EditText edit_requirement;
    private EditText edit_price;
    private String uid;
    private Button button_next;

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
        edit_requirement = (EditText)viewGroup.findViewById(R.id.edit_requirement);
        edit_address = (EditText)viewGroup.findViewById(R.id.edit_address);

        getAddress();

        return viewGroup;
    }

    private void getAddress()
    {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists())
                    {
                        String address = (String)document.get("address");
                        edit_address.setText(address);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "위치 데이터 조회 실패", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "데이터 조회 실패.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void uploadEstimate()
    {
        String price = edit_price.getText().toString();
        String requirement = edit_requirement.getText().toString();
        String address = edit_address.getText().toString();

        // Key와 Value를 가지는 맵
        Map<String, Object> user = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        user.put("uid", uid);
        user.put("price", price);
        user.put("requirement", requirement);
        user.put("address", address);
        // 파이어 스토어는 자동 증가가 없다 따라서 document명을 지정하지 않고 add메소드로 데이터를 추가한다.
        // 하지만 파이어 스토어는 이렇게 임의로 생성한 문서ID를 정렬하지 못한다고 한다.
        // 구글이 추천하는 방법은 필드에 타임스탬프를 넣어 시간별로 정렬할 수 있도록 만드는 것이 좋다고 한다.
        user.put("timestamp", new Timestamp(new Date()));
        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        db.collection("estimate")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Activity activity = getActivity();
                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                        activity.finish();
                        Toast.makeText(getContext(), "견적서 업로드 성공", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "견적서 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}