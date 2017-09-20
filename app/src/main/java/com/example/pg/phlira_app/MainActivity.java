package com.example.pg.phlira_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pg.phlira_app.msg.LoadMsgData;
import com.example.pg.phlira_app.inc.SettingVar;
import com.example.pg.phlira_app.msg.ReadMsgData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * 2017-05-12
 * 작성자 : 서봉교
 * 메인 페이지
 */

public class MainActivity extends Activity implements View.OnClickListener,View.OnTouchListener{

    ImageButton btnMsg;
    ImageButton btnBack;
    ImageButton btnForward;
    ImageButton btnHome;
    ImageButton btnReload;
    ImageButton btnSet;

    WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //토큰 생성은 앱실행시 바로 행하여짐(IntroPage)
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

        mWeb = (WebView) findViewById(R.id.web);
        mWeb.setWebViewClient(new MyWebClient());
        WebSettings set = mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        set.setSupportZoom(true);
        mWeb.setVerticalScrollBarEnabled(true);
        mWeb.setHorizontalScrollBarEnabled(false);
        mWeb.clearCache(true);
        mWeb.destroyDrawingCache();
        mWeb.getSettings().setUseWideViewPort(true);
        mWeb.getSettings().setLoadWithOverviewMode(true);
        mWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        //웹뷰에서 javascript의 alert창을 쓰기위해 아래코드가 추가되어야됨
        mWeb.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        mWeb.setWebViewClient(new WebViewClient());


        //<a href='tel:010-0101-0202'></a> 형식으로 전화걸기를 가능하게 함
        //<uses-permission android:name="android.permission.CALL_PHONE"/> 권한도 줘야됨
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("tel:")){
                    Intent dial = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    //현재의 activity 에 대하여 startActivity 호출
                    startActivity(dial);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });


        //처음 메인페이지 접속은 세션 셋팅 페이지로 설정
        //세션값들을 설정한다
        mWeb.loadUrl(SettingVar.SET_SESSION + "?id=" + SettingVar.id + "&license=" + SettingVar.license);

        btnMsg = (ImageButton) findViewById(R.id.btnmsg);
        btnBack = (ImageButton) findViewById(R.id.btnback);
        btnForward = (ImageButton) findViewById(R.id.btnforward);
        btnHome = (ImageButton) findViewById(R.id.btnhome);
        btnReload = (ImageButton) findViewById(R.id.btnreload);
        btnSet = (ImageButton) findViewById(R.id.btnset);

        btnMsg.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnReload.setOnClickListener(this);
        btnSet.setOnClickListener(this);

        mWeb.setOnTouchListener(this);

        //푸쉬 알림창으로 실행했을 경우 바로 메세지 확인창으로 이동
        if(SettingVar.moveMsg == null){
            //1
        }else{
            Intent intent = new Intent(MainActivity.this, ReadMsgData.class);
            intent.putExtra("num",SettingVar.moveMsg);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_left_slide, R.anim.anim_right_slide);
        }



        //스레드 사용시 사용 (스레드)
        //WebViewUrlCheck wvc = new WebViewUrlCheck();
        //wvc.start();

    }

    public boolean onTouch(View v, MotionEvent evt){

        return false;
    }

    /*  스레드 클래스 (스레드)
    private class WebViewUrlCheck extends Thread{
        public WebViewUrlCheck(){

        }

        public void run(){
            //작업내용 들어감
        }
    } */

    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.btnmsg:
                //메세지 확인창으로 이동
                Intent intent = new Intent(MainActivity.this, LoadMsgData.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_slide, R.anim.anim_right_slide);

            break;
            case R.id.btnback:
                mWeb.goBack();
                break;
            case R.id.btnforward:
                mWeb.goForward();
                break;
            case R.id.btnhome:
                mWeb.loadUrl(SettingVar.MAIN_WEBSITE);
                break;
            case R.id.btnreload:
                mWeb.reload();
                break;
            case R.id.btnset:
                //엽션설정창으로 이동
                Intent sOption = new Intent(MainActivity.this, SettingOption.class);
                startActivity(sOption);
                overridePendingTransition(R.anim.anim_left_slide, R.anim.anim_right_slide);
                break;
        }
    }

    private class MyWebClient extends WebViewClient {
        public boolean shouldOverriderUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // WebView의 페이지 로드가 완료되면 콜백의 형태로 이 메쏘드가 호출됩니다..
            if(view.getUrl().equals(SettingVar.ARC_NOTE_URL)){
                //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //Log.d("sbg_test","가로 화면 : " + view.getUrl());
            }else{
                //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //Log.d("sbg_test","세로 화면 : " + view.getUrl());
            }

        }
    }

    //백버튼 클릭시 뒤로가기 추가 뒤로가기 더이상 없으면 종료
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWeb.canGoBack()) {
            mWeb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //종료처리시 종료 할지 물어보기 추가
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("종료")
                .setMessage("종료 하시겠습니까?")
                .setNegativeButton("아니요", null)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                }).show();
    }
}
