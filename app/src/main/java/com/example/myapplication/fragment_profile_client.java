package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class fragment_profile_client extends Fragment implements View.OnClickListener {

    ViewGroup viewGroup;
    private Button button_client_info;
    private Button button_client_pet_info;
    private Button button_client_review;
    private Button button_service_center;
    private Button button_profile_logout;
    private Button button_profile_delete_account;

    private FirebaseFirestore db; //파이어스토어 db 객체
    private FirebaseAuth auth; //파이어베이스 인증 객체

    private fragment_client_info fragment_client_info = new fragment_client_info();
    private fragment_client_pet_info fragment_client_pet_info = new fragment_client_pet_info();
    private fragment_client_review fragment_client_review = new fragment_client_review();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();

        button_client_info = (Button)viewGroup.findViewById(R.id.button_client_info);
        button_client_info.setOnClickListener(this);

        button_client_pet_info = (Button)viewGroup.findViewById(R.id.button_client_pet_info);
        button_client_pet_info.setOnClickListener(this);

        button_client_review = (Button)viewGroup.findViewById(R.id.button_client_review);
        button_client_review.setOnClickListener(this);

        button_service_center = (Button)viewGroup.findViewById(R.id.button_service_center);
        button_service_center.setOnClickListener(this);

        button_profile_logout = (Button)viewGroup.findViewById(R.id.button_profile_logout);
        button_profile_logout.setOnClickListener(this);

        button_profile_delete_account = (Button)viewGroup.findViewById(R.id.button_profile_delete_account);
        button_profile_delete_account.setOnClickListener(this);

        return viewGroup;
    }

    // 버튼 클릭 메소드
   @Override
    public void onClick(View v) {
       FragmentManager fragmentManager = getFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

       switch (v.getId())
       {
           // 회원 정보 버튼을 클릭한 경우
           // 회원 정보 프래그먼트로 전환한다.
           case R.id.button_client_info :
               fragmentTransaction.replace(R.id.layout_main_frame, fragment_client_info).commit();

               break;
           // 반려동물 정보 버튼을 클릭한 경우
           // 반려동물 정보 프래그먼트로 전환한다.
           case R.id.button_client_pet_info :
               fragmentTransaction.replace(R.id.layout_main_frame, fragment_client_pet_info).commit();
               break;
           // 나의 후기 버튼을 클릭한 경우
           // 나의 후기 프래그먼트로 전환한다.
           case R.id.button_client_review :
               fragmentTransaction.replace(R.id.layout_main_frame, fragment_client_review).commit();
               break;
           // 고객 센터 버튼을 클릭한 경우
           // 고객 센터 액티비티를 호출한다.
           case R.id.button_service_center :
               Intent intent_service_center = new Intent(getActivity(), activity_service_center.class);
               startActivity(intent_service_center);
               break ;
           // 로그아웃 버튼을 클릭한 경우
           // 로그아웃 메소드를 실행하고
           // 로그인 액티비티를 호출한다.
           case R.id.button_profile_logout :
               FirebaseAuth.getInstance().signOut();
               Intent intent_profile_logout = new Intent(getActivity(), activity_login.class);
               startActivity(intent_profile_logout);
               break ;
           // 회원탈퇴 버튼을 클릭한 경우
           // 회원탈퇴 메소드를 호출한다.
           case R.id.button_profile_delete_account :
               deleteUserAccount();
               break ;
       }

       }

    // 회원탈퇴 메소드
    public void deleteUserAccount()
    {
        // 현재 유저를 가져온다.
        FirebaseUser user = auth.getCurrentUser();

        if(user != null)
        {
            // 유저의 uid
            String uid = user.getUid();

            // 현재 로그인한 유저를 삭제하는 메소드 실행.
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // db에 등록된 유저 정보를 삭제하는 메소드 호출
                                deleteUserData(uid);
                                // 로그아웃
                                auth.signOut();
                                // 로그인 액티비티 호출
                                Activity activity = getActivity();
                                Intent intent = new Intent(activity, activity_login.class);
                                startActivity(intent);
                                activity.finish();

                                Toast.makeText(getContext(), "계정 삭제 완료.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {

        }

    }

    // 유저 정보를 삭제하는 메소드.
    private void deleteUserData(String uid)
    {
        db = FirebaseFirestore.getInstance(); //파이어스토어 db 객체

        db.collection("users").document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(getContext(), "유저 정보 삭제 완료.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getContext(), "유저 정보 삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}