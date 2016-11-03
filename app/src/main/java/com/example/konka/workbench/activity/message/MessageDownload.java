package com.example.konka.workbench.activity.message;

import android.os.Handler;
import android.util.Log;

import com.example.konka.workbench.activity.main.MainActivity;
import com.example.konka.workbench.domain.Message;
import com.example.konka.workbench.domain.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @file  MessageDownload.java(本文件的文件名)
 * @breif 从bmob下载信息数据 (本文件实现的功能的简述)
 *
 * 从bmob下载信息数据. (本文件的功能详述)
 *
 * @author zhuhua (作者)
 * @version V1.0.00 (版本声明)
 * @date 2016-10-26 9:10
 */
public class MessageDownload {

    public static final int MESSAGE_DOWLOAD = 1;

    public List<Message> messages;

    private Message message;

    private User FromUser;

    private String[] data1;

    private String[] data2;

    private String[] data3;

    private String[] data4;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_DOWLOAD:
                    messages = (List<Message>) msg.obj;  // 下载的数据
                    MessageDBrow[] messageDBrows = new MessageDBrow[messages.size()];
                    for (int i = 0; i < messages.size(); i++) {
                        message = messages.get(i);
                        FromUser = message.getFromUser();
                        data1 = message.getBaseData();
                        data2 = message.getMarkData();
                        data3 = message.getTimeData();
                        data4 = message.getTimeBPData();
                        Log.d("ceshi", FromUser + " " + data1[0] + " " + data1[1] + " " + data1[2] + " " + data1[3]);
                        MessageDBrow messageDBrow = new MessageDBrow();
                        messageDBrow.setMessageDBrow(false,data2,data1,data3,data4);
                        messageDBrows[i] = messageDBrow;
                    }
                    if (messages.size() != 0) {
                        MessageDBHelper.resortByIDAndInsert(messageDBrows);
                    }
                    System.out.println("下载完成");
                    break;
                default:
                    break;
            }
        }
    };
    /**
    *查询并下载信息数据
    *@author HuaZhu
    *created at 2016-10-26 9:19
    */
    public void messageDownload() {
        BmobQuery<Message> query = new BmobQuery<Message>();
        BmobQuery<User> innerQuery = new BmobQuery<User>();
        // 用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        User user = BmobUser.getCurrentUser(User.class); // 取得当前用户
        innerQuery.addWhereEqualTo("objectId", user.getObjectId());
        query.addWhereMatchesQuery("FromUser", "_User", innerQuery);
        query.findObjects(new FindListener<Message>() {

            @Override
            public void done(List<Message> objects, BmobException e) {

                android.os.Message message = new android.os.Message();
                message.what = MESSAGE_DOWLOAD;
                message.obj = objects;
                handler.sendMessage(message);
            }
        });
    }
}
