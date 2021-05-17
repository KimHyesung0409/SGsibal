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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class fragment_reserve extends Fragment implements OnCustomClickListener {

    ViewGroup viewGroup;
    private RecyclerView recyclerView_reserve;
    private RecyclerViewAdapter adapter_reserve;

    private RecyclerView recyclerView_estimate;
    private RecyclerViewAdapter adapter_estimate;

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

        recyclerView_reserve = viewGroup.findViewById(R.id.reserve_check);

        LinearLayoutManager linearLayoutManager_reserve = new LinearLayoutManager(getContext());
        recyclerView_reserve.setLayoutManager(linearLayoutManager_reserve);

        adapter_reserve = new RecyclerViewAdapter(getContext());
        recyclerView_reserve.setAdapter(adapter_reserve);

        adapter_reserve.setOnItemClickListener(this);

        recyclerView_estimate = viewGroup.findViewById(R.id.estimate_check);

        LinearLayoutManager linearLayoutManager_estimate = new LinearLayoutManager(getContext());
        recyclerView_estimate.setLayoutManager(linearLayoutManager_estimate);

        adapter_estimate = new RecyclerViewAdapter(getContext());
        recyclerView_estimate.setAdapter(adapter_estimate);

        adapter_estimate.setOnItemClickListener(this);

        getReserve_list();

        return viewGroup;
    }

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

        ViewGroup parent = (ViewGroup) view.getParent();
        Activity activity = getActivity();

        switch (parent.getId())
        {
            case R.id.reserve_check :

                //

                break;

            case R.id.estimate_check :

                ListViewItem_search_estimate data = (ListViewItem_search_estimate) adapter_estimate.getItem(position);

                Intent intent = new Intent(activity, activity_estimate_offer.class);
                intent.putExtra("estimate_id", data.getEstimate_id());
                intent.putExtra("pet_id", data.getPet_id());
                intent.putExtra("uid", data.getUser_id());
                intent.putExtra("info", data.getInfo());
                intent.putExtra("datetime", DateString.DateToString(data.getDatetime()));
                startActivity(intent);

                break;
        }


    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }

    private void getReserve_list() {
        db.collection("reserve")
                .whereEqualTo("client_id", uid)
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

                                String sittername = document.getString("sitter_name");
                                Date datetime = document.getDate("datetime");
                                String petname = document.getString("pet_name");

                                data.setSittername(sittername);
                                data.setDatetime(DateString.DateToString(datetime));
                                data.setPetname(petname);

                                adapter_reserve.addItem(data);
                                Log.d("", document.getId() + " => " + document.getData());
                            }
                            adapter_reserve.notifyDataSetChanged();
                            getEstimate_list();
                        }
                        else
                        {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void getEstimate_list() {
        db.collection("estimate")
                .whereEqualTo("uid", uid)
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
                                ListViewItem_search_estimate data = new ListViewItem_search_estimate();

                                String estimate_id = document.getId();
                                String pet_name = document.getString("pet_name");
                                Date timestamp = document.getDate("timestamp");
                                String species = document.getString("species");
                                String species_detail = document.getString("species_detail");
                                String price = document.getString("price");
                                String pet_age = document.getString("pet_age");
                                String pet_id = document.getString("pet_id");
                                String user_id = document.getString("uid");
                                String info = document.getString("info");
                                Date datetime = document.getDate("datetime");

                                data.setEstimate_id(estimate_id);
                                data.setPet_name(pet_name);
                                data.setDatetime(timestamp);
                                data.setSpecies(species);
                                data.setSpecies_detail(species_detail);
                                data.setPet_age(pet_age);
                                data.setPrice(price);
                                data.setPet_id(pet_id);
                                data.setUser_id(user_id);
                                data.setInfo(info);
                                data.setDatetime(datetime);

                                adapter_estimate.addItem(data, false);
                                Log.d("", document.getId() + " => " + document.getData());
                            }
                            adapter_estimate.notifyDataSetChanged();
                        }
                        else
                        {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}