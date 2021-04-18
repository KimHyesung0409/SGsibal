package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ViewPagerItem> viewPagerItemList = new ArrayList<>();
    private int images[];

    //파이어베이스 스토리지 인스턴스
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    //파이어베이스 스토리지 레퍼런스
    private StorageReference storageRef = storage.getReference();

    public ViewPagerAdapter(Context context, int images[])
    {
        this.context = context;
        this.images = images;
    }

    public Object instantiateItem(ViewGroup container, int position)
    {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.slide_item, container, false);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.imageview_reserve_entrust);
        StorageReference child = storageRef.child("images_entrust/entrust_" + images[position] + ".jpg");
       // StorageReference child = storageRef.child("images_entrust/entrust_1.jpg");
        child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(container)
                            .load(task.getResult())
                            .override(500, 500)
                            .into(imageView);
                    Log.d("가져왔어용 : ", ""+position);
                } else {

                }
            }
        });

        imageView.getLayoutParams().width = 500;
        imageView.getLayoutParams().height = 500;
        ((ViewPager)container).addView(itemView);
        return itemView;
    }

    public void addItem(int image)
    {
        ViewPagerItem item = new ViewPagerItem();

        item.setImage(image);

        viewPagerItemList.add(item);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }

    @Override
    public int getCount()
    {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View)object);
    }

}
