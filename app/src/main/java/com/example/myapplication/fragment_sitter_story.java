package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class fragment_sitter_story extends Fragment implements  OnCustomClickListener {

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

        recyclerView = viewGroup.findViewById(R.id.recyclerview_storylist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        
        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        
        getStorylist();
        
        
        return viewGroup;
    }

    private void getStorylist() {
        db.collection("users").document(uid).collection("story_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                ListViewItem_storylist data = new ListViewItem_storylist();
                                String name_customer = document.getString("name_customer");
                                String name_pet = document.getString("name_pet");
                                String content_sitting = document.getString("content_sitting");

                                String story_id = document.getId();

                                data.setStory_id(story_id);

                                data.setName_customer(name_customer);
                                data.setName_pet(name_pet);
                                data.setContent_sitting(content_sitting);
                                adapter.addItem(data);
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
        ListViewItem_storylist data_story = (ListViewItem_storylist)adapter.getItem(position);
        //selected_story = data;
        String detail_tv = data_story.getContent_sitting();

        String delete_story_id = data_story.getStory_id();

        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        dlg.setTitle("스토리 세부 내용")
                .setMessage(detail_tv)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection("users").document(uid)
                                .collection("story_list").document(delete_story_id)
                                .delete();

                        Toast.makeText(getActivity(),"삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        refreshListView();
                    }
                })
                .show();

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }

    public void refreshListView()
    {
        System.out.println("리플레시");
        adapter.clear();
        // 안해주니까 이상해짐.
        adapter.notifyDataSetChanged();
        getStorylist();
    }



}