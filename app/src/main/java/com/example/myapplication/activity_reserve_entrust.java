package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class activity_reserve_entrust extends AppCompatActivity {

    private FirebaseFirestore db;
    private static final int IMAGE_NUM = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_entrust);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);

        ListView listview = (ListView)findViewById(R.id.listview_reserve_entrust);

        ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        //파이어베이스 db
        db = FirebaseFirestore.getInstance();

        //db 에서 데이터를 가져온다.
        CollectionReference docRef = db.collection("entrust_list");


        db.collection("entrust_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String image_start_num = (String)document.get("images_num");
                                int start_num = Integer.parseInt(image_start_num);
                                int images_num[] = new int[IMAGE_NUM];

                                for(int i = start_num; i < start_num + IMAGE_NUM; i++)
                                {
                                    images_num[i - start_num] = i;
                                }

                                adapter.addItem(images_num,"위탁_1");
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("에러 났어용 : ", "Error getting documents: ", task.getException());
                        }
                    }
                });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ListViewItem_reserve_entrust data = (ListViewItem_reserve_entrust)adapterView.getItemAtPosition(i);

                int images[] = data.getImages();

                Intent intent = new Intent(activity_reserve_entrust.this,activity_reserve_entrust_detail.class);
                intent.putExtra("images", images);
                startActivity(intent);

            }
        });

    }




}