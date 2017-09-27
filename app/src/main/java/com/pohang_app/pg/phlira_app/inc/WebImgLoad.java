package com.pohang_app.pg.phlira_app.inc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lime1 on 2017-05-17.
 */

public class WebImgLoad extends Thread {
    private String imgUrl = "";
    private Bitmap bitmap;

    public WebImgLoad(String url){
        imgUrl = url;
    }

    public void run(){
        try {
            URL url = new URL(imgUrl); // URL 주소를 이용해서 URL 객체 생성

            //  아래 코드는 웹에서 이미지를 가져온 뒤
            //  이미지 뷰에 지정할 Bitmap을 생성하는 과정

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch(IOException ex) {

        }
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

}
