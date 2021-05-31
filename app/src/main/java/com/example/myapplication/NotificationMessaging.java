package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NotificationMessaging extends Thread {

    public static final int FCM_RESERVE = 1;

    private String to;
    private String title;
    private String body;
    private Context context;
    private String user_id;
    private int type;

    // 생성자
    // 받는 사람의 토큰, 제목, 내용, 받는 사람의 user_id, type, context.
    public NotificationMessaging(String to, String title, String body, String user_id, int type, Context context)
    {
        this.to = to;
        this.title = title;
        this.body = body;
        this.context = context;
        this.user_id = user_id;
        this.type = type;
    }

    public void run()
    {

        try
        {
            // 메시지 전송은 HTTP POST를 사용하기 때문에 Thread 클래스로 실행해야 한다.
            sendNotification(createPushMessage());
        }
        catch (Exception e)
        {

        }

    }

    // notification message 생성
    private JSONObject createPushMessage()
    {
        JSONObject content = new JSONObject();

        JSONObject data = new JSONObject();


        try {
            data.put("title", title);
            data.put("body", body);
            data.put("user_id", user_id);
            data.put("type", type);

            content.put("to", to);
            content.put("priority", "high");
            content.put("data", data);

        } catch (JSONException e) {
            Log.e("", "onCreate: " + e.getMessage() );
        }

        return content;
    }

    private void sendNotification(JSONObject notification) {

        try
        {
            // HTTP 서버 주소
            URL url = new URL(context.getString(R.string.FCM_Server_Url));
            // 연결
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "key=" + context.getString(R.string.FCM_Server_Key));
            con.setRequestProperty("Content-Type", "application/json;  charset=utf-8");

            con.setDoOutput(true);
            // 내용을 버퍼와 스트림 라이터로 입력한다.
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
            bw.write(notification.toString());
            bw.flush();
            bw.close();

            // 응답 코드가 200이면 성공적으로 전송 아니면 에러.
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                String response = inputstreamToString(con.getInputStream());
                System.out.println("Message sent to Firebase for delivery, response:");
                System.out.println(response);
            } else {
                System.out.println("Unable to send message to Firebase:");
                String response = inputstreamToString(con.getErrorStream());
                System.out.println(response);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
    // 응답으로 받은 인풋 스트림을 String으로 반환하는 메소드.
    private String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

}
