package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;


public class fragment_sitter_story extends Fragment implements  OnCustomClickListener {

    private static final int REQUEST_CODE = 0;

    ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_sitter_story, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        recyclerView = viewGroup.findViewById(R.id.recyclerview_sitter_profile_story_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        
        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        
        getReservelist();

        return viewGroup;
    }

    // 펫시터의 user_id를 사용하여 펫시터의 예약 목록을 조회한다.
    private void getReservelist() {

        db.collection("reserve")
                .whereEqualTo("sitter_id", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {

                                String reserve_id = document.getId();

                                // 해당 예약_id의 스토리 리스트를 조회하는 메소드를 호출한다.
                                getStorylist(reserve_id);
                            }

                        }
                        else
                        {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });

    }
    // 해당 예약_id의 스토리 리스트를 조회하는 메소드
    private void getStorylist(String reserve_id)
    {
        db.collection("reserve").document(reserve_id).collection("story_list")
                .orderBy("timestamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                ListViewItem_storylist data = new ListViewItem_storylist();


                                String story_id = document.getId();
                                String title = document.getString("title");
                                String content = document.getString("content");
                                String image_num = document.getString("image_num");
                                Date timestamp = document.getDate("timestamp");

                                // 조회한 스토리 리스트 정보를 어뎁터에 추가한다.
                                data.setReserve_id(reserve_id);
                                data.setStory_id(story_id);
                                data.setStory_title(title);
                                data.setStory_content(content);
                                data.setImage_num(image_num);
                                data.setTimestamp(timestamp);

                                adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });
    }

    // 스토리 목록(리사이클러뷰) 아이템 클릭 메소드
    // 해당 스토리의 상세정보를 출력하고 스토리 삭제, 수정 기능을 제공하는 액티비티를 호출한다.
    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

        ListViewItem_storylist data = (ListViewItem_storylist) adapter.getItem(position);

        Activity activity = getActivity();
        Intent intent = new Intent(activity, activity_upload_story.class);

        intent.putExtra("callFrom", true);

        intent.putExtra("reserve_id", data.getReserve_id());
        intent.putExtra("story_id", data.getStory_id());

        intent.putExtra("story_title", data.getStory_title());
        intent.putExtra("story_content", data.getStory_content());
        intent.putExtra("story_image_num", data.getImage_num());
        intent.putExtra("story_timestamp", DateString.DateToString(data.getTimestamp()));

        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }



}