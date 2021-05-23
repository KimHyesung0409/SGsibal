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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class fragment_reserve_entrust extends Fragment implements OnCustomClickListener {
    ViewGroup viewGroup;

    private FirebaseFirestore db;
    private static final int IMAGE_NUM = 5;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_entrust, container, false);

        recyclerView = viewGroup.findViewById(R.id.recyclerview_reserve_entrust);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        db = FirebaseFirestore.getInstance();

        CollectionReference docRef = db.collection("entrust_list");

        db.collection("entrust_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String images_num = document.getString("images_num");

                                ListViewItem_reserve_entrust data = new ListViewItem_reserve_entrust();

                                String entrust_id = document.getId();
                                String address = document.getString("address");
                                String address_detail = document.getString("address_detail");
                                String user_name = document.getString("name");
                                String price = document.getString("price");
                                String title = document.getString("title");

                                // 초반에 작성한거라 일관성이 떨어졌네요.
                                // 위탁 리스트에 데이터보면 uid가 있는데 이게 해당 위탁을 등록한 유저의 id입니다.

                                String user_id = document.getString("uid");

                                data.setImages_num(images_num);
                                data.setEntrust_id(entrust_id);
                                data.setAddress(address);
                                data.setAddress_detail(address_detail);
                                data.setUser_name(user_name);
                                data.setPrice(price);
                                data.setTitle(title);

                                data.setUser_id(user_id);

                                adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("에러 : " , "Error getting documents : ", task.getException());
                        }
                    }
                });
        return viewGroup;
    }

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {
        ListViewItem_reserve_entrust item = (ListViewItem_reserve_entrust) adapter.getItem(position);

        String images_num = item.getImages_num();
        String entrust_detail_price_str = item.getPrice();
        //String entrust_detail_intro_str = item.getIntro();
        //String entrust_detail_caution_str = item.getCaution();
        String entrust_id = item.getEntrust_id();
        String user_id = item.getUser_id();
        String user_name = item.getUser_name();

        String address = item.getAddress();
        String address_detail = item.getAddress_detail();

        Activity activity = getActivity();
        Intent intent = new Intent(activity, activity_reserve_entrust_detail.class);

        intent.putExtra("images", images_num);
        intent.putExtra("price", entrust_detail_price_str);
        intent.putExtra("entrust_id", entrust_id);
        intent.putExtra("user_id", user_id);
        intent.putExtra("user_name", user_name);

        intent.putExtra("address", address);
        intent.putExtra("address_detail", address_detail);

        System.out.println("줍자웆ㅂㅇ : " + address_detail  );

        //intent.putExtra("intro", entrust_detail_intro_str);
        //intent.putExtra("caution", entrust_detail_caution_str);

        // 예약을 신청하고 메인화면으로 돌아가기 위해서는
        // 지금 이 프래그먼트 즉 fragment_reserve_entrust가 자신을 출력해주는 액티비티인 activity_reserve_visit 에게
        // 위탁 상세페이지 즉 activity_reserve_entrust_detail에 리퀘스트 코드를 주고 startActivityForResult로 실행시켜
        // 해당 페이지가 종료되는 것을 onActivityResult 메소드로 인식하여 activity_reserve_visit 본인을 finish(종료) 시키면 된다.
        // 주의할 점은 startActivityForResult 메소드 앞에 프래그먼트를 출력하는 activity를 명시해야한다.
        // 그래야 해당 액티비티가 onActivityResult로 결과를 받을 수 있다. 명시를 안하면 어떤 액티비티가 실행한 것인지 모른다.
        activity.startActivityForResult(intent, activity_reserve_visit.REQUEST_CODE_5);
    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}