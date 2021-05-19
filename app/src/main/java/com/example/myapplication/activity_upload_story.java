package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

        Intent intent = getIntent();

        callFrom = intent.getBooleanExtra("callFrom", false);
        reserve_id = intent.getStringExtra("reserve_id");

        story_id = intent.getStringExtra("story_id");
        story_title = intent.getStringExtra("story_title");
        story_content = intent.getStringExtra("story_content");
        story_image_num = intent.getStringExtra("story_image_num");
        user_token = intent.getStringExtra("user_token");

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

        if(callFrom)
        {
            String date = intent.getStringExtra("story_timestamp");
            story_timestamp = DateString.StringToDate(date);
            loadImage(this);
            button_upload_story_1.setText("스토리 삭제");
            button_upload_story_2.setText("스토리 수정");
        }
        else
        {
            button_upload_story_1.setVisibility(View.GONE);
            button_upload_story_2.setText("스토리 작성");
        }

    }

    public void onClickUploadStoryImage(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK); // 선택하는 인텐트 호출
        intent.setType("image/*"); // 타입 지정(이미지)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 멀티 초이스 true
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //URL로 데이터를 받음
        startActivityForResult(intent, REQUEST_CODE_GALLERY); //
    }

    public void onClickUploadStory_1(View view)
    {
        deleteStoryImages(story_image_num);
        db.collection("reserve").document(reserve_id).collection("story_list")
                .document(story_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
    }

    public void onClickUploadStory_2(View view)
    {
        String title = edit_upload_story_title.getText().toString().trim();
        String content = edit_upload_story_content.getText().toString().trim();

        Map<String, Object> story = new HashMap<>();

        story.put("title", title);
        story.put("content", content);

        if(callFrom)
        {
            story.put("timestamp", story_timestamp);
            story.put("image_num", story_image_num);
            if(select_image)
            {
                story.put("image_num", hashcode);
            }

            db.collection("reserve").document(reserve_id).collection("story_list")
                    .document(story_id)
                    .update(story)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(select_image)
                            {
                                deleteStoryImages(story_image_num);
                                uploadStoryImages(hashcode);
                            }
                            else
                            {
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        }
                    });
        }
        else
        {
            story.put("timestamp", new Timestamp(new Date()));
            story.put("image_num", hashcode);
            db.collection("reserve").document(reserve_id).collection("story_list")
                    .add(story)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(select_image)
                            {
                                uploadStoryImages(hashcode);
                            }
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            select_image = true;
            if(data != null)
            {
                if(data.getClipData() != null)
                {
                    ClipData clipData = data.getClipData();
                    if(clipData.getItemCount() == 1)
                    {

                        for(int i = 0; i < clipData.getItemCount(); i++)
                        {
                            Uri uri = clipData.getItemAt(i).getUri();
                            images_uri = uri;
                            imageView_upload_story.setImageURI(uri);
                        }

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

    private void uploadStoryImages(String hashcode)
    {
        Context context = this;

        String firstPathSegment = "images_story/";
        String format = ".jpg";

        AlertDialog.Builder progressBuilder = new AlertDialog.Builder(this)
                .setTitle(null)
                .setMessage("업로드 중...");

        AlertDialog progress = progressBuilder.create();
        progress.show();

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
                    progress.dismiss();

                    String title = "모두의 집사";
                    String body = "스토리가 도착했습니다.";

                    NotificationMessaging messaging = new NotificationMessaging(user_token, title, body, context);

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