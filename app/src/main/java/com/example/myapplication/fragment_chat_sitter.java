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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class fragment_chat_sitter extends Fragment implements OnCustomClickListener {

    ViewGroup viewGroup;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String uid;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_chat_sitter, container, false);

        db = FirebaseFirestore.getInstance(); // 파이어스토어 객체
        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증객체
        uid = auth.getUid(); // 유저 id

        recyclerView = viewGroup.findViewById(R.id.recyclerview_chatroom);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        getReserveList();

        return viewGroup;
    }

    private void getReserveList()
    {
        ListViewItem_chatroom data = new ListViewItem_chatroom();

        db.collection("reserve")
                .whereEqualTo("sitter_id", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String client_id = (String)document.get("client_id");
                                String chatroom = (String)document.get("chatroom");
                                String opponent_name;


                                data.setOpponent_id(client_id);
                                opponent_name = (String)document.get("client_name");


                                data.setOpponent_name(opponent_name);
                                data.setChatroom(chatroom);

                                adapter.addItem(data);
                                adapter.notifyItemChanged(adapter.getItemCount() - 1);

                                Log.d("", "예약 리스트 : " + document.getId());
                            }
                        } else {

                        }
                    }
                });

    }

    @Override
    public void onItemClick(View view, int position) {

        ListViewItem_chatroom item = (ListViewItem_chatroom)adapter.getItem(position);

        Activity activity = getActivity();

        Intent intent = new Intent(activity, activity_chatroom.class);

        intent.putExtra("opponent_id", item.getOpponent_id());
        intent.putExtra("opponent_name", item.getOpponent_name());
        intent.putExtra("chatroom", item.getChatroom());

        activity.startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}