package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class activity_reserve_entrust extends AppCompatActivity implements OnCustomClickListener {

    private FirebaseFirestore db;
    private static final int IMAGE_NUM = 5;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_entrust);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);

        recyclerView = findViewById(R.id.recyclerview_reserve_entrust);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnCustomClickListener) this);

        //파이어베이스 db
        db = FirebaseFirestore.getInstance();

        //db 에서 데이터를 가져온다.
        CollectionReference docRef = db.collection("entrust_list");


        db.collection("entrust_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String images_num = document.getString("images_num");

                                ListViewItem_reserve_entrust data = new ListViewItem_reserve_entrust();

                                String entrust_id = document.getId();
                                String address = document.getString("address");
                                String user_name = document.getString("name");
                                String price = document.getString("price");
                                String title = document.getString("title");

                                data.setEntrust_id(entrust_id);
                                data.setImages_num(images_num);
                                data.setTitle(title);
                                data.setAddress(address);
                                data.setUser_name(user_name);
                                data.setPrice(price);

                                adapter.addItem(data);

                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("에러 났어용 : ", "Error getting documents: ", task.getException());
                        }
                    }
                });

        /*
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ListViewItem_reserve_entrust data = (ListViewItem_reserve_entrust)adapterView.getItemAtPosition(i);

                int images[] = data.getImages();

                Intent intent = new Intent(activity_reserve_entrust.this,activity_reserve_entrust_detail.class);
                intent.putExtra("images", images);
                startActivity(intent);

            }
        });
        */
    }


    @Override
    public void onItemClick(View view, int position) {
        ListViewItem_reserve_entrust item = (ListViewItem_reserve_entrust) adapter.getItem(position);

        String images_num = item.getImages_num();
        String entrust_detail_price_str = item.getPrice();
        //String entrust_detail_intro_str = item.getIntro();
        //String entrust_detail_caution_str = item.getCaution();
        String entrust_id = item.getEntrust_id();

        Intent intent = new Intent(activity_reserve_entrust.this,activity_reserve_entrust_detail.class);

        intent.putExtra("images", images_num);
        intent.putExtra("price", entrust_detail_price_str);
        intent.putExtra("entrust_id", entrust_id);
        //intent.putExtra("intro", entrust_detail_intro_str);
        //intent.putExtra("caution", entrust_detail_caution_str);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}