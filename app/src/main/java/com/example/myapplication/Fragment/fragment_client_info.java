package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.activity_popup_address;
import com.example.myapplication.Gender;
import com.example.myapplication.R;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class fragment_client_info extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_CODE_GALLERY = 1;
    ViewGroup viewGroup;

    TextView client_info_name, client_info_gender, client_info_birth, client_info_pnum,
            client_info_address, client_info_address_detail, client_info_email;

    String client_name, client_birth, client_pnum,
            client_address, client_address_detail, client_email;

    FirebaseAuth auth;
    FirebaseFirestore db;
    String uid;

    Button change_pwd_client, return_profile_client, client_info_address_change_button;

    CircleImageView client_info_image;

    private Double lat;
    private Double lon;

    private Uri images_uri;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_client_info, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        uid = auth.getUid();

        client_info_name = viewGroup.findViewById(R.id.client_info_name);

        client_info_gender = viewGroup.findViewById(R.id.client_info_gender);

        client_info_birth = viewGroup.findViewById(R.id.client_info_age);

        client_info_pnum = viewGroup.findViewById(R.id.client_info_pnum);

        client_info_address = viewGroup.findViewById(R.id.client_info_address);
        client_info_address_detail = viewGroup.findViewById(R.id.client_info_address_detail);
        client_info_address.setOnLongClickListener(this);
        client_info_address_detail.setOnLongClickListener(this);

        client_info_email = viewGroup.findViewById(R.id.client_info_email);

        change_pwd_client = viewGroup.findViewById(R.id.change_pwd_client);
        change_pwd_client.setOnClickListener(this);

        return_profile_client = viewGroup.findViewById(R.id.return_profile_client);
        return_profile_client.setOnClickListener(this);

        client_info_address_change_button = viewGroup.findViewById(R.id.client_info_address_change_button);
        client_info_address_change_button.setOnClickListener(this);

        client_info_image = viewGroup.findViewById(R.id.client_info_image);
        client_info_image.setOnClickListener(this);

        String path = "images_profile/";
        String format = ".jpg";

        // 파이어 스토리지 레퍼런스로 파일을 참조한다.
        StorageReference child = storageRef.child(path + uid + format);

        // 참조한 파일의 다운로드 링크가 성공적으로 구해지면 Glide를 이용해 이미지뷰에 로딩한다.
        child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(getActivity())
                            .load(task.getResult())
                            .fitCenter()
                            .into(client_info_image);
                } else {

                }
            }
        });

        getUserData();

        return viewGroup;
    }

    // 고객의 정보를 조회하는 메소드.
    private void getUserData()
    {

        db.collection("users").document(auth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    // task.getResult().get ~~~ 하는 것 보다는
                    // DocumentSnapshot document = task.getResult(); 로 관리하는게 쉬움.
                    DocumentSnapshot document = task.getResult();

                    if (document.exists())
                    {
                        //이름
                        client_name = document.getString("name");
                        client_info_name.setText(client_name);

                        //성별
                        String client_gender = Gender.getGender(document.getBoolean("gender"));
                        client_info_gender.setText(client_gender);


                        //생년월일
                        //DB에 생년월일(birth)이 문자열로 0000년 0월 0일로 저장되어 있는데,
                        //여기서 0000년만 빼와서 현재연도(2021? getDate?)에서 빼고 한국나이로 +1해서 나타내는법을 모르겠어요
                        //일단은 생년월일로 했는데, 나이로 바꿀려면 위에 말한 것처럼 해야할 것 같아요
                        client_birth = document.getString("birth");
                        client_info_birth.setText(client_birth);

                        //휴대폰 번호
                        client_pnum = document.getString("phone");
                        client_info_pnum.setText(client_pnum);

                        //주소 : 파싱 없이 하기 위해, address, address_detail 분리
                        client_address = document.getString("address");
                        client_info_address.setText(client_address);

                        client_address_detail = document.getString("address_detail");
                        client_info_address_detail.setText(client_address_detail);

                        // 이메일의 경우 auth.getCurrentUser().getEmail(); 로 가져올 수 있음.
                        client_email = auth.getCurrentUser().getEmail();
                        client_info_email.setText(client_email);

                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No document exist", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });
    }

    // 클릭 메소드
    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment_profile_client fragment_profile = new fragment_profile_client();

        switch (view.getId()){
            // 비밀번호 변경 버튼을 클릭한 경우
            // 비밀번호 변경 메일을 보낸다.
            case R.id.change_pwd_client :
                String pwd_change_email = auth.getCurrentUser().getEmail();
                auth.sendPasswordResetEmail(pwd_change_email);
                Toast.makeText(getActivity(),"메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                break;

            // 주소 수정버튼을 클릭한 경우
            case R.id.client_info_address_change_button :
                Intent intnet_change_address = new Intent(getActivity(), activity_popup_address.class);
                startActivityForResult(intnet_change_address, REQUEST_CODE);
                break;

            // 확인 버튼을 클릭한 경우.
            // 고객 프로필 화면으로 되돌아간다.
            case R.id.return_profile_client :
                fragmentTransaction.replace(R.id.layout_main_frame, fragment_profile).commit();
                break;

            case R.id.client_info_image :
                Intent intent = new Intent(Intent.ACTION_PICK); // 선택하는 인텐트 호출
                intent.setType("image/*"); // 타입 지정(이미지)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 멀티 초이스 false
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //URL로 데이터를 받음
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
                break;

        }

    }
    // 롱클릭 메소드
    @Override
    public boolean onLongClick(View view) {

        // 다이얼로그 설정.

        AlertDialog.Builder dlg_client = new AlertDialog.Builder(getActivity());

        switch (view.getId()) {
            // 주소 정보를 클릭한 경우
            // 주소 정보 팝업 액티비티를 호출한다.
            case R.id.client_info_address:
                Intent intnet_change_address = new Intent(getActivity(), activity_popup_address.class);
                startActivityForResult(intnet_change_address, REQUEST_CODE);

                break;
            // 세부주소 정보를 클릭한 경우
            // 세부주소 정보를 수정할 수 있는 다이얼 로그를 출력하고 수정 내용을 db에 업데이트 한다.
            case R.id.client_info_address_detail:
                final EditText change_address_detail = new EditText(getActivity());
                dlg_client.setTitle("세부주소 변경")
                        .setView(change_address_detail)
                        .setPositiveButton("변경", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String change_ad_detail = change_address_detail.getText().toString();
                                client_info_address_detail.setText(change_ad_detail);
                                db.collection("users").document(uid)
                                        .update("address_detail", change_address_detail.getText().toString());
                                Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                dlg_client.show();
                break;
        }
        return false;
    }
    // 위에서 호출한 주소 정보 팝업 액티비티의 실행이 종료되면.
    // 주소 정보를 반환받아 db에 업데이트 한다.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String postal_code = data.getStringExtra("postal_code");
            String road_address = data.getStringExtra("road_address");
            //String jibun_address = data.getStringExtra("jibun_address");
            String str_lat = data.getStringExtra("lat");
            String str_lon = data.getStringExtra("lon");

            lat = Double.parseDouble(str_lat);
            lon = Double.parseDouble(str_lon);

            // GeoPoint 좌표 / lat : 위도 lon : 경도
            GeoPoint geoPoint = new GeoPoint(lat, lon);

            // GeoHash 를 통해 nearby를 구현할 수 있다.
            String geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon));

            client_info_address.setText(road_address);
            db.collection("users").document(uid)
                    .update("address", client_info_address.getText().toString(),
                            "geoPoint", geoPoint,
                            "geoHash", geoHash,
                            "postal", postal_code)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("change address : ", "complete!");
                            }else{
                                Log.d("chaneg address : ", "failed!");
                            }
                        }
                    });
        }

        // 위에서 실행시킨 이미지 클릭 인텐트에서 선택한 이미지들을 전달받는다.
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK)
        {
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
                            client_info_image.setImageURI(uri);
                        }
                        uploadProfileImages();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "1개의 이미지를 선택해야 합니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }


    }

    private void uploadProfileImages()
    {
        String firstPathSegment = "images_profile/"; // 저장소 기본 경로
        String format = ".jpg"; // 이미지 형식
        // 다이얼로그로 업로드 진행사항을 표시
        AlertDialog.Builder progressBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(null)
                .setMessage("업로드 중...");

        AlertDialog progress = progressBuilder.create();
        progress.show();
        // 이미지 경로와 이미지 명을 stringbuilder로 합쳐서 만들고
        // 만들어진 경로와 이미지 명으로 저장소에 이미지를 업로드.
        StringBuilder stringBuilder = new StringBuilder(firstPathSegment);
        stringBuilder.append(uid);
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