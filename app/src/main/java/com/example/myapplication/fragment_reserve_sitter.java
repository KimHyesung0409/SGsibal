package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
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

import java.util.Date;


public class fragment_reserve_sitter extends Fragment implements OnCustomClickListener {

    ViewGroup viewGroup;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uid;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_reserve_sitter, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        recyclerView = viewGroup.findViewById(R.id.reserve_check);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);


        getReserve_list();


        return viewGroup;
    }

    private void getReserve_list() {
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
                                ListViewItem_reserve data = new ListViewItem_reserve();

                                String reserve_id = document.getId();
                                String clientname = document.getString("client_name");
                                Date datetime = document.getDate("datetime");
                                String petname = document.getString("pet_name");
                                String client_id = document.getString("client_id");
                                String pet_id = document.getString("pet_id");

                                data.setReserve_id(reserve_id);
                                data.setUser_name(clientname);
                                data.setDatetime(DateString.DateToString(datetime));
                                data.setPetname(petname);
                                data.setUser_id(client_id);
                                data.setPet_id(pet_id);

                                adapter.addItem(data);
                                Log.d("", document.getId() + " => " + document.getData());
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    public void onItemClick(View view, int position) throws InterruptedException {
        ListViewItem_reserve reserve_data = (ListViewItem_reserve)adapter.getItem(position);

        Activity activity = getActivity();
        Intent intent = new Intent(activity, activity_reserve_detail.class);

        //callFrom true = 고객 예약현황, false = 펫시터 예약현황
        intent.putExtra("callFrom", false);
        intent.putExtra("reserve_id", reserve_data.getReserve_id());
        intent.putExtra("user_id", reserve_data.getUser_id());
        intent.putExtra("pet_id", reserve_data.getPet_id());

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}