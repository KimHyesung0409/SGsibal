package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class activity_chatroom extends AppCompatActivity {

    private String opponent_id;
    private String opponent_name;
    private String chatroom;
    private String user_name;
    private String user_id;
    private EditText edit_chat;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        Intent intent = getIntent();
        // 상태방 아이디와 이름, 채팅방 id를 intent를 통해 전달받는다.
        opponent_id = intent.getStringExtra("opponent_id");
        opponent_name = intent.getStringExtra("opponent_name");
        chatroom = intent.getStringExtra("chatroom");

        edit_chat = (EditText)findViewById(R.id.edit_chat);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user_id = auth.getUid();

        user_name = LoginUserData.getUser_name();

        recyclerView = findViewById(R.id.recyclerview_chatlist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        getChatList();
    }

    // 채팅 전송 버튼 클릭 메소드
    public void onClickChat(View view)
    {
        // edit_text 에서 채팅 내용을 가져온다.
        String text = edit_chat.getText().toString().trim();

        // 비어있거나 null이 아닌경우 즉 내용이 있는 경우에만 채팅을 전송한다.
        if(!text.isEmpty() && text != null)
        {
            edit_chat.setText("");
            uploadChat(text);
        }
        else //
        {
            Toast.makeText(activity_chatroom.this, "문자를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }
    // 전송한 채팅을 db에 업로드 하는 부분.
    private void uploadChat(String text)
    {
        // Key와 Value를 가지는 맵
        Map<String, Object> chat = new HashMap<>();
        // 위에서 만든 맵(user) 변수에 데이터 삽입
        chat.put("from", user_id);
        chat.put("text", text);
        // 파이어 스토어는 자동 증가가 없다 따라서 document명을 지정하지 않고 add메소드로 데이터를 추가한다.
        // 하지만 파이어 스토어는 이렇게 임의로 생성한 문서ID를 정렬하지 못한다고 한다.
        // 구글이 추천하는 방법은 필드에 타임스탬프를 넣어 시간별로 정렬할 수 있도록 만드는 것이 좋다고 한다.
        chat.put("timestamp", new Timestamp(new Date()));
        // db에 업로드
        db.collection("chatroom").document(chatroom).collection("messages")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // 로컬에 저장된 캐시데이터를 불러오고 서버에서 변동되는 문서를 실시간으로 감지함.
    private void getChatList()
    {
        db.collection("chatroom").document(chatroom).collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen error", e);
                            return;
                        }

                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            // db에 문서 추가가 감지되었을 때.
                            if (change.getType() == Type.ADDED) {
                                String from = change.getDocument().getString("from");

                                // from는 채팅을 보낸 user_id를 의미하는데
                                // 본인이 보낸 채팅인 경우와 상대방이 보낸 채팅인 경우를 나누어
                                // 어뎁터에 데이터를 추가한다.
                                if(from.equals(user_id))
                                {
                                    ListViewItem_chat chat_data = new ListViewItem_chat();

                                    String text = (String) change.getDocument().get("text");
                                    Timestamp timestamp = (Timestamp) change.getDocument().get("timestamp");
                                    String time = transformToDate(timestamp);

                                    chat_data.setText(text);
                                    chat_data.setName(user_name);
                                    chat_data.setSelf(true);
                                    chat_data.setTimestamp(time);

                                    adapter.addItem(chat_data);
                                    Log.d("", change.getDocument().getId() + " => " + change.getDocument().getData());
                                }
                                else
                                {
                                    ListViewItem_chat chat_data = new ListViewItem_chat();

                                    String text = (String) change.getDocument().get("text");
                                    Timestamp timestamp = (Timestamp) change.getDocument().get("timestamp");
                                    String time = transformToDate(timestamp);

                                    chat_data.setText(text);
                                    chat_data.setName(opponent_name);
                                    chat_data.setSelf(false);
                                    chat_data.setTimestamp(time);

                                    adapter.addItem(chat_data);
                                    Log.d("", change.getDocument().getId() + " => " + change.getDocument().getData());
                                }
                                adapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                Log.d("TAG", "New city:" + change.getDocument().getData());
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d("TAG", "Data fetched from " + source);
                        }
                    }
                });
    }
    // timestamp의 경우 utc(gmt)를 기준으로 하기 때문에
    // utc+9(kst : korea standard time)으로 변환하여 출력해야한다.
    private String transformToDate(Timestamp timestamp)
    {
        Date date = timestamp.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("a hh:mm", Locale.KOREA);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String time = dateFormat.format(date);

        return time;
    }

}