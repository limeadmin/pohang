package com.pohang_app.pg.phlira_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pohang_app.pg.phlira_app.inc.SettingVar;

/**
 * Created by lime1 on 2017-05-22.
 * 작성자 : 서봉교
 * 기본 액티비티
 */

public class BaseActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SettingVar.id.equals("")){
            Log.d("sbg_test","BaseActivity : id not exsits");
            /*Intent mIntent = new Intent(this, IntroPage.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(mIntent), 0);

            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            am.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, pendingIntent);

            moveTaskToBack(true);*/

            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        }else{
            Log.d("sbg_test","BaseActivity : id exsits");
        }
    }
}
