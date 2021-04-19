package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class activity_signup extends AppCompatActivity {

    private FirebaseAuth auth; //파이어베이스 인증 객체
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어 db 객체

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private EditText editText_name, editText_email, editText_password, editText_password_re, editText_address;
    private String name = "";
    private String email = "";
    private String password = "";
    private String password_re = "";
    private String address = "";


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

        auth = FirebaseAuth.getInstance(); //파이어베이스 인증 객체 초기화

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
        editText_address = (EditText)findViewById(R.id.edit_signup_address);

        if(auth.getCurrentUser() != null)
        {
            auth.signOut();
            Toast.makeText(activity_signup.this, "로그아웃.", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickRequestSignUp(View view)
    {
        name = editText_name.getText().toString().trim();
        email = editText_email.getText().toString().trim();
        password = editText_password.getText().toString().trim();
        password_re = editText_password_re.getText().toString().trim();
        address = editText_address.getText().toString().trim();

        VerifyString verifyString = new VerifyString();

        if(verifyString.isValidEmail(email) && verifyString.isValidPasswd(password))
        {
            if(verifyString.isValidPasswd_re(password, password_re))
            {
                if(verifyString.isValidname(name))
                {
                    createUser(email, password);
                }
                else
                {
                    Toast.makeText(activity_signup.this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(activity_signup.this, "비밀번호와 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }

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

    private void loginUser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity_signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            uploadUserInfo(name, address);
                            sendVerifyEmail();
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
    private void uploadUserInfo(String name, String address)
    {
        // Key와 Value를 가지는 맵
        Map<String, Object> user = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        user.put("name", name);
        user.put("address", address);
        user.put("sitter_auth", false);
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
        startActivity(intent);
    }

}