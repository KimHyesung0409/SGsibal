package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_upload_review extends AppCompatActivity {

    private String user_id;
    private String user_name;
    private String reserve_id;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText edit_upload_review_title;
    private EditText edit_upload_review_content;
    private RatingBar ratingBar_upload_review;

    private Button button_upload_review_1;
    private Button button_upload_review_2;

    private String review_id;
    private String review_title;
    private String review_content;
    private double review_rating;
    private Date datetime;
    private boolean gender;
    private String birth;

    private boolean callFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_review);

        Intent intent = getIntent();

        callFrom = intent.getBooleanExtra("callFrom", false);
        user_id = intent.getStringExtra("user_id");// 펫시터 id
        user_name = intent.getStringExtra("user_name"); // 펫시터 이름
        reserve_id = intent.getStringExtra("reserve_id"); // 예약 id

        String date = intent.getStringExtra("datetime");
        datetime = DateString.StringToDate(date);

        String gen = intent.getStringExtra("gender");
        gender = Gender.getGender(gen);

        birth = intent.getStringExtra("birth");

        review_id = intent.getStringExtra("review_id");
        review_title = intent.getStringExtra("review_title");
        review_content = intent.getStringExtra("review_content");
        review_rating = intent.getDoubleExtra("review_rating", 0.0);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edit_upload_review_title = (EditText)findViewById(R.id.edit_upload_review_title);
        edit_upload_review_content = (EditText)findViewById(R.id.edit_upload_review_content);
        ratingBar_upload_review = (RatingBar)findViewById(R.id.ratingBar_upload_review);

        button_upload_review_1 = (Button)findViewById(R.id.button_upload_review_1);
        button_upload_review_2 = (Button)findViewById(R.id.button_upload_review_2);

        edit_upload_review_title.setText(review_title);
        edit_upload_review_content.setText(review_content);
        ratingBar_upload_review.setRating((float)review_rating);

        if(callFrom)
        {
            button_upload_review_1.setText("후기 삭제");
            button_upload_review_2.setText("후기 수정");
        }
        else
        {
            button_upload_review_1.setVisibility(View.GONE);
            button_upload_review_2.setText("후기 작성");
        }

    }

    public void onClickUploadReview_1(View view)
    {
        db.collection("review").document(review_id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        calcReviewRating();
                    }
                });
    }

    public void onClickUploadReview_2(View view)
    {
        double rating = ratingBar_upload_review.getRating();
        String title = edit_upload_review_title.getText().toString().trim();
        String content = edit_upload_review_content.getText().toString().trim();

        Map<String, Object> review = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        review.put("client_id", auth.getUid()); // 후기를 작성한 유저의 id
        review.put("client_name", LoginUserData.getUser_name());
        review.put("sitter_id", user_id);
        review.put("sitter_name", user_name);
        review.put("timestamp", new Timestamp(new Date()));
        review.put("title", title);
        review.put("rating", rating);
        review.put("content", content);

        if(callFrom)
        {
            db.collection("review").document(review_id)
                    .update(review)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            deleteReserveData();
                        }
                    });
        }
        else
        {
            db.collection("review")
                    .add(review)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            checkHistory();
                            calcReviewRating();
                            deleteReserveData();
                        }
                    });
        }
    }

    private void calcReviewRating()
    {

        db.collection("review")
                .whereEqualTo("sitter_id", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            ArrayList<Double> sum = new ArrayList<>();


                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                double rating = document.getDouble("rating");

                                sum.add(rating);

                            }

                            if(!sum.isEmpty())
                            {
                                double avg = sum.stream().mapToDouble(Double -> Double).average().getAsDouble();
                                updateUserRating(avg);
                            }
                            else
                            {
                                updateUserRating(0);
                            }


                        }
                        else
                        {

                        }
                    }
                });

    }

    private void updateUserRating(double avg)
    {
        db.collection("users").document(user_id)
                .update("rating", avg)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
    }

    private void deleteReserveData()
    {
        // 해당 예약의 채팅방을 구하고 삭제 함수 호출
        Task task = db.collection("reserve").document(reserve_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        deleteChatMessage(document.getString("chatroom"));
                    }

                }
            }
        });
        // 위 작업이 끝나면 예약 데이터 삭제
        task.continueWith(Task::isComplete).addOnCompleteListener(new OnCompleteListener()
        {
            @Override
            public void onComplete(@NonNull Task task) {

                db.collection("reserve").document(reserve_id).delete();
            }
        });

    }

    private void deleteChatMessage(String chatroom_id)
    {
        //채팅방에 있는 모든 메시지 확인
         Task deleteMessages = db.collection("chatroom").document(chatroom_id).collection("messages")
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
                                // 메시지 삭제
                                db.collection("chatroom").document(chatroom_id)
                                        .collection("messages").document(document.getId()).delete();

                            }
                        }
                        else
                        {

                        }
                    }
                });

        // 위 작업이 끝나면 채팅방 삭제 후 평점 함수 호출
        deleteMessages.continueWith(Task::isComplete).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        // 채팅방 삭제
                        db.collection("chatroom").document(chatroom_id).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                calcReviewRating();
                            }
                        });

                    }
                });
    }

    // 무작정 추가하면 안된다. 이미 히스토리에 등록이 되어있는 경우가 있을 수 있다.
    // 따라서 히스토리에 이미 해당 유저가 등록되어 있다면 해당 히스토리 데이터를 업데이트 시켜주고
    // 해당 유저가 히스토리에 없으면 추가해준다.
    private void checkHistory()
    {
        db.collection("users").document(auth.getUid()).collection("history")
                .whereEqualTo("user_id", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            // 결과가 비어있지 않으면 즉 해당 유저가 히스토리에 존재하면.
                            if(!task.getResult().isEmpty())
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    updateHistory(document.getId());
                                }
                            }
                            else // 해당 유저가 히스토리에 존재하지 않으면
                            {
                                addHistory();
                            }

                        }
                        else
                        {

                        }
                    }
                });
    }

    private void addHistory()
    {

        Map<String, Object> history = new HashMap<>();

        history.put("user_id", user_id);
        history.put("name", user_name);
        history.put("birth", birth);
        history.put("gender", gender);
        history.put("datetime", datetime);

        db.collection("users").document(auth.getUid()).collection("history")
                .document()
                .set(history)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 히스토리 등록 실패
                    }
                });
    }

    private void updateHistory(String history_id)
    {
        db.collection("users").document(auth.getUid()).collection("history")
                .document(history_id)
                .update("datetime", datetime)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 업데이트 실패
                    }
                });
    }

}