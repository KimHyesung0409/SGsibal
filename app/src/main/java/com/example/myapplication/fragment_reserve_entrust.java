package com.example.myapplication;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class fragment_reserve_entrust extends Fragment implements OnCustomClickListener {
    ViewGroup viewGroup;

    private FirebaseFirestore db;
    private static final int IMAGE_NUM = 5;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_entrust, container, false);

        recyclerView = viewGroup.findViewById(R.id.recyclerview_reserve_entrust);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        db = FirebaseFirestore.getInstance();

        CollectionReference docRef = db.collection("entrust_list");

        db.collection("entrust_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String images_num = document.getString("images_num");

                                ListViewItem_reserve_entrust data = new ListViewItem_reserve_entrust();

                                String entrust_id = document.getId();
                                String address = document.getString("address");
                                String user_name = document.getString("name");
                                String price = document.getString("price");
                                String title = document.getString("title");

                                data.setImages_num(images_num);
                                data.setEntrust_id(entrust_id);
                                data.setAddress(address);
                                data.setUser_name(user_name);
                                data.setPrice(price);
                                data.setTitle(title);

                                adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("에러 : " , "Error getting documents : ", task.getException());
                        }
                    }
                });
        return viewGroup;
    }

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {
        ListViewItem_reserve_entrust item = (ListViewItem_reserve_entrust) adapter.getItem(position);

        String images_num = item.getImages_num();
        String entrust_detail_price_str = item.getPrice();
        //String entrust_detail_intro_str = item.getIntro();
        //String entrust_detail_caution_str = item.getCaution();
        String entrust_id = item.getEntrust_id();
        String user_id = item.getUser_id();
        String user_name = item.getUser_name();

        Intent intent = new Intent(getActivity(), activity_reserve_entrust_detail.class);

        intent.putExtra("images", images_num);
        intent.putExtra("price", entrust_detail_price_str);
        intent.putExtra("entrust_id", entrust_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("user_name", user_name);
        //intent.putExtra("intro", entrust_detail_intro_str);
        //intent.putExtra("caution", entrust_detail_caution_str);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}