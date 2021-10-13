package com.example.myapplication.Fragment;

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
import com.example.myapplication.Activity.activity_sitter_estimate_detail;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_reserve_detail_pet extends Fragment {

    ViewGroup viewGroup;

    private TextView textview_reserve_detail_pet_name;
    private TextView textview_reserve_detail_pet_gender;
    private TextView textview_reserve_detail_pet_age;
    private TextView textview_reserve_detail_pet_species;
    private TextView textview_reserve_detail_pet_species_detail;
    private TextView textview_reserve_detail_pet_mbti;
    private String[] petData;

    private CircleImageView reserve_detail_pet_info_image;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_reserve_detail_pet, container, false);

        textview_reserve_detail_pet_name = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_name);
        textview_reserve_detail_pet_gender = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_gender);
        textview_reserve_detail_pet_age = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_age);
        textview_reserve_detail_pet_species = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_species);
        textview_reserve_detail_pet_species_detail = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_species_detail);
        textview_reserve_detail_pet_mbti = (TextView)viewGroup.findViewById(R.id.textview_reserve_detail_pet_mbti);

        reserve_detail_pet_info_image = (CircleImageView)viewGroup.findViewById(R.id.reserve_detail_pet_info_image);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if(getActivity() instanceof activity_reserve_detail)
        {

            activity_reserve_detail activity = (activity_reserve_detail) getActivity();
            petData = activity.getPetFragment();
        }
        else
        {
            activity_sitter_estimate_detail activity = (activity_sitter_estimate_detail) getActivity();
            petData = activity.getPetFragment();
        }

        setReserveDetailPet();

        return viewGroup;
    }

    public void setReserveDetailPet()
    {
        textview_reserve_detail_pet_name.setText(petData[0]);
        textview_reserve_detail_pet_gender.setText(petData[1]);
        textview_reserve_detail_pet_age.setText(petData[2]);
        textview_reserve_detail_pet_species.setText(petData[3]);
        textview_reserve_detail_pet_species_detail.setText(petData[4]);
        textview_reserve_detail_pet_mbti.setText(petData[5]);

        String path = "images_pet/";
        String format = ".jpg";

        // 파이어 스토리지 레퍼런스로 파일을 참조한다.
        StorageReference child = storageRef.child(path + petData[6] + format);

        // 참조한 파일의 다운로드 링크가 성공적으로 구해지면 Glide를 이용해 이미지뷰에 로딩한다.
        child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(getActivity())
                            .load(task.getResult())
                            .fitCenter()
                            .into(reserve_detail_pet_info_image);
                } else {

                }
            }
        });
    }

}