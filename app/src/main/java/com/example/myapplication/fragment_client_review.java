package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class fragment_client_review extends Fragment implements OnCustomClickListener {

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
    private void getreviewlist() {
        db.collection("users").document(uid).collection("review_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                ListViewitem_reviewlist data = new ListViewitem_reviewlist();
                                String review_title = document.getString("review_title");
                                String review_content = document.getString("review_content");

                                data.setReview_title(review_title);
                                data.setReview_content(review_content);

                               // adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            Log.d("","Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}