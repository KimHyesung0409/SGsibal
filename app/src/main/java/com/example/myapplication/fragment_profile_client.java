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

    // 버튼이 많이 있으므로 switch case로 구분하자자
   @Override
    public void onClick(View v) {
       FragmentManager fragmentManager = getFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

       switch (v.getId())
       {
           case R.id.button_client_info :
               fragmentTransaction.replace(R.id.layout_main_frame, fragment_client_info).commit();

               break;

           case R.id.button_client_pet_info :
               fragmentTransaction.replace(R.id.layout_main_frame, fragment_client_pet_info).commit();
               break;

           case R.id.button_client_review :
               fragmentTransaction.replace(R.id.layout_main_frame, fragment_client_review).commit();
               break;

           case R.id.button_service_center :
               Intent intent_service_center = new Intent(getActivity(), service_center.class);
               startActivity(intent_service_center);
               break ;

           case R.id.button_profile_logout :
               FirebaseAuth.getInstance().signOut();
               Intent intent_profile_logout = new Intent(getActivity(), activity_login.class);
               startActivity(intent_profile_logout);
               break ;

           case R.id.button_profile_delete_account :
               deleteUserAccount();
               break ;
       }

       }


    public void deleteUserAccount()
    {
        FirebaseUser user = auth.getCurrentUser();

        if(user != null)
        {
            String uid = user.getUid();

            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                deleteUserData(uid);
                                auth.signOut();

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