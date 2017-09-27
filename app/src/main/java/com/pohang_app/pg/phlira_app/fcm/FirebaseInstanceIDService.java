package com.pohang_app.pg.phlira_app.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import com.pohang_app.pg.phlira_app.inc.SettingVar;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {


    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        //최초 앱 실행시에 토큰 생성할때만 호출된다
        //토큰생성후에는 호출되지 않음

        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("sbg_test", "Refreshed token : " + token);

        // 생성등록된 토큰을 개인 앱서버에 보내 저장해 두었다가 추가 뭔가를 하고 싶으면 할 수 있도록 한다.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .add("Tel", SettingVar.phNumber)
                .build();


        //request
        Request request = new Request.Builder()
                .url(SettingVar.REG_SERVER_FILE)
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        //request
        Request request = new Request.Builder()
                .url("http://서버주소/fcm/register.php")
                .post(body)
                .build();
        url에는 토큰을 받아서 저장할 서버의 주소와 php 파일을 적어 준다. Part2에서 작성을 할 것이다.
        */
    }
}
