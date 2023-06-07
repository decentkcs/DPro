package kr.co.decent.dpro.android.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import kr.co.decent.dpro.android.MainActivity;
import kr.co.decent.dpro.android.nexacro.NexacroActivityExt;
import kr.co.decent.dpro.android.R;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()) {
                    // 실패
                    return;
                }
                else {
                    String token = task.getResult();
                    sendRegistrationToServer(token);
                }

            }
        });
    }

    private void sendRegistrationToServer(String token){
        //앱 서버로 토큰을 보낼 때, 즉 푸시를 할 때 수행할 동작코드를 작성
    }

    @Override
    public void handleIntent(@NonNull Intent intent) {
        Bundle bundle = intent.getExtras();
        Object objTitle = bundle.get("gcm.notification.title");
        if ( !bundle.isEmpty() && objTitle != null ){
            if ( NexacroActivityExt.pauseFlag ){
                NexacroActivityExt.pushMsgFlag = true;
            }
        }
        super.handleIntent(intent);
    }


    @Override
    public void onMessageSent(@NonNull String var1) {
        super.onMessageSent(var1);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        ((NexacroActivityExt)NexacroActivityExt.context).callMethod("selectPushList", null);

        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() != null) {

            // background
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            setNotification2(title, body);
        }
        else {
            // forgraound
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            setNotification2(title, body);
        }

    }

    public void setNotification2(String title, String msg) {

        String NOTIFICATION_CHANNEL_ID = "121212";

        int notiId = (int) System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, NexacroActivityExt.class);
//        notificationIntent.putExtra("notificationId", 111111); //전달할 값
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK ) ;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon)) //BitMap 이미지 요구
                .setContentTitle(title)
                .setContentText(msg)
                // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("더 많은 내용을 보여줘야 하는 경우..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                .setAutoCancel(true);

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.icon); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName  = "노티페케이션 채널";
            String description = "오레오 이상을 위한 것임";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        }else builder.setSmallIcon(R.drawable.icon); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        assert notificationManager != null;
        notificationManager.notify(notiId, builder.build()); // 고유숫자로 노티피케이션 동작시킴

    }

    private void setNotification (String title, String msg) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.putExtra("", "'"); 메인에 데이터 넘김
//
        int notiId = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notiId, intent, PendingIntent.FLAG_ONE_SHOT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //API 수준 26(Android Oreo) 이상일 때
            String channelId = "one-channel";
            String channelName = "My Channel One";
            String channelDescription = "My Channel One Description";
            NotificationManager notificationManager = getSystemService(NotificationManager.class); //발생하는 이벤트를 사용자에게 알리는 클래스
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT); //알림 채널 객체 생성
            //알림 소리 설정을 위한 AudioAttributes 객체 생성, NotificationChannel.setSound()의 매개변수로 넣어준다
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel.setDescription(channelDescription); //알림에 대한 설명 추가(옵션)
            //각종 채널에 대한 설정
            channel.enableLights(true); //알림 올 때 불 깜빡이게 설정
            channel.setLightColor(Color.RED); //빨간불
            channel.setVibrationPattern(new long[]{100, 200, 300}); //2개 이상의 진동 패턴 설정, 0이거나 null값일 시 무음모드
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),audioAttributes);   // 알림소리 설정
            notificationManager.createNotificationChannel(channel);
            //channel이 등록된 NotificationCompat.Builder 클래스
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(title)//알림제목 설정
                    .setContentText(msg)//알림메시지 설정
                    .setSmallIcon(R.drawable.icon)//알림 아이콘 설정
                    .setAutoCancel(true); //사용자가 터치하면 자동으로 알림이 닫힘

            notificationManager.notify(notiId,builder.build());
            builder.setContentIntent(pendingIntent); //알림이 클릭됐을 때 펜딩 인텐트를 제공

        } else {//API 수준 26(Android Oreo)이하일 때
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // 알림소리 설정
                    .setVibrate(new long[]{1, 1000});// 1초동안 진동 울림

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notiId,builder.build());
            builder.setContentIntent(pendingIntent);
        }
    }
}
