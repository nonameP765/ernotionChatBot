package com.ernotion.ngdb.noname_bot;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //onclick으로 실행되는 함수
    public void start(View v) {
        //권한을 부여하는 기초단계
        String notifiPermission = Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners");
        if ((notifiPermission != null && !notifiPermission.contains("com.ernotion.ngdb.prototype")) || notifiPermission == null) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            Toast.makeText(this, "알림읽기 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "시작됨", Toast.LENGTH_SHORT).show();
    }
}