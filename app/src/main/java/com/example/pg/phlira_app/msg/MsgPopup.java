package com.example.pg.phlira_app.msg;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pg.phlira_app.IntroPage;
import com.example.pg.phlira_app.MainActivity;
import com.example.pg.phlira_app.R;
import com.example.pg.phlira_app.inc.SettingVar;

import java.util.List;

/**
 * Created by pg on 2017-05-12.
 * 작성자 : 서봉교
 * 푸시 메세지 팝업창
 */

public class MsgPopup extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.msg_popup);

        // 이 부분이 바로 화면을 깨우는 부분 되시겠다.
        // 화면이 잠겨있을 때 보여주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                // 키잠금 해제하기
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                // 화면 켜기
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        TextView tv = (TextView)findViewById(R.id.msg_pop1);
        tv.setText(getIntent().getStringExtra("답변이 도착했어요!"));
        Button tBtn = (Button)findViewById(R.id.t_btn);

        Intent intent = getIntent();
        SettingVar.moveMsg = intent.getStringExtra("num");
        //true 이면 앱이 실행중에 푸시 알림창이 뜬경우, false 이면 앱이 완전 종료된 상태에 푸시 알림창이 뜬경우
        final boolean appChk = intent.getBooleanExtra("appchk",false);

        tBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 확인버튼을 누르면 앱의 런처액티비티를 호출한다.
                Intent intent;
                if(appChk){
                    intent = new Intent(MsgPopup.this, MainActivity.class);
                }else{
                    intent = new Intent(MsgPopup.this, IntroPage.class);
                    intent.putExtra("num",SettingVar.moveMsg);
                }
                startActivityForResult(intent, 1);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
