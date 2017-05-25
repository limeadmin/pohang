package com.example.pg.phlira_app.msg;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pg.phlira_app.BaseActivity;
import com.example.pg.phlira_app.R;
import com.example.pg.phlira_app.inc.SendHttpData;
import com.example.pg.phlira_app.inc.SettingVar;
import com.example.pg.phlira_app.inc.WebImgLoad;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by pg on 2017-05-08.
 * 작성자 : 서봉교
 * 메세지 상세 보기
 */

public class ReadMsgData extends Activity{
    TextView readSub;
    TextView readDate;
    TextView readContent;

    String myJSON;
    JSONArray peoples = null;

    ImageView msgImg;
    Bitmap bitmap;

    SendHttpData sendData = new SendHttpData(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_msg_page);
        readSub = (TextView)findViewById(R.id.read_sub);
        readDate = (TextView)findViewById(R.id.read_date);
        readContent = (TextView)findViewById(R.id.read_content);

        //readContent.setMovementMethod(new ScrollingMovementMethod());

        msgImg = (ImageView)findViewById(R.id.msg_img);

        Intent intent = getIntent();
        String num = intent.getStringExtra("num");

        //Log.d("sbg_test","받은 num 값 : "+num);

        sendData.getData(SettingVar.READ_MSG_SERVER_FILE+"?num="+num+"&id="+SettingVar.id,"rmsf");
    }

    public void showList(String result) {
        myJSON = result;
        try {

            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(SettingVar.TAG_RESULTS);
            String text = "";
            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String[] cont = new String[7];

                cont[0] = c.getString(SettingVar.MSG_DB_NUM);
                cont[1] = c.getString(SettingVar.MSG_DB_ID);
                cont[2] = c.getString(SettingVar.MSG_DB_KIND);
                cont[3] = c.getString(SettingVar.MSG_DB_SUBJECT);
                cont[4] = c.getString(SettingVar.MSG_DB_MSG);
                cont[5] = c.getString(SettingVar.MSG_DB_IMG);
                cont[6] = c.getString(SettingVar.MSG_DB_WDATE);

                readSub.setText(cont[3]);
                readDate.setText(cont[6]);
                readContent.setText(cont[4]);

                //  안드로이드에서 네트워크 관련 작업을 할 때는
                //  반드시 메인 스레드가 아닌 별도의 작업 스레드에서 작업해야 합니다.

                final String imgPath = SettingVar.MSG_IMG_PATH+cont[5];
                //웹서버에서 이미지 불러오는 클라스
                WebImgLoad mThread = new WebImgLoad(imgPath);

                mThread.start(); // 웹에서 이미지를 가져오는 작업 스레드 실행.

                try {

                    //  메인 스레드는 작업 스레드가 이미지 작업을 가져올 때까지
                    //  대기해야 하므로 작업스레드의 join() 메소드를 호출해서
                    //  메인 스레드가 작업 스레드가 종료될 까지 기다리도록 합니다.

                    mThread.join();

                    //  이제 작업 스레드에서 이미지를 불러오는 작업을 완료했기에
                    //  UI 작업을 할 수 있는 메인스레드에서 이미지뷰에 이미지를 지정합니다.
                    bitmap = mThread.getBitmap();
                    if(bitmap != null){
                        msgImg.setImageBitmap(bitmap);
                    }
                } catch (InterruptedException e) {

                }
                //text += cont[0]+"__"+cont[1]+"__"+cont[2]+"__"+cont[3]+"__"+cont[4]+"__"+cont[5]+"\n";
            }

            //Toast.makeText(this, text,Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //백버튼 클릭시 에니메이션 효과 주기

    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            this.finish();
            overridePendingTransition(R.anim.anim_left_slide, R.anim.anim_right_slide);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //폰트일괄적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(SettingVar.id.equals("")){

            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
