package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.activity_reserve_detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_reserve_detail_sitter extends Fragment {

    ViewGroup viewGroup;

    private TextView textview_reserve_detail_sitter_name;
    private TextView textview_reserve_detail_sitter_gender;
    private TextView textview_reserve_detail_sitter_birth;
    private TextView textview_reserve_detail_sitter_phone;
    private TextView textview_reserve_detail_sitter_email;

    private String[] sitterData;

    private CircleImageView reserve_detail_sitter_info_image;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_detail_sitter, container, false);

        textview_reserve_detail_sitter_name = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_name);
        textview_reserve_detail_sitter_gender = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_gender);
        textview_reserve_detail_sitter_birth = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_birth);
        textview_reserve_detail_sitter_phone = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_phone);
        textview_reserve_detail_sitter_email = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_sitter_email);

        reserve_detail_sitter_info_image = (CircleImageView)viewGroup.findViewById(R.id.reserve_detail_sitter_info_image);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        activity_reserve_detail activity = (activity_reserve_detail) getActivity();
        sitterData = activity.getSitterFragment();

        setReserveDetailSitter();

        return viewGroup;
    }

    public void setReserveDetailSitter()
    {
        textview_reserve_detail_sitter_name.setText(sitterData[0]);
        textview_reserve_detail_sitter_gender.setText(sitterData[1]);
        textview_reserve_detail_sitter_birth.setText(sitterData[2]);
        textview_reserve_detail_sitter_phone.setText(sitterData[3]);
        textview_reserve_detail_sitter_email.setText(sitterData[4]);

        String path = "images_profile/";
        String format = ".jpg";

        // 파이어 스토리지 레퍼런스로 파일을 참조한다.
        StorageReference child = storageRef.child(path + sitterData[5] + format);

        // 참조한 파일의 다운로드 링크가 성공적으로 구해지면 Glide를 이용해 이미지뷰에 로딩한다.
        child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(getActivity())
                            .load(task.getResult())
                            .fitCenter()
                            .into(reserve_detail_sitter_info_image);
                } else {

                }
            }
        });
    }

}