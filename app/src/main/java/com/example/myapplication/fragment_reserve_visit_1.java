package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


public class fragment_reserve_visit_1 extends Fragment implements OnCustomClickListener{

    ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uid;
    private ListViewItem_petlist add;
    private static ListViewItem_petlist selected_pet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_visit_1, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        recyclerView = viewGroup.findViewById(R.id.recyclerview_petlist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        getPetList();

        return viewGroup;
    }

    // 반려동물 목록(리사이클러뷰) 아이템 클릭 메소드
    @Override
    public void onItemClick(View view, int position) {

        ListViewItem_petlist data = (ListViewItem_petlist)adapter.getItem(position);
        activity_reserve_visit activity = (activity_reserve_visit) getActivity();

        // 해당 아이템이 추가 객체일 경우
        // 반려동물 추가 액티비티를 호출한다.
        if(data == add)
        {
            Intent intent = new Intent(activity, activity_add_pet.class);
            activity.startActivityForResult(intent, activity_reserve_visit.REQUEST_CODE);
        }
        else // 아닐경우 선택한 반려동물을 저장하고 다음 과정으로 넘어간다.
        {
            selected_pet = data;
            activity.nextProgress();
        }

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {
        System.out.println("롱클릭");
    }

    // 반려동물 목록을 조회하는 메소드
    private void getPetList()
    {
        db.collection("users").document(uid).collection("pet_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ListViewItem_petlist data = new ListViewItem_petlist();

                                String pet_name = document.getString("name");
                                String pet_species = document.getString("species");
                                String pet_age = document.getString("age");
                                String pet_detail_species = document.getString("detail_species");
                                String pet_mbti = document.getString("mbti");
                                String pet_info = document.getString("info");
                                String pet_id = document.getId();

                                // 조회한 반려동물 정보를 어뎁터에 추가한다.
                                data.setName(pet_name);
                                data.setSpecies(pet_species);
                                data.setAge(pet_age);
                                data.setDetail_species(pet_detail_species);
                                data.setMbti(pet_mbti);
                                data.setInfo(pet_info);
                                data.setPet_id(pet_id);

                                adapter.addItem(data);
                            }
                            // 반려동물 추가 아이템을 마지막 항목으로 추가한다.
                            add = new ListViewItem_petlist();
                            add.setName("반려동물 추가");
                            adapter.addItem(add);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // 리사이클러뷰 리프레쉬 메소드
    public void refreshListView()
    {
        adapter.clear();
        adapter.notifyDataSetChanged();
        getPetList();
    }

    // 선택된 반려동물의 정보를 리턴하는 메소드
    public static ListViewItem_petlist getSelected_pet()
    {
        return selected_pet;
    }

    public void setSelected_pet(ListViewItem_petlist data)
    {
        selected_pet = data;
    }

}