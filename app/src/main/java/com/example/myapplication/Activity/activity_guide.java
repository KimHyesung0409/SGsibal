package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

// 서비스 안내를 표시하기 위한 액티비티이므로 별도의 코드가 없다.
public class activity_guide extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        Button return_main_client2;
        return_main_client2 = (Button)findViewById(R.id.return_main_client2);
        return_main_client2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    public void onClickOfferEstimate(View view)
    {

    }
}
