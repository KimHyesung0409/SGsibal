package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.health.SystemHealthManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // 이부분에 원하는 로직을 짜면 됨.

        /*
        * data 메시지와 notification 메시지가 있다.
        * notification은 간단한 텍스트만 보내는 것이고 클릭시 앱 시작화면을 띄워줄 수 있다.
        * 반면에 data 메시지는 다양한 기능을 수행할 수 있다.
         */

        // data 메시지인 경우
        if (remoteMessage.getData().size() > 0)
        {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String user_id = remoteMessage.getData().get("user_id");
            int type = Integer.parseInt(remoteMessage.getData().get("type"));

            sendNotification(title, body, user_id, type);

        }

        // notification 메시지인 경우
        if (remoteMessage.getNotification() != null) {
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            //sendNotification(notification.getTitle(), notification.getBody());
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        // 해당 유저의 토큰을 변경하는 로직

    }

    // fcm 메시지를 수신 받으면 핸드폰에 알림을 띄우게 된다.
    // 이때 외부에서 앱을 실행할 수 있는 PendingIntent를 사용하여 원하는 액티비티를 호출할 수 있다.
    // 위에서 받은 정보, PendingIntent를 사용하여 알림 메시지를 만들어 출력한다.
    private void sendNotification(String title ,String messageBody, String user_id, int type) {
        Intent intent = new Intent(this, activity_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("callFrom", true);
        intent.putExtra("fcm_user_id", user_id);
        intent.putExtra("type", type);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "모두의 집사";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(channelId)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 , notificationBuilder.build());
    }

}
