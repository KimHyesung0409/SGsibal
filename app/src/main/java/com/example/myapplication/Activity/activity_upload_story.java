package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.DateString;
import com.example.myapplication.NotificationMessaging;
import com.example.myapplication.R;
import com.example.myapplication.VerifyString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_upload_story extends AppCompatActivity {

    private static final int REQUEST_CODE_GALLERY = 3;

    private FirebaseFirestore db;

    private boolean callFrom;
    private String reserve_id;
    private String story_id;
    private String story_title;
    private String story_content;
    private String story_image_num;
    private String user_token;
    private String user_id;

    private Uri images_uri;
    private String hashcode = null;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private Button button_upload_story_1;
    private Button button_upload_story_2;

    private EditText edit_upload_story_title;
    private EditText edit_upload_story_content;
    private ImageView imageView_upload_story;

    private Date story_timestamp;

    private boolean select_image = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_story);

        // --------- 액션바, 스위치 버튼 설정 ----------
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);
        // ---------------------------------------------

        // intent로 해당 예약_id와 스토리 기본정보를 전달받는다.
        // 스토리작성 / 스토리 수정, 삭제 기능을 수행하는 액티비티로서 재사용되기 때문에
        // 어디서 call 했는지에 대한 정보를 전달받는다.
        Intent intent = getIntent();

        callFrom = intent.getBooleanExtra("callFrom", false);
        reserve_id = intent.getStringExtra("reserve_id");

        // 스토리 수정, 삭제시 전달받는다.
        story_id = intent.getStringExtra("story_id");
        story_title = intent.getStringExtra("story_title");
        story_content = intent.getStringExtra("story_content");
        story_image_num = intent.getStringExtra("story_image_num");
        user_token = intent.getStringExtra("user_token");
        user_id = intent.getStringExtra("user_id");

        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        button_upload_story_1 = (Button)findViewById(R.id.button_upload_story_1);
        button_upload_story_2 = (Button)findViewById(R.id.button_upload_story_2);

        edit_upload_story_title = (EditText)findViewById(R.id.edit_upload_story_title);
        edit_upload_story_content = (EditText)findViewById(R.id.edit_upload_story_content);
        imageView_upload_story = (ImageView)findViewById(R.id.imageview_upload_story);

        edit_upload_story_title.setText(story_title);
        edit_upload_story_content.setText(story_content);

        // 스토리 삭제 수정 기능을 위해
        // 관련 정보를 intent로 부터 가져오고
        // 버튼의 text를 수정한다.
        if(callFrom)
        {
            String date = intent.getStringExtra("story_timestamp");
            story_timestamp = DateString.StringToDate(date);
            loadImage(this);
            button_upload_story_1.setText("스토리 삭제");
            button_upload_story_2.setText("스토리 수정");
        }
        else // 스토리 작성 기능만 사용하면 되므로 버튼 하나를 지우고 text를 변경한다.
        {
            button_upload_story_1.setVisibility(View.GONE);
            button_upload_story_2.setText("스토리 작성");
        }

    }
    // 이미지 선택 버튼 클릭 메소드
    public void onClickUploadStoryImage(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK); // 선택하는 인텐트 호출
        intent.setType("image/*"); // 타입 지정(이미지)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 멀티 초이스 false
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //URL로 데이터를 받음
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }
    // 스토리 제거 버튼 클릭 메소드
    public void onClickUploadStory_1(View view)
    {
        deleteStoryImages(story_image_num);
        db.collection("reserve").document(reserve_id).collection("story_list")
                .document(story_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    // 스토리 제거가 완료되면 액티비티를 종료한다.
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
    }
    // 스토리 작성과 수정 버튼 클릭 메소드
    public void onClickUploadStory_2(View view)
    {
        String title = edit_upload_story_title.getText().toString().trim();
        String content = edit_upload_story_content.getText().toString().trim();

        Map<String, Object> story = new HashMap<>();

        story.put("title", title);
        story.put("content", content);

        // 내용이 입력되었는지 확인
        if(!VerifyString.isEmptyAndNull(title) & !VerifyString.isEmptyAndNull(content))
        {

            // 스토리 수정인 경우.
            if (callFrom) {
                story.put("timestamp", story_timestamp);
                story.put("image_num", story_image_num);
                // 스토리 수정 시 이미지를 그대로 둘 수도 있고 이미지를 변경할 수도 있기 때문에 조건문으로 분기
                if (select_image)  // 선택된 이미지가 있으므로 스토리 수정 시 이미지를 변경한 것이다
                {
                    // 따라서 이미지를 선택하고 가져올 때의 시간을 hashcode로 변환한 데이터를 등록한다.
                    story.put("image_num", hashcode);
                }

                db.collection("reserve").document(reserve_id).collection("story_list")
                        .document(story_id)
                        .update(story)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // 스토리 수정 시 이미지를 그대로 둘 수도 있고 이미지를 변경할 수도 있기 때문에 조건문으로 분기
                                if (select_image)  // 선택된 이미지가 있으므로 스토리 수정 시 이미지를 변경한 것이다
                                {
                                    // 기존에 있던 스토리 이미지를 제거하는 메소드 호출
                                    deleteStoryImages(story_image_num);
                                    // 새로운 스토리 이미지를 업로드하기 위해 이미지 업로드 메소드 호출
                                    uploadStoryImages(hashcode);
                                } else {
                                    // 선택된 이미지가 없으면 액티비티 종료
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }
                        });
            } else // 스토리 작성인 경우
            {
                // 이미지가 등록되었는지 확인
                if (hashcode != null) {
                    story.put("timestamp", new Timestamp(new Date()));
                    story.put("image_num", hashcode);
                    db.collection("reserve").document(reserve_id).collection("story_list")
                            .add(story)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (select_image) {
                                        // 이미지를 업로드 메소드 호출
                                        uploadStoryImages(hashcode);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(this, "이미지가 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

     // 위에서 실행시킨 이미지 클릭 인텐트에서 선택한 이미지들을 전달받는다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            select_image = true;
            if(data != null) // 선택한 데이터가 존재하는지.
            {
                if(data.getClipData() != null) // 선택한 데이터가 존재하는지.
                {
                    ClipData clipData = data.getClipData();
                    if(clipData.getItemCount() == 1) // 선택한 이미지가 1개인경우.
                    {
                        // 이미지의 URI를 구하고 이미지 뷰에 출력
                        for(int i = 0; i < clipData.getItemCount(); i++)
                        {
                            Uri uri = clipData.getItemAt(i).getUri();
                            images_uri = uri;
                            imageView_upload_story.setImageURI(uri);
                        }
                        // 선택한 이미지가 반환되는 시간을 해쉬코드로 변환(이미지를 저장소에 저장하기 위해 사용)
                        hashcode = String.valueOf(Timestamp.now().hashCode());
                        System.out.println("이미지 선택 :" + hashcode);
                    }
                    else
                    {
                        Toast.makeText(this, "1개의 이미지를 선택해야 합니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }
    }
    // 저장소에 선택한 이미지를 업로드하는 메소드
    private void uploadStoryImages(String hashcode)
    {
        Context context = this;

        String firstPathSegment = "images_story/"; // 저장소 기본 경로
        String format = ".jpg"; // 이미지 형식
        // 다이얼로그로 업로드 진행사항을 표시
        AlertDialog.Builder progressBuilder = new AlertDialog.Builder(this)
                .setTitle(null)
                .setMessage("업로드 중...");

        AlertDialog progress = progressBuilder.create();
        progress.show();
        // 이미지 경로와 이미지 명을 stringbuilder로 합쳐서 만들고
        // 만들어진 경로와 이미지 명으로 저장소에 이미지를 업로드.
        StringBuilder stringBuilder = new StringBuilder(firstPathSegment);
        stringBuilder.append(hashcode);
        stringBuilder.append(format);
        StorageReference ref = storageRef.child(stringBuilder.toString());

        ref.putFile(images_uri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // 업로드 완료시 다이얼로그와 액티비티 종료.
                    // fcm 메시지 전송.
                    progress.dismiss();

                    String title = "모두의 집사";
                    String body = "스토리가 도착했습니다.";

                    NotificationMessaging messaging = new NotificationMessaging(user_token, title, body, user_id, NotificationMessaging.FCM_RESERVE, context);

                    messaging.start();

                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();

                    System.out.println("업로드 완료");
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    Log.d("", "Upload is " + progress + "% done");
                }
            });


    }
    // Glide 라이브러리를 사용하여 이미지를 출력
    private void loadImage(Context context)
    {
        String path = "images_story/";
        String format = ".jpg";

        // 파이어 스토리지 레퍼런스로 파일을 참조한다.
        StorageReference child = storageRef.child(path + story_image_num + format);

        // 참조한 파일의 다운로드 링크가 성공적으로 구해지면 Glide를 이용해 이미지뷰에 로딩한다.
        child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(context)
                            .load(task.getResult())
                            .fitCenter()
                            .into(imageView_upload_story);
                } else {

                }
            }
        });
    }
    // 기존에 존재하던 스토리 이미지를 제거하는 메소드
    private void deleteStoryImages(String story_image_num)
    {
        String firstPathSegment = "images_story/";
        String format = ".jpg";

        StringBuilder stringBuilder = new StringBuilder(firstPathSegment);
        stringBuilder.append(story_image_num);
        stringBuilder.append(format);
        StorageReference ref = storageRef.child(stringBuilder.toString());

        ref.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                    }
                });
    }
}