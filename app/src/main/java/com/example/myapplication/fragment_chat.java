package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class fragment_chat extends Fragment implements AdapterView.OnItemClickListener {

    ViewGroup viewGroup;
    private ListView listview;
    private ListViewAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String uid;
    private ListViewItem_chatroom data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_chat, container, false);

        db = FirebaseFirestore.getInstance(); // 파이어스토어 객체
        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증객체
        uid = auth.getUid(); // 유저 id

        listview = (ListView)viewGroup.findViewById(R.id.listview_chatroom);
        adapter = new ListViewAdapter();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

        getReserveList();

        return viewGroup;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ListViewItem_chatroom chatroom_data = (ListViewItem_chatroom) parent.getItemAtPosition(position);

        Activity activity = getActivity();

        Intent intent = new Intent(activity, activity_chatroom.class);

        intent.putExtra("opponent_id", chatroom_data.getOpponent_id());
        intent.putExtra("opponent_name", chatroom_data.getOpponent_name());
        intent.putExtra("chatroom", chatroom_data.getChatroom());

        activity.startActivity(intent);

    }

    private void getReserveList()
    {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // 예약 리스트의 하위 항목들은 null 값을 가지기 때문에 Nullable로 두었다.
                        Map<String, Nullable> map = new HashMap<>();
                        map = (Map)document.get("reserve_list");
                        //키 집합을 가져온다.
                        Set<String> reserve_list = (Set)map.keySet();

                        Iterator<String> iterator = reserve_list.iterator();

                        while(iterator.hasNext())
                        {
                            String reserve_id = iterator.next();

                            getReservedata(reserve_id);
                        }

                        Log.d("", "DocumentSnapshot data: ");
                    } else {
                        Log.d("", "No such document");
                    }
                } else {
                    Log.d("", "get failed with ", task.getException());
                }
            }
        });


    }

    private void getReservedata(String reserve_id)
    {
        data = new ListViewItem_chatroom();

        // 예약 정보를 조회
        DocumentReference docRef = db.collection("reserve").document(reserve_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String client_id = (String)document.get("client");
                        String sitter_id = (String)document.get("sitter");
                        String chatroom = (String)document.get("chatroom");

                        // 내가 클라이언트 인지 확인하는 작업
                        if(uid.equals(client_id))
                        {
                            data.setOpponent_id(sitter_id);
                        }
                        else // 클라이언트가 아니므로 시터.
                        {
                            data.setOpponent_id(client_id);
                        }

                        data.setChatroom(chatroom);

                        getUserName();

                        Log.d("", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("", "No such document");
                    }
                } else {
                    Log.d("", "get failed with ", task.getException());
                }
            }
        });

    }

    private void getUserName()
    {
        // 상대방의 uid를 이용해서 상대방의 이름을 조희
        DocumentReference docRef = db.collection("users").document(data.getOpponent_id());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String opponent_name = (String)document.get("name");
                        data.setOpponent_name(opponent_name);

                        adapter.addItem(data);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("", "No such document");
                    }
                } else {
                    Log.d("", "get failed with ", task.getException());
                }
            }
        });
    }

}