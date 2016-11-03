package com.example.konka.workbench.activity.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.konka.workbench.domain.*;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ValueEventListener;


/**
 * Created by Wangfan on 2016-10-18.
 */
public class MessagePresenter {
    public final static String START_MAINATY = "_START_MAINATY";
    public final static String CREATE_MAINATY = "_CREATE_MAINATY";
    public final static String START_MSGATY = "_START_MSGATY";
    public final static String CREATE_MSGATY = "_CREATE_MSGATY";
    public final static String DESTROY_MAINATY = "_DESTROY_MAINATY";
    public final static String STOP_MSGATY = "_STOP_MSGATY";
    public final static String LOGOUT = "_LOGOUT";
    private static boolean mOnMainAty = false;
    private static boolean mOnMessageAty = false;
    private static MessageUpdateListener mUpdateListenerForService;
    private static MessageUpdateListener mUpdateListenerForActivity;
    private static MessagePresenterControlListener mControlListener;
    private static Timer mOutlineTimer;
    private static Timer mDisconnectTimer;
    private static MessagePush mMessagePush;
    private static BmobRealTimeData mProjectConnect = new BmobRealTimeData();//监听项目变化
    private static MsgUpdateThread mMsgThread;
    private static MsgCheckThread mMsgCheck;
    /**
     * 计时器正在运行标记
     */
    private static boolean mIsTimerRun = false;
    /**
     * 与BOMB云连接状态标记
     */
    private static boolean mIsConnect = false;
    private static boolean mIsConnectIMD = false;//即时更新标记,在CREATE和START都有连接的函数，为了不让他们同时执行连接而设计
    /**
     * 消息中心状态计数器,执行一次MessageATY的DESTROY（++），就应当执行一次MAIN的START（--），如果大于0就说明不是离开主界面的ONSTART
     * 如果大于0则说明这次主界面的ONSTART是执行之前的
     */
    private static int mMessageAtyCount = 0;
    private static int mMainAtyCount = 0;

