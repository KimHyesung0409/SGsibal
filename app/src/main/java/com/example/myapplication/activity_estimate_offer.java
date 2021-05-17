package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class activity_estimate_offer extends AppCompatActivity implements OnCustomClickListener {

    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private String estimate_id;
    private String pet_id;
    private String uid;
    private String datetime;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_offer);

        Intent intent = getIntent();

        estimate_id = intent.getStringExtra("estimate_id");
        pet_id = intent.getStringExtra("pet_id");
        uid = intent.getStringExtra("uid");
        info = intent.getStringExtra("info");
        datetime = intent.getStringExtra("datetime");

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerview_estimate_offer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        getOfferList();
    }

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

        ListViewItem_estimate_offer data = (ListViewItem_estimate_offer) adapter.getItem(position);

        Intent intent = new Intent(this, activity_sitter_estimate_detail.class);
        intent.putExtra("callFrom",2);

        intent.putExtra("estimate_id", estimate_id);
        intent.putExtra("offer_id", data.getOffer_id());
        intent.putExtra("user_id", uid);
        intent.putExtra("offer_user_id", data.getUser_id());
        intent.putExtra("pet_id", pet_id);
        intent.putExtra("price", data.getPrice());
        intent.putExtra("appeal", data.getAppeal());
        intent.putExtra("info", info);
        intent.putExtra("datetime", datetime);

        startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }

    private void getOfferList()
    {
        db.collection("estimate").document(estimate_id).collection("offer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                ListViewItem_estimate_offer data = new ListViewItem_estimate_offer();

                                String offer_id = document.getId();
                                String appeal = document.getString("appeal");
                                String price = document.getString("price");
                                String user_id = document.getString("user_id");
                                Date timestamp = document.getDate("timestamp");

                                data.setOffer_id(offer_id);
                                data.setAppeal(appeal);
                                data.setPrice(price);
                                data.setUser_id(user_id);
                                data.setTimestamp(timestamp);

                                adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {

                        }
                    }
                });
    }

}