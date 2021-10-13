package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.example.myapplication.ListViewItem.ListViewItem_storylist;
import com.example.myapplication.OnCustomClickListener;
import com.example.myapplication.R;
import com.example.myapplication.ViewHolder.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class activity_story_list extends AppCompatActivity implements OnCustomClickListener {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uid;
    private String reserve_id;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        // --------- 액션바, 스위치 버튼 설정 ----------
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);
        // ---------------------------------------------

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();

        // 해당 예약에 등록된 스토리를 조회하기 위해서 intent로 해당 예약_id를 전달받는다.
        Intent intent = getIntent();

        reserve_id = intent.getStringExtra("reserve_id");

        recyclerView = findViewById(R.id.recyclerview_story_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        getStorylist();
    }

    // 스토리 목록을 조회하는 메소드
    private void getStorylist()
    {
        db.collection("reserve").document(reserve_id).collection("story_list")
                .orderBy("timestamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                ListViewItem_storylist data = new ListViewItem_storylist();


                                String story_id = document.getId();
                                String title = document.getString("title");
                                String content = document.getString("content");
                                String image_num = document.getString("image_num");
                                Date timestamp = document.getDate("timestamp");

                                // 조회한 스토리 정보를 어뎁터에 추가한다.
                                data.setReserve_id(reserve_id);
                                data.setStory_id(story_id);
                                data.setStory_title(title);
                                data.setStory_content(content);
                                data.setImage_num(image_num);
                                data.setTimestamp(timestamp);

                                adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
}