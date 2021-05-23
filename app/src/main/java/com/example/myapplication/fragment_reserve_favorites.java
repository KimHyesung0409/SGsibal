package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class fragment_reserve_favorites extends Fragment implements OnCustomClickListener{

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

        getfavoriteslist();

        return viewGroup;
    }

    private void getfavoriteslist()
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