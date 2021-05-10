package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class activity_upload_entrust extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText edit_upload_entrust_title;
    private EditText edit_upload_entrust_intro;
    private EditText edit_upload_entrust_caution;
    private EditText edit_upload_entrust_price;

    private String address;
    private String user_name;
    private String intro;
    private String caution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_entrust);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edit_upload_entrust_title = (EditText)findViewById(R.id.edit_upload_entrust_title);
        edit_upload_entrust_intro = (EditText)findViewById(R.id.edit_upload_entrust_intro);
        edit_upload_entrust_caution = (EditText)findViewById(R.id.edit_upload_entrust_caution);
        edit_upload_entrust_price = (EditText)findViewById(R.id.edit_upload_entrust_price);

        getUserData();

    }

    public void onClickUploadEntrust(View view)
    {
        String title = edit_upload_entrust_title.getText().toString().trim();
        String price = edit_upload_entrust_price.getText().toString().trim();

        intro = edit_upload_entrust_intro.getText().toString().trim();
        caution = edit_upload_entrust_caution.getText().toString().trim();

        uploadEntrust(title, price);
    }

    private void getUserData()
    {

        db.collection("users").document(auth.getUid())
        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        address = document.getString("address");
                        user_name = document.getString("name");

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

    private void uploadEntrust(String title, String price)
    {
        // Key와 Value를 가지는 맵
        Map<String, Object> entrust = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        entrust.put("address", address);
        entrust.put("images_num", "1");
        entrust.put("name", user_name);
        entrust.put("title", title);
        entrust.put("price", price);
        entrust.put("uid", auth.getUid());

        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        db.collection("entrust_list").document()
                .set(entrust)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        uploadEntrustDetail();
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

    private void uploadEntrustDetail()
    {
        // Key와 Value를 가지는 맵
        Map<String, Object> entrust = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        entrust.put("intro", intro);
        entrust.put("caution", caution);

        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        db.collection("entrust_list").document().collection("detail").document("content")
                .set(entrust)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
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