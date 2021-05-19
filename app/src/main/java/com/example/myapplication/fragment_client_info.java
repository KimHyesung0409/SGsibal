package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import static android.app.Activity.RESULT_OK;


public class fragment_client_info extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private static final int REQUEST_CODE = 0;
    ViewGroup viewGroup;

    TextView client_info_name, client_info_gender, client_info_birth, client_info_pnum,
            client_info_address, client_info_address_detail, client_info_email;

    String client_name, client_birth, client_pnum,
            client_address, client_address_detail, client_email;

    Boolean client_gender;

    FirebaseAuth auth;
    FirebaseFirestore db;
    String uid;

    Button change_pwd_client, return_profile_client;

    private Double lat;
    private Double lon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_client_info, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

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
                        client_gender = document.getBoolean("gender");
                        if(client_gender == true){
                            client_info_gender.setText("남성");
                        }else{
                            client_info_gender.setText("여성");
                        }

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

        return viewGroup;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment_profile_client fragment_profile = new fragment_profile_client();

        switch (view.getId()){

            case R.id.change_pwd_client :
                String pwd_change_email = auth.getCurrentUser().getEmail();
                auth.sendPasswordResetEmail(pwd_change_email);
                Toast.makeText(getActivity(),"메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.return_profile_client :
                fragmentTransaction.replace(R.id.layout_main_frame, fragment_profile).commit();
                break;

        }

    }

    @Override
    public boolean onLongClick(View view) {
        AlertDialog.Builder dlg_client = new AlertDialog.Builder(getActivity());

        switch (view.getId()) {

            case R.id.client_info_address:
                Intent intnet_change_address = new Intent(getActivity(), activity_popup_address.class);
                startActivityForResult(intnet_change_address, REQUEST_CODE);

                break;

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
    }
}