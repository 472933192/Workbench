package com.example.konka.workbench.activity.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.konka.workbench.R;
import com.example.konka.workbench.activity.allProject.AllProjectAty;
import com.example.konka.workbench.activity.login.LoginActivity;
import com.example.konka.workbench.activity.message.ListenerControlPanel;
import com.example.konka.workbench.activity.message.LogListener;
import com.example.konka.workbench.activity.message.MessageDBHelper;
import com.example.konka.workbench.activity.message.MessageReadListener;
import com.example.konka.workbench.activity.message.MessageUpdateHelper;
import com.example.konka.workbench.activity.message.MyMessageAty;
import com.example.konka.workbench.activity.myProject.MyProjectAty;
import com.example.konka.workbench.domain.User;
import com.example.konka.workbench.service.MessageService;
import com.example.konka.workbench.util.BadgeView;
import com.example.konka.workbench.util.IsOnline;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;


/**
 * 首页
 */
public class MainActivity extends Activity {

    private final String BMOB_ID = "f7253a4eee6058cc235b9197d64c5749";//应用秘匙
    private boolean mIsLogout;//是否注销
    //private boolean mIsInitService = false;
    final private String DESTROY_DELAY = "_DESTROY_DELAY";
    final private String LOGOUT_DELAY = "_LOGOUT_DELAY";
    final private String LOGOUT_NOW = "_LOGOUT_NOW";
    final private String NONE = "_NULL";
    private String mDelayMark = NONE;
    private String mLogoutDelayMark = NONE;
    private BadgeView mBadgeView;
    private MainActivityStatusListener mMainListener;
    private LogListener mLogListener;

/*
    private MessagePresenterControlListener mControlListener;
    private MessageServiceListener mServiceListener =  new MessageServiceListener() {
        @Override
        public void onCreateService() {
            System.out.println("消息服务Create回调");
            //mIsInitService = true;
            if(MessageService.getCallBackCount()==1) {
                mControlListener.onChangeStatue(MessagePresenter.CREATE_MAINATY);
                if (!mLogoutDelayMark.equals(LOGOUT_NOW) && !mLogoutDelayMark.equals(LOGOUT_DELAY)) {//在没有延时退出或登出的情况下主动更新一次
                    MessagePresenter.update();//主动更新一次
                }
            }
        }
        @Override
        public void onStartService(){
            System.out.println("消息服务Start回调");
            if(MessageService.getCallBackCount()==2) {
                mControlListener.onChangeStatue(MessagePresenter.START_MAINATY);
                if (mDelayMark.equals(DESTROY_DELAY)) {//因为回调了onCreate还会回调onStart，使得线下更新被关闭，所以放在这里做回调恢复
                    mControlListener.onChangeStatue(MessagePresenter.DESTROY_MAINATY);//控制器状态设为MainATY关闭
                    MessageService.setMessageServiceListener(null);//释放静态监听器，防止溢出
                    onDestroy();
                    mDelayMark = NONE;
                }
                if (mLogoutDelayMark.equals(LOGOUT_DELAY)) {
                    mControlListener.onChangeStatue(MessagePresenter.LOGOUT);//控制器状态设为登出
                    Intent service = new Intent(MainActivity.this, MessageService.class);
                    stopService(service);//关闭Service
                    //mIsInitService = false;//Service建立标识为未建立
                    mLogoutDelayMark = NONE;
                } else if (mLogoutDelayMark.equals(LOGOUT_NOW)) {
                    mLogoutDelayMark = NONE;
                }
            }
        }
    };
*/
    private MessageReadListener mReadListener = new MessageReadListener() {
        @Override
        public void onChangeRead() {
            mBadgeView.setBadgeCount(MessageDBHelper.getNoReadNum());//显示的数字
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bmob.initialize(this, BMOB_ID);
        if (BmobUser.getCurrentUser() != null) {//在没有USER的情况下为注销状态{
            LogListener listener = ListenerControlPanel.getLogListener();
            listener.onLogin();
        }

        MessageDBHelper.openTable(MainActivity.this);//打开表
        CreateMessageService();//启动服务
        this.mMainListener = ListenerControlPanel.getMainListner();
        mMainListener.onCreateMain();

        //MessageService.setMessageServiceListener(this.mServiceListener);//设置监听器

        //MessagePresenter.initControlListener();//建立监听器,用于调整Presenter状态
        //mControlListener = MessagePresenter.getControlListener();//获得监听器，调整状态

        /*
        if(MessageService.getCallBackCount()>=2) {
            mControlListener.onChangeStatue(MessagePresenter.CREATE_MAINATY);
            MessagePresenter.update();//主动更新一次
        }
*/

        if (BmobUser.getCurrentUser() != null) {
            mLogListener = ListenerControlPanel.getLogListener();
            mLogListener.onLogin();

            setContentView(R.layout.home_page);

            //---我的项目按钮
            ImageButton myPro_btn = (ImageButton) findViewById(R.id.myPro_btn);
            myPro_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MyProjectAty.class);
                    startActivity(intent);
                }
            });
            //---所有项目按钮
            ImageButton proList_btn = (ImageButton) findViewById(R.id.proList_btn);
            proList_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AllProjectAty.class);
                    startActivity(intent);
                }
            });
            //---消息中心按钮
            ImageButton messageCenter_btn = (ImageButton) findViewById(R.id.messageCenter_btn);
            messageCenter_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MyMessageAty.class);
                    startActivity(intent);
                }
            });

            if(mBadgeView == null) {
                mBadgeView = new BadgeView(this);
            }
            mBadgeView.setTargetView(messageCenter_btn);
            mBadgeView.setBadgeCount(MessageDBHelper.getNoReadNum());//显示的数字
            mBadgeView.setTextSize(20);//显示的大小
            MessageDBHelper.setReadListener(this.mReadListener);


            //退出登陆按钮
            ImageButton logout_btn = (ImageButton) findViewById(R.id.logout_btn);
            logout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**modified by zhuhua ,for 检测网络状态，若网络可用则正常退出，上传本地信息数据；若网络不可用则提示不能保存本地信息数据*/
                   /* MessageUpdateHelper messageUpdateHelper = new MessageUpdateHelper(BmobUser.getCurrentUser(User.class));
                    messageUpdateHelper.Update();//上传数据
                    mMainListener.onLogout();
                    *//*
                    if (MessageService.getCallBackCount()>=2) {//后台建立好以后直接修改控制器状态
                        mControlListener.onChangeStatue(MessagePresenter.LOGOUT);//控制器状态设为登出
                        mLogoutDelayMark = LOGOUT_NOW;//建立当前为退出状态的标记
                    }else {//否则做一个标记，得到service建立回调的时候断开连接
                        mLogoutDelayMark = LOGOUT_DELAY;
                    }
                    mIsLogout = true;//设置为注销状态
*//*
                    //BmobUser.logOut();//删除缓存对象(这部分到上传完成以后再删除

                    Intent service = new Intent(MainActivity.this,MessageService.class);
                    stopService(service);//关闭Service
                    //mIsInitService = false;//Service建立标识为未建立

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();*/
                    if (IsOnline.isOnline(MainActivity.this)) { // 如果联网，直接启动退出登录
                        LogoutWithU(); // 启动退出登陆，上传数据
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("温馨提示!")
                                .setMessage("无网络连接，不能上传信息数据，请谨慎操作！")
                                .setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        LogoutWithoutU(); // 启动退出登录，不上传数据
                                    }
                                })
                                .setNegativeButton("取消退出", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel(); // 不退出登录
                                    }
                                });
                        builder.show();
                    }

                }
                /**
                *退出登录，且上传信息数据
                *@author HuaZhu
                *created at 2016-10-28 17:37
                */
                private void LogoutWithU() {
                    MessageUpdateHelper messageUpdateHelper = new MessageUpdateHelper(BmobUser.getCurrentUser(User.class));
                    messageUpdateHelper.Update();//上传数据
                    mMainListener.onLogout();
                    Intent service = new Intent(MainActivity.this,MessageService.class);
                    stopService(service);//关闭Service
                    //mIsInitService = false;//Service建立标识为未建立

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                /**
                *退出登录，不上传信息数据
                *@author HuaZhu
                *created at 2016-10-28 17:37
                */
                public void LogoutWithoutU() {
                    MessageDBHelper.clearTable();//清空表
                    BmobUser.logOut();//删除缓存对象
                    mMainListener.onLogout();
                    Intent service = new Intent(MainActivity.this,MessageService.class);
                    stopService(service);//关闭Service
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                /**end modify***/
            });

        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mMainListener.onStartMain();
        /*
        if(MessageService.getCallBackCount()>=2) {
            mControlListener.onChangeStatue(MessagePresenter.START_MAINATY);
            StartMessageService();//启动服务
        }
        */
    }

    @Override
    protected void onDestroy(){
        MessageDBHelper.setReadListener(null);//释放掉静态类DBHelper的静态成员变量对本实例中监听器的引用，防止内存泄漏
        mMainListener.onDestroyMain();
        /*
        if(!mIsLogout) {
            if(MessageService.getCallBackCount()>=2) {//非注销状态,并且下打开service的情况下开始离线更新
                mControlListener.onChangeStatue(MessagePresenter.DESTROY_MAINATY);//控制器状态设为MainATY关闭
                MessageService.setMessageServiceListener(null);//释放静态监听器，防止溢出
            }else {//否则做一个标记，得到service建立回调的时候开始离线更新
                mDelayMark = DESTROY_DELAY;
            }
        }
        */
        super.onDestroy();
    }

    /**
     * 启动消息更新服务函数
     */
    private void CreateMessageService(){
        if (ListenerControlPanel.isServiceInit())
            return;
        Intent intent = new Intent(MainActivity.this,MessageService.class);
        PendingIntent pi = PendingIntent.getService(MainActivity.this,0,intent,0);
        AlarmManager aManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        aManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 100,pi);//100毫秒后唤醒
        System.out.println("创建服务");
    }

    private void StartMessageService(){
        Intent service = new Intent(MainActivity.this,MessageService.class);
        startService(service);//开启Service
        System.out.println("开启服务");
    }
}

