package com.example.konka.workbench.activity.message;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.konka.workbench.R;
import com.example.konka.workbench.adapter.MessageAdapterForTest;
import com.example.konka.workbench.util.SlideCutListView;
import com.example.konka.workbench.util.SlideCutListView.LongpressListener;
import com.example.konka.workbench.util.SlideCutListView.RemoveDirection;
import com.example.konka.workbench.util.SlideCutListView.RemoveListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by HP on 2016-9-13.
 * 我的消息
 */
public class MyMessageAty extends Activity implements RemoveListener,LongpressListener {
    Button test_button;
    Button del_button;

    private MessageAtyStatusListener mMessageListener;

    private SlideCutListView slideCutListView;

    private MessageUpdateListener mMessageUpdateListener;
    private MessagePresenterControlListener mControlListener;

    private MessageAdapterForTest mAdapter;

    private List<MessageDBrow> mMessageList = new ArrayList<MessageDBrow>();
/*
    class MsgThread extends Thread{
        private Handler msgHandler;
        private Timer timer;
        private TimerTask timerTask;
        public void run(){
            Looper.prepare();
            msgHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0x10){
                        SQLite.SetStart();
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            boolean doVS = true;
                            boolean doBD = true;
                            boolean doUI = true;
                            @Override
                            public void run() {
                                if(doVS&&!isInterrupted()){
                                    SQLite.Update_VS();
                                    doVS = false;
                                }
                                if(SQLite.isVSfinnish()&&doBD&&!isInterrupted()){
                                    SQLite.Update_BD();
                                    doBD = false;
                                }
                                if(SQLite.isBDfinnish()&&doUI&&!isInterrupted()){
                                    mUIHandler.sendEmptyMessage(0x13);
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
*/
    Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==0x10){
                refreshListView(true);
            }
        }
    };

