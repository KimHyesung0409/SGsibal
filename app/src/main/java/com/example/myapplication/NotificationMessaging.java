package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

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

    private String to;
    private String title;
    private String body;
    private Context context;

    public NotificationMessaging(String to, String title, String body, Context context)
    {
        this.to = to;
        this.title = title;
        this.body = body;
        this.context = context;
    }

    public void run()
    {

        try
        {
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

            content.put("to", to);
            content.put("priority", "high");
            content.put("notification", data);

        } catch (JSONException e) {
            Log.e("", "onCreate: " + e.getMessage() );
        }

        return content;
    }

    private void sendNotification(JSONObject notification) {

        try
        {
            URL url = new URL(context.getString(R.string.FCM_Server_Url));
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "key=" + context.getString(R.string.FCM_Server_Key));
            con.setRequestProperty("Content-Type", "application/json;  charset=utf-8");

            con.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
            bw.write(notification.toString());
            bw.flush();
            bw.close();

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

    private String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

}
