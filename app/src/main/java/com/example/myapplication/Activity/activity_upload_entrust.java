package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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

import com.example.myapplication.LoginUserData;
import com.example.myapplication.R;
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
        // 해당 액티비티 xml에 존재하는 모든 imageView를 가져온다.
        for(int i = 0; i < linearLayout.getChildCount(); i++)
        {
            ImageView imageView = (ImageView) linearLayout.getChildAt(i);
            imageList[i] = imageView;
        }

    }

    // 위탁 업로드 버튼 클릭 메소드
    public void onClickUploadEntrust(View view)
    {
        boolean ischecked = true;

        String title = edit_upload_entrust_title.getText().toString().trim();
        String price = edit_upload_entrust_price.getText().toString().trim();

        intro = edit_upload_entrust_intro.getText().toString().trim();
        caution = edit_upload_entrust_caution.getText().toString().trim();

        // 위탁 등록 시 기입해야할 정보를 전부 다 기입했는지 확인한다.
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

        // 모두 기입되었다면 db에 위탁을 등록하는 메소드를 실행한다.
        if(ischecked)
        {
            uploadEntrust(title, price);
        }
        else
        {
             Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    // 이미지 선택 버튼 클릭 메소드
    public void onClickSelectPic(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK); // 선택하는 인텐트 호출
        intent.setType("image/*"); // 타입 지정(이미지)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 멀티 초이스 true
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //URL로 데이터를 받음
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    // 위에서 실행시킨 이미지 클릭 인텐트에서 선택한 이미지들을 전달받는다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){

            if(data != null) // 선택한 데이터가 존재하는지.
            {
                if(data.getClipData() != null) // 선택한 데이터가 존재하는지.
                {
                    ClipData clipData = data.getClipData();
                    if(clipData.getItemCount() == 5) // 선택한 이미지가 5개인경우.
                    {
                        // 이미지의 URI를 구하고 이미지 뷰에 출력
                        for(int i = 0; i < clipData.getItemCount(); i++)
                        {
                            Uri uri = clipData.getItemAt(i).getUri();
                            images_uri[i] = uri;
                            imageList[i].setImageURI(uri);
                        }
                        // 선택한 이미지가 반환되는 시간을 해쉬코드로 변환(이미지를 저장소에 저장하기 위해 사용)
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
    // 위탁 정보를 db에 등록
    private void uploadEntrust(String title, String price)
    {

        // Key와 Value를 가지는 맵
        Map<String, Object> entrust = new HashMap<>();
        // 위에서 만든 맵 변수에 데이터 삽입
        entrust.put("address", LoginUserData.getAddress());
        entrust.put("address_detail", LoginUserData.getAddress_detail());
        entrust.put("geoHash", LoginUserData.getGeoHash());
        entrust.put("geoPoint", LoginUserData.getGeoPoint());
        entrust.put("images_num", hashcode);
        entrust.put("name", LoginUserData.getUser_name());
        entrust.put("title", title);
        entrust.put("price", price);
        entrust.put("uid", auth.getUid());

        // db에 업로드
        DocumentReference ref = db.collection("entrust_list").document();
                ref.set(entrust)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 위탁을 등록했으므로 더 이상 등록하지 못하도록
                        // 해당 user 정보에 sitter_entrust 항목을 true로 업데이트 하여
                        // 위탁을 등록했다고 표시.
                        updateUserData();
                        // 위탁 목록에 위탁에 위탁 상세정보까지 조회할 필요가 없고 리소스가 낭비되므로
                        // 해당 위탁에 대한 세부정보를 표시할 때 따로 불러오기 위해 따로 detail 컬렉션을 만들어서 저장.
                        uploadEntrustDetail(ref.getId());
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

    // 위탁 세부정보를 db에 등록하는 메소드
    private void uploadEntrustDetail(String refId)
    {
        // Key와 Value를 가지는 맵
        Map<String, Object> entrust = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        entrust.put("intro", intro);
        entrust.put("caution", caution);
        System.out.println("111111111111111133333333333333333");
        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        db.collection("entrust_list").document(refId).collection("detail").document("content")
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

    // 저장소에 선택한 이미지를 업로드하는 메소드
    private void uploadEntrustImages()
    {

        String firstPathSegment = "images_entrust/"; // 저장소 기본 경로
        String format = ".jpg"; // 이미지 형식
        String sep = "_"; // 분할 양식.

        // 다이얼로그로 업로드 진행사항을 표시
        AlertDialog.Builder progressBuilder = new AlertDialog.Builder(this)
                .setTitle(null)
                .setMessage("업로드 중...");

        AlertDialog progress = progressBuilder.create();
        progress.show();

        // 이미지 경로와 이미지 명을 stringbuilder로 합쳐서 만들고
        // 만들어진 경로와 이미지 명으로 저장소에 이미지를 업로드.
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
                // 업로드 완료시 다이얼로그와 액티비티 종료.
                if(index == images_uri.length - 1)
                {
                    progress.dismiss();
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
    // 위탁이 등록되고 해당 유저의 db에서 sitter_entrust 항목을 true로 변경시켜주는 메소드.
    private void updateUserData()
    {
        db.collection("users").document(auth.getUid())
                .update("sitter_entrust", true)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        LoginUserData.setSitter_entrust(true);
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

}