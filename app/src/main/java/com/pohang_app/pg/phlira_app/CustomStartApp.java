package com.pohang_app.pg.phlira_app;

import android.app.Application;

import com.pohang_app.pg.phlira_app.inc.SettingVar;
import com.tsengvn.typekit.Typekit;

/**
 * Created by lime1 on 2017-05-19.
 */

public class CustomStartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, SettingVar.FONT_FILE));
                //.addBold(Typekit.createFromAsset(this, "폰트2.ttf"))
                //.addCustom1(Typekit.createFromAsset(this, "폰트3.ttf"));// "fonts/폰트.ttf"
    }
}
