package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.example.myapplication.Fragment.fragment_reserve_auto;
import com.example.myapplication.Fragment.fragment_reserve_estimate;
import com.example.myapplication.Fragment.fragment_reserve_favorites;
import com.example.myapplication.Fragment.fragment_reserve_visit_1;
import com.example.myapplication.Fragment.fragment_reserve_visit_2;
import com.example.myapplication.Fragment.fragment_reserve_visit_3;
import com.example.myapplication.Fragment.fragment_reserve_visit_4;

public class activity_reserve_visit extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    public static final int REQUEST_CODE = 10;
    public static final int REQUEST_CODE_2 = 11;
    public static final int REQUEST_CODE_3 = 12;
    public static final int REQUEST_CODE_4 = 13;
    public static final int REQUEST_CODE_5 = 14;

    private static final int PROGRESS_MAX = 3;

    public static final int TAG_NONE = 1000;
    public static final int TAG_AUTO = 1001;
    public static final int TAG_ESTIMATE = 1002;
    public static final int TAG_HISTORY = 1003;
    public static final int TAG_SEARCH = 1004;
    public static final int TAG_FAVORITES = 1005;
    public static final int TAG_ENTRUST = 1006;

    private int status = TAG_NONE;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private com.example.myapplication.Fragment.fragment_reserve_visit_1 fragment_reserve_visit_1 = new fragment_reserve_visit_1();
    private com.example.myapplication.Fragment.fragment_reserve_visit_2 fragment_reserve_visit_2 = new fragment_reserve_visit_2();
    private com.example.myapplication.Fragment.fragment_reserve_visit_3 fragment_reserve_visit_3 = new fragment_reserve_visit_3();
    /*
    private com.example.myapplication.Fragment.fragment_reserve_visit_4 fragment_reserve_visit_4 = new fragment_reserve_visit_4();
    private Fragment fragment_reserve_list[] = {fragment_reserve_visit_1, fragment_reserve_visit_2,
            fragment_reserve_visit_3, fragment_reserve_visit_4};
    */
    private Fragment fragment_reserve_list[] = {fragment_reserve_visit_1, fragment_reserve_visit_2,
            fragment_reserve_visit_3};

    private SeekBar seekBar;
    private TextView TextView_progress[];
    private Fragment fragment_current = null;
    private int current_progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_visit);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);

        replaceFragment(fragment_reserve_visit_1, TAG_NONE);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        ViewGroup layout_progress_visit = (ViewGroup) findViewById(R.id.layout_progress_visit);
        TextView_progress = new TextView[3];

        // seekbar 와 관련된 TextView
        for(int i = 0; i < layout_progress_visit.getChildCount(); i++)
        {
            TextView_progress[i] = (TextView)layout_progress_visit.getChildAt(i);
            TextView_progress[i].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.seekbar_background));
        }

        TextView_progress[0].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.seekbar_progress));
    }

    // 파라미터로 전달 받은 프래그먼트와 태그를 사용하여 화면을 전환하고 현재 프래그먼트와 태그를 저장한다.
    public void replaceFragment(Fragment fragment, int tag)
    {
        fragment_current = fragment;
        status = tag;
        fragmentManager.beginTransaction().replace(R.id.layout_reserve_visit_main, fragment_current).commitAllowingStateLoss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // 반려동물 추가 후 프래그먼트의 리스트뷰를 리프레쉬.
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            fragment_reserve_visit_1.refreshListView();
        }

        // 주소 변경 후 해당 프래그먼트의 EditText의 text를 수정.
        if(requestCode == REQUEST_CODE_2 && resultCode == RESULT_OK){

            String road_address = data.getStringExtra("road_address");
            String str_lat = data.getStringExtra("lat");
            String str_lon = data.getStringExtra("lon");

            Double lat = Double.parseDouble(str_lat);
            Double lon = Double.parseDouble(str_lon);

            ((fragment_reserve_auto)fragment_current).setCenter(lat, lon, road_address);
        }
        // 자동 매칭 완료 후 액티비티 종료
        if(requestCode == REQUEST_CODE_3 && resultCode == RESULT_OK)
        {
            finish();
        }
        // 즐겨찾기 삭제 이후 재호출
        if(requestCode == REQUEST_CODE_4 && resultCode == RESULT_OK)
        {
            replaceFragment(new fragment_reserve_favorites(), TAG_FAVORITES);
        }
        // 견적서 등록 완료 후 액티비티 종료
        if(requestCode == REQUEST_CODE_5 && resultCode == RESULT_OK)
        {
            finish();
        }

    }

    // seekbar 메소드 오버라이드.
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // seekbar와 관련된 TextView의 색상을 설정.
        for(int i = 0; i < TextView_progress.length; i++)
        {
            if(i == progress)
            {
                TextView_progress[i].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.seekbar_progress));

            }
            else
            {
                TextView_progress[i].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.seekbar_background));
            }
        }
        // 액티비티에서 해당 프래그먼트에서 설정했던 데이터들을 저장해야함.

        // 프로그레스가 가리키는 프래그먼트로 전환.
        replaceFragment(fragment_reserve_list[progress], TAG_NONE);
        // 현재 프로그래스 재설정
        current_progress = progress;

    }

    // seekbar 메소드 오버라이드. - 사용안함.
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    // seekbar 메소드 오버라이드. - 사용안함.
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    // 다음 버튼.
    public void onClickNext(View view)
    {
        // 매칭방법을 선택한 경우. ( 즉 매칭 방법을 선택해서 해당 프래그먼트가 출력된 경우)
        if(status != TAG_NONE)
        {

            switch (status)
            {
                case TAG_AUTO :

                    // 매칭 방법이 자동일 때 발생하는 이벤트

                    break;

                case TAG_ESTIMATE :

                    // 매칭 방법이 견적서 작성일 때 발생하는 이벤트
                    ((fragment_reserve_estimate)fragment_current).uploadEstimate();

                    break;

                case TAG_HISTORY :

                    // 매칭 방법이 사용 기록일 때 발생하는 이벤트

                    break;

                case TAG_SEARCH :

                    // 매칭 방법이 검색일 때 발생하는 이벤트

                    break;

                case TAG_FAVORITES :

                    // 매칭 방법이 즐겨찾기일 때 발생하는 이벤트

                    break;

                case TAG_ENTRUST :

                    //매칭 방법이 위탁일 때 발생하는 이벤트

                    break;

            }

        }
        else // 매칭방법을 선택하지 않았을 경우.
        {
            // 액티비티에서 해당 프래그먼트에서 설정했던 데이터들을 저장해야함.
            //saveCurrentData();
            // 다음 프로그레스로 이동.
            nextProgress();
        }

    }

    // 뒤로가기 버튼.
    @Override
    public void onBackPressed() {

        // 매칭방법을 선택한 경우. ( 즉 매칭 방법을 선택해서 해당 프래그먼트가 출력된 경우)
        if(status != TAG_NONE)
        {
            // 해당 매칭 방법을 취소한다는 의미이므로 매칭방법 선택 프래그먼트를 호출하면 된다.
            replaceFragment(fragment_reserve_list[seekBar.getProgress()], TAG_NONE);
        }
        else // 매칭방법을 선택하지 않았을 경우.
        {
            // 액티비티에서 해당 프래그먼트에서 설정했던 데이터들을 저장해야함.
            //saveCurrentData();
            // 이전 프로그레스로 이동.
            preProgress();
        }

    }

    // 이전 프로그레스로 이동하는 메소드.
    public void preProgress()
    {
        // 현재 프로그레스가 최소 프로그레스보다 크면
        if(current_progress > 0)
        {
            // current_progress-- 로 안하는 이유는 onProgressChanged 메소드에서 current_progress = progress 하기 때문.
            seekBar.setProgress(current_progress - 1);
        }
        else
        {
            finish();
        }

    }

    //  다음 프로그레스로 이동하는 메소드.
    public void nextProgress()
    {
        // 현재 프로그레스가 최대 프로그레스보다 작으면
        if(current_progress < PROGRESS_MAX)
        {
            // current_progress++ 로 안하는 이유는 onProgressChanged 메소드에서 current_progress = progress 하기 때문.
            seekBar.setProgress(current_progress + 1);
        }
    }

}