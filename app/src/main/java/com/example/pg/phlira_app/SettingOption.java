package com.example.pg.phlira_app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


import com.example.pg.phlira_app.inc.SettingVar;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by pg on 2017-05-16.
 * 작성자 : 서봉교
 * 옵션설정 페이지
 */

public class SettingOption extends Activity implements CompoundButton.OnCheckedChangeListener{

    Switch alamSwitch,soundSwitch,vibrateSwitch;
    boolean switchLoadCheck = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_option);

        alamSwitch = (Switch)findViewById(R.id.alam_switch);
        soundSwitch = (Switch)findViewById(R.id.sound_switch);
        vibrateSwitch = (Switch)findViewById(R.id.vibrate_switch);

        alamSwitch.setOnCheckedChangeListener(this);
        soundSwitch.setOnCheckedChangeListener(this);
        vibrateSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.alam_switch:

                SettingVar.ALAM_POPUP = isChecked;
                if(switchLoadCheck){ FileMake(); }
                Log.d("sbg_test","알람설정 : "+isChecked);
                break;
            case R.id.sound_switch:
                SettingVar.ALAM_SOUND = isChecked;
                if(switchLoadCheck){ FileMake(); }
                Log.d("sbg_test","사운드설정 : "+isChecked);
                break;
            case R.id.vibrate_switch:
                SettingVar.ALAM_VIBRATE = isChecked;
                if(switchLoadCheck){ FileMake(); }
                Log.d("sbg_test","진동설정 : "+isChecked);
                break;
        }
    }

    //폰트일괄적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    public void FileLoad(){
        /**
         * 2017-05-19
         * 작성자 : 서봉교
         * 설정값을 읽어옴
         */
        String dirPath = getFilesDir().getAbsolutePath();
        String loadPath = dirPath+"/"+"option_set.txt";
        File files = new File(loadPath);

        if(files.exists()) {
            try {
                FileInputStream fis = new FileInputStream(loadPath);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                String temp = "";

                int count = 0;

                while ((temp = bufferReader.readLine()) != null) {
                    if(count == 0) SettingVar.ALAM_POPUP = Boolean.valueOf(temp.replaceAll(" ", "")).booleanValue();
                    if(count == 1) SettingVar.ALAM_SOUND = Boolean.valueOf(temp.replaceAll(" ", "")).booleanValue();
                    if(count == 2) SettingVar.ALAM_VIBRATE = Boolean.valueOf(temp.replaceAll(" ", "")).booleanValue();
                    count = count + 1;

                }
                Log.d("sbg_test","설정파일 카운트 : "+count);

                alamSwitch.setChecked(SettingVar.ALAM_POPUP);
                soundSwitch.setChecked(SettingVar.ALAM_SOUND);
                vibrateSwitch.setChecked(SettingVar.ALAM_VIBRATE);

                switchLoadCheck = true;
            } catch (Exception e) {
                Log.d("sbg_test", "설정 파일 읽기 에러 : " + e.getMessage().toString());
                Toast.makeText(this, "설정파일 읽기 실패~~~!", Toast.LENGTH_LONG).show();
            }
        }else{
            Log.d("sbg_test", "설정 파일 없음");
            FileMake();
        }
    }

    public void FileMake(){

        /**
         * 2017-05-19
         * 작성자 : 서봉교
         * 설정값을 저장시킬 파일을 만들고 설정값 저장
         */

        String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);

        // 일치하는 폴더가 없으면 생성
        if( !file.exists() ) {
            file.mkdirs();
            Log.d("sbg_test", "옵션설정 : 폴더 생성 성공");
        }

        // txt 파일 생성
        String testStr = SettingVar.ALAM_POPUP+ "\n" + SettingVar.ALAM_SOUND + "\n" + SettingVar.ALAM_VIBRATE;
        File savefile = new File(dirPath+"/option_set.txt");

        try{
            FileOutputStream fos = new FileOutputStream(savefile);
            fos.write(testStr.getBytes());
            fos.close();
            Log.d("sbg_test", "옵션설정 : 파일 생성 성공");
        } catch(IOException e){
            Log.d("sbg_test", "옵션설정 : 인증 파일 생성 에러 : " + e.getMessage().toString());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        switchLoadCheck = false;
        FileLoad();
        Log.d("sbg_test","옵션설정 : RESUMT");
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
