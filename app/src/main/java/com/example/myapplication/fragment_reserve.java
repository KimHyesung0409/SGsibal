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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class fragment_reserve extends Fragment {

    ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve, container, false);
        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        recyclerView = viewGroup.findViewById(R.id.reserve_check);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

        getReserve_list();

        return viewGroup;
    }

    private void getReserve_list() {
        db.collection("users").document(uid).collection("reserve_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String reserve_id = document.getId();

                                getReserveData(reserve_id);

                                Log.d("---------", "읽어온 문서: " + document.getId() + document.getData());
                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getReserveData(String reserve_id)
    {

        db.collection("reserve").document(reserve_id)
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {

                        ListViewItem_reserve data = new ListViewItem_reserve();

                        String sittername = document.getString("sitter_name");
                        Date datetime = document.getDate("datetime");
                        String petname = document.getString("pet_name");

                        data.setSittername(sittername);
                        data.setDatetime(DateString.DateToString(datetime));
                        data.setPetname(petname);

                        adapter.addItem(data);
                        adapter.notifyDataSetChanged();
                        Log.d("", "DocumentSnapshot data: " + document.getData());
                    }
                    else
                    {
                        Log.d("", "No such document");
                    }
                }
                else
                {
                    Log.d("", "get failed with ", task.getException());
                }
            }
        });


    }
    /*
    public void refreshListView()
    {
        System.out.println("리플레시");
        adapter.clear();
        // 안해주니까 이상해짐.
        adapter.notifyDataSetChanged();
        getReservecheck();
    }
    */

}