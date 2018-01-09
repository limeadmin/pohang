package com.pohang_app.pg.phlira_app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pohang_app.pg.phlira_app.inc.SettingVar;
import com.pohang_app.pg.phlira_app.inc.SendHttpData;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;


/**
 * Created by pg on 2017-05-04.
 * 인트로 페이지
 * 작성자 : 서봉교
 */

public class IntroPage extends Activity implements View.OnClickListener{

    Handler intro_delay;
    EditText regId;
    EditText regLicense;
    Button regBtn;
    TextView alertTxt;
    LinearLayout inputTag;
    SendHttpData sendData = new SendHttpData(this);
    String rId =  "";
    String rLicense = "";



    //PhoneNumberUtils.formatNumber API 요구 버젼 높음
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //페이지 타이틀 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.intro_layout);
        Log.d("sbg_test", "인트로 진입");

        regId = (EditText)findViewById(R.id.id);
        regLicense = (EditText)findViewById(R.id.license);
        regBtn = (Button)findViewById(R.id.do_reg);
        inputTag = (LinearLayout)findViewById(R.id.input_tag);
        alertTxt = (TextView)findViewById(R.id.alert_txt);
        regBtn.setOnClickListener(this);


        Intent intent = getIntent();
        SettingVar.moveMsg = intent.getStringExtra("num");
        Log.d("sbg_test","넘어온 Intent 값 : "+SettingVar.moveMsg);

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        //전화번호를 000-0000-0000 형식으로 맞추기
        //KT인 경우 전화번호가 +82형식으로 저장되기 때문에 +82를 0 으로 리플레이스 시킴
        String phn = telephonyManager.getLine1Number().replace("-", "").replace("+82", "0");
        SettingVar.phNumber = PhoneNumberUtils.formatNumber(phn, Locale.getDefault().getCountry());

