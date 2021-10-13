package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.NotificationMessaging;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_add_pet extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final int REQUEST_CODE_GALLERY = 0;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText edit_pet_name;
    private EditText edit_pet_species;
    private EditText edit_pet_age;
    private EditText edit_pet_detail_species;
    private EditText edit_pet_mbti;
    private EditText edit_pet_info;

    private RadioGroup radioGroup_add_pet;

    private boolean pet_gender = false;

    private String uid;

    private CircleImageView edit_pet_image;

    private boolean select_image = false;
    private Uri images_uri;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        // --------- 액션바, 스위치 버튼 설정 ----------
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);
        // ---------------------------------------------

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        uid = auth.getUid();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        edit_pet_name = (EditText)findViewById(R.id.edit_pet_name);
        edit_pet_species = (EditText)findViewById(R.id.edit_pet_species);
        edit_pet_age = (EditText)findViewById(R.id.edit_pet_age);
        edit_pet_detail_species = (EditText)findViewById(R.id.edit_pet_detail_species);
        edit_pet_mbti = (EditText)findViewById(R.id.edit_pet_mbti);
        edit_pet_info = (EditText)findViewById(R.id.edit_pet_info);

        radioGroup_add_pet = (RadioGroup)findViewById(R.id.radioGroup_add_pet);
        radioGroup_add_pet.setOnCheckedChangeListener(this);

        edit_pet_image = (CircleImageView)findViewById(R.id.edit_pet_image);

    }

    // 입력한 반려동물 정보를 DB에 등록하는 메소드
    public void onClickAddPet(View view)
    {
            String pet_name = edit_pet_name.getText().toString().trim();
            String pet_species = edit_pet_species.getText().toString().trim();
            String pet_age = edit_pet_age.getText().toString().trim();
            String pet_detail_species = edit_pet_detail_species.getText().toString().trim();
            String pet_mbti = edit_pet_mbti.getText().toString().trim();
            String pet_info = edit_pet_info.getText().toString().trim();

            // Key와 Value를 가지는 맵
            Map<String, Object> data = new HashMap<>();
            // 위에서 만든 맵(user) 변수에 데이터 삽입
            data.put("name", pet_name);
            data.put("gender", pet_gender);
            data.put("species", pet_species);
            data.put("age", pet_age);
            data.put("detail_species", pet_detail_species);
            data.put("mbti", pet_mbti);
            data.put("info", pet_info);

            // db에 업로드
            // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
            // db에 등록이 완료되면 종료.
            DocumentReference ref = db.collection("users").document(uid).collection("pet_list").document();
            ref
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(activity_add_pet.this, "반려동물 등록 완료", Toast.LENGTH_SHORT).show();

                            uploadPetImage(ref.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity_add_pet.this, "반려동물 등록 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    // 반려동물의 성별을 설정하기 위한 메소드
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {

        switch (checkedId)
        {
            case R.id.radiobutton_add_pet_male:

                pet_gender = true;

                break;

            default:

                pet_gender = false;
        }
    }

    public void onClickAddPetImage(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK); // 선택하는 인텐트 호출
        intent.setType("image/*"); // 타입 지정(이미지)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 멀티 초이스 false
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //URL로 데이터를 받음
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    // 위에서 실행시킨 이미지 클릭 인텐트에서 선택한 이미지들을 전달받는다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
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
                            edit_pet_image.setImageURI(uri);
                        }

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
    private void uploadPetImage(String pet_id)
    {
        String firstPathSegment = "images_pet/"; // 저장소 기본 경로
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
        stringBuilder.append(pet_id);
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
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();

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