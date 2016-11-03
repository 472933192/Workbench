package com.example.konka.workbench.activity.message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.example.konka.workbench.R;
import com.example.konka.workbench.activity.message.MSG_SQLite_line;

/**
 * Created by HP on 2016-9-22.
 */
public class MessagePush {
    /*
    private String messageid;//消息ID（消息唯一标识）
    private char messagetype;//消息类型(1:新项目消息 2：项目变更通知 3：BOM生效时间通知)
    private int pushNO;     //推送号（后台推送用）
    private String projectname;//消息来源项目
    private String BOMtime;//项目BOM生效时间
    private String messagetime;//消息中心发布时间

    public MessagePush(String flag,String Project_Name,String BOM_time,String msg_time){
        messageid = flag;
        messagetype = flag.charAt(0);
        pushNO = GetMessageNO(flag);
        projectname = Project_Name;
        BOMtime = BOM_time;
        messagetime = msg_time;
    }
*/

    private Context context;//页面标识
    private NotificationManager my_message;
    private SQLiteDatabase MSGdatabase;
    private MSG_SQLite_line[] Message;
    private int NumOfMsg;//消息数

    public MessagePush(Context context, NotificationManager mymsg, SQLiteDatabase db, MSG_SQLite_line[] msg){
        this.context = context;
        this.my_message = mymsg;
        this.Message = msg;
        this.NumOfMsg = msg.length;
        this.MSGdatabase = db;
    }

    //从SQLite数据库flag列中提取int型推送号
    //BOMB云的项目生成时间格式为XXXX-XX-XX xx:xx:xx
    private int GetMessageID(String type,String time){
        String string =type+time.substring(11,13)+time.substring(14,16)+time.substring(17);
        return Integer.parseInt(string);
    }

    //项目变更通知
    private  void change_message(int message_id,String projectname,String state){
        Intent intent = new Intent(context,MyMessageAty.class);//点开这个消息窗可以链接到我的消息界面
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);
        String case21="";
        if(state.equals("合格定版"))
            case21 = "请提交项目'"+projectname+"'的主观评价报告";
        else
            case21 = "项目'"+projectname+"'的软件状态变更为："+state;

        Notification notify = new Notification.Builder(context)
                .setAutoCancel(true)
                .setTicker("项目变更通知")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("软件状态变更通知")
                .setContentText(case21)
                .setContentIntent(pi)
                .build();
        my_message.notify(message_id,notify);//发送通知
    }

    //新项目通知
    private  void newproject_message(int message_id,String projectname){
        Intent intent = new Intent(context,MyMessageAty.class);//点开这个消息窗可以链接到我的消息界面
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);

        Notification notify = new Notification.Builder(context)
                .setAutoCancel(true)
                .setTicker("新项目通知")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("新项目通知")
                .setContentText("有一个名为'"+projectname+"'的新项目")
                .setContentIntent(pi)
                .build();
        my_message.notify(message_id,notify);//发送通知

    }

    //项目时间通知
    private  void BOMtime_message(int message_id,String projectname,String timebp,String type){
        Intent intent = new Intent(context,MyMessageAty.class);//点开这个消息窗可以链接到我的消息界面
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);

        int i = Integer.parseInt(type.substring(1))-1;
        String[] name = new String[]{"BOM生效","初始下机","样评完成", "中试下机","开始批量","批量入库"};

        Notification notify = new Notification.Builder(context)
                .setAutoCancel(true)
                .setTicker(name[i]+"通知")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name[i]+"时间通知")
                .setContentText("距离项目'"+projectname+"'"+name[i]+"时间还有"+timebp+"天")
                .setContentIntent(pi)
                .build();
        my_message.notify(message_id,notify);//发送通知
    }

    private  void report_message(int message_id,String projectname,char ch){
        Intent intent = new Intent(context,MyMessageAty.class);//点开这个消息窗可以链接到我的消息界面
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);

        String st;
        if(ch=='2')
            st="已提交";
        else
            st="已变更";

        Notification notify = new Notification.Builder(context)
                .setAutoCancel(true)
                .setTicker("软件入库通知")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("软件入库通知")
                .setContentText("项目'"+projectname+"'主观评价报告"+st+"\n请将软件入库")
                .setContentIntent(pi)
                .build();
        my_message.notify(message_id,notify);//发送通知
    }

    //消息推送
    public void PushMessage(){
        String bombtime;
        String projectname;
        String state;

        String type;
        String timebp;

        int messageid;
        String[] strings;

        //System.out.println(this.NumOfMsg);

        for (int i=0;i<this.NumOfMsg;i++){
            strings = Message[i].GetPushData();
            bombtime = strings[0];
            projectname = strings[1];
            state = strings[2];
            ///可能这里有问题
            type = strings[3];

            messageid = GetMessageID(type,bombtime);
            System.out.println("消息ID："+messageid);

            switch (type.charAt(0)){
                case '1':{newproject_message(messageid,projectname);break;}//1：新项目通知
                case '2': {
                    if (type.charAt(1) == '1') {
                        change_message(messageid, projectname, state);
                        break;
                    } else if (type.charAt(1) == '2'||type.charAt(1) == '3') {
                        report_message(messageid, projectname, type.charAt(1));
                        break;
                    } else break;
                }
                case '3':{timebp = Message[i].GetPushBPtime(Integer.parseInt(type.substring(1))-1);
                    BOMtime_message(messageid,projectname,timebp,type);break;}//3:BOM生效时间提醒通知
            }
            Message[i].UpdateLine(MSGdatabase,"Msg_push","0");//设置消息已推送

        }
    }
/*
    //使这条消息的推送过期
    public String get_invalid_messageid(){
        return "0"+messageid;
    }

    public String getMessageid(){
        return messageid;
    }

    //设置改变消息的id
    public void set_change_messageid(){
        String st = this.messageid;
        messageid = "2"+st.substring(1);
        pushNO = GetMessageNO(messageid);
        messagetype = messageid.charAt(0);
    }

    public String getProjectname(){
        return projectname;
    }

    public String getBOMtime(){
        return BOMtime;
    }

    public String getMessagetime(){
        return messagetime;
    }


    //等到本条消息的SQLite数据表示
    public String[] message_to_SQLite(){
        String []st = new String[]{messageid,projectname,BOMtime,messagetime};
        return st;
    }

    //获得消息类型
    public  char getMessagetype(){
        return messagetype;
    }

    //设置消息中心用时间
    public void setMessagetime(String time){
        messagetime = time;
    }
*/

}

