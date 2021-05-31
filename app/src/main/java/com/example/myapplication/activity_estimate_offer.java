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

    private static final int REQUEST_CODE = 0;

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

        // 견적서 정보를 intent를 통해 전달받는다.
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

    // 역제안서 리사이클러뷰 아이템 클릭 메소드
    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

        ListViewItem_estimate_offer data = (ListViewItem_estimate_offer) adapter.getItem(position);

        // 역제안서의 상세정보를 표시하는 액티비티를 실행시킨다.
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

        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }

    // 해당 견적서에 대한 역제안서 목록을 db에서 조회하는 메소드
    private void getOfferList()
    {
        // 견적서 - 해당견적서_id - 역제안 컬렉션에 있는 모든 역제안서를 조회한다.
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

                                // 리사이클러뷰에 출력하기 위해 역제안서 정보를 리사이클러뷰 어뎁터에 추가한다.
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

    // 역제안을 수락하는 경우 자동으로 예약현황 화면으로 돌아가기 위해 해당 액티비티를 종료한다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }

    }

}