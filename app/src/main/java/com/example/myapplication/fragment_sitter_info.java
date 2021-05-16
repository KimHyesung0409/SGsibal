package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;


public class fragment_sitter_info extends DialogFragment implements View.OnClickListener, View.OnLongClickListener {

    ViewGroup viewGroup;
    TextView sitter_info_name, sitter_info_address, sitter_info_address_detail,
            sitter_info_age, sitter_info_phonenumber, sitter_info_email,
            sitter_info_can_pet, sitter_info_can_time;
    String sitter_name, sitter_address, sitter_address_detail, sitter_age,
            sitter_phonenumber, sitter_email, sitter_can_pet, sitter_can_time;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String uid;
    FirebaseFirestore fstore;
    Button change_pwd_sitter, return_profile_sitter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_sitter_info, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        sitter_info_name = viewGroup.findViewById(R.id.sitter_info_name);


        sitter_info_address = viewGroup.findViewById(R.id.sitter_info_address);
        sitter_info_address_detail = viewGroup.findViewById(R.id.sitter_info_address_detail);
        sitter_info_address.setOnLongClickListener(this);
        sitter_info_address_detail.setOnLongClickListener(this);

        sitter_info_age = viewGroup.findViewById(R.id.sitter_info_age);


        sitter_info_phonenumber = viewGroup.findViewById(R.id.sitter_info_phonenumber);


        sitter_info_email = viewGroup.findViewById(R.id.sitter_info_email);


        sitter_info_can_pet = viewGroup.findViewById(R.id.sitter_info_can_pet);
        sitter_info_can_pet.setOnLongClickListener(this);

        sitter_info_can_time = viewGroup.findViewById(R.id.sitter_info_can_time);
        sitter_info_can_time.setOnLongClickListener(this);


        change_pwd_sitter = viewGroup.findViewById(R.id.change_pwd_sitter);
        change_pwd_sitter.setOnClickListener(this);

        return_profile_sitter = viewGroup.findViewById(R.id.return_profile_sitter);
        return_profile_sitter.setOnClickListener(this);

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
                        sitter_name = document.getString("name");
                        sitter_info_name.setText(sitter_name);

                        //파싱 없이 하기 위해, address, address_detail 분리
                        sitter_address = document.getString("address");
                        sitter_info_address.setText(sitter_address);

                        sitter_address_detail = document.getString("address_detail");
                        sitter_info_address_detail.setText(sitter_address_detail);

                        //sitter_age
                        sitter_age = document.getString("age");
                        sitter_info_age.setText(sitter_age);

                        //sitter_phonenumber
                        sitter_phonenumber = document.getString("pnum");
                        sitter_info_phonenumber.setText(sitter_phonenumber);

                        // 이메일의 경우 auth.getCurrentUser().getEmail(); 로 가져올 수 있음.
                        sitter_email = auth.getCurrentUser().getEmail();
                        sitter_info_email.setText(sitter_email);

                        //sitter_can_pet = task.getResult().getString("care_list");
                        // 가져오려는 데이터가 array 인 경우 ArrayList<?>() 로 가져와야함.
                        // 그 이후 반복자 등을 통해서 요소를 분해하여 가져와야함.
                        ArrayList<String> care_list = (ArrayList<String>)document.get("care_list");
                        // 반복자
                        Iterator<String> iterator = care_list.iterator();

                        // StringBuilder
                        StringBuilder stringBuilder = new StringBuilder();
                        // 리스트에 다음 요소가 존재하면. loop
                        while (iterator.hasNext())
                        {
                            // 리스트의 요소를 가져옴
                            String cate_pet = iterator.next();

                            // StringBuilder에 추가
                            stringBuilder.append(cate_pet);
                            stringBuilder.append(",");

                        }
                        // 마지막에 붙은 , 를 지워줌.
                        stringBuilder.deleteCharAt(stringBuilder.length() -1);

                        // StringBuilder.toString 으로 String 객채를 반환해줌.
                        sitter_can_pet = stringBuilder.toString();
                                sitter_info_can_pet.setText(sitter_can_pet);

                        //sitter_can_time       데이터베이스에 넣을지 어떻게 할지 고민
                        sitter_can_time = document.getString("care_time");
                        sitter_info_can_time.setText(sitter_can_time);

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
        fragment_profile_sitter fragment_profile_sitter = new fragment_profile_sitter();

        switch (view.getId()){

            case R.id.change_pwd_sitter:
                String pwd_change_email = auth.getCurrentUser().getEmail();
                auth.sendPasswordResetEmail(pwd_change_email);
                Toast.makeText(getActivity(),"메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.return_profile_sitter:
                fragmentTransaction.replace(R.id.layout_main_frame_sitter, fragment_profile_sitter).commit();
                break;
        }
    }


    @Override
    public boolean onLongClick(View view) {
        Intent intnet_change_address = new Intent(getActivity(), activity_popup_address.class);
        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());

        switch (view.getId()){
            case R.id.sitter_info_address:
                startActivity(intnet_change_address);
                break;

            case R.id.sitter_info_address_detail:
                startActivity(intnet_change_address);
                break;

                /* 이 부분은 미완성이라 안봐도댐
            case R.id.sitter_info_can_pet:
                final EditText change_can_pet = new EditText(getActivity());
                dlg.setTitle("케어가능한 동물 종류 변경");
                dlg.setView(change_can_pet);
                dlg.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
                break;

            case R.id.sitter_info_can_time:
                final EditText change_can_time = new EditText(getActivity());
                dlg.setTitle("케어가능한 시간 변경");
                dlg.setView(change_can_time);
                dlg.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
                break;
                */
        }
        return false;
    }

}