    private static Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1011) {
                mUpdateListenerForActivity.onUpdateForActivity();
            }
            else if (msg.what == 0x1021){
                mUpdateListenerForService.onUpdateForService();
            }
        }
    };

    private static class MsgUpdateThread extends Thread{
        private Handler msgHandler;
        private Timer timer;
        private TimerTask timerTask;
        public void run(){
            Looper.prepare();
            msgHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0x101){//消息更新
                        MessageDBHelper.startUpdate();
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            boolean doVS = true;
                            boolean doBD = true;
                            boolean doUI = true;
                            @Override
                            public void run() {
                                if(doVS&&!isInterrupted()){
                                    MessageDBHelper.updateVersion();
                                    doVS = false;
                                }
                                if(MessageDBHelper.isUpdateVSfinish()&&doBD&&!isInterrupted()){
                                    MessageDBHelper.updateTable(MessageDBHelper.MSG_UPDATE);
                                    doBD = false;
                                }
                                if(MessageDBHelper.isUpdateBDfinish()&&doUI&&!isInterrupted()){
                                    if(mOnMessageAty) {//传回界面更新的消息
                                        mainHandler.sendEmptyMessage(0x1011);//把更新完成信号回传给主线程
                                    }else {
                                        //不刷新界面（已经更新了表）
                                    }
                                    doUI = false;
                                    timer.cancel();
                                }
                                //System.out.println("定时器发送");
                            }
                        };
                        timer.schedule(timerTask,0,100);
                    }
                    if(msg.what == 0x102){//推送更新
                        MessageDBHelper.startUpdate();
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            boolean doVS = true;
                            boolean doBD = true;
                            boolean doUI = true;
                            @Override
                            public void run() {
                                if(doVS&&!isInterrupted()){
                                    MessageDBHelper.updateVersion();
                                    doVS = false;
                                }
                                if(MessageDBHelper.isUpdateVSfinish()&&doBD&&!isInterrupted()){
                                    MessageDBHelper.updateTable(MessageDBHelper.PUSH_UPDATE);//这里设为推送更新
                                    doBD = false;
                                }
                                if(MessageDBHelper.isUpdateBDfinish()&&doUI&&!isInterrupted()){
                                    if(!mOnMessageAty) {//不在界面里，传回推送消息
                                        mainHandler.sendEmptyMessage(0x1021);//这里传递推送消息
                                    }else {
                                        //不推送(已经更新了表)
                                    }
                                    doUI = false;
                                    timer.cancel();
                                }
                                //System.out.println("定时器发送");
                            }
                        };
                        timer.schedule(timerTask,0,100);
                    }
                }
            };
            Looper.loop();
        }
    }

    private static class MsgCheckThread extends Thread{
        private BmobQuery<Project> query = new BmobQuery<Project>();
        private BmobQuery<User> innerQuery = new BmobQuery<User>();
        private User user = BmobUser.getCurrentUser(User.class);//取得当前用户
        private Handler checkHandler;
        public void run(){
            Looper.prepare();
            checkHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0x10){
                        JSONObject data = (JSONObject) msg.obj;
                        JSONObject projectdata = data.optJSONObject("data");//JSONObject里的data存放的是表里的有效数据
                        String projectname = projectdata.optString("projectName");//project里的userlist存放的是表里的有效数据

                        System.out.println(projectname);

                        innerQuery.addWhereEqualTo("objectId", user.getObjectId());//查找为登录ID的用户
                        query.addWhereMatchesQuery("userList", "_User", innerQuery);//关联查找
                        query.addWhereEqualTo("projectName", projectname);

                        query.count(Project.class,new CountListener(){
                            @Override
                            public void done(Integer count, BmobException e) {
                                if (e == null) {
                                    System.out.println("消息更新 count:"+count);
                                    if(count>0){
                                        if(mOnMessageAty) {//如果处于消息中心界面 就更新数据表
                                            mMsgThread.msgHandler.sendEmptyMessage(0x101);
                                        }else {//否则执行推送
                                            mMsgThread.msgHandler.sendEmptyMessage(0x102);
                                        }
                                    }
                                }else {
                                    System.out.println("bomb云连接查询失败");
                                }
                            }
                        });
                    }
                    else if (msg.what == 0x11){
                        user = BmobUser.getCurrentUser(User.class);//重新获取当前用户
                    }
                }
            };
            Looper.loop();
        }
    }

