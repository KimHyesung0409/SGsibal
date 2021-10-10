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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
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

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class fragment_sitter_info extends DialogFragment implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_CODE_GALLERY = 1;
    ViewGroup viewGroup;
    TextView sitter_info_name, sitter_info_address, sitter_info_address_detail, sitter_postal,
            sitter_info_birth, sitter_info_pnum, sitter_info_email,
            sitter_info_can_pet, sitter_info_can_time, sitter_info_gender;
    String sitter_name, sitter_address, sitter_address_detail, sitter_birth,
            sitter_pnum, sitter_email, sitter_can_pet, sitter_can_time;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String uid;
    FirebaseFirestore fstore;
    Button change_pwd_sitter, return_profile_sitter, sitter_info_address_change_button;

    CircleImageView sitter_info_image;

    private Uri images_uri;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private Double lat;
    private Double lon;

    private ListView listView_sitter_can_pet;
    private ArrayList<String> can_pet_list;
    private ArrayAdapter adapter;
    private ScrollView scrollView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_sitter_info, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        uid = auth.getUid();

        sitter_info_name = viewGroup.findViewById(R.id.sitter_info_name);

        sitter_info_gender = viewGroup.findViewById(R.id.sitter_info_gender);

        sitter_info_address = viewGroup.findViewById(R.id.sitter_info_address);
        sitter_info_address_detail = viewGroup.findViewById(R.id.sitter_info_address_detail);
        sitter_info_address.setOnLongClickListener(this);
        sitter_info_address_detail.setOnLongClickListener(this);

        sitter_info_birth = viewGroup.findViewById(R.id.sitter_info_birth);


        sitter_info_pnum = viewGroup.findViewById(R.id.sitter_info_pnum);


        sitter_info_email = viewGroup.findViewById(R.id.sitter_info_email);

        listView_sitter_can_pet = viewGroup.findViewById(R.id.listview_info_can_pet);
        can_pet_list = new ArrayList<>();
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, can_pet_list);
        listView_sitter_can_pet.setAdapter(adapter);
        listView_sitter_can_pet.setOnItemClickListener(this);
        listView_sitter_can_pet.setOnItemLongClickListener(this);
        listView_sitter_can_pet.setOnTouchListener(this);

        scrollView = viewGroup.findViewById(R.id.scrollView_sitter_info);

        sitter_info_can_time = viewGroup.findViewById(R.id.sitter_info_can_time);
        sitter_info_can_time.setOnLongClickListener(this);


        change_pwd_sitter = viewGroup.findViewById(R.id.change_pwd_sitter);
        change_pwd_sitter.setOnClickListener(this);

        return_profile_sitter = viewGroup.findViewById(R.id.return_profile_sitter);
        return_profile_sitter.setOnClickListener(this);

        sitter_info_address_change_button = viewGroup.findViewById(R.id.sitter_info_address_change_button);
        sitter_info_address_change_button.setOnClickListener(this);

        sitter_info_image = viewGroup.findViewById(R.id.sitter_info_image);
        sitter_info_image.setOnClickListener(this);

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
                            .into(sitter_info_image);
                } else {

                }
            }
        });

        getUserData();

        return viewGroup;
    }

    // 펫시터의 정보를 조회하는 메소드
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
                        sitter_name = document.getString("name");
                        sitter_info_name.setText(sitter_name);

                        //파싱 없이 하기 위해, address, address_detail 분리
                        sitter_address = document.getString("address");
                        sitter_info_address.setText(sitter_address);

                        sitter_address_detail = document.getString("address_detail");
                        sitter_info_address_detail.setText(sitter_address_detail);

                        //sitter_age
                        sitter_birth = document.getString("birth");
                        sitter_info_birth.setText(sitter_birth);

                        //sitter_phonenumber
                        sitter_pnum= document.getString("phone");
                        sitter_info_pnum.setText(sitter_pnum);

                        // 이메일의 경우 auth.getCurrentUser().getEmail(); 로 가져올 수 있음.
                        sitter_email = auth.getCurrentUser().getEmail();
                        sitter_info_email.setText(sitter_email);

                        //성별
                        String sitter_gender = Gender.getGender(document.getBoolean("gender"));
                        sitter_info_gender.setText(sitter_gender);

                        //sitter_can_pet = task.getResult().getString("care_list");
                        // 가져오려는 데이터가 array 인 경우 ArrayList<?>() 로 가져와야함.
                        // 그 이후 반복자 등을 통해서 요소를 분해하여 가져와야함.
                        ArrayList<String> care_list = (ArrayList<String>)document.get("care_list");

                        can_pet_list.add("추가하기");
                        // 에러 핸들, 케어 리스트에 아무것도 없으면
                        if(care_list != null)
                        {
                            // 반복자
                            Iterator<String> iterator = care_list.iterator();

                            // 리스트에 다음 요소가 존재하면. loop
                            while (iterator.hasNext())
                            {
                                // 리스트의 요소를 가져옴
                                String care_pet = iterator.next();
                                can_pet_list.add(care_pet);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        sitter_can_time = document.getString("care_time");
                        sitter_info_can_time.setText(sitter_can_time);

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
        fragment_profile_sitter fragment_profile_sitter = new fragment_profile_sitter();

        switch (view.getId()){
            // 비밀번호 변경 버튼을 클릭한 경우
            // 비밀번호 변경 메일을 보낸다.
            case R.id.change_pwd_sitter:
                String pwd_change_email = auth.getCurrentUser().getEmail();
                auth.sendPasswordResetEmail(pwd_change_email);
                Toast.makeText(getActivity(),"메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                break;


            // 주소 수정버튼을 클릭한 경우
            case R.id.sitter_info_address_change_button :
                Intent intnet_change_address = new Intent(getActivity(), activity_popup_address.class);
                startActivityForResult(intnet_change_address, REQUEST_CODE);
                break;

            // 확인 버튼을 클릭한 경우.
            // 펫시터 프로필 화면으로 되돌아간다.
            case R.id.return_profile_sitter:
                fragmentTransaction.replace(R.id.layout_main_frame_sitter, fragment_profile_sitter).commit();
                break;

            case R.id.sitter_info_image :
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

        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        switch (view.getId()){
            // 주소 정보를 클릭한 경우
            // 주소 정보 팝업 액티비티를 호출한다.
            case R.id.sitter_info_address:
                Intent intnet_change_address = new Intent(getActivity(), activity_popup_address.class);
                startActivityForResult(intnet_change_address, REQUEST_CODE );

                break;
            // 세부주소 정보를 클릭한 경우
            // 세부주소 정보를 수정할 수 있는 다이얼 로그를 출력하고 수정 내용을 db에 업데이트 한다.
            case R.id.sitter_info_address_detail:
                final EditText change_address_detail = new EditText(getActivity());
                dlg.setTitle("세부주소 변경")
                        .setView(change_address_detail)
                        .setPositiveButton("변경", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String change_ad_detail = change_address_detail.getText().toString();
                                sitter_info_address_detail.setText(change_ad_detail);
                                db.collection("users").document(uid)
                                        .update("address_detail",change_address_detail.getText().toString());
                                Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                dlg.show();
                break;
            // 펫시터 케어 가능 시간을 클릭한 경우
            // 펫시터 케어 가능 시간을 수정할 수 있는 다이얼 로그를 출력하고 수정 내용을 db에 업데이트 한다.
            case R.id.sitter_info_can_time:
                final EditText change_can_time = new EditText(getActivity());
                change_can_time.setHint("ex)오전 11시 ~ 오후 8시");
                dlg.setTitle("케어가능한 시간 변경");
                dlg.setView(change_can_time);
                dlg.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String change_can_times = change_can_time.getText().toString();
                        sitter_info_can_time.setText(change_can_times);
                        db.collection("users").document(uid)
                                .update("care_time", change_can_time.getText().toString());
                        Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
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

            sitter_info_address.setText(road_address);
            db.collection("users").document(uid)
                    .update("address", sitter_info_address.getText().toString(),
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
                            sitter_info_image.setImageURI(uri);
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

    // 펫시터 케어 가능 리스트 아이템 클릭 메소드
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // 추가하기 아이템을 선택한 경우
        if(position == 0)
        {
            // 다이얼로그 설정.

            AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());

            final EditText add_care_list = new EditText(getActivity());
            add_care_list.setHint("ex)개");
            dlg.setTitle("반려동물의 종류 추가");
            dlg.setView(add_care_list);
            dlg.setPositiveButton("추가하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    // 추가하기 버튼을 누르면 db에 다이얼로그에 입력한 반려동물 종류를 추가한다.

                    String care_pet = add_care_list.getText().toString().trim();

                    can_pet_list.add(care_pet);
                    ArrayList<String> care_list = (ArrayList<String>) can_pet_list.clone();
                    care_list.remove(0);

                    db.collection("users").document(uid)
                            .update("care_list", care_list).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            dlg.show();

        }

    }

    // 펫시터 케어 가능 리스트 아이템 롱클릭 메소드
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        // 다이얼로그 설정.

        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());

        dlg.setTitle("항목을 삭제하시겠습니까?");
        dlg.setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // 추가하기 버튼을 누르면 해당 반려동물 종류를 db에서 제거.

                can_pet_list.remove(position);
                ArrayList<String> care_list = (ArrayList<String>) can_pet_list.clone();
                care_list.remove(0);

                db.collection("users").document(uid)
                        .update("care_list", care_list)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        adapter.notifyDataSetChanged();
                    }
                });

                Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dlg.show();

        return false;
    }

    // 리스트뷰가 터치되면 스크롤뷰의 터치를 막음.
    // 따라서 스크롤뷰 안에 리스트뷰를 스크롤 할 수 있게됨.
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        scrollView.requestDisallowInterceptTouchEvent(true);

        return false;
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