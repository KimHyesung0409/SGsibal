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

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class fragment_reserve_entrust extends Fragment implements OnCustomClickListener {
    ViewGroup viewGroup;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private static final int IMAGE_NUM = 5;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private GeoPoint geoPoint;
    private Double lat, lon;
    private GeoLocation center;
    private Double distance;

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

        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        // 초기 중심 위치 설정.
        geoPoint = LoginUserData.getGeoPoint();

        lat = geoPoint.getLatitude();
        lon = geoPoint.getLongitude();

        center = new GeoLocation(lat, lon);

        distance = 100000.0;

        //db 에서 데이터를 가져온다.
        getEntrust();

        /*
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

         */
        return viewGroup;
    }

    private void getEntrust()
    {

        // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
        // 'bounds'에 속한 각 아이템들은 시작/끝 쌍에 해당한다.
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // 각 쌍의 분리 쿼리. 9쌍의 경계가 될 수 있다.
        // depending on overlap, but in most cases there are 4.
        // 겹치는 것에 따라서 9쌍의 경계가 될 수 있지만 대부분은 4개.
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, distance);

        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = db.collection("entrust_list")
                    .orderBy("geoHash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }



        // Collect all the query results together into a single list
        // 쿼리 결과를 하나의 리스트안에 모아서 수집.
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocForDistance> matchingDocs = new ArrayList<>();

                        int num = 0;

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();

                            for (DocumentSnapshot doc : snap.getDocuments()) {

                                String user_id = doc.getString("uid");

                                // 본인꺼는 무시
                                if (user_id.equals(auth.getUid())) {
                                    continue;
                                }

                                GeoPoint geoPoint = doc.getGeoPoint("geoPoint");

                                double lat = geoPoint.getLatitude();
                                double lng = geoPoint.getLongitude();

                                // We have to filter out a fews false positive due to GeoHash
                                // GeoHash 에 의한 약간의 거짓양성을 필터링 해야한다.
                                // accuracy, but most will match
                                // 대부분은 일치하겠지만.
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= distance)
                                {
                                    DocForDistance docForDistance = new DocForDistance();

                                    docForDistance.setDocumentSnapshot(doc);
                                    docForDistance.setDistance(distanceInM);
                                    matchingDocs.add(docForDistance);

                                }

                            }
                        }

                        // 거리순으로 정렬
                        Collections.sort(matchingDocs);

                        Iterator<DocForDistance> iterator = matchingDocs.iterator();

                        while(iterator.hasNext())
                        {
                            DocForDistance docForDistance = iterator.next();

                            DocumentSnapshot documentSnapshot = docForDistance.getDocumentSnapshot();

                            String images_num = documentSnapshot.getString("images_num");

                            ListViewItem_reserve_entrust data = new ListViewItem_reserve_entrust();

                            String entrust_id = documentSnapshot.getId();
                            String address = documentSnapshot.getString("address");
                            String address_detail = documentSnapshot.getString("address_detail");
                            String user_name = documentSnapshot.getString("name");
                            String price = documentSnapshot.getString("price");
                            String title = documentSnapshot.getString("title");

                            // 초반에 작성한거라 일관성이 떨어졌네요.
                            // 위탁 리스트에 데이터보면 uid가 있는데 이게 해당 위탁을 등록한 유저의 id입니다.

                            String user_id = documentSnapshot.getString("uid");

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

                    }

                });
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