package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class fragment_reserve_history extends Fragment implements OnCustomClickListener{

    private static final double RATINGBAR_GONE = 100.0;

    ViewGroup viewGroup;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_history, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView=viewGroup.findViewById(R.id.recyclerview_reserve_history);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnCustomClickListener) this);

        getHistory();

        return viewGroup;
    }

    private void getHistory()
    {
        db.collection("users").document(auth.getUid()).collection("history")
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
                                Date datetime = document.getDate("datetime");
                                double rating = RATINGBAR_GONE;

                                data.setUser_id(user_id);
                                data.setUser_name(user_name);
                                data.setBirth(birth);
                                data.setGender(gender);
                                data.setRating(rating);
                                data.setDatetime(datetime);

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

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

        ListViewItem_searchlist data = (ListViewItem_searchlist)adapter.getItem(position);

        Activity activity = getActivity();

        Intent intent = new Intent(activity, activity_popup_user_data.class);
        intent.putExtra("user_id", data.getUser_id());
        intent.putExtra("callFrom", 1);
        activity.startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}