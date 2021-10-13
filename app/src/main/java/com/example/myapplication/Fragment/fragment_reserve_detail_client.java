package com.example.myapplication.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.activity_reserve_detail;
import com.example.myapplication.Activity.activity_sitter_estimate_detail;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_reserve_detail_client extends Fragment {

    ViewGroup viewGroup;

    private TextView textview_reserve_detail_client_name;
    private TextView textview_reserve_detail_client_gender;
    private TextView textview_reserve_detail_client_birth;
    private TextView textview_reserve_detail_client_phone;
    private TextView textview_reserve_detail_client_email;
    private String[] clientData;

    private CircleImageView reserve_detail_client_info_image;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_detail_client, container, false);

        textview_reserve_detail_client_name = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_name);
        textview_reserve_detail_client_gender = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_gender);
        textview_reserve_detail_client_birth = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_birth);
        textview_reserve_detail_client_phone = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_phone);
        textview_reserve_detail_client_email = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_client_email);

        reserve_detail_client_info_image = (CircleImageView)viewGroup.findViewById(R.id.reserve_detail_client_info_image);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if(getActivity() instanceof activity_reserve_detail)
        {

            activity_reserve_detail activity = (activity_reserve_detail) getActivity();
            clientData = activity.getClientFragment();
        }
        else
        {
            activity_sitter_estimate_detail activity = (activity_sitter_estimate_detail) getActivity();
            clientData = activity.getClientFragment();
        }

        setReserveDetailClient();

        return viewGroup;
    }

    public void setReserveDetailClient()
    {
        textview_reserve_detail_client_name.setText(clientData[0]);
        textview_reserve_detail_client_gender.setText(clientData[1]);
        textview_reserve_detail_client_birth.setText(clientData[2]);
        textview_reserve_detail_client_phone.setText(clientData[3]);
        textview_reserve_detail_client_email.setText(clientData[4]);

        String path = "images_profile/";
        String format = ".jpg";

        // 파이어 스토리지 레퍼런스로 파일을 참조한다.
        StorageReference child = storageRef.child(path + clientData[5] + format);

        // 참조한 파일의 다운로드 링크가 성공적으로 구해지면 Glide를 이용해 이미지뷰에 로딩한다.
        child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(getActivity())
                            .load(task.getResult())
                            .fitCenter()
                            .into(reserve_detail_client_info_image);
                } else {

                }
            }
        });
    }


}