package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class fragment_reserve_search extends Fragment {

    ViewGroup viewGroup;
    private Context context;
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private RecyclerView.ViewHolder viewHolder;
    private Object ListView;
    private EditText search_write;
    private RecyclerViewAdapter adapter;
    private String uid;
    private EditText editText_search;
    private FirebaseAuth auth;
    private FirebaseFirestore db;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_search, container, false);
        FirebaseAuth auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체
         uid = auth.getUid();

        editText_search= (EditText)viewGroup.findViewById(R.id.search_write);
        recyclerView=viewGroup.findViewById(R.id.recyclerview_searchlist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnCustomClickListener) this);

        getsearchlist();
        


        return viewGroup;
    }

    private void getsearchlist() {
        db.collection("user").document(uid).collection("searchlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ListViewItem_searchlist data = new ListViewItem_searchlist();

                                String sitter_name = document.getString("name");
                                String sitter_age = document.getString("age");

                                //adapter.addItem(add);
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            Log.d("", "Error getting documents: ", task.getException());
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