        Log.d("sbg_test","전화번호 : "+SettingVar.phNumber);
        //메인 이동간에 3초의 딜레이를 줌
        intro_delay = new Handler();
        intro_delay.postDelayed(i_run,3000);

    }

    public void resultData(String result){

        /**
         * 2017_05_11
         * 인증값 유효성 검사
         * 작성자 : 서봉교
         * 인증버튼 클릭 후 인증값의 유효성 처리결과를 SendHttpData 에서 받아서 결과를 보여주는 페이지
         * noid : ID가 일치하지 않을 경우,                   noli : 회원DB에 면허번호가 없는 경우
         * notel : 회원DB에 폰번호가 없는 경우,              dili : 면허번호가 일치하지 않는 경우
         * ditel : 폰 번호가 일치하지 않는 경우              sucess : 회원인증이 성공적으로 완료됨
         */

        if(result.equals("noid")){
            alertTxt.setText("포항지역 건축사회 회원이 아니십니다.\n포항지역 건축사회 홈페이지에서 회원 등록후\n이용해 주시기 바랍니다.");
        }else if(result.equals("noli")){
            alertTxt.setText("면허번호가 없습니다.\n포항지역 건축사회 홈페이지에 로그인 후\n회원정보수정에서 면허번호를 입력하시고\n다시 시도해 보십시오.");
        }else if(result.equals("notel")){
            alertTxt.setText("휴대폰번호가 없습니다.\n포항지역 건축사회 홈페이지에 로그인 후\n회원정보수정에서 휴대폰번호를 입력하시고\n다시 시도해 보십시오.");
        }else if(result.equals("dili")){
            alertTxt.setText("포항지역건축사회 회원정보와 일치하지 않습니다.\n(면허번호 불일치)");
        }else if(result.equals("ditel")){
            alertTxt.setText("포항지역건축사회 회원정보와 일치하지 않습니다.\n(휴대폰번호 불일치)");
        }else if(result.equals("sucess")){
            FileMake();
        }
        Log.d("sbg_test", "FINISH : " + result + "_" + rId + "_" + rLicense);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.do_reg:
                rId =  regId.getText().toString().replaceAll(" ", "");
                rLicense = regLicense.getText().toString().replaceAll(" ", "");

                if(rId.equals("")) {
                    Toast.makeText(this, "아이디을 입력해 주십시오.", Toast.LENGTH_SHORT).show();
                    regId.setText("");
                    regId.requestFocus();
                    //키보드 보이게 하는 부분
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    break;
                }
                if(rLicense.equals("")) {
                    Toast.makeText(this, "면허번호를 입력해 주십시오.", Toast.LENGTH_SHORT).show();
                    regLicense.setText("");
                    regLicense.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    break;
                }
                //인증값 유효성 검사하기
                sendData.getData(SettingVar.REG_MEMBER_CHECK+"?tel="+SettingVar.phNumber+"&id="+rId+"&license="+rLicense,"rmc");
                break;
        }
    }

    Runnable i_run = new Runnable() {
        @Override
        public void run() {
            //인증된 값이 저장된 파일이 있는지 체크
            FileLoad();

        }
    };

    public void FileLoad(){
        /**
         * 2017-05-11
         * 작성자 : 서봉교
         * 인증된 값이 저장된 파일을 읽어옴
         * -파일이 있으면 파일의 값을 읽어서 메인페이지로 이동
         * -파일이 없으면 인증절차를 실행
         */
        String dirPath = getFilesDir().getAbsolutePath();
        String loadPath = dirPath+"/"+"regist.txt";
        File files = new File(loadPath);

        if(files.exists()) {
            try {
                FileInputStream fis = new FileInputStream(loadPath);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                String temp = "";

                int count = 0;
                while ((temp = bufferReader.readLine()) != null) {
                    if(count == 0) rId = temp.replaceAll(" ", "");
                    if(count == 1) rLicense = temp.replaceAll(" ", "");
                    count = count + 1;
                }
                Log.d("sbg_test", "인증 파일 있음" + rId + "_" + rLicense);
                MoveMainPage();
            } catch (Exception e) {
                Log.d("sbg_test", "인증 파일 읽기 에러 : " + e.getMessage().toString());
            }
        }else{
            Log.d("sbg_test", "인증 파일 없음");
            inputTag.setVisibility(View.VISIBLE);
        }
    }

    public void FileMake(){

        /**
         * 2017-05-11
         * 작성자 : 서봉교
         * 인증값을 저장시킬 파일을 만들고 인증값 저장
         * fcm DB에 인증값을 적용시키고 해당 휴대폰번호로 중복된 토큰값이 있으면 삭제
         * (앱을 언인스톨 후 인스톨을 하게되면 새로운 토큰값을 생성하기 때문에 여러번 언인스톨 후 인스톨 실행하게 되면 같은 휴대폰 번호로 여러개의 토큰값들이 저장되게 됨)
         * (그래서 가장 최근에 생성된 토큰값 이외의 값들은 모두 삭제)
         */

        String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);

        // 일치하는 폴더가 없으면 생성
        if( !file.exists() ) {
            file.mkdirs();
            Log.d("sbg_test", "폴더 생성 성공");
        }

        // txt 파일 생성
        String testStr = rId + "\n" + rLicense;
        File savefile = new File(dirPath+"/regist.txt");

        try{

            FileOutputStream fos = new FileOutputStream(savefile);
            fos.write(testStr.getBytes());
            fos.close();
            Log.d("sbg_test", "파일 생성 성공");
            CheckFcmDb();
        } catch(IOException e){
            Log.d("sbg_test", "인증 파일 생성 에러 : " + e.getMessage().toString());
        }

    }

    public void CheckFcmDb(){
        //인증값(id,license)을 fcm DB에 적용시키고 중복값 삭제
        sendData.getData(SettingVar.CHECK_FCM_DB+"?tel="+SettingVar.phNumber+"&id="+rId+"&license="+rLicense,"cfd");
    }

    public void MoveMainPage(){
        /**
         * 2017-05-11
         * 작성자 : 서봉교
         * 인증값을 전역변수에 저장시키고 메인 페이지로 이동
         */
        SettingVar.id =  rId.replaceAll("\\p{Z}", "");
        SettingVar.license = rLicense.replaceAll("\\p{Z}", "");

        if(SettingVar.id != "" && SettingVar.license != ""){
            //아이디와 라이센스 값이 있으면 메인페이지 이동
            Intent i = new Intent(IntroPage.this,MainActivity.class);

            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }else {
            //아이디와 라이센스값이 없으면 실행
        }

    }

    //폰트일괄적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}
