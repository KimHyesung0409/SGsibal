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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class fragment_profile extends Fragment {

    ViewGroup viewGroup;
    private Button button_profile_delete_account;
    private FirebaseFirestore db; //파이어스토어 db 객체
    private FirebaseAuth auth; //파이어베이스 인증 객체

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();

        button_profile_delete_account = (Button)viewGroup.findViewById(R.id.button_profile_delete_account);

        // 프래그먼트에서는 xml 상에서 선언하는 onClick 메소드를 사용할 수 없다.
        // 따라서 임의의 리스너를 생성하여 클릭을 구현한다.

        button_profile_delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUserAccount();
            }
        });

        return viewGroup;
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
                        Toast.makeText(getContext(), "유저 정보 삭제 완료.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "유저 정보 삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}