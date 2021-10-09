package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DateString;
import com.example.myapplication.ListViewItem.ListViewItem_search_estimate;
import com.example.myapplication.OnCustomClickListener;
import com.example.myapplication.R;
import com.example.myapplication.ViewHolder.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class activity_sitter_estimate extends AppCompatActivity implements OnCustomClickListener {

    private static final int REQUEST_CODE = 0;

    private FirebaseAuth auth;
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

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerview_search_estimate);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);


        getEstimateList();
    }

    // 리사이클러뷰 (견적서 목록) 아이템 클릭 메소드
    @Override
    public void onItemClick(View view, int position) {

        ListViewItem_search_estimate data = (ListViewItem_search_estimate)adapter.getItem(position);

        String estimate_id = data.getEstimate_id();
        String user_id = data.getUser_id();
        String pet_id = data.getPet_id();
        String info = data.getInfo();
        String price = data.getPrice();
        String datetime = DateString.DateToString(data.getDatetime());

        // 해당 견적서에 대한 상세 정보를 출력하는 액티비티를 실행.
        // 해당 견적서의 기본 정보를 intent에 삽입하여 전송.

        Intent intent = new Intent(activity_sitter_estimate.this, activity_sitter_estimate_detail.class);
        intent.putExtra("callFrom", 0);
        intent.putExtra("estimate_id", estimate_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("pet_id", pet_id);
        intent.putExtra("info", info);
        intent.putExtra("price", price);
        intent.putExtra("datetime", datetime);

        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }

    // 견적서 목록을 조회하기 위한 메소드
    private void getEstimateList()
    {
        // orderBy를 timestamp로 설정하여 시간순으로 정렬하여 조회한다.
        db.collection("estimate")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String user_id = document.getString("uid");

                                // 본인의 견적서인 경우 무시한다.
                                if(user_id.equals(auth.getUid()))
                                {
                                    continue;
                                }

                                ListViewItem_search_estimate data = new ListViewItem_search_estimate();

                                String estimate_id = document.getId();
                                String address = document.getString("address");
                                String price = document.getString("price");
                                String pet_age = document.getString("pet_age");
                                String species = document.getString("species");
                                String species_detail = document.getString("species_detail");
                                String pet_id = document.getString("pet_id");
                                String info = document.getString("info");
                                Date datetime = document.getDate("datetime");


                                // 조회한 견적서 정보를 어뎁터에 추가한다.
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
                                Log.d("", document.getId() + " => " + document.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 견적서 상세정보에서 해당 견적서를 수락한 경우 해당 견적서 db를 삭제하기 때문에
        // 리사이클러뷰를 초기화하고 다시 목록을 불러온다.
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            adapter.clear();
            getEstimateList();
        }

        // 해당 견적서의 역제안서를 작성하고 나서 해당 액티비티를 종료한다.
       if(requestCode == REQUEST_CODE && resultCode == RESULT_CANCELED)
        {
            finish();
        }

    }

}