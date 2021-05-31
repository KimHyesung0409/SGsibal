package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class activity_login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //추가한 것
    private SignInButton button_glogin; // 구글 로그인 버튼
    private FirebaseAuth auth; //파이어베이스 인증 객체
    private FirebaseFirestore db; //파이어베이스 db
    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과 코드

    private Boolean login_state = false;
    private String user_id;


    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    // 로그인 버튼 , 회원가입 버튼
    // onCreate 메소드 내부에 임의의 리스너로 구현한 것이 아닌
    // xml 상에서 직접 클릭 메소드의 이름을 지정해서 사용할 예정. (login, register)
    private Button button_login;
    private Button button_register;

    // 아이디 입력 창, 비밀번호 입력 창
    private EditText editText_email;
    private EditText editText_pw;

    // 입력된 아이디(이메일) 과 패스워드
    private String email = "";
    private String password = "";

    private boolean callFrom;
    private String fcm_user_id;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // --------- 액션바, 스위치 버튼 설정 ----------
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);
        // ---------------------------------------------

        // ---------------구글 로그인 부분 ------------------
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance(); //파이어베이스 인증 객체 초기화
        db = FirebaseFirestore.getInstance(); // 파이어스토어 객체

        button_login = (Button)findViewById(R.id.button_login);
        button_register = (Button)findViewById(R.id.button_signup);

        editText_email = (EditText)findViewById(R.id.edit_email);
        editText_pw = (EditText)findViewById(R.id.edit_password);

        button_glogin = findViewById(R.id.button_glogin);
        button_glogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });

        // --------- FCM 알림을 클릭하여 앱이 실행된 경우 ----------
        Intent intent = getIntent();

        callFrom = intent.getBooleanExtra("callFrom", false);

        if(callFrom)
        {
            fcm_user_id = intent.getStringExtra("fcm_user_id");
            type = intent.getIntExtra("type", 0);
        }


    }
    // 로그인 버튼 클릭 메소드
    public void onClickLogin(View view)
    {

        email = editText_email.getText().toString().trim();
        password = editText_pw.getText().toString().trim();

        // 이메일 형식과 비밀번호 형식이 올바르면 로그인을 요청한다.
        if(VerifyString.isValidEmail(email) && VerifyString.isValidPasswd(password)) {
            loginUser(email, password);
        }

    }

    // 회원가입 버튼 클릭 메소드
    public void onClickSignUp(View view)
    {
        // 회원가입 창이 뜰 수 있도록 intent 설정하고 액티비티 전환
        Intent intent = new Intent(activity_login.this, activity_signup.class);
        startActivity(intent);
        finish();
    }

    // 로그인
    private void loginUser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            // 메일 인증 확인
                            if(user.isEmailVerified())
                            {
                                // 로그인 성공
                                Toast.makeText(activity_login.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                                // 로그인한 유저의 정보를 저장
                                getLoginUserData(user.getUid());
                            }
                            else
                            {
                                // 메일을 인증하지 않음.
                                Toast.makeText(activity_login.this, "인증 메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // 로그인 실패
                            Toast.makeText(activity_login.this, "아이디, 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override //구글 로그인 인증을 요청 했을 때, 결과값을 되돌려 받는 곳
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess() == true){ //인증결과가 성공적이면..
                GoogleSignInAccount account = result.getSignInAccount(); //
                // account라는 데이터는 구글로그인 정보를 담고 있음(닉네임, 프로필사진URL, 이메일주소 등)
                resultLogin(account); //로그인 결과 값 출력 수행하라는 메소드
            }
        }

    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()==true){//로그인이 성공했으면,
                            Toast.makeText(activity_login.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            //intent.putExtra("닉네임",account.getDisplayName());// 닉네임 전달
                            //intent.putExtra("프로필 사진", String.valueOf(account.getPhotoUrl())); // 프로필 사진 전달

                            startActivity(intent);
                        }else{//로그인이 실패했으면,
                            Toast.makeText(activity_login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // 로그인을 성공하면 해당 유저의 데이터를 불러와서 LoginUserData 클래스 변수에 저장한다.
    // 클래스 변수(static)이기 때문에 패키지 내부에서 자유롭게 사용할 수 있다.
    private void getLoginUserData(String uid)
    {

        db.collection("users").document(uid)
        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {

                        String user_name = document.getString("name");
                        String address = document.getString("address");
                        String address_detail = document.getString("address_detail");
                        String fcm_token = document.getString("fcm_token");
                        String geoHash = document.getString("geoHash");
                        GeoPoint geoPoint = document.getGeoPoint("geoPoint");
                        boolean sitter_auth = document.getBoolean("sitter_auth");
                        boolean sitter_entrust = document.getBoolean("sitter_entrust");
                        ArrayList<String> care_list = (ArrayList<String>)document.get("care_list");
                        //테스트용 String 형식 care_list
                        String care_list_Str = document.getString("care_list_Str");
                        boolean gender = document.getBoolean("gender");
                        String phone = document.getString("phone");
                        String birth = document.getString("birth");

                        // 유저의 정보를 세팅한다.
                        // LoginUserData의 클래스 속성은 전부 static 즉 클래스 변수이므로
                        // 한번만 메모리에 올리면 계속 유지되기 때문에 로그인 정보를 관리하기 쉽다.
                        LoginUserData.setUser_name(user_name);
                        LoginUserData.setAddress(address);
                        LoginUserData.setAddress_detail(address_detail);
                        LoginUserData.setFcm_token(fcm_token);
                        LoginUserData.setGeoHash(geoHash);
                        LoginUserData.setGeoPoint(geoPoint);
                        LoginUserData.setSitter_auth(sitter_auth);
                        LoginUserData.setSitter_entrust(sitter_entrust);
                        LoginUserData.setCare_list(care_list);
                        //테스트용 String 형식 care_list
                        LoginUserData.setCare_list_Str(care_list_Str);
                        LoginUserData.setGender(gender);
                        LoginUserData.setPhone(phone);
                        LoginUserData.setBirth(birth);

                        // 메인화면 으로 전환하기 위해 인텐트를 설정하고 실행한다.
                        Intent intent = new Intent(activity_login.this, MainActivity.class);

                        // fcm 메시지에 의해서 실행되었다면. 그것에 관련된 정보를 intent로 보내줘야함
                        if(callFrom)
                        {
                            // fcm 메시지에 포함된 유저 id와 로그인한 uid와 같아야함.
                            if(fcm_user_id.equals(uid))
                            {
                                intent.putExtra("callFrom", true);
                                intent.putExtra("fcm_user_id", fcm_user_id);
                                intent.putExtra("type", type);
                            }
                        }

                        startActivity(intent);
                        finish();

                    }
                    else
                    {

                    }
                }
                else
                {

                }
            }
        });

    }

}