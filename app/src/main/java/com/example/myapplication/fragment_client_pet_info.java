package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class fragment_client_pet_info extends Fragment implements OnCustomClickListener{

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_CODE2 = 1;

    ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uid;
    private String pet_id;
    private ListViewItem_petlist add;
    private ListViewItem_petlist selected_pet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_client_pet_info, container, false);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); // 파이어 스토어 객체

        uid = auth.getUid();
        ListViewItem_petlist data = new ListViewItem_petlist();
        pet_id = data.getPet_id();

        recyclerView = viewGroup.findViewById(R.id.recyclerview_petlist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        getPetList();

        return viewGroup;
    }

    @Override
    public void onItemClick(View view, int position) {

        ListViewItem_petlist data_pet = (ListViewItem_petlist)adapter.getItem(position);

        String delete_pet_id = data_pet.getPet_id();
        // 해당 아이템이 추가 객체일 경우
        if(data_pet == add)
        {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, activity_add_pet.class);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
        else
        {
            //selected_pet = data;
            String pet_detail_name, pet_detail_species, pet_detail_detail_species,
                    pet_detail_age, pet_detail_mbti, pet_detail_info;
            pet_detail_name = data_pet.getName();
            pet_detail_species = data_pet.getSpecies();
            pet_detail_detail_species = data_pet.getDetail_species();
            pet_detail_age = data_pet.getAge();
            pet_detail_mbti = data_pet.getMbti();
            pet_detail_info = data_pet.getInfo();

            AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
            dlg.setTitle("반려동물 세부 정보")
                    .setMessage("이름 : " + pet_detail_name
                    +"\n 종류 : " + pet_detail_species
                    +"\n 세부 종류 : " + pet_detail_detail_species
                    +"\n 나이 : " + pet_detail_age +"개월"
                    +"\n 성격 : " + pet_detail_mbti
                    +"\n 주의사항 : " + pet_detail_info)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.collection("users").document(uid)
                                    .collection("pet_list")
                                    .document(delete_pet_id)
                                    .delete();

                            Toast.makeText(getActivity(),"삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            refreshListView();
                        }
                    })
                    .setNeutralButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 이건 왜 선언했는지 모르겠음.
                            //activity_client_pet_info_change change = new activity_client_pet_info_change();

                            Activity activity = getActivity();
                            Intent intent = new Intent(activity, activity_client_pet_info_change.class);

                            //여기서 부터는 intent로 데이터를 날려주는 부분
                            intent.putExtra("name", data_pet.getName());
                            intent.putExtra("age", data_pet.getAge());
                            intent.putExtra("species", data_pet.getSpecies());
                            intent.putExtra("detail_species", data_pet.getDetail_species());
                            intent.putExtra("mbti", data_pet.getMbti());
                            intent.putExtra("info", data_pet.getInfo());
                            intent.putExtra("pet_id", data_pet.getPet_id());


                            //activity. 을 명시하지 않으면 반환이 안되는 사례가 있어 명시를 해줬음.
                            // REQUEST_CODE_2 -> REQUEST_CODE 로 수정했는데 그 이유는 반려동물 정보를 수정하면
                            // fragment_client_pet_info 를 불러와야 하기 때문에 반려동물 삭제와 같은 코드를 보내주면 된다.
                            activity.startActivityForResult(intent, REQUEST_CODE);

                            /*db.collection("users").document(uid)
                                    .collection("pet_list").document(pet_id)
                                    .update("name", change.change_edit_pet_name,
                                            "species", change.change_edit_pet_species,
                                            "detail_species", change.change_edit_pet_detail_species,
                                            "age", change.change_edit_pet_age,
                                            "mbti", change.change_edit_pet_mbti,
                                            "info", change.change_edit_pet_info);

                             */
                        }
                    })
                    .show();
        }

    }



    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {
        System.out.println("롱클릭");
    }

    private void getPetList()
    {
        db.collection("users").document(uid).collection("pet_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ListViewItem_petlist data = new ListViewItem_petlist();

                                String pet_name = document.getString("name");
                                String pet_species = document.getString("species");
                                String pet_age = document.getString("age");
                                String pet_detail_species = document.getString("detail_species");
                                String pet_mbti = document.getString("mbti");
                                String pet_info = document.getString("info");
                                String pet_id = document.getId();

                                data.setName(pet_name);
                                data.setSpecies(pet_species);
                                data.setAge(pet_age);
                                data.setDetail_species(pet_detail_species);
                                data.setMbti(pet_mbti);
                                data.setInfo(pet_info);
                                data.setPet_id(pet_id);

                                adapter.addItem(data);
                            }
                            add = new ListViewItem_petlist();
                            add.setName("반려동물 추가");
                            adapter.addItem(add);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void refreshListView()
    {
        System.out.println("리플레시");
        adapter.clear();
        // 안해주니까 이상해짐.
        adapter.notifyDataSetChanged();
        getPetList();
    }


}