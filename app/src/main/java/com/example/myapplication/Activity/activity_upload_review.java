package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapplication.DateString;
import com.example.myapplication.Gender;
import com.example.myapplication.LoginUserData;
import com.example.myapplication.R;
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

        // --------- 액션바, 스위치 버튼 설정 ----------
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);
        // ---------------------------------------------

        // intent로 예약과 리뷰에대한 기본적인 정보를 전달받는다.
        // 리뷰 작성과 리뷰 수정, 삭제 기능을 수행하는 액티비티로서 재사용되기 때문에
        // 어디서 call 했는지에 대한 정보를 전달받는다.
        Intent intent = getIntent();

        callFrom = intent.getBooleanExtra("callFrom", false);
        user_id = intent.getStringExtra("user_id");// 펫시터 id
        user_name = intent.getStringExtra("user_name"); // 펫시터 이름
        reserve_id = intent.getStringExtra("reserve_id"); // 예약 id

        String date = intent.getStringExtra("datetime");
        datetime = DateString.StringToDate(date);

        birth = intent.getStringExtra("birth");

        // 리뷰 수정, 삭제 시 전달받는다.
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

        // 고객 - 나의 후기 에서 후기 삭제, 수정
        // 두 버튼의 텍스트를 수정한다.
        if(callFrom)
        {
            button_upload_review_1.setText("후기 삭제");
            button_upload_review_2.setText("후기 수정");
        }
        // 고객 - 예약현황 후기 작성
        // 후기 삭제 버튼을 지우고 후기 작성으로 텍스트를 수정한다.
        else
        {
            String gen = intent.getStringExtra("gender");
            gender = Gender.getGender(gen);

            button_upload_review_1.setVisibility(View.GONE);
            button_upload_review_2.setText("후기 작성");
        }

    }
    // 후기 삭제 버튼 클릭 메소드
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
    // 후기 등록 수정 버튼 클릭 메소드
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

        // 후기 수정이므로 update 메소드를 사용한다.
        if(callFrom)
        {
            db.collection("review").document(review_id)
                    .update(review)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        // 수정된 평점을 반영하기 위해 평점을 계산하는 메소드 호출
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            calcReviewRating();
                        }
                    });
        }
        else // 후기 작성이므로 add 메소드 사용
        {
            db.collection("review")
                    .add(review)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {


                        // 후기 작성이 완료되면 사용기록에 해당 유저가 존재하는지 파악하는 메소드 호출
                        // 평점을 계산하는 메소드 호출
                        // 해당 예약을 삭제하는 메소드 호출
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            checkHistory();
                            calcReviewRating();
                            deleteReserveData();
                        }
                    });
        }
    }
    // 후기 평점을 계산하는 메소드
    private void calcReviewRating()
    {
        // 후기 db에서 해당 펫시터의 user_id로 모든 후기를 조회한다.
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
                            // 임의의 Thread로 조회하기 때문에 주소값을 사용하는
                            // ArrayList를 사용하여 평점을 저장하고 계산한다.
                            ArrayList<Double> sum = new ArrayList<>();

                            // 각 후기의 평점을 위 ArrayList에 추가한다.
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                double rating = document.getDouble("rating");

                                sum.add(rating);

                            }
                            // 위 ArrayList가 비어있지 않다면 즉 후기가 존재한다면.
                            if(!sum.isEmpty())
                            {
                                // 평균을 구하고
                                double avg = sum.stream().mapToDouble(Double -> Double).average().getAsDouble();
                                // 해당 펫시터의 db에서 rating 평점 항목을 업데이트 한다.
                                updateUserRating(avg);
                            }
                            else
                            {
                                // 후기가 존재하지 않으면 0으로 업데이트
                                updateUserRating(0);
                            }


                        }
                        else
                        {

                        }
                    }
                });

    }

    // 유저의 평점을 업데이트 하는 메소드
    private void updateUserRating(double avg)
    {
        db.collection("users").document(user_id)
                .update("rating", avg)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {

                    // 평점 업데이트가 완료되면 액티비티를 종료한다.
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
    }

    // 예약을 db에서 제거하는 메소드
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
                        // 해당 예약의 채팅방을 조회하고 채팅방을 삭제하는 메소드를 실행.
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
    // 채팅방을 삭제하는 메소드
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

    // 히스토리에 해당 유저를 추가하는 메소드
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

    // 히스토리에 이미 해당 유저가 존재하여 정보만 업데이트 해주는 메소드.
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