/*
    private void initBombConnect(){
        this.innerQuery.addWhereEqualTo("objectId", user.getObjectId());//查找为登录ID的用户
        //Bmob.initialize(this,BMOB_ID);//在主界面已经连接了 可以不做这步
        ProjectConnect.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {//只要保存一次工程表就会回调一个这个函数，所以只要监听工程表就可以同时了解版本是不是改变了
                mMessagePresenter.connectUpdate(data);

                JSONObject projectdata = data.optJSONObject("data");//JSONObject里的data存放的是表里的有效数据
                String projectname = projectdata.optString("projectName");//project里的userlist存放的是表里的有效数据

                System.out.println(projectname);
                BmobQuery<Project> query = new BmobQuery<Project>();
                query.addWhereMatchesQuery("userList", "_User", innerQuery);//关联查找
                query.addWhereEqualTo("projectName", projectname);

                query.count(Project.class,new CountListener(){
                    @Override
                    public void done(Integer count, BmobException e) {
                        if (e == null) {
                            System.out.println("消息更新 count:"+count);
                            if(count>0){
                                msgThread.msgHandler.sendEmptyMessage(0x10);
                            }
                        }else {
                            System.out.println("bomb云连接查询失败");
                        }
                    }
                });

            }

            @Override
            public void onConnectCompleted(Exception ex) {
                if(ProjectConnect.isConnected()) {
                    ProjectConnect.subTableUpdate("Project");//监听表更新
                }
                Log.d("bmob", "连接成功:"+ProjectConnect.isConnected());
            }
        });
    }
*/
    private void initMsgPresenter(){
        mMessageUpdateListener = new MessageUpdateListener() {
            @Override
            public void onUpdateForActivity() {
                refreshListView(false);
            }
            @Override
            public void onUpdateForService(){
                //监听到对Service的更新不采取任何操作
            }
        };
        MessagePresenter.setListenerForMessage(mMessageUpdateListener);
        //MessagePresenter.update();//主动更新一次
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_center_page);
        Log.d("MyMessageAty","MyMessageAty");

        mUIHandler.sendEmptyMessage(0x10);
        mMessageListener = ListenerControlPanel.getMessageListener();
        mMessageListener.onCreateMessage();
        /*
        MessagePresenter.isConnect();//预先执行初始化一下 不然返回的misconnect可能有问题
        mControlListener = MessagePresenter.getControlListener();
        mControlListener.onChangeStatue(MessagePresenter.START_MSGATY);
*/
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(MessagePresenter.isConnect()){
                    initMsgPresenter();//建立Presenter监听器
                    timer.cancel();
                }
            }
        },0,100);


        del_button = (Button) findViewById(R.id.table_del_button);
        del_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v){
                MessageDBHelper.delTable();
            }
        });

        /*
        test_button = (Button) findViewById(R.id.message_test_button);
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDBHelper.outLineUpdate();
                refreshListView(false);
            }
        });
*/
    }

    @Override
    public void onStart(){
        //mControlListener.onChangeStatue(MessagePresenter.START_MSGATY);
        mMessageListener.onStartMessage();
        super.onStart();
    }

    @Override
    public void onStop(){
        //mControlListener.onChangeStatue(MessagePresenter.STOP_MSGATY);
        mMessageListener.onStopMessage();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        MessagePresenter.setListenerForMessage(null);//释放掉静态类Presenter的静态成员变量对本实例中监听器的引用，防止内存泄漏
        System.out.println("结束我的消息");
        super.onDestroy();
    }


    /**
     * 实现的removelistener接口中需要重写的函数
     * @param direction 滑动移除的方向（向左的移除已经被屏蔽了）
     * @param position 移除的是第几个Item
     */
    @Override
    public void removeItem(RemoveDirection direction, int position) {
        mAdapter.deletItemFromMessageDB(position);
        mAdapter.remove(mAdapter.getItem(position));

        switch (direction) {
            case RIGHT:
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                break;
            case LEFT:
                Toast.makeText(this, "向左删除  "+ position, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

    }

    /**
     * 实现 LongpressListener接口需要重写的回调函数 在长按事件发生时回调
     * @param position 长按的ITEM项
     */
    @Override
    public void longpressItem(int position){
        MessageDBrow messageDBrow = mMessageList.get(position);
        if(messageDBrow.isNotRead()) {//查看消息是否未读
            messageDBrow.setMessageLocal(true);//设为本地才能修改
            MessageDBHelper.updateBDrow(messageDBrow,"MsgRead","1");
            refreshListView(false);
            Toast.makeText(this, "消息设为已读", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshListView(boolean isFirst){
        MessageDBrow[] msg = MessageDBHelper.searchMessage(MessageDBHelper.VISIBLE_MESSAGE);//获得所有需要显示的消息（包括已读未读）返回的结果都是新的在前
        int length = msg.length;

        mMessageList.clear();
        for(int i = 0;i<length;i++){
            System.out.println("写入Message"+i);
            mMessageList.add(msg[i]);
        }

        try {
            if (isFirst) {
                //MessageBaseAdapter adapter = new MessageBaseAdapter(MyMessageAty.this, R.layout.message_item, list);
                slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
                slideCutListView.setRemoveListener(this);
                slideCutListView.setLongpressListener(this);

                this.mAdapter = new MessageAdapterForTest(MyMessageAty.this, R.layout.message_item, mMessageList);
                slideCutListView.setAdapter(this.mAdapter);
            }else {
                this.mAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            Log.d("刷新失败", e.getMessage());
            System.out.println(e.getMessage());
            //稍后加上List自检函数
        }


        System.out.println("刷新");
    }
/*
    private void  RefreshListView(boolean isFirst){
        MSG_SQLite_line[] msg = SQLite.GetAllMsg();
        int length = msg.length;
        System.out.println(length);

        list.clear();
        for(int i=length;i>0;i--){
            System.out.println("写入List"+i);
            list.add(msg[i-1]);
        }

        if (isFirst) {
            //MessageBaseAdapter adapter = new MessageBaseAdapter(MyMessageAty.this, R.layout.message_item, list);
            slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
            slideCutListView.setRemoveListener(this);
            slideCutListView.setLongpressListener(this);

            this.adapter = new MessageAdapter(MyMessageAty.this, R.layout.message_item, list);
            slideCutListView.setAdapter(this.adapter);
        }else {
            this.adapter.notifyDataSetChanged();
        }


        //ListView listview = (ListView) findViewById(R.id.lv_message);
        //listview.setAdapter(this.adapter);


        System.out.println("刷新");
        //return "freshed";
    }
*/

    /*
    private void StartPushService(){
        Intent intent = new Intent(MyMessageAty.this,MessageService.class);
        PendingIntent pi = PendingIntent.getService(MyMessageAty.this,0,intent,0);
        AlarmManager aManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        aManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 500,pi);//1000毫秒后唤醒

        System.out.println("启动服务");
    }

    private void StopPushService(){
        Intent intent = new Intent(MyMessageAty.this,MessageService.class);
        stopService(intent);
    }
*/
}
