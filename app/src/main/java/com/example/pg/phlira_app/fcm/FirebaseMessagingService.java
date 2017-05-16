package com.example.pg.phlira_app.fcm;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pg.phlira_app.IntroPage;
import com.example.pg.phlira_app.MainActivity;
import com.example.pg.phlira_app.R;
import com.example.pg.phlira_app.inc.SettingVar;
import com.example.pg.phlira_app.msg.MsgPopup;
import com.example.pg.phlira_app.msg.ReadMsgData;
import com.google.firebase.messaging.RemoteMessage;
import android.os.CountDownTimer;

import java.util.List;

/**
 * 2017-05-12
 * 작성자 : 서봉교
 * fcm 메세지 받는 페이지
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private SendMassgeHandler mMainHandler = null;
    CountDownTimer mCountDown = null;
    String appCheck = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mMainHandler = new SendMassgeHandler();
    }

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        //MainActivity 가 실행중인지 아닌지 체크
        boolean appCheck = getRunActivity();

        if(isScreenOn) {
            //푸쉬메세지 알림창(폰이 켜졌을때)
            mMainHandler.sendEmptyMessage(SettingVar.TOAST_POPUP);
        }else {
            //푸쉬메세지 알림창(폰이 꺼졌을때)
          if (SettingVar.msgPopup) {
            // 팝업으로 사용할 액티비티를 호출할 인텐트를 작성한다.

            if(appCheck){
               //앱이 실행중
            }else{
               //앱이 죽음
            }
            Intent popupIntent = new Intent(this, com.example.pg.phlira_app.msg.MsgPopup.class)
                      .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                      .putExtra("num",remoteMessage.getData().get("num"))
                      .putExtra("appchk",appCheck);
                      //.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

            // 그리고 호출한다.
            startActivity(popupIntent);
          }
        }
        sendNotification(remoteMessage.getData().get("message"),remoteMessage.getData().get("num"));
    }

    // Handler 클래스
    class SendMassgeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SettingVar.TOAST_POPUP:

                    //MsgPopupToast();
                    mCountDown = new CountDownTimer(5000,1000){

                        public void onTick(long millisUntilFinished) {
                            MsgPopupToast();
                        }

                        public void onFinish() {
                            mCountDown.cancel();
                        }

                    }.start();

                    break;

                default:
                    break;
            }
        }

    };

    public void MsgPopupToast(){

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.msg_popup_toast,null);

        //ImageView imageView = (ImageView) layout.findViewById(R.id.like_popup_iv);

        //TextView text = (TextView) layout.findViewById(R.id.like_popup_tv);
        //text.setText("Like");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, -200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        toast.show();
    }

    private void sendNotification(String messageBody,String num) {
        Intent intent;


        //앱이 실행중일때와 앱이 종료되었을때 알림창 클릭시 실행되는 액티비티 설정
        if(SettingVar.id.equals("")){
            intent = new Intent(this, IntroPage.class);
        }else{
            intent = new Intent(this, ReadMsgData.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("num",num);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher2)
                .setContentTitle("FCM Push Test")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        /*
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Push Test")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        아이콘이나 제목을 수정해 줄 수 있다.
        */


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    boolean getRunActivity(){

        ActivityManager activity_manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> task_info = activity_manager.getRunningTasks(9999);

        for(int i=0; i<task_info.size(); i++) {
            Log.d("sbg_test","팩키지 이름 : "+task_info.get(i).topActivity.getPackageName());
            if(task_info.get(i).topActivity.getPackageName().equals("com.example.pg.phlira_app")){
                appCheck = task_info.get(i).topActivity.getPackageName();
                return true;
            }
        }
        return false;
    }

}
