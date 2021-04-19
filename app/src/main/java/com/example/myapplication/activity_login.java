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

import java.util.regex.Pattern;

public class activity_login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //추가한 것
    private SignInButton button_glogin; // 구글 로그인 버튼
    private FirebaseAuth auth; //파이어베이스 인증 객체
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

        //---------------------------------------------------------------------------------

        /*
        EditText edit_id = (EditText)findViewById(R.id.edit_id);
        EditText edit_password = (EditText)findViewById(R.id.edit_password);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input_id = edit_id.getText().toString();
                String input_password = edit_password.getText().toString();

                requestLogin(input_id, input_password);

                //Intent intent = new Intent(activity_login.this,MainActivity.class);
                //startActivity(intent);
               // finish();
            }
        });
        */

    }

    public void onClickLogin(View view)
    {
        email = editText_email.getText().toString().trim();
        password = editText_pw.getText().toString().trim();
        VerifyString verifyString = new VerifyString();

        if(verifyString.isValidEmail(email) && verifyString.isValidPasswd(password)) {
            loginUser(email, password);
        }
    }

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

                                Intent intent = new Intent(activity_login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
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

    /*
    private void requestLogin(String id, String password)
    {
        CollectionReference docRef = db.collection("users");
        Query query = docRef.whereEqualTo("id", id);
        query.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                    {
                        String temp_user_id = null;
                        String temp_password = null;

                        // for 문이지만 어차피 1개 or null의 데이터만 나옴.
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            temp_user_id = document.getId();
                            temp_password = (String)document.get("pw");
                        }

                        if(temp_password != null)
                        {
                            if(password.equals(temp_password))
                            {
                                user_id = temp_user_id;
                                Log.d("비밀번호 조회 결과 : ", "비밀번호가 맞네?");
                            }
                            else
                            {
                                Log.d("비밀번호 조회 결과 : ", "비밀번호가 틀렸어..");
                            }
                        }
                        else
                        {
                            Log.d("아이디 조회 결과 : ", "조회한 아이디가 없는데?");
                        }
                    }
                    else
                    {
                        Log.d("에러 : ", "Error getting documents: ", task.getException());
                    }
                }
            });

    }
*/

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
}