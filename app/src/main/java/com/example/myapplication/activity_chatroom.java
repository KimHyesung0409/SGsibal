package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_chatroom extends AppCompatActivity {

    private String opponent_id;
    private String opponent_name;
    private String chatroom;
    private String user_name;
    private String user_id;
    private EditText edit_chat;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ListView listview;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        Intent intent = getIntent();

        opponent_id = intent.getStringExtra("opponent_id");
        opponent_name = intent.getStringExtra("opponent_name");
        chatroom = intent.getStringExtra("chatroom");

        edit_chat = (EditText)findViewById(R.id.edit_chat);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user_id = auth.getUid();

        getUserName();

        listview = (ListView)findViewById(R.id.listview_chatlist);
        adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        // 처음 실행 시 db에 쌓여있던 채팅 내역을 가져옴
        getChatList();
        // 그 이후엔 db가 업데이트 되었는지 체크하여 실시간으로 데이터를 가져옴
        realTimeChat();
    }

    public void onClickChat(View view)
    {
        ListViewItem_chat chat_data = new ListViewItem_chat();

        String text = edit_chat.getText().toString();
        Date time = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        String timestamp = dateFormat.format(time);

        chat_data.setName(user_name);
        chat_data.setSelf(true);
        chat_data.setText(text);
        chat_data.setTimestamp(timestamp);

        adapter.addItem(chat_data);
        adapter.notifyDataSetChanged();

        uploadChat(text);
    }

    private void getUserName()
    {
        DocumentReference docRef = db.collection("users").document(auth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name = (String)document.get("name");
                        user_name = name;

                    } else {
                        Log.d("", "No such document");
                    }
                } else {
                    Log.d("", "get failed with ", task.getException());
                }
            }
        });
    }

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
        chat.put("timestamp", FieldValue.serverTimestamp());
        // db에 업로드
        // auth.getUid 를 문서명으로 지정했으므로 해당 유저에 대한 내용을 나타낸다.
        db.collection("chat").document("chatA").collection("messages")
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

    private void getChatList()
    {
        db.collection("chat").document("chatA").collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String from = (String)document.get("from");

                                if(!from.equals(user_id))
                                {

                                    ListViewItem_chat chat_data = new ListViewItem_chat();

                                    String text = (String) document.get("text");
                                    Timestamp time = (Timestamp) document.get("timestamp");
                                    Date date = time.toDate();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                                    String timestamp = dateFormat.format(date);

                                    chat_data.setText(text);
                                    chat_data.setName(opponent_name);
                                    chat_data.setSelf(false);
                                    chat_data.setTimestamp(timestamp);

                                    adapter.addItem(chat_data);
                                    Log.d("", document.getId() + " => " + document.getData());
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void realTimeChat()
    {
        db.collection("chat").document("chatA").collection("messages")
                .whereEqualTo("from", opponent_id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshots,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:

                                    ListViewItem_chat chat_data = new ListViewItem_chat();

                                    String text = (String) dc.getDocument().get("text");
                                    Timestamp time = (Timestamp) dc.getDocument().get("timestamp");
                                    Date date = time.toDate();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                                    String timestamp = dateFormat.format(date);

                                    chat_data.setText(text);
                                    chat_data.setName(opponent_name);
                                    chat_data.setSelf(false);
                                    chat_data.setTimestamp(timestamp);

                                    adapter.addItem(chat_data);
                                    adapter.notifyDataSetChanged();

                                    Log.d("", "New chat: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:

                                    Log.d("", "Modified city: " + dc.getDocument().getData());

                                    break;
                                case REMOVED:
                                    Log.d("", "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                    }
                });
    }

}