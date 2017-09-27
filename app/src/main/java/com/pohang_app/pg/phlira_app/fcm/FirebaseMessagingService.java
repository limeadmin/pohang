package com.pohang_app.pg.phlira_app.fcm;


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


import com.pohang_app.pg.phlira_app.IntroPage;

import com.pohang_app.pg.phlira_app.R;
import com.pohang_app.pg.phlira_app.inc.SettingVar;
import com.pohang_app.pg.phlira_app.msg.ReadMsgData;
import com.google.firebase.messaging.RemoteMessage;
import android.os.CountDownTimer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 2017-05-12
 * 작성자 : 서봉교
 * fcm 메세지 받는 페이지
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private SendMassgeHandler mMainHandler = null;
    CountDownTimer mCountDown = null;
    boolean appCheck = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mMainHandler = new SendMassgeHandler();
    }

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        FileLoad();

        //화면이 꺼져 있는지 체크 true : 켜짐, false : 꺼짐
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        //앱이 실행중인지 아닌지 체크
        appCheck = getRunActivity();

        //알람 설정
        if(SettingVar.ALAM_POPUP){
            if(isScreenOn) {
                //Toast가 스레드라 핸들러를 통해 실행해야됨
                //mMainHandler.sendEmptyMessage(SettingVar.TOAST_POPUP);

                //새로운 Activity 로 팝업창을 띄움
                MsgPopupAct(remoteMessage.getData().get("num"),remoteMessage.getData().get("message"));
            }else {
                //새로운 Activity 로 팝업창을 띄움
                MsgPopupAct(remoteMessage.getData().get("num"),remoteMessage.getData().get("message"));
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

                    //토스트 유지시간을 임의로 늘리기 위해 CountDownTimer 사용
                    mCountDown.cancel();
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

    public void MsgPopupAct(String num,String msg){
        /**
         * 2017_05_16
         * 작성자 : 서봉교
         * appCheck = true (앱이 실행중), appCheck = false (앱이 종료됨)
         */

        Intent popupIntent = new Intent(this, com.pohang_app.pg.phlira_app.msg.MsgPopup.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("num",num)
                .putExtra("msg",msg)
                .putExtra("appchk",appCheck);

        startActivity(popupIntent);
    }

    public void MsgPopupToast(){
        /**
         * 2017_05_16
         * 작성자 : 서봉교
         * Toast 커스텀 마이징
         */
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

        if(appCheck){
            intent = new Intent(this, ReadMsgData.class);
        }else{
            intent = new Intent(this, IntroPage.class);
        }
        //Log.d("sbg_test","넘오온 num 값 : "+num);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("num",num);

        //여러개의 notifi가 생성되었을때 가장 처음 생성된 notifi의 값만 (num값)적용되는 문제 해결
        //PendingIntent에 Request code 값을 주면 됨(고유아이디 개념)
        int rc = Integer.parseInt(num);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,rc /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher2)
                .setContentTitle(SettingVar.MAIN_TITLE)
                .setContentText(messageBody)
                // 알림 터치시 반응 후 알림 삭제 여부.
                .setAutoCancel(true)
                //.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                // 알림이 출력될 때 상단에 나오는 문구.
                .setTicker(SettingVar.MAIN_TITLE)
                // 알림 터치시 반응
                .setContentIntent(pendingIntent);

        if(SettingVar.ALAM_SOUND){ notificationBuilder.setSound(defaultSoundUri); }
        if(SettingVar.ALAM_VIBRATE){ notificationBuilder.setVibrate(new long[]{0,2000}); }

        notificationBuilder.setLights(0xff00ff00,100,2000);
        /*


        // Notification Field

        Notification.DEFAULT_SOUND : 소리 발생
        Notification.DEFAULT_VIBRATE : 진동을 발생
        Notification.DEFAULT_LIGHTS : 불빛 발생
        Notification.DEFAULT_ALL : 상단의 세 가지를 모두 실행

        Notification.FLAG_ONGOING_EVENT : 노티피케이션이 알림에 뜨지 않고 진행중에 뜨게 되는 플래그
        Notification.FLAG_INSISTENT : 확인 할 때 까지 집요하게 사운드를 울려주는 플래그
        Notification.FLAG_NO_CLEAR : 앱의 프로세스를 종료하지 않는 한 노티를 제거 할 수 없게 만드는 플래그
        Notification.FLAG_ONLY_ALERT_ONCE : 취소되더라도 매번 소리와 진동을 발생하는 플래그
        Notification.FLAG_SHOW_LIGHTS : LED 불빛을 출력하는 플래그
        */

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID */, notificationBuilder.build());
    }

    boolean getRunActivity(){
        /**
         * 2017-05-15
         * 작성자 : 서봉교
         * 앱이 실행중인지 아닌지 체크 해서 리턴
         */
        ActivityManager activity_manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task_info = activity_manager.getRunningTasks(9999);

        for(int i=0; i<task_info.size(); i++) {
            Log.d("sbg_test","팩키지 이름 : "+task_info.get(i).topActivity.getPackageName());
            if(task_info.get(i).topActivity.getPackageName().equals("com.example.pg.phlira_app")){
                return true;
            }
        }
        return false;
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


            } catch (Exception e) {
                Log.d("sbg_test", "설정 파일 읽기 에러 : " + e.getMessage().toString());
            }
        }else{
            Log.d("sbg_test", "설정 파일 없음");

        }
    }

}
