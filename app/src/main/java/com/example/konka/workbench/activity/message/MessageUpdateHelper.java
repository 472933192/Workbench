package com.example.konka.workbench.activity.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by HP on 2016-10-25.
 */
public class MessageUpdateHelper {
    private MsgUpdateThread mThread;
    private User mUser;
    private MessageDBrow[] mMessageDBrows;
    private List<BmobObject> mMessages = new ArrayList<BmobObject>();
    private boolean mIsFinishSearch = false;
    private boolean mIsFinishDelete = false;
    private boolean mIsFinishUpdate = false;
    private int mStep;

    private class MsgUpdateThread extends Thread {
        private Handler msgHandler;

        public void run() {
            Looper.prepare();
            msgHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x10) {
                        MessageDBHelper.autoDelete();//清理一下不需要的数据
                        mMessageDBrows = MessageDBHelper.searchMessage(MessageDBHelper.ALL_MESSAGE);
                    }
                    else if (msg.what == 0x11){
                        setDataAndUpdate();
                    }
                    else if (msg.what == 0x12){
                        searchData();
                    }
                    else if (msg.what == 0x13){
                        deleteDate();
                    }
                }
            };
            Looper.loop();
        }
    }

    public MessageUpdateHelper(User user){
        this.mThread = new MsgUpdateThread();
        mThread.start();
        this.mUser = user;
    }

    private void setDataAndUpdate(){
        if (mMessageDBrows.length==0){
            mIsFinishUpdate = true;
            System.out.println("没有需要保存的数据");
            return;
        }
        com.example.konka.workbench.domain.Message message = new com.example.konka.workbench.domain.Message();
        message.setFromUser(this.mUser);
        for (int i=0;i<mMessageDBrows.length;i++){
            message.setData(mMessageDBrows[i].getRowStringArray(MessageDBrow.ALL_DATA));
            message.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if(e==null){
                        System.out.println("保存成功：" + objectId);
                    }else{
                        System.out.println("保存失败："+e);
                    }
                }
            });

            if(i==mMessageDBrows.length-1){
                mIsFinishUpdate = true;
                System.out.println("保存完成");
            }
        }
    }

    private void searchData() {
        System.out.println("开始查找");
        mMessages.clear();

        BmobQuery<com.example.konka.workbench.domain.Message> query = new BmobQuery<com.example.konka.workbench.domain.Message>();
        BmobQuery<User> innerQuery = new BmobQuery<User>();

        innerQuery.addWhereEqualTo("objectId", mUser.getObjectId());//项目关联用户
        query.addWhereMatchesQuery("FromUser", "_User", innerQuery);

        query.setLimit(100);//如果不加上这条语句，默认返回10条数据

        query.findObjects(new FindListener<com.example.konka.workbench.domain.Message>() {
            @Override
            public void done(List<com.example.konka.workbench.domain.Message> list, BmobException e) {
                if(list.size()==0){
                    System.out.println("无先前数据，查找完成，跳过删除");
                    mIsFinishSearch = true;//查找完成
                    mIsFinishDelete = true;//无数据跳过删除步
                    mStep = 2;
                }
                for (com.example.konka.workbench.domain.Message message : list) {
                    mMessages.add(message);
                    if (mMessages.size() == list.size()) {
                        mIsFinishSearch = true;
                        System.out.println("查找完成");
                    }
                }
            }
        });
    }
        ///和底下的timer合并 到一起 专门一个函数，把SEARCHandUPDATE拆开


    private void deleteDate(){
        new BmobBatch().deleteBatch(mMessages).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        BatchResult result = list.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            System.out.println("第" + i + "个数据批量删除成功");
                        } else {
                            System.out.println("第" + i + "个数据批量删除失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                        if(i==list.size()-1){
                            mIsFinishDelete = true;
                            System.out.println("删除完成");
                        }
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    public void Update(){
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!mIsFinishDelete&&!mIsFinishUpdate&&!mIsFinishSearch&&mStep==0) {
                    mThread.msgHandler.sendEmptyMessage(0x12);
                    mStep = 1;
                }
                else if(!mIsFinishDelete&&!mIsFinishUpdate&&mIsFinishSearch&&mStep==1) {
                    mThread.msgHandler.sendEmptyMessage(0x13);
                    mStep = 2;
                }
                else if(mIsFinishDelete&&!mIsFinishUpdate&&mIsFinishSearch&&mStep==2) {
                    mThread.msgHandler.sendEmptyMessage(0x10);
                    mThread.msgHandler.sendEmptyMessage(0x11);
                    mStep = 3;
                }
                else if(mIsFinishDelete&&mIsFinishUpdate&&mIsFinishSearch&&mStep==3) {
                    MessageDBHelper.clearTable();//清空表
                    BmobUser.logOut();//删除缓存对象
                    timer.cancel();
                    System.out.println("同步完成");
                }
            }
        },100,100);
    }
}
