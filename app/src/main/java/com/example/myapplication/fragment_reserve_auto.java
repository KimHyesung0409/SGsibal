package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class fragment_reserve_auto extends Fragment implements RadioGroup.OnCheckedChangeListener, OnCustomClickListener, View.OnClickListener {

    ViewGroup viewGroup;

    private static final int REQUEST_CODE_2 = 1;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private RadioGroup radioGroup_search_radius;


    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private Double lat, lon;
    private Double distance = 1000.0; // m 단위 기본이 1km 이므로 1000.0
    private GeoLocation center;
    private String uid;
    private EditText editText_search_address;
    private Button button_search_address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_auto, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = auth.getUid();

        editText_search_address = (EditText)viewGroup.findViewById(R.id.edit_search_address_reserve_auto);
        button_search_address = (Button)viewGroup.findViewById(R.id.button_search_address_reserve_auto);
        button_search_address.setOnClickListener(this);

        radioGroup_search_radius = (RadioGroup)viewGroup.findViewById(R.id.radioGroup_search_radius);
        radioGroup_search_radius.setOnCheckedChangeListener(this);

        recyclerView = viewGroup.findViewById(R.id.recyclerview_reserve_auto);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnCustomClickListener) this);

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
        // 'bounds'에 속한 각 아이템들은 시작/끝 쌍에 해당한다.
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // 각 쌍의 분리 쿼리. 9쌍의 경계가 될 수 있다.
        // depending on overlap, but in most cases there are 4.
        // 겹치는 것에 따라서 9쌍의 경계가 될 수 있지만 대부분은 4개.
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
        // 쿼리 결과를 하나의 리스트안에 모아서 수집.
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

                                // We have to filter out a fews false positive due to GeoHash
                                // GeoHash 에 의한 약간의 거짓양성을 필터링 해야한다.
                                // accuracy, but most will match
                                // 대부분은 일치하겠지만.
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

                            String user_id = document.getId();
                            String user_name = document.getString("name");
                            String address = document.getString("address");
                            String address_detail = document.getString("address_detail");
                            String token_id = document.getString("fcm_token");
                            String dist = d + " km";

                            data.setUser_id(user_id);
                            data.setUser_name(user_name);
                            data.setAddress(address);
                            data.setAddress_detail(address_detail);
                            data.setToken_id(token_id);
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

        if(data.isEmpty())
        {
            return false;
        }

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

        ListViewItem_reserve_auto data = (ListViewItem_reserve_auto)adapter.getItem(position);

        String to = data.getToken_id();
        String title = "모두의 집사";
        String body = "예약이 접수되었습니다.";

        createChatroom(data.getUser_id(), data.getUser_name());

        NotificationMessaging messaging = new NotificationMessaging(to, title, body, getContext());

        messaging.start();
    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {


    }

    @Override
    public void onClick(View v) {
        Activity activity = getActivity();
        Intent intent = new Intent(activity, activity_popup_address.class);
        activity.startActivityForResult(intent, REQUEST_CODE_2);
    }

    public void setCenter(Double lat, Double lon, String address)
    {
        center = new GeoLocation(lat, lon);
        editText_search_address.setText(address);

        adapter.clear();
        adapter.notifyDataSetChanged();
        GetNearBy();
    }

    private void createChatroom(String user_id, String user_name)
    {
        Map<String, Object> chatroom = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입

        chatroom.put("client_id", auth.getUid());
        chatroom.put("sitter_id", user_id);


        // db에 업로드
        DocumentReference ref = db.collection("chatroom").document();
        ref.set(chatroom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        createReserve(ref.getId(), user_id, user_name);

                        Log.d("결과 : ", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("결과 : ", "Error writing document", e);
                    }
                });

    }

    private void createReserve(String chatroom, String user_id, String user_name)
    {
        ListViewItem_petlist pet_data = fragment_reserve_visit_1.getSelected_pet();

        // Key와 Value를 가지는 맵
        Map<String, Object> reserve = new HashMap<>();

        //String address_detail = textView_estimate_detail_address_detail.getText().toString();

        reserve.put("client_id", auth.getUid());
        reserve.put("sitter_id", user_id);
        reserve.put("client_name", LoginUserData.getUser_name());
        reserve.put("sitter_name", user_name);


        // 위에서 만든 맵(user) 변수에 데이터 삽입
        reserve.put("chatroom", chatroom);
        reserve.put("timestamp", new Timestamp(new Date()));
        reserve.put("datetime", fragment_reserve_visit_2.getSelectedTime());
        reserve.put("pet_id", pet_data.getPet_id());
        reserve.put("price", "15000"); // <- 가격은 펫시터 프로필에서 설정하는 가격으로.
        reserve.put("address", LoginUserData.getAddress());
        //reserve.put("address_detail", address_detail);
        reserve.put("info", pet_data.getInfo());



        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        DocumentReference ref = db.collection("reserve").document();
        ref.set(reserve)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //updateUserReserve(ref.getId());
                        getActivity().finish();
                        Log.d("결과 : ", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("결과 : ", "Error writing document", e);
                    }
                });
    }

}