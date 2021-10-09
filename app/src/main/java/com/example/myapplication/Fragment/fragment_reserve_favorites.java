package com.example.myapplication.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.activity_popup_user_data;
import com.example.myapplication.Activity.activity_reserve_visit;
import com.example.myapplication.Gender;
import com.example.myapplication.ListViewItem.ListViewItem_searchlist;
import com.example.myapplication.OnCustomClickListener;
import com.example.myapplication.R;
import com.example.myapplication.ViewHolder.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.fragment.app.Fragment;

public class fragment_reserve_favorites extends Fragment implements OnCustomClickListener {

    private static final double RATINGBAR_GONE = 100.0;

    ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_favorites, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView=viewGroup.findViewById(R.id.recyclerview_reserve_favorites);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnCustomClickListener) this);

        getFavoritesList();

        return viewGroup;
    }

    // 즐겨찾기 목록 조회 메소드
    private void getFavoritesList()
    {

        db.collection("users").document(auth.getUid()).collection("favorites")
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
                                ListViewItem_searchlist data = new ListViewItem_searchlist();

                                String user_id = document.getString("user_id");
                                String user_name = document.getString("name");
                                String birth = document.getString("birth");
                                String gender = Gender.getGender(document.getBoolean("gender"));
                                double rating = RATINGBAR_GONE;

                                // 조회한 즐겨찾기 정보를 어뎁터에 추가한다.
                                data.setUser_id(user_id);
                                data.setUser_name(user_name);
                                data.setBirth(birth);
                                data.setGender(gender);
                                data.setRating(rating);

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

    // 즐겨찾기 목록(리사이클러뷰) 아이템 클릭 메소드
    // 해당 펫시터의 상세 정보를 출력하고 예약, 즐겨찾기 삭제 기능을 제공하는 액티비티를 호출한다.
    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

        ListViewItem_searchlist data = (ListViewItem_searchlist)adapter.getItem(position);

        Activity activity = getActivity();

        Intent intent = new Intent(activity, activity_popup_user_data.class);
        intent.putExtra("user_id", data.getUser_id());
        intent.putExtra("callFrom", 2);
        activity.startActivityForResult(intent, activity_reserve_visit.REQUEST_CODE_4);

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}