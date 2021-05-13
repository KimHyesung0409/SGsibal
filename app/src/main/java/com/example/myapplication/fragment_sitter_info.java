package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class fragment_sitter_info extends Fragment {
    ViewGroup viewGroup;
    TextView sitter_info_name, sitter_info_address, sitter_info_age, sitter_info_phonenumber,
            sitter_info_email, sitter_info_can_pet, sitter_info_can_time;
    String sitter_name, sitter_address, sitter_phonenumber, sitter_email, sitter_can_pet, sitter_can_time;
    Integer sitter_age;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String uid;
    FirebaseFirestore fstore;
    Button change_pwd_sitter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_sitter_info, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        sitter_info_name = viewGroup.findViewById(R.id.sitter_info_name);
        sitter_info_address = viewGroup.findViewById(R.id.sitter_info_address);
        sitter_info_age = viewGroup.findViewById(R.id.sitter_info_age);
        sitter_info_phonenumber = viewGroup.findViewById(R.id.sitter_info_phonenumber);
        sitter_info_email = viewGroup.findViewById(R.id.sitter_info_email);
        sitter_info_can_pet = viewGroup.findViewById(R.id.sitter_info_can_pet);
        sitter_info_can_time = viewGroup.findViewById(R.id.sitter_info_can_time);

        change_pwd_sitter = viewGroup.findViewById(R.id.change_pwd_sitter);
        change_pwd_sitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //대화상자 출력해서 할 예정, 진행 중 어렵다 생각하면 바꿈.
            }
        });

        fstore = FirebaseFirestore.getInstance();
        fstore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    sitter_name = task.getResult().getString("name");
                    sitter_info_name.setText(sitter_name);

                    sitter_address = task.getResult().getString("address")+task.getResult().getString("address_detail");
                    sitter_info_address.setText(sitter_address);

                    //sitter_age            데이터베이스에 넣을지 어떻게 할지 고민

                    //sitter_phonenumber    데이터베이스에 넣을지 어떻게 할지 고민

                    sitter_email = task.getResult().getString("uid");
                    sitter_info_email.setText(sitter_email);

                    sitter_can_pet = task.getResult().getString("care_list");
                    sitter_info_can_pet.setText(sitter_can_pet);

                    //sitter_can_time       데이터베이스에 넣을지 어떻게 할지 고민

                } else{
                    Toast.makeText(getActivity(), "Currently logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return viewGroup;
    }
}