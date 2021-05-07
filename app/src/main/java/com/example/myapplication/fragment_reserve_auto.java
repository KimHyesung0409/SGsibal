package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import java.util.Iterator;
import java.util.List;

public class fragment_reserve_auto extends Fragment {

    ViewGroup viewGroup;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    // 임시 TextView
    private TextView temp_user_name, temp_lat, temp_lon, temp_geohash, temp_address, temp_distance;

    // 임시 변수
    private Double lat, lon;
    private String geoHash;
    private Double distance = 5.0 * 1000; // m 단위 이므로 1000을 곱해준다.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_auto, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
        DocumentReference docRef = db.collection("users").document(auth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        GeoPoint geoPoint = document.getGeoPoint("geoPoint");

                        lat = geoPoint.getLatitude();
                        lon = geoPoint.getLongitude();
                        geoHash = document.getString("geoHash");

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
        GeoLocation center = new GeoLocation(lat, lon);

        // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // depending on overlap, but in most cases there are 4.
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, distance);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = db.collection("users")
                    //.whereEqualTo("care_species", "강아지") // <- where 문을 써서 일치하는 반려동물 품종만 필터링.
                    //.whereArrayContains("care_species", "개", "고양이", "사자"); // <- 케어 가능한 품종을 여러개로 설정 했다면. 이런식으로.
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

                                if(doc.getId().equals(auth.getUid()))
                                {
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

                            temp_user_name.setText(document.getString("name"));
                            temp_lat.setText(String.valueOf(geoPoint.getLatitude()));
                            temp_lon.setText(String.valueOf(geoPoint.getLongitude()));
                            temp_geohash.setText(document.getString("geoHash"));
                            temp_address.setText(document.getString("address"));
                            temp_distance.setText(d + " km");
                        }

                    }
                });


    }

}