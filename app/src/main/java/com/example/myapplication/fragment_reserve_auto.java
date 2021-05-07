package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class fragment_reserve_auto extends Fragment implements RadioGroup.OnCheckedChangeListener, OnCustomClickListener {

    ViewGroup viewGroup;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private RadioGroup radioGroup_search_radius;

    // 임시 TextView
    private TextView temp_user_name, temp_lat, temp_lon, temp_geohash, temp_address, temp_distance;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    // 임시 변수
    private Double lat, lon;
    private Double distance = 1000.0; // m 단위 기본이 1km 이므로 1000.0
    private GeoLocation center;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_auto, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = auth.getUid();

        radioGroup_search_radius = (RadioGroup)viewGroup.findViewById(R.id.radioGroup_search_radius);
        radioGroup_search_radius.setOnCheckedChangeListener(this);

        recyclerView = viewGroup.findViewById(R.id.recyclerview_reserve_auto);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnCustomClickListener) this);

        temp_user_name = (TextView)viewGroup.findViewById(R.id.temp_user_name);
        temp_lat = (TextView)viewGroup.findViewById(R.id.temp_lat);
        temp_lon = (TextView)viewGroup.findViewById(R.id.temp_lon);
        temp_geohash = (TextView)viewGroup.findViewById(R.id.temp_geohash);
        temp_address = (TextView)viewGroup.findViewById(R.id.temp_address);
        temp_distance = (TextView)viewGroup.findViewById(R.id.temp_distance);

        getUserGeo();

        return viewGroup;
    }

    // 임시 메소드
    private void getUserGeo()
    {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        GeoPoint geoPoint = document.getGeoPoint("geoPoint");

                        lat = geoPoint.getLatitude();
                        lon = geoPoint.getLongitude();

                        center = new GeoLocation(lat, lon);

                        GetNearBy();

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

    private void GetNearBy()
    {
        // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // depending on overlap, but in most cases there are 4.
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, distance);

        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = db.collection("users")
                    .orderBy("geoHash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }

        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {

                                if(doc.getId().equals(uid))
                                {
                                    continue;
                                }

                                ArrayList<String> care_list = (ArrayList<String>)doc.get("care_list");

                                // 해당 펫시터의 케어 가능한 동물 종류중에 내가 원하는 동물 종류가 없으면.
                                if(!isCareable(care_list, "개")) // <- 여기에 반려동물 종류 기입.
                                {
                                    // 무시.
                                    continue;
                                }


                                GeoPoint geoPoint = doc.getGeoPoint("geoPoint");

                                double lat = geoPoint.getLatitude();
                                double lng = geoPoint.getLongitude();

                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= distance) {
                                    matchingDocs.add(doc);
                                }
                            }
                        }

                        // matchingDocs contains the results
                        // matchingDocs 는 거짓 양성이 제거된 결과인 것 같다.
                        // List 형식이므로 반복자를 사용하면 될 것 같다.

                        Iterator<DocumentSnapshot> iterator = matchingDocs.iterator();

                        while(iterator.hasNext())
                        {
                            DocumentSnapshot document = iterator.next();

                            GeoPoint geoPoint = document.getGeoPoint("geoPoint");
                            Double d = GeoFireUtils.getDistanceBetween(new GeoLocation(geoPoint.getLatitude(), geoPoint.getLongitude()), center);
                            d = Math.round(d) / 1000.0;

                            ListViewItem_reserve_auto data = new ListViewItem_reserve_auto();

                            String user_name = document.getString("name");
                            String address = document.getString("address");
                            String address_detail = document.getString("address_detail");
                            String dist = d + " km";

                            data.setUser_name(user_name);
                            data.setAddress(address);
                            data.setAddress_detail(address_detail);
                            data.setDistance(dist);

                            adapter.addItem(data);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


    }
    // 얕은 복사 (인자로 받은 ArrayList의 주소를 그대로 사용한다.)
    private boolean isCareable(ArrayList<String> data, String pet_species)
    {
        boolean checked = false;

        Iterator<String> iterator = data.iterator();

        while(iterator.hasNext())
        {
            String care_pet = iterator.next();

            if(care_pet.equals(pet_species)) // 선택한 동물의 종류를 기입
            {
                checked = true;
            }
        }

        return checked;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId)
        {
            case R.id.radiobutton_search_radius_1 :

                distance = 1000.0;
                System.out.println("1키로미터");
                break;

            case R.id.radiobutton_search_radius_2 :

                distance = 3000.0;
                System.out.println("3키로미터");
                break;

            case R.id.radiobutton_search_radius_3 :

                distance = 5000.0;
                System.out.println("5키로미터");
                break;

            case R.id.radiobutton_search_radius_4 :

                distance = 10000.0;
                System.out.println("10키로미터");
                break;
        }
        adapter.clear();
        adapter.notifyDataSetChanged();
        GetNearBy();

    }

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

    }

}