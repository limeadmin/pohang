package com.example.pg.phlira_app.msg;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pg.phlira_app.R;
import com.example.pg.phlira_app.inc.MsgCustomAdapter;
import com.example.pg.phlira_app.inc.SendHttpData;
import com.example.pg.phlira_app.inc.SettingVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by pg on 2017-05-02.
 * 작성자 : 서봉교
 * 메세지 리스트 확인페이지
 */

public class LoadMsgData extends Activity implements View.OnClickListener{

    String myJSON;
    JSONArray peoples = null;
    MsgCustomAdapter adapter;
    ListView listview;

    Button sdateBtn;
    Button edateBtn;
    Button msgSearch;
    int sYear,sMonth,sDay,sHour,sMinute;
    int eYear,eMonth,eDay,eHour,eMinute;
    SendHttpData sendData = new SendHttpData(this);


    //메세지를 읽어옴

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_view_page);
        sendData.getData(SettingVar.MSG_LOAD_SERVER_FILE+"?id="+SettingVar.id,"mls");

        listview = (ListView)findViewById(R.id.listview);

        sdateBtn = (Button)findViewById(R.id.sdate_btn);
        edateBtn = (Button)findViewById(R.id.edate_btn);
        msgSearch = (Button)findViewById(R.id.msg_search);

        sdateBtn.setOnClickListener(this);
        edateBtn.setOnClickListener(this);
        msgSearch.setOnClickListener(this);

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        sYear = cal.get(Calendar.YEAR);
        sMonth = cal.get(Calendar.MONTH);
        sDay = cal.get(Calendar.DAY_OF_MONTH);
        sHour = cal.get(Calendar.HOUR_OF_DAY);
        sMinute = cal.get(Calendar.MINUTE);

        eYear = cal.get(Calendar.YEAR);
        eMonth = cal.get(Calendar.MONTH);
        eDay = cal.get(Calendar.DAY_OF_MONTH);
        eHour = cal.get(Calendar.HOUR_OF_DAY);
        eMinute = cal.get(Calendar.MINUTE);
        Typeface face = Typeface.createFromAsset(getAssets(), SettingVar.FONT_FILE);
        sdateBtn.setTypeface(face);
        edateBtn.setTypeface(face);
        msgSearch.setTypeface(face);


        UpdateNow();
    }
    
    public void onClick(View v){
        switch(v.getId()){
            case R.id.sdate_btn:
                new DatePickerDialog(LoadMsgData.this, sDateSetListener, sYear,sMonth, sDay).show();
                break;
            case R.id.edate_btn:
                new DatePickerDialog(LoadMsgData.this, eDateSetListener, eYear,eMonth, eDay).show();
                break;
            case R.id.msg_search:
                Toast.makeText(this, sdateBtn.getText()+" ~ "+edateBtn.getText(), Toast.LENGTH_SHORT).show();
                break;

        }
    }
    
    public void showList(String result) {
        /**
         * 메세지 리스트를 불러옴
         * 0:num(DB 인덱스), 1:id(작성자ID), 2:kind(소속), 3:subject(제목), 4:msg(내용), 5:img(등록된 이미지), 6:wdate(작성날짜), 7:mconf(메세지 읽음 유무 체크, fcm_msg의 항목은 아님)
         */
        myJSON = result;
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(SettingVar.TAG_RESULTS);
            adapter = new MsgCustomAdapter();
            listview.setAdapter(adapter);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String[] cont = new String[8];

                cont[0] = c.getString(SettingVar.MSG_DB_NUM);
                cont[1] = c.getString(SettingVar.MSG_DB_ID);
                cont[2] = c.getString(SettingVar.MSG_DB_KIND);
                cont[3] = c.getString(SettingVar.MSG_DB_SUBJECT);
                cont[4] = c.getString(SettingVar.MSG_DB_MSG);
                cont[5] = c.getString(SettingVar.MSG_DB_IMG);
                cont[6] = c.getString(SettingVar.MSG_DB_WDATE);
                cont[7] = c.getString(SettingVar.MSG_CONFIRM);

                adapter.add(cont);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //날짜 대화상자 리스너 부분
    DatePickerDialog.OnDateSetListener sDateSetListener =

            new DatePickerDialog.OnDateSetListener() {

                @Override

                public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {

                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤

                    sYear = year;

                    sMonth = monthOfYear;

                    sDay = dayOfMonth;

                    //텍스트뷰의 값을 업데이트함

                    UpdateNow();

                }

            };

    //날짜 대화상자 리스너 부분

    DatePickerDialog.OnDateSetListener eDateSetListener =

            new DatePickerDialog.OnDateSetListener() {

                @Override

                public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {

                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤

                    eYear = year;

                    eMonth = monthOfYear;

                    eDay = dayOfMonth;

                    //텍스트뷰의 값을 업데이트함

                    UpdateNow();

                }

            };

    //날짜 텍스트뷰의 값을 업데이트 하는 메소드
    void UpdateNow(){
        sdateBtn.setText(String.format("%d-%d-%d", sYear,sMonth + 1, sDay));
        edateBtn.setText(String.format("%d-%d-%d", eYear,eMonth + 1, eDay));
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("sbg_test","RESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("sbg_test","PAUSE");
    }
}
