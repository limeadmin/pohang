package com.example.pg.phlira_app;


import android.app.Activity;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by pg on 2017-04-28.
 */

public class AppUninstallCheck {
    public void SendHttpUninstallArm(String tel){

        //서버로 데이터 저장
        Log.d("첫째", "전화번호: " + tel);

        final String tel2 = tel;
        // Thread로 웹서버에 접속
        new Thread() {
            public void run() {
                OkHttpClient cHttp = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("test1",tel2)
                        .add("test2","22222")
                        .build();
                Request request = new Request.Builder()
                        .url("http://limeweb.net/sbg_fcm/test2.php")
                        .post(body)
                        .build();
                Log.d("둘째", "전송전");
                try {
                    cHttp.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
