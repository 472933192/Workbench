package com.example.konka.workbench.activity.message;

import android.bluetooth.BluetoothProfile;
import android.content.Intent;

import com.example.konka.workbench.activity.main.MainActivityStatusListener;
import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.service.MessageService;
import com.example.konka.workbench.service.MessageServiceListener;

/**
 * Created by HP on 2016-10-28.
 */
public class ListenerControlPanel {
    private static final String CREATE = "_CREATE";
    private static final String CREATE_DELAY = "_CREATE_DELAY";
    private static final String START = "_START";
    private static final String STOP = "_STOP";
    private static final String DESTROY = "_DESTROY";
    private static final String UNSET = "_UNSET";
    private static final String LOGIN = "_LOGIN";
    private static final String LOGOUT = "_LOGOUT";
    private static final String LOGOUT_DELAY = "_LOGOUT_DELAY";

    private static String mMainStatus = UNSET;
    private static String mServiceStatus = UNSET;
    private static String mMessageStatus = UNSET;
    private static String mLogoutStatus = UNSET;


    private static boolean mCreateMainDelay = false;
    private static boolean mStartMainDelay = false;
    private static boolean mStartMessageDelay = false;
    private static boolean mDestroyMainDelay = false;
    private static boolean mStopMessageDelay = false;
    private static boolean mCreateMessageDelay = false;
    private static boolean mLogoutDelay = false;

    private static boolean mIsLogoutWithoutClear = false;

    private static MessagePresenterControlListener mControlListener;

    private static MessageServiceListener mServiceListener =  new MessageServiceListener() {
        @Override
        public void onCreateService() {
            System.out.println("消息服务Create回调");
            mServiceStatus = CREATE;
            if (!mLogoutStatus.equals(LOGIN))//如果不处于登录状态不执行后面的离线更新后台推送等
                return;

            if (mCreateMainDelay){
                mControlListener.onChangeStatue(MessagePresenter.CREATE_MAINATY);
                MessagePresenter.update();//主动更新一次
                mCreateMainDelay = false;
            }
            if (mStartMainDelay){
                mControlListener.onChangeStatue(MessagePresenter.START_MAINATY);
                mStartMessageDelay = false;
            }
            if (mDestroyMainDelay){
                mControlListener.onChangeStatue(MessagePresenter.DESTROY_MAINATY);
                mDestroyMainDelay = false;
            }
            if (mLogoutDelay){
                mControlListener.onChangeStatue(MessagePresenter.LOGOUT);
                mLogoutDelay = false;
            }

            if (mCreateMessageDelay){
                mControlListener.onChangeStatue(MessagePresenter.CREATE_MSGATY);
                mCreateMessageDelay = false;
            }
            if (mStartMessageDelay){
                mControlListener.onChangeStatue(MessagePresenter.START_MSGATY);
                mStartMessageDelay = false;
            }
            if (mStopMessageDelay){
                mControlListener.onChangeStatue(MessagePresenter.STOP_MSGATY);
                mStopMessageDelay = false;
            }
        }
        @Override
        public void onStartService(){
            System.out.println("消息服务Start回调");
            mServiceStatus = START;
        }
        @Override
        public void onDestroyService(){
            mServiceStatus = DESTROY;
        }
    };

    private static MainActivityStatusListener mMainListener = new MainActivityStatusListener() {
        @Override
        public void onCreateMain() {
            if(mControlListener == null) {
                MessagePresenter.initControlListener();
                mControlListener = MessagePresenter.getControlListener();
            }
            mMainStatus = CREATE;
            if (mServiceStatus.equals(UNSET)||mServiceStatus.equals(DESTROY)) {//服务未建立的时候服务处于等待建立的状态
                mServiceStatus = CREATE_DELAY;
            }
            if (mLogoutStatus.equals(LOGIN)) {
                if (mServiceStatus.equals(START) || mServiceStatus.equals(CREATE)) {
                    mControlListener.onChangeStatue(MessagePresenter.CREATE_MAINATY);
                    MessagePresenter.update();//主动更新一次
                } else if (mServiceStatus.equals(CREATE_DELAY)) {
                    System.out.println("主活动Create回调等待执行");
                    mCreateMainDelay = true;
                }
            }
        }

        @Override
        public void onStartMain() {
            mMainStatus = START;
            if (mServiceStatus.equals(START)||mServiceStatus.equals(CREATE)) {
                mControlListener.onChangeStatue(MessagePresenter.START_MAINATY);
            }else if(mServiceStatus.equals(CREATE_DELAY)){
                System.out.println("主活动Start回调等待执行");
                mStartMainDelay = true;
            }

        }

        @Override
        public void onDestroyMain() {
            mMainStatus = DESTROY;
            if (!mLogoutStatus.equals(LOGIN))//如果不处于登录状态不执行后面的离线更新后台推送等
                return;
            if (mServiceStatus.equals(START)||mServiceStatus.equals(CREATE)) {
                mControlListener.onChangeStatue(MessagePresenter.DESTROY_MAINATY);
            }else if(mServiceStatus.equals(CREATE_DELAY)){
                mDestroyMainDelay = true;
            }

        }

        @Override
        public void onLogout(){
            mLogoutStatus = LOGOUT;//登出
            mIsLogoutWithoutClear = true;//设置重连时重设用户标记

            if (mServiceStatus.equals(START)||mServiceStatus.equals(CREATE)) {
                mControlListener.onChangeStatue(MessagePresenter.LOGOUT);
            } else if(mServiceStatus.equals(CREATE_DELAY)){
                mLogoutDelay = true;
            }
        }
    };

    private static MessageAtyStatusListener mMessageListener = new MessageAtyStatusListener() {
        @Override
        public void onCreateMessage() {
            mMessageStatus = CREATE;
            if (mServiceStatus.equals(START)||mServiceStatus.equals(CREATE)) {
                mControlListener.onChangeStatue(MessagePresenter.CREATE_MSGATY);
            }else if(mServiceStatus.equals(CREATE_DELAY)){
                mCreateMessageDelay = true;
            }
        }

        @Override
        public void onStartMessage() {
            mMessageStatus = START;
            if (mServiceStatus.equals(START)||mServiceStatus.equals(CREATE)) {
                mControlListener.onChangeStatue(MessagePresenter.START_MSGATY);
            }else if(mServiceStatus.equals(CREATE_DELAY)){
                mStartMessageDelay = true;
            }
        }

        @Override
        public void onStopMessage() {
            mMessageStatus = STOP;
            if (mServiceStatus.equals(START)||mServiceStatus.equals(CREATE)) {
                mControlListener.onChangeStatue(MessagePresenter.STOP_MSGATY);
            }else if(mServiceStatus.equals(CREATE_DELAY)){
                mStopMessageDelay = true;
            }
        }
    };

    private static LogListener mLogListener = new LogListener() {
        @Override
        public void onLogin() {// 登录回调
            mLogoutStatus = LOGIN;//登入
            if (mIsLogoutWithoutClear){//如果之前存在未清理的用户 需要重设
                MessagePresenter.resetUsers();
            }
        }
    };

    public static MessageAtyStatusListener getMessageListener(){
        return mMessageListener;
    }

    public static MainActivityStatusListener getMainListner(){
        return mMainListener;
    }

    public static MessageServiceListener getServiceListener(){
        return mServiceListener;
    }

    public static LogListener getLogListener(){
        return mLogListener;
    }

    public static boolean isServiceInit(){
        if (mServiceStatus.equals(UNSET)||mServiceStatus.equals(DESTROY)){
            return false;
        }else
            return true;
    }

}
