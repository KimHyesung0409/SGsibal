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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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

    private boolean callFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_review);

        Intent intent = getIntent();

        callFrom = intent.getBooleanExtra("callFrom", false);
        user_id = intent.getStringExtra("user_id");// 펫시터 id
        user_name = intent.getStringExtra("user_name"); // 펫시터 이름

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
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

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
                            calcReviewRating();
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
        else
        {
            db.collection("review")
                    .add(review)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            calcReviewRating();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

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
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

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


}