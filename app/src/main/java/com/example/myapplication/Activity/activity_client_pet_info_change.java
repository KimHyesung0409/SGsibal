package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_client_pet_info_change extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final int REQUEST_CODE_GALLERY = 0;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText change_edit_pet_name;
    private EditText change_edit_pet_species;
    private EditText change_edit_pet_detail_species;
    private EditText change_edit_pet_age;
    private EditText change_edit_pet_mbti;
    private EditText change_edit_pet_info;

    private RadioGroup radioGroup_change_pet_info;
    private boolean gender = false;

    private String pet_id;

    private CircleImageView change_edit_pet_image;

    private boolean select_image = false;
    private Uri images_uri;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pet_info_change);

        // --------- 액션바, 스위치 버튼 설정 ----------
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);
        // ---------------------------------------------

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        // 인텐트를 얻음
        // 여기에 정보가 담겨있음
        // 수정할 반려동물의 정보를 intent를 통해 전달받는다.
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String species = intent.getStringExtra("species");
        String detail_species = intent.getStringExtra("detail_species");
        String age = intent.getStringExtra("age");
        String mbti = intent.getStringExtra("mbti");
        String info = intent.getStringExtra("info");
        //pet_id 는 업데이트에 사용하기 위해서 전역 변수.
        pet_id = intent.getStringExtra("pet_id");

        change_edit_pet_name = (EditText) findViewById(R.id.change_edit_pet_name);
        change_edit_pet_species = (EditText) findViewById(R.id.change_edit_pet_species);
        change_edit_pet_detail_species = (EditText) findViewById(R.id.change_edit_pet_detail_species);
        change_edit_pet_age = (EditText) findViewById(R.id.change_edit_pet_age);
        change_edit_pet_mbti = (EditText) findViewById(R.id.change_edit_pet_mbti);
        change_edit_pet_info = (EditText) findViewById(R.id.change_edit_pet_info);

        radioGroup_change_pet_info = (RadioGroup)findViewById(R.id.radioGroup_change_pet_info);
        radioGroup_change_pet_info.setOnCheckedChangeListener(this);

        // intent로 전달받은 반려동물 정보를 출력한다.
        change_edit_pet_name.setText(name);
        change_edit_pet_species.setText(species);
        change_edit_pet_detail_species.setText(detail_species);
        change_edit_pet_age.setText(age);
        change_edit_pet_mbti.setText(mbti);
        change_edit_pet_info.setText(info);

        change_edit_pet_image = (CircleImageView)findViewById(R.id.change_edit_pet_image);

    }

    public void onClickChangePetImage(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK); // 선택하는 인텐트 호출
        intent.setType("image/*"); // 타입 지정(이미지)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 멀티 초이스 false
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //URL로 데이터를 받음
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    // 수정버튼 클릭 메소드
    public void onClickPetInfoChange(View view)
    {
        // 모든 반려동물 정보를 불러오고 db에 등록한다.
        String name = change_edit_pet_name.getText().toString().trim();
        String age = change_edit_pet_age.getText().toString().trim();
        String species = change_edit_pet_species.getText().toString().trim();
        String detail_species = change_edit_pet_detail_species.getText().toString().trim();
        String mbti = change_edit_pet_mbti.getText().toString().trim();
        String info = change_edit_pet_info.getText().toString().trim();

        // 불러온 반려동물 정보를 HashMap으로 만들어 db에 등록한다.
        Map<String, Object> pet = new HashMap<>();

        pet.put("name", name);
        pet.put("gender", gender);
        pet.put("age", age);
        pet.put("species", species);
        pet.put("detail_species", detail_species);
        pet.put("mbti", mbti);
        pet.put("info", info);

        // 유저 - 반려동물 목록 컬렉션에 위에서 만든 반려동물 HashMap정보를 등록한다.
        db.collection("users").document(auth.getUid())
                .collection("pet_list").document(pet_id)
                .update(pet)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    // 등록에 성공하면 액티비티를 종료한다.
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        uploadPetImage();
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
    // 반려동물의 성별을 설정하기 위한 메소드
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {

        switch (checkedId)
        {
            case R.id.radiobutton_change_pet_info_male:

                gender = true;

                break;

            default:

                gender = false;
        }
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
                            change_edit_pet_image.setImageURI(uri);
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
    private void uploadPetImage()
    {
        String firstPathSegment = "images_pet/"; // 저장소 기본 경로
        String format = ".jpg"; // 이미지 형식

        // 이미지 경로와 이미지 명을 stringbuilder로 합쳐서 만들고
        // 만들어진 경로와 이미지 명으로 저장소에 이미지를 업로드.
        StringBuilder stringBuilder = new StringBuilder(firstPathSegment);
        stringBuilder.append(pet_id);
        stringBuilder.append(format);

        // 다이얼로그로 업로드 진행사항을 표시
        AlertDialog.Builder progressBuilder = new AlertDialog.Builder(this)
                .setTitle(null)
                .setMessage("업로드 중...");

        AlertDialog progress = progressBuilder.create();
        progress.show();

        StorageReference ref = storageRef.child(stringBuilder.toString());

        try
        {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), images_uri);
            Bitmap.createScaledBitmap(bitmap, 64, 64, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // 업로드 완료시 다이얼로그와 액티비티 종료.
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
        catch (IOException e)
        {
            e.printStackTrace();
            progress.dismiss();
        }

    }

}
