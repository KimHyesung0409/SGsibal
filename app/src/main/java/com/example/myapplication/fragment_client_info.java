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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class fragment_client_info extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private static final int REQUEST_CODE = 0;
    ViewGroup viewGroup;
    TextView client_info_name, client_info_address, client_info_address_detail,
            client_info_age, client_info_phonenumber, client_info_email;
    String client_name, client_address, client_address_detail, client_age,
            client_phonenumber, client_email;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String uid;
    FirebaseFirestore fstore;
    Button change_pwd_client, return_profile_client;
    private Double lat;
    private Double lon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_client_info, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        client_info_name = viewGroup.findViewById(R.id.client_info_name);


        client_info_address = viewGroup.findViewById(R.id.client_info_address);
        client_info_address_detail = viewGroup.findViewById(R.id.client_info_address_detail);
        client_info_address.setOnLongClickListener(this);
        client_info_address_detail.setOnLongClickListener(this);

        client_info_age = viewGroup.findViewById(R.id.client_info_age);


        client_info_phonenumber = viewGroup.findViewById(R.id.client_info_phonenumber);


        client_info_email = viewGroup.findViewById(R.id.client_info_email);

        change_pwd_client = viewGroup.findViewById(R.id.change_pwd_client);
        change_pwd_client.setOnClickListener(this);

        return_profile_client = viewGroup.findViewById(R.id.return_profile_client);
        return_profile_client.setOnClickListener(this);

        db.collection("users").document(auth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    // task.getResult().get ~~~ 하는 것 보다는
                    // DocumentSnapshot document = task.getResult(); 로 관리하는게 쉬움.
                    DocumentSnapshot document = task.getResult();

                    if (document.exists())
                    {
                        client_name = document.getString("name");
                        client_info_name.setText(client_name);

                        //파싱 없이 하기 위해, address, address_detail 분리
                        client_address = document.getString("address");
                        client_info_address.setText(client_address);

                        client_address_detail = document.getString("address_detail");
                        client_info_address_detail.setText(client_address_detail);

                        //sitter_age
                        client_age = document.getString("age");
                        client_info_age.setText(client_age);

                        //sitter_phonenumber
                        client_phonenumber = document.getString("pnum");
                        client_info_phonenumber.setText(client_phonenumber);

                        // 이메일의 경우 auth.getCurrentUser().getEmail(); 로 가져올 수 있음.
                        client_email = auth.getCurrentUser().getEmail();
                        client_info_email.setText(client_email);

                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No document exist", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        return viewGroup;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment_profile fragment_profile = new fragment_profile();

        switch (view.getId()){

            case R.id.change_pwd_client :
                String pwd_change_email = auth.getCurrentUser().getEmail();
                auth.sendPasswordResetEmail(pwd_change_email);
                Toast.makeText(getActivity(),"메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.return_profile_client :
                fragmentTransaction.replace(R.id.layout_main_frame, fragment_profile).commit();
                break;

        }

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}