/*
    public MessagePresenter(MessageUpdateListener messageUpdateListener){
        mMsgThread = new MsgUpdateThread();
        mMsgThread.start();
        mMsgCheck = new MsgCheckThread();
        mMsgCheck.start();
        this.mUpdateListener = messageUpdateListener;
        initControlListener();
    }
   */
    public static void initMessagePresenter(MessageUpdateListener listenerForService){
        mMsgThread = new MsgUpdateThread();
        mMsgThread.start();
        mMsgCheck = new MsgCheckThread();
        mMsgCheck.start();
        mUpdateListenerForService = listenerForService;//默认对Service设立监听器
        // initControlListener();
    }

    public static void setListenerForMessage(MessageUpdateListener listenerForActivity){
        mUpdateListenerForActivity = listenerForActivity;
    }

    /**
     * 主动更新函数
     */
    public static void update(){

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mMsgThread.msgHandler.sendEmptyMessage(0x101);
            }
        },100);
    }


    public static MessagePresenterControlListener getControlListener(){
        return mControlListener;
    }

    public static void initControlListener() {
        mControlListener = new MessagePresenterControlListener() {
            @Override
            public void onChangeStatue(String statue) {
                switch (statue){
                    case CREATE_MAINATY:{
                        /**
                         * 打开软件时
                         * 操作：与服务器同步绑定
                         * 收到表变动时，确认是否为用户的消息变动 如果是就更新本地表
                         */
                        mOnMainAty = true;
                        if (!mIsConnect&&!mIsConnectIMD){//未连接时建立用户连接，并且START函数没有连接时
                            System.out.println("连接BOMB云");
                            mIsConnectIMD = true;
                            initBombConnect();
                        }else if (mIsConnectIMD){
                            mIsConnectIMD = false;
                        }
                        MessageDBHelper.autoDelete();
                        break;
                    }
                    case START_MAINATY:{
                        if(mMainAtyCount<0)
                            mMainAtyCount++;

                        if(mMessageAtyCount==0&&mMainAtyCount==0){
                            mOnMainAty = true;
                            if(mIsTimerRun) {
                                mOutlineTimer.cancel();
                                mIsTimerRun = false;
                                System.out.println("关闭离线更新");
                            }
                        }
                        if (mMessageAtyCount>0){
                            mMessageAtyCount--;
                        }

                        if (!mIsConnect&&!mIsConnectIMD){//如果下线了 重连的时候建立新的用户连接,当前CREATE建立了连接则不再建立
                            mIsConnectIMD = true;
                            initBombConnect();
                            //mMsgCheck.checkHandler.sendEmptyMessage(0x11);
                        }else if (mIsConnectIMD){
                        mIsConnectIMD = false;
                        }
                        break;
                    }
                    case CREATE_MSGATY:{
                        /**
                         * 开始消息中心时
                         * 操作：关闭对消息中心的更新刷新回调
                         */
                        mMessageAtyCount++;
                        break;
                    }
                    case START_MSGATY:{
                        /**
                         * 打开消息中心时
                         * 操作：建立对消息中心的更新回调
                         * 收到表变动时，确认是否为用户的消息变动 如果是就更新本地表
                         * 更新完成后在消息中心回调刷新函数
                         */
                        mOnMessageAty = true;
                        System.out.println("结束消息推送");
                        break;
                    }
                    case STOP_MSGATY:{
                        /**
                         * 暂停消息中心时
                         * 操作：关闭对消息中心的更新刷新回调
                         */
                        mOnMessageAty = false;
                        System.out.println("开始消息推送");
                        break;
                    }
                    case DESTROY_MAINATY:{
                        /**
                         * 关闭程序时
                         * 操作：打开离线更新
                         */
                        mOnMainAty = false;
                        if (!mIsTimerRun) {
                            mOutlineTimer = new Timer();
                            mOutlineTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (!mOnMainAty) {
                                        MessageDBHelper.outLineUpdate();
                                        mUpdateListenerForService.onUpdateForService();//发出推送信号
                                    }
                                }
                            }, 0, 60 * 60 * 1000);
                            mIsTimerRun = true;
                            System.out.println("开始离线更新");
                        }
                        mMainAtyCount--;
                        break;
                    }
                    case LOGOUT:{
                        /**
                         * 登出时
                         * 操作：关闭连接，设置断开标记
                         */
                        mDisconnectTimer = new Timer();
                        mDisconnectTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if(mIsConnect&&mProjectConnect.isConnected()) {
                                    mProjectConnect.unsubTableUpdate("Project");//取消监听
                                    mIsConnect = false;
                                    mIsConnectIMD = false;
                                    mDisconnectTimer.cancel();
                                    System.out.println("断开连接");
                                }
                            }
                        },0,100);

                        break;

                    }

                }
            }
        };
    }

    private static void initBombConnect(){
        mProjectConnect.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {//只要保存一次工程表就会回调一个这个函数，所以只要监听工程表就可以同时了解版本是不是改变了
                connectUpdate(data);
            }
            @Override
            public void onConnectCompleted(Exception ex) {
                if(mProjectConnect.isConnected()) {
                    mProjectConnect.subTableUpdate("Project");//监听表更新
                    mIsConnect = true;
                }
                Log.d("bmob", "连接成功:"+mProjectConnect.isConnected());
            }
        });
    }

    /**
     * 建立连接以后 每更改一次表 就会用此函数寻找一次属于谁的更新
     * @param data 更新的那一行数据
     */
    public static void connectUpdate(JSONObject data) {
        Message message = new Message();
        message.obj = data;
        message.what = 0x10;
        mMsgCheck.checkHandler.sendMessage(message);
    }

    /**
     * 返回连接状态
     */
    public static boolean isConnect(){
        return mIsConnect;
    }

    public static void resetUsers(){//重新获取用户
        mMsgCheck.checkHandler.sendEmptyMessage(0x11);
    }

}