package com.ernotion.ngdb.noname_bot;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Listener extends NotificationListenerService {
    static Context context;
    static Session session = new Session();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        super.onNotificationPosted(sbn);
        if (sbn.getPackageName().equals("com.kakao.talk")) {
            Notification.WearableExtender wearableExtender = new Notification.WearableExtender(sbn.getNotification());
            for (Notification.Action act : wearableExtender.getActions())
                if (act.getRemoteInputs() != null && act.getRemoteInputs().length > 0) {
                    context = getApplicationContext();

                    session.session = act; // 한 일(사실상 쓸데없음)
                    session.message = sbn.getNotification().extras.get("android.text").toString();// 카카오톡 내용(롤리팝 이하 단톡에서는 이름 + 내용)
                    session.sender = sbn.getNotification().extras.getString("android.title");// 카카오톡 보낸 사람 이름(롤리팝 이하 단톡에서는 단톡방 이름)
                    session.room = act.title.toString().replace("답장 (", "").replace(")", ""); // 톡방 이름
                    //종합해서 로그찍기
                    Log.i("asd", "message : " + session.message + " sender : " + session.sender + " nroom : " + session.room + " nsession : " + session);

                    //여기 아래에 알림 내용에 따라 다른 send()를 호출하면 됨

                    //ex)

                    //if(session.message.contain("시공"))
                    //  send("시공조아");

                    //위 코드는 메세지에 시공 이라는 문자열이 포함되어 있으면 "시공조아" 라는 답장을 보냄

                }
            stopSelf();
        }
    }
    //메세지를 보내는 함수, 원리는 원작자도 모름..
    //send(문자열); 로 작동함
    public static void send(String message) {
        Intent sendIntent = new Intent();
        Bundle msg = new Bundle();
        for (RemoteInput inputable : session.session.getRemoteInputs())
            msg.putCharSequence(inputable.getResultKey(), message);
        RemoteInput.addResultsToIntent(session.session.getRemoteInputs(), sendIntent, msg);

        try {
            session.session.actionIntent.send(context, 0, sendIntent);
            Log.i("send() complete",message);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


}