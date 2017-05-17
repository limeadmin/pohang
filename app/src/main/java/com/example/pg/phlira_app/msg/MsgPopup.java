package com.example.pg.phlira_app.msg;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pg.phlira_app.IntroPage;
import com.example.pg.phlira_app.MainActivity;
import com.example.pg.phlira_app.R;
import com.example.pg.phlira_app.inc.SettingVar;
import com.example.pg.phlira_app.inc.WebImgLoad;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by pg on 2017-05-12.
 * 작성자 : 서봉교
 * 푸시 메세지 팝업창
 */

public class MsgPopup extends Activity implements View.OnClickListener{
    Bitmap bitmap;
    TextView tv,tv2;
    Button tBtn,tBtn2;
    ImageView adImg;

    boolean appChk;
    WebImgLoad mThread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Activity 투명 화면 설정
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

        tv = (TextView)findViewById(R.id.msg_pop1);
        tv2 = (TextView)findViewById(R.id.msg_pop2);
        tBtn = (Button)findViewById(R.id.t_btn);
        tBtn2 = (Button)findViewById(R.id.t_btn2);
        adImg = (ImageView)findViewById(R.id.ad_img);

        Typeface face = Typeface.createFromAsset(getAssets(), SettingVar.FONT_FILE);
        tv.setTypeface(face);
        tv2.setTypeface(face);
        tBtn.setTypeface(face);
        tBtn2.setTypeface(face);

        Intent intent = getIntent();
        //true 이면 앱이 실행중에 푸시 알림창이 뜬경우, false 이면 앱이 완전 종료된 상태에 푸시 알림창이 뜬경우
        appChk = intent.getBooleanExtra("appchk",false);
        SettingVar.moveMsg = intent.getStringExtra("num");
        String msg = intent.getStringExtra("msg");

        // 현재 시간을 msec으로 구한다.
        long now = System.currentTimeMillis();
        // 현재 시간을 저장 한다.
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //웹에서 이미지를 가져오는 스레드 생성
        mThread = new WebImgLoad(SettingVar.AD_IMG_PATH);
        mThread.start(); // 웹에서 이미지를 가져오는 작업 스레드 실행.

        tv.setText(msg);
        tv2.setText(CurDateFormat.format(date));

        try {
            //  메인 스레드는 작업 스레드가 이미지 작업을 가져올 때까지
            //  대기해야 하므로 작업스레드의 join() 메소드를 호출해서
            //  메인 스레드가 작업 스레드가 종료될 까지 기다리도록 합니다.
            mThread.join();

            //  이제 작업 스레드에서 이미지를 불러오는 작업을 완료했기에
            //  UI 작업을 할 수 있는 메인스레드에서 이미지뷰에 이미지를 지정합니다.

            //광고 이미지가 없을경우 기본 이미지(로고) 띄움
            bitmap = mThread.getBitmap();
            if(bitmap != null){
                adImg.setImageBitmap(bitmap);
            }

        } catch (InterruptedException e) {

        }

        tBtn.setOnClickListener(this);
        tBtn2.setOnClickListener(this);

    }


    public void onClick(View v){
        switch(v.getId()){
            case R.id.t_btn:
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
                break;
            case R.id.t_btn2:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {

        super.onPause();

        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
