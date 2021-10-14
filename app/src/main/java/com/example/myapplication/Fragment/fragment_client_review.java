package com.example.myapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.activity_upload_review;
import com.example.myapplication.ListViewItem.ListViewItem_reviewlist;
import com.example.myapplication.OnCustomClickListener;
import com.example.myapplication.R;
import com.example.myapplication.ViewHolder.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class fragment_client_review extends Fragment implements OnCustomClickListener {

    private static final int REQUEST_CODE_2 = 1;

    ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_client_review, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        recyclerView = viewGroup.findViewById(R.id.recyclerview_reviewlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnCustomClickListener) this);


        getreviewlist();

        return viewGroup;
    }
    // 고객이 작성한 후기를 조회하는 메소드
    private void getreviewlist() {
        db.collection("review")
                .whereEqualTo("client_id", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                ListViewItem_reviewlist data = new ListViewItem_reviewlist();

                                String review_id = document.getId();
                                String sitter_id = document.getString("sitter_id");
                                String sitter_name = document.getString("sitter_name");
                                double rating = document.getDouble("rating");
                                String title = document.getString("title");
                                String content = document.getString("content");

                                // 조회한 후기 정보를 어뎁터에 추가한다.
                                data.setReview_id(review_id);
                                data.setUser_id(sitter_id);
                                data.setUser_name(sitter_name);
                                data.setRating(rating);
                                data.setReview_title(title);
                                data.setReview_content(content);

                                adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // 후기 목록(리사이클러뷰) 아이템 클릭 메소드
    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

        // 리뷰 수정과 삭제가 가능한 액티비티를 호출한다.
        // intent에 관련 정보를 삽입하여 전송한다.

        ListViewItem_reviewlist data = (ListViewItem_reviewlist) adapter.getItem(position);

        Activity activity = getActivity();
        Intent intent = new Intent(activity, activity_upload_review.class);

        intent.putExtra("callFrom", true);
        intent.putExtra("user_id", data.getUser_id());
        intent.putExtra("user_name", data.getUser_name());

        intent.putExtra("review_id", data.getReview_id());
        intent.putExtra("review_title", data.getReview_title());
        intent.putExtra("review_content", data.getReview_content());
        intent.putExtra("review_rating", data.getRating());

        activity.startActivityForResult(intent, REQUEST_CODE_2);

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }

}