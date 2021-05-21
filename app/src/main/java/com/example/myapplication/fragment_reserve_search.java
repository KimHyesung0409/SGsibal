package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class fragment_reserve_search extends Fragment implements OnCustomClickListener, View.OnClickListener {

    private static final int USER_ID_LENGTH = 28;

    ViewGroup viewGroup;
    private Context context;
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private RecyclerView.ViewHolder viewHolder;
    private Object ListView;
    private EditText search_write;
    private RecyclerViewAdapter adapter;
    private String uid;
    private EditText editText_search;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private ImageButton imagebutton_reserve_search;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_search, container, false);
        //FirebaseAuth auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        editText_search= (EditText)viewGroup.findViewById(R.id.edit_reserve_search);
        imagebutton_reserve_search = (ImageButton)viewGroup.findViewById(R.id.imagebutton_reserve_search);

        imagebutton_reserve_search.setOnClickListener(this);

        recyclerView=viewGroup.findViewById(R.id.recyclerview_searchlist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnCustomClickListener) this);

        //getsearchlist();

        return viewGroup;
    }

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

        ListViewItem_searchlist data = (ListViewItem_searchlist)adapter.getItem(position);

        Activity activity = getActivity();

        Intent intent = new Intent(activity, activity_popup_user_data.class);
        intent.putExtra("user_id", data.getUser_id());
        intent.putExtra("callFrom", 1);
        activity.startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }

    @Override
    public void onClick(View v) {

        adapter.clear();

        String keyword = editText_search.getText().toString().trim();

        if(!VerifyString.isEmptyAndNull(keyword))
        {
            // user_id의 길이는 28이므로 user_id를 사용하여 탐색
            if(keyword.length() == USER_ID_LENGTH)
            {
                searchForUserId(keyword);
            }
            else if(keyword.length() < USER_ID_LENGTH) // user_id 길이보다 짧으면 user_name을 사용하여 탐색
            {
                searchForUserName(keyword);
            }
            else // 형식에 맞지 않는다.
            {
                Toast.makeText(getContext(), "형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    private void searchForUserId(String keyword)
    {
          db.collection("users").document(keyword)
                  .get()
                  .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                  {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task)
                      {

                          if(task.isSuccessful())
                          {
                              DocumentSnapshot document = task.getResult();

                              if(document.exists())
                              {
                                  ListViewItem_searchlist data = new ListViewItem_searchlist();

                                  String user_id = document.getId();
                                  String user_name = document.getString("name");
                                  String birth = document.getString("birth");
                                  String gender = Gender.getGender(document.getBoolean("gender"));

                                  double rating;

                                  if(document.getDouble("rating") == null)
                                  {
                                      rating = 0.0;
                                  }
                                  else
                                  {
                                      rating = document.getDouble("rating");
                                  }

                                  data.setUser_id(user_id);
                                  data.setUser_name(user_name);
                                  data.setBirth(birth);
                                  data.setGender(gender);
                                  data.setRating(rating);

                                  adapter.addItem(data);
                                  adapter.notifyDataSetChanged();
                              }
                              else
                              {

                              }

                          }

                      }
                  });

    }

    private void searchForUserName(String keyword)
    {
        db.collection("users")
                .whereEqualTo("name", keyword)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {

                        if(task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                ListViewItem_searchlist data = new ListViewItem_searchlist();

                                String user_id = document.getId();
                                String user_name = document.getString("name");
                                String birth = document.getString("birth");
                                String gender = Gender.getGender(document.getBoolean("gender"));

                                double rating;

                                if(document.getDouble("rating") == null)
                                {
                                    rating = 0.0;
                                }
                                else
                                {
                                    rating = document.getDouble("rating");
                                }

                                data.setUser_id(user_id);
                                data.setUser_name(user_name);
                                data.setBirth(birth);
                                data.setGender(gender);
                                data.setRating(rating);

                                adapter.addItem(data);

                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {

                        }

                    }
                });
    }

}



