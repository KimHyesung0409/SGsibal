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

        // ?????? ?????? ?????? ??????.
        geoPoint = LoginUserData.getGeoPoint();

        lat = geoPoint.getLatitude();
        lon = geoPoint.getLongitude();

        center = new GeoLocation(lat, lon);

        distance = 100000.0;

        //db ?????? ???????????? ????????????.
        getEntrust();

        return viewGroup;
    }

    // ?????? ????????? ???????????? ????????????
    // ??????????????? ????????????.
    private void getEntrust()
    {

        // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
        // 'bounds'??? ?????? ??? ??????????????? ??????/??? ?????? ????????????.
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // ??? ?????? ?????? ??????. 9?????? ????????? ??? ??? ??????.
        // depending on overlap, but in most cases there are 4.
        // ????????? ?????? ????????? 9?????? ????????? ??? ??? ????????? ???????????? 4???.
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
        // ?????? ????????? ????????? ??????????????? ????????? ??????.
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

                                // ????????? ????????? ????????? ?????? ????????????.
                                if (user_id.equals(auth.getUid())) {
                                    continue;
                                }

                                GeoPoint geoPoint = doc.getGeoPoint("geoPoint");

                                double lat = geoPoint.getLatitude();
                                double lng = geoPoint.getLongitude();

                                // We have to filter out a fews false positive due to GeoHash
                                // GeoHash ??? ?????? ????????? ??????????????? ????????? ????????????.
                                // accuracy, but most will match
                                // ???????????? ??????????????????.
                                // ??? ?????? ????????? ????????? ???????????? ????????? ???????????? ???????????? ????????????.
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= distance)
                                {
                                    // ??????????????? ???????????? ?????? ???????????? ????????? ??????.
                                    DocForDistance docForDistance = new DocForDistance();
                                    // ?????? ?????? ????????? ????????? ????????? ?????? ????????? set??????
                                    docForDistance.setDocumentSnapshot(doc);
                                    docForDistance.setDistance(distanceInM);
                                    // ???????????? ????????????.
                                    matchingDocs.add(docForDistance);

                                }

                            }
                        }

                        // ??????????????? ??????
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

                            // ?????? ????????? ????????? ???????????? ????????????.

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


    // ?????? ??????(??????????????????) ????????? ?????? ?????????
    // ?????? ????????? ??????????????? ???????????? ??????????????? ????????????.
    // ?????? ????????? intent??? ???????????? ????????????.
    @Override
    public void onItemClick(View view, int position) throws InterruptedException {
        ListViewItem_reserve_entrust item = (ListViewItem_reserve_entrust) adapter.getItem(position);

        String images_num = item.getImages_num();
        String entrust_detail_price_str = item.getPrice();
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

        activity.startActivityForResult(intent, activity_reserve_visit.REQUEST_CODE_5);
    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}