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

    private static final int REQUEST_CODE_3 = 2;
    private static final int REQUEST_CODE_4 = 3;

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

    // 리사이클러뷰 아이템 클릭 메소드
    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

        ViewGroup parent = (ViewGroup) view.getParent();
        Activity activity = getActivity();
        Intent intent;

        // 고객 예약 현황에는 예약 목록과 고객이 작성한 견적서 목록이 나오게 된다.
        // 따라서 클릭한 아이템의 부모가 예약 목록을 출력하는 리사이클러뷰 인지
        // 견적서 목록을 출력하는 리사이클러뷰 인지 알아내야 한다.
        switch (parent.getId())
        {
            // 부모가 예약 목록을 출력하는 리사이클러뷰인 경우
            // 예약 상세 정보를 출력하는 액티비티를 호출한다.
            case R.id.reserve_check :

                ListViewItem_reserve reserve_data = (ListViewItem_reserve)adapter_reserve.getItem(position);

                intent = new Intent(activity, activity_reserve_detail.class);

                //callFrom true = 고객 예약현황, false = 펫시터 예약현황
                intent.putExtra("callFrom", true);
                intent.putExtra("reserve_id", reserve_data.getReserve_id());
                intent.putExtra("user_id", reserve_data.getUser_id());
                intent.putExtra("pet_id", reserve_data.getPet_id());

                activity.startActivityForResult(intent, REQUEST_CODE_3);

                break;
            // 부모가 견적서 목록을 출력하는 리사이클러뷰인 경우
            // 역제안서 목록을 출력하는 액티비티를 호출한다.
            case R.id.estimate_check :

                ListViewItem_search_estimate estimate_data = (ListViewItem_search_estimate) adapter_estimate.getItem(position);

                intent = new Intent(activity, activity_estimate_offer.class);
                intent.putExtra("estimate_id", estimate_data.getEstimate_id());
                intent.putExtra("pet_id", estimate_data.getPet_id());
                intent.putExtra("uid", estimate_data.getUser_id());
                intent.putExtra("info", estimate_data.getInfo());
                intent.putExtra("datetime", DateString.DateToString(estimate_data.getDatetime()));

                activity.startActivityForResult(intent, REQUEST_CODE_4);

                break;
        }

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }

    // 예약 목록을 조회하는 메소드
    // 예약 db에서 고객의 user_id를 조건으로 조회한다.
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

                                String reserve_id = document.getId();
                                String sittername = document.getString("sitter_name");
                                Date datetime = document.getDate("datetime");
                                String petname = document.getString("pet_name");
                                String sitter_id = document.getString("sitter_id");
                                String pet_id = document.getString("pet_id");

                                // 조회한 예약 정보를 어뎁터에 추가한다.
                                data.setReserve_id(reserve_id);
                                data.setUser_name(sittername);
                                data.setDatetime(DateString.DateToString(datetime));
                                data.setPetname(petname);
                                data.setUser_id(sitter_id);
                                data.setPet_id(pet_id);


                                adapter_reserve.addItem(data);
                                Log.d("", document.getId() + " => " + document.getData());
                            }
                            adapter_reserve.notifyDataSetChanged();
                            // FireStore db관련 메소드는 임의의 Thread를 사용하기 때문에
                            // 예약, 견적서 목록을 동시에 조회하여 리사이클러뷰에 추가하는 경우
                            // 에러가 발생하기 때문에 예약 조회가 완료되면 견적서를 조회해야한다.
                            getEstimate_list();
                        }
                        else
                        {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // 견적서 목록을 조회하는 메소드
    // 견적서 db에서 고객의 user_id를 조건으로 조회한다.
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

                                // 조회한 견적서 정보를 어뎁터에 추가한다.
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

    // 리사이클러뷰 리프레쉬 메소드
    public void refresh()
    {
        adapter_reserve.clear();
        adapter_estimate.clear();
        getReserve_list();
    }

}