package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.VerifyString;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class activity_signup extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final int SIGNUP_PROCESS_NUM = 7;
    private static final int REQUEST_CODE = 0;

    private FirebaseAuth auth; //파이어베이스 인증 객체
    private FirebaseFirestore db; //파이어스토어 db 객체

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private EditText editText_name, editText_email, editText_password,
            editText_password_re, editText_postal, editText_address, editText_address_detail,
            editText_birth_year, editText_birth_month, editText_birth_day, editText_pnum_start,
            editText_pnum_middle,editText_pnum_end;

    private RadioGroup radioGroup_sign_up;
    private RadioButton radioButton_sign_up_male;
    private RadioButton radioButton_sign_up_female;

    private String name = "";
    private String age = "";
    private String email = "";
    private String pnum = "";
    private String password = "";
    private String password_re = "";
    private String address = "";
    private String postal = "";
    private String address_detail = "";
    private Double lat;
    private Double lon;
    private String token;
    private String phone;
    private String birth;
    private boolean gender = false; //true 면 남성 false면 여성


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance(); //파이어베이스 인증 객체
        db = FirebaseFirestore.getInstance(); //파이어베이스 클라우드 파이어 스토어 객체

        if(auth.getApp().isDefaultApp())
        {
            Toast.makeText(activity_signup.this, "디폴트 앱입니다.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(activity_signup.this, "디폴트 앱이 아닙니다.", Toast.LENGTH_SHORT).show();
        }

        editText_name = (EditText)findViewById(R.id.edit_signup_name);
        editText_email = (EditText)findViewById(R.id.edit_signup_email);
        editText_password = (EditText)findViewById(R.id.edit_signup_password);
        editText_password_re = (EditText)findViewById(R.id.edit_signup_password_re);
        editText_postal = (EditText)findViewById(R.id.edit_signup_postal);
        editText_address = (EditText)findViewById(R.id.edit_signup_address);
        editText_address_detail = (EditText)findViewById(R.id.edit_signup_address_detail);
        editText_birth_year = (EditText)findViewById(R.id.edit_signup_birth_year);
        editText_birth_month = (EditText)findViewById(R.id.edit_signup_birth_month);
        editText_birth_day = (EditText)findViewById(R.id.edit_signup_birth_day);
        editText_pnum_start = (EditText)findViewById(R.id.edit_signup_pnum_start);
        editText_pnum_middle = (EditText)findViewById(R.id.edit_signup_pnum_middle);
        editText_pnum_end = (EditText)findViewById(R.id.edit_signup_pnum_end);

        radioGroup_sign_up = (RadioGroup)findViewById(R.id.radioGroup_sign_up);
        radioButton_sign_up_male = (RadioButton)findViewById(R.id.radiobutton_sign_up_male);
        radioButton_sign_up_female = (RadioButton)findViewById(R.id.radiobutton_sign_up_female);

        radioGroup_sign_up.setOnCheckedChangeListener(this);

        if(auth.getCurrentUser() != null)
        {
            auth.signOut();
            Toast.makeText(activity_signup.this, "로그아웃.", Toast.LENGTH_SHORT).show();
        }

    }

    // 회원가입 버튼 클릭 메소드
    public void onClickRequestSignUp(View view) {
        name = editText_name.getText().toString().trim();
        email = editText_email.getText().toString().trim();
        password = editText_password.getText().toString().trim();
        password_re = editText_password_re.getText().toString().trim();
        address = editText_address.getText().toString().trim();
        postal = editText_postal.getText().toString().trim();
        address_detail = editText_address_detail.getText().toString().trim();

        String birth_year = editText_birth_year.getText().toString().trim();
        String birth_month = editText_birth_month.getText().toString().trim();
        String birth_day = editText_birth_day.getText().toString().trim();

        // 입력한 생년월일을 조합하여 하나의 문자열로 변환
        StringBuilder birthBuilder = new StringBuilder();

        birthBuilder.append(birth_year);
        birthBuilder.append("년 ");
        birthBuilder.append(birth_month);
        birthBuilder.append("월 ");
        birthBuilder.append(birth_day);
        birthBuilder.append("일");

        birth = birthBuilder.toString();

        String pnum_start = editText_pnum_start.getText().toString().trim();
        String pnum_middle = editText_pnum_middle.getText().toString().trim();
        String pnum_end = editText_pnum_end.getText().toString().trim();


        // 입력한 핸드폰번호를 조합하여 하나의 문자열로 변환
        StringBuilder phoneBuilder = new StringBuilder();

        phoneBuilder.append(pnum_start);
        phoneBuilder.append("-");
        phoneBuilder.append(pnum_middle);
        phoneBuilder.append("-");
        phoneBuilder.append(pnum_end);

        phone = phoneBuilder.toString();

        // 회원가입에 필요한 모든 항목을 채웠는지 체크하기 위한 변수
        int sign_num = 0;

        // 이름

        if (VerifyString.isValidname(name)) {
            sign_num++;
        }
        else
        {
            editText_name.requestFocus();
            Toast.makeText(this, "이름을 확인해주세요", Toast.LENGTH_SHORT).show();
        }

        // 생년월일 아직은 빈칸과 null 만 체크했지만 나중에는 더 상세하게 따져야함.
        if (!VerifyString.isEmptyAndNull(birth_year) & !VerifyString.isEmptyAndNull(birth_month) & !VerifyString.isEmptyAndNull(birth_day)) {
            sign_num++;
        }
        else
        {
            editText_birth_year.requestFocus();
            Toast.makeText(this, "생년월일을 확인해주세요", Toast.LENGTH_SHORT).show();
        }

        // 이메일
        if (VerifyString.isValidEmail(email)) {
            sign_num++;
        }
        else
        {
            editText_email.requestFocus();
            Toast.makeText(this, "이메일을 확인해주세요", Toast.LENGTH_SHORT).show();
        }
        // 핸드폰 번호 아직은 빈칸과 null 만 체크했지만 나중에는 더 상세하게 따져야함.
        if (!VerifyString.isEmptyAndNull(pnum_start) & !VerifyString.isEmptyAndNull(pnum_middle) & !VerifyString.isEmptyAndNull(pnum_end)) {
            sign_num++;
        }
        else
        {
            editText_pnum_start.requestFocus();
            Toast.makeText(this, "전화번호를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
        // 비밀번호
        if (VerifyString.isValidPasswd(password)) {
            sign_num++;
        }
        else
        {
            editText_password.requestFocus();
            Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
        // 비밀번호 확인
        if (VerifyString.isValidPasswd_re(password, password_re)) {
            sign_num++;
        }
        else
        {
            editText_password_re.requestFocus();
            Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
        // 주소 정보
        if (!VerifyString.isEmptyAndNull(postal) & !VerifyString.isEmptyAndNull(address) & !VerifyString.isEmptyAndNull(address_detail)) {
            sign_num++;
        }
        else
        {
            editText_postal.requestFocus();
            Toast.makeText(this, "주소를 확인해주세요", Toast.LENGTH_SHORT).show();
        }

        // 회원가입에 필요한 모든 항목을 채웠으면 유저생성.
        if (sign_num == SIGNUP_PROCESS_NUM)
        {
            createUser(email, password);
        }

    }

    // 회원가입
    private void createUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity_signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            loginUser(email, password);
                            Toast.makeText(activity_signup.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(activity_signup.this, "회원가입 실패 : " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 유저 로그인
    // FCMToken을 얻어오기 위해 잠시 로그인 한다.
    private void loginUser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity_signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            getFCMToken();
                            Toast.makeText(activity_signup.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            Toast.makeText(activity_signup.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 이메일 인증
    private void sendVerifyEmail()
    {
        FirebaseUser user = auth.getCurrentUser();
        // 이메일 링크 보내기
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            // 이메일 전송 성공
                            Toast.makeText(activity_signup.this, "이메일이 전송되었습니다.", Toast.LENGTH_SHORT).show();
                            // 이메일 전송 이후에는 다시 한번 로그인을 시키기 위해서 강제로 로그아웃 시키는 방법이 좋을 것 같음.
                            auth.signOut();
                            Intent intent = new Intent(activity_signup.this, activity_login.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {

                        }
                    }
                });

    }

    // 유저정보 db에 업로드
    private void uploadUserInfo(String name, String postal ,String address, String address_detail)
    {

        // GeoPoint 좌표 / lat : 위도 lon : 경도
        GeoPoint geoPoint = new GeoPoint(lat, lon);

        // GeoHash 를 통해 nearby를 구현할 수 있다.
        String geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon));

        // Key와 Value를 가지는 맵
        Map<String, Object> user = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        user.put("name", name);
        user.put("postal", postal);
        user.put("address", address);
        user.put("address_detail", address_detail);
        user.put("sitter_auth", false);
        user.put("geoPoint", geoPoint);
        user.put("geoHash", geoHash);
        user.put("fcm_token", token);
        user.put("sitter_entrust", false);
        user.put("email", email);
        user.put("phone", phone);
        user.put("gender", gender);
        user.put("birth", birth);

        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        db.collection("users").document(auth.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("결과 : ", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("결과 : ", "Error writing document", e);
                    }
                });
    }

    // 주소 검색 다이얼로그 호출 메소드
    public void onClickSearchDialog(View view)
    {
        Intent intent = new Intent(activity_signup.this, activity_popup_address.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 주소 검색 다이얼로그 액티비티의 결과를 받아온다.
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String postal_code = data.getStringExtra("postal_code");
            String road_address = data.getStringExtra("road_address");
            //String jibun_address = data.getStringExtra("jibun_address");
            String str_lat = data.getStringExtra("lat");
            String str_lon = data.getStringExtra("lon");

            lat = Double.parseDouble(str_lat);
            lon = Double.parseDouble(str_lon);

            editText_postal.setText(postal_code);
            editText_address.setText(road_address);
        }
    }
    // 해당 유저의 fcm 토큰을 조회하고 유저 정보를 업로드 하고 검증 메일을 보낸다.
    private void getFCMToken()
    {
        // 파이어베이스 클라우드 메시징(FCM) 토큰 얻기.
        Task task = FirebaseMessaging.getInstance().getToken();

                task.addOnCompleteListener(new OnCompleteListener<String>()
                {
                    @Override
                    public void onComplete(@NonNull Task<String> task)
                    {
                        if (!task.isSuccessful())
                        {
                            Log.w("", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        token = task.getResult();
                        // 유저 정보를 db에 업로드
                        uploadUserInfo(name, postal, address, address_detail);
                        // 검증 메일 발송
                        sendVerifyEmail();

                    }
                });

    }
    // 유저의 성별을 설정하기 위한 메소드
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {

        switch (checkedId)
        {
            case R.id.radiobutton_sign_up_male:

                gender = true;

                break;

            default:

                gender = false;
        }
    }
}