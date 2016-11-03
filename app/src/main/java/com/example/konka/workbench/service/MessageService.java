package com.example.konka.workbench.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.example.konka.workbench.activity.main.MainActivity;
import com.example.konka.workbench.R;
import com.example.konka.workbench.activity.message.*;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.PendingIntent;

/**
 * Created by HP on 2016-9-13.
 * 我的消息提醒
 */
public class MessageService extends Service {
    NotificationManager my_message;
    MessagePush Push;
    //private PushThread pushThread;
    //Timer timer = new Timer();

    private static MessageServiceListener mServiceListener;
    //private static int mCallBackCount = 0;//建立回调计数器

    private MessageUpdateListener mMessageUpdateListener;
/*
    public static int getCallBackCount(){
        return mCallBackCount;
    }
*/
    //MessagePresenter mMessagePresenter;

    /*
        private boolean isOnline(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            NetworkInfo netWorkInfo = info[i];
                            if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                return true;
                            } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                return true;						}
                        }
                    }
                }
            }
            return false;
        }
    */
/*
    class PushThread extends Thread{
        public Handler pushHandler;
        public void run(){
            Looper.prepare();
            pushHandler = new Handler() {
                @Override
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == 0x12) {
                        SQLite = new MSG_SQLite(MessageService.this, "push");
                        //SQLite.SetStart();
                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            boolean doVS = true;
                            boolean doBD = true;
                            boolean doUI = true;

                            @Override
                            public void run() {
                                if (doVS) {
                                    SQLite.Update_VS();
                                    doVS = false;
                                }
                                if (SQLite.isVSfinnish() && doBD) {
                                    SQLite.Update_BD();
                                    doBD = false;
                                }
                                if (SQLite.isBDfinnish() && doUI) {
                                    Push();
                                    SQLite.CloseBD();
                                    doUI = false;
                                    timer.cancel();
                                }
                                //System.out.println("定时器发送");
                            }
                        }, 0, 100);
                    }
                }
            };
            Looper.loop();
        }
    }
*/
    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        flags = START_STICKY;
        Intent mainintent = new Intent(this,MainActivity.class);//点开这个消息窗可以链接到我的消息界面
        PendingIntent pi = PendingIntent.getActivity(this,0,mainintent,0);
        Notification notify = new Notification.Builder(this)
                .setAutoCancel(true)
                .setTicker("后台运行通知")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("KONKA")
                .setContentText("KONKA正在运行")
                .setContentIntent(pi)
                .build();

        startForeground(0x111, notify);
        System.out.println("Service_onStart");

        mServiceListener.onStartService();
        /*
        if(mListenerForMain != null) {
            mCallBackCount++;
            mListenerForMain.onStartService();//回调StartService事件
        }
        */
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service_onCreate");
        my_message = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initMsgPresenter();//有问题 这里

        if (mServiceListener == null) {

            mServiceListener = ListenerControlPanel.getServiceListener();
        }
        mServiceListener.onCreateService();
        //mListenerForMain = MainActivity.setServiceListener();//绑定主界面事件
        /*
        if(mListenerForMain != null) {
            mCallBackCount++;
            mListenerForMain.onCreateService();//回调建立完成事件
        }
*/
    }
/*
    public static void setMessageServiceListener(MessageServiceListener listener){
        mListenerForMain = listener;
    }
*/
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onDestroy(){
        mServiceListener.onDestroyService();
        stopForeground(true);
        super.onDestroy();
        System.out.println("结束后台");
    }

    private void Push(){
        Push = new MessagePush(this,my_message,MessageDBHelper.searchMessage(MessageDBHelper.PUSH_MESSAGE));
        Push.PushMessage();
    }

    private void initMsgPresenter(){
        mMessageUpdateListener = new MessageUpdateListener() {
            @Override
            public void onUpdateForActivity() {
                //监听到对ATY的更新不采取任何操作
            }
            @Override
            public void onUpdateForService(){
                System.out.println("后台推送：");
                Push();
                //监听到对Service的操作采取动作
                //执行推送
            }
        };
        MessagePresenter.initMessagePresenter(mMessageUpdateListener);
    }


}