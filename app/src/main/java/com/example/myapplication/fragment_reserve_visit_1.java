package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class fragment_reserve_visit_1 extends Fragment implements AdapterView.OnItemClickListener{

    private static final int REQUEST_CODE = 0;

    ViewGroup viewGroup;
    private ListView listview;
    private ListViewAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uid;
    private ListViewItem_petlist add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_visit_1, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        listview = (ListView)viewGroup.findViewById(R.id.listview_petlist);
        adapter = new ListViewAdapter();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

        getPetList();

        Button button = (Button)viewGroup.findViewById(R.id.bbbttt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshListView();
            }
        });

        return viewGroup;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ListViewItem_petlist data = (ListViewItem_petlist)parent.getItemAtPosition(position);

        // 해당 아이템이 추가 객체일 경우
        if(data == add)
        {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, activity_add_pet.class);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
        else
        {
            System.out.println(data.getName());
            System.out.println(data.getSpecies());
        }

    }

    private void getPetList()
    {
        db.collection("users").document(uid).collection("pet_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ListViewItem_petlist data = new ListViewItem_petlist();

                                String pet_name = (String)document.get("name");
                                String pet_species = (String)document.get("species");

                                data.setName(pet_name);
                                data.setSpecies(pet_species);

                                adapter.addItem(data);
                            }
                            add = new ListViewItem_petlist();
                            add.setName("반려동물 추가");
                            adapter.addItem(add);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void refreshListView()
    {
        System.out.println("리플레시");
        adapter.clearListView();
        // 안해주니까 이상해짐.
        adapter.notifyDataSetChanged();
        getPetList();
    }

}