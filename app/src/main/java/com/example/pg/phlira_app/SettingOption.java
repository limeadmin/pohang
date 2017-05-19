package com.example.pg.phlira_app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;


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

public class SettingOption extends Activity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_option);

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
                    //if(count == 0) rId = temp.replaceAll(" ", "");
                    //if(count == 1) rLicense = temp.replaceAll(" ", "");
                    count = count + 1;
                }


            } catch (Exception e) {
                Log.d("sbg_test", "설정 파일 읽기 에러 : " + e.getMessage().toString());
            }
        }else{
            Log.d("sbg_test", "설정 파일 없음");

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
        String testStr = "11"+ "\n" + "22";
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
        Log.d("sbg_test","옵션설정 : RESUMT");
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
