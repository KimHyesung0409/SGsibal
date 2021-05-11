package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_upload_entrust extends AppCompatActivity {

    private static final int REQUEST_CODE_GALLERY = 3;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private EditText edit_upload_entrust_title;
    private EditText edit_upload_entrust_intro;
    private EditText edit_upload_entrust_caution;
    private EditText edit_upload_entrust_price;

    private String address;
    private String user_name;
    private String intro;
    private String caution;

    private LinearLayout linearLayout;
    private ImageView imageList[] = new ImageView[5];

    private String hashcode = null;
    private Uri images_uri[] = new Uri[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_entrust);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        edit_upload_entrust_title = (EditText)findViewById(R.id.edit_upload_entrust_title);
        edit_upload_entrust_intro = (EditText)findViewById(R.id.edit_upload_entrust_intro);
        edit_upload_entrust_caution = (EditText)findViewById(R.id.edit_upload_entrust_caution);
        edit_upload_entrust_price = (EditText)findViewById(R.id.edit_upload_entrust_price);

        linearLayout = (LinearLayout)findViewById(R.id.linearlayout_upload_entrust);

        for(int i = 0; i < linearLayout.getChildCount(); i++)
        {
            ImageView imageView = (ImageView) linearLayout.getChildAt(i);
            imageList[i] = imageView;
        }

        getUserData();

    }

    public void onClickUploadEntrust(View view)
    {
        boolean ischecked = true;

        String title = edit_upload_entrust_title.getText().toString().trim();
        String price = edit_upload_entrust_price.getText().toString().trim();

        intro = edit_upload_entrust_intro.getText().toString().trim();
        caution = edit_upload_entrust_caution.getText().toString().trim();

        if(title.isEmpty() | title == null)
        {
            ischecked = false;
        }
        else if(price.isEmpty() | price == null)
        {
            ischecked = false;
        }
        else if(intro.isEmpty() | intro == null)
        {
            ischecked = false;
        }
        else if(caution.isEmpty() | caution == null)
        {
            ischecked = false;
        }
        else if(hashcode == null)
        {
            ischecked = false;
        }

        if(ischecked)
        {
            uploadEntrust(title, price);
        }
        else
        {
             Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickSelectPic(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK); // 선택하는 인텐트 호출
        intent.setType("image/*"); // 타입 지정(이미지)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 멀티 초이스 true
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //URL로 데이터를 받음
        startActivityForResult(intent, REQUEST_CODE_GALLERY); //
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){

            if(data != null)
            {
                if(data.getClipData() != null)
                {
                    ClipData clipData = data.getClipData();
                    if(clipData.getItemCount() == 5)
                    {

                        for(int i = 0; i < clipData.getItemCount(); i++)
                        {
                            Uri uri = clipData.getItemAt(i).getUri();
                            images_uri[i] = uri;
                            imageList[i].setImageURI(uri);
                        }

                        hashcode = String.valueOf(Timestamp.now().hashCode());

                    }
                    else
                    {
                        Toast.makeText(this, "5개의 이미지를 선택해야 합니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }
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
        entrust.put("images_num", hashcode);
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
                        uploadEntrustImages();
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

    private void uploadEntrustImages()
    {

        String firstPathSegment = "images_entrust/";
        String format = ".jpg";
        String sep = "_";

        for(int i = 0; i < images_uri.length; i++)
        {
            final int index = i;
            StringBuilder stringBuilder = new StringBuilder(firstPathSegment);
            stringBuilder.append(hashcode);
            stringBuilder.append(sep);
            stringBuilder.append(i);
            stringBuilder.append(format);
            System.out.println("------------------------------");
            System.out.println(stringBuilder.toString());
            System.out.println("------------------------------");
            StorageReference ref = storageRef.child(stringBuilder.toString());

            ref.putFile(images_uri[i]).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(index == images_uri.length - 1)
                {
                    finish();
                }
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


    }

}