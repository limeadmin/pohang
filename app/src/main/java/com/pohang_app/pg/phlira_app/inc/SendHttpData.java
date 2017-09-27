package com.pohang_app.pg.phlira_app.inc;

import android.content.Context;
import android.os.AsyncTask;

import com.pohang_app.pg.phlira_app.IntroPage;
import com.pohang_app.pg.phlira_app.msg.LoadMsgData;
import com.pohang_app.pg.phlira_app.msg.ReadMsgData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pg on 2017-05-10.
 * 작성자 : 서봉교
 * 웹서버와 통신
 */

public class SendHttpData {
    private Context context = null;

    public SendHttpData(Context cont){
        this.context = cont;
    }

    public void getData(String url,String kind) {
        //처리해야될 서버파일의 종류를 저장
        final String rKind = kind;

        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];


                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    //종류(rKind)에 따라 처리해야될 프로세스 구분
                    if(rKind.equals("rmc")){ ((IntroPage)context).resultData(result); }
                    if(rKind.equals("cfd")){ ((IntroPage)context).MoveMainPage(); }
                    if(rKind.equals("mls")){ ((LoadMsgData)context).showList(result); }
                    if(rKind.equals("rmsf")){ ((ReadMsgData)context).showList(result); }

                }catch(Exception e) {

                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}
