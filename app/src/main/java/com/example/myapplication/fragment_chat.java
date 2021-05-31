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

public class fragment_chat extends Fragment implements OnCustomClickListener {

    ViewGroup viewGroup;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String uid;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_chat, container, false);

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

    /*
       - 기존db는 reserve 컬렉션에 채팅방id, 고객id, 펫시터id 로만 구성되어 있었다.
       - 하지만 이렇게 구성하는 경우 db에서 예약정보를 조회하고
       - 다시 상대방의 uid를 사용하여 상대방의 이름을 얻게된다.
       - 따라서 조회를 2번 하게 된다. 예약이 많을경우 db조회수가 2n으로 늘어나게 된다.
       - 구글에서 제공하는 cloud firestore, realtime database는 join쿼리를 사용할 수 없다.
       - 따라서 공간이 낭비되더라도 예약정보에  채팅방id, 고객id, 펫시터id 이 세 가지 뿐만아니라
       - 고객_이름, 펫시터_이름 도 추가해서 db조회 빈도를 낮추는 방법이 더 좋을 것 같다.
     */

    // 예약 db에서 해당 고객의 user_id로 조회하는 메소드.
    // 조회된 결과는 고객 입장에서의 예약 이므로 채팅의 대상은 펫시터이다.
    private void getReserveList()
    {
        ListViewItem_chatroom data = new ListViewItem_chatroom();

        db.collection("reserve")
                .whereEqualTo("client_id", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String sitter_id = (String)document.get("sitter_id");
                                String chatroom = (String)document.get("chatroom");
                                String opponent_name;

                                // 조회한 채팅 상대 정보를 어뎁터에 추가한다.
                                data.setOpponent_id(sitter_id);
                                opponent_name = (String)document.get("sitter_name");

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

    // 채팅방 목록 아이템 클릭 메소드로
    // 해당 상대방과의 채팅 내역을 출력해주는 액티비티를 호출한다.
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