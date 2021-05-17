package com.example.myapplication;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class activity_sitter_estimate extends AppCompatActivity implements OnCustomClickListener{

    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_estimate);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerview_search_estimate);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);


        getEstimateList();
    }

    @Override
    public void onItemClick(View view, int position) {

        ListViewItem_search_estimate data = (ListViewItem_search_estimate)adapter.getItem(position);

        String estimate_id = data.getEstimate_id();
        String user_id = data.getUser_id();
        String pet_id = data.getPet_id();
        String info = data.getInfo();
        String price = data.getPrice();
        String datetime = DateString.DateToString(data.getDatetime());

        Intent intent = new Intent(activity_sitter_estimate.this, activity_sitter_estimate_detail.class);
        intent.putExtra("callFrom", 0);
        intent.putExtra("estimate_id", estimate_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("pet_id", pet_id);
        intent.putExtra("info", info);
        intent.putExtra("price", price);
        intent.putExtra("datetime", datetime);

        startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }

    private void getEstimateList()
    {
        db.collection("estimate")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ListViewItem_search_estimate data = new ListViewItem_search_estimate();

                                String estimate_id = document.getId();
                                String address = document.getString("address");
                                String price = document.getString("price");
                                String pet_age = document.getString("pet_age");
                                String species = document.getString("species");
                                String species_detail = document.getString("species_detail");
                                String user_id = document.getString("uid");
                                String pet_id = document.getString("pet_id");
                                String info = document.getString("info");
                                Date datetime = document.getDate("datetime");

                                data.setEstimate_id(estimate_id);
                                data.setAddress(address);
                                data.setPrice(price);
                                data.setPet_age(pet_age);
                                data.setSpecies(species);
                                data.setSpecies_detail(species_detail);
                                data.setUser_id(user_id);
                                data.setPet_id(pet_id);
                                data.setInfo(info);
                                data.setDatetime(datetime);

                                adapter.addItem(data, true);
                                adapter.notifyDataSetChanged();
                                Log.d("", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}