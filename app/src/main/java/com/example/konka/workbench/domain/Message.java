package com.example.konka.workbench.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;

/**
 * Created by HP on 2016-9-13.
 * 消息类
 */
public class Message extends BmobObject {
    private User FromUser;
    private String MsgID;//消息ID 对应唯一消息
    private String ProjectName;//项目名
    private String State;//软件状态
    private String ReportNumber;//主观评价报告编号

    private String MsgTime;//消息生成时间
    private String MsgType;//消息类型 0无类型 1新项目消息 2项目变更消息 3BOM提醒消息
    private String MsgRead;//读删标记 0未读 1已读 2隐藏
    private String MsgMark;//消息标记 0无效 1有效 null空(查找返回空值的时候使用)
    private String MsgPush;//推送标记 0为不推送 1为推送 默认为0

    private String BbomEffective;   //BOM生效时间
    private String FirstTestDate;   //初始下机时间
    private String SampleFinishDate;//样评完成时间
    private String MidTestDate;     //中试下机时间
    private String VolProDate;      //开始批量时间
    private String StorageDate;     //批量入库时间

    private String BbomEffectiveBP;   //BOM生效
    private String FirstTestDateBP;   //初始下机
    private String SampleFinishDateBP;//样评完成
    private String MidTestDateBP;     //中试下机
    private String VolProDateBP;      //开始批量
    private String StorageDateBP;     //批量入库

    public void setFromUser(User fromUser) {
        this.FromUser = fromUser;
    }

    public void setData(String[] dataStrings){
        this.MsgID = dataStrings[0];
        this.ProjectName = dataStrings[1];
        this.State = dataStrings[2];
        this.ReportNumber = dataStrings[3];

        this.MsgTime = dataStrings[4];
        this.MsgType = dataStrings[5];
        this.MsgRead = dataStrings[6];
        this.MsgMark = dataStrings[7];
        this.MsgPush = dataStrings[8];

        this.BbomEffective = dataStrings[9];
        this.FirstTestDate = dataStrings[10];
        this.SampleFinishDate = dataStrings[11];
        this.MidTestDate = dataStrings[12];
        this.VolProDate = dataStrings[13];
        this.StorageDate = dataStrings[14];

        this.BbomEffectiveBP = dataStrings[15];
        this.FirstTestDateBP = dataStrings[16];
        this.SampleFinishDateBP = dataStrings[17];
        this.MidTestDateBP = dataStrings[18];
        this.VolProDateBP = dataStrings[19];
        this.StorageDateBP = dataStrings[20];
    }

    public User getFromUser() {
        return FromUser;
    }
    public String[] getBaseData() {
        String[] Data1;
        Data1 = new String[]{MsgID, ProjectName, State, ReportNumber};
        return Data1;
    }

    public String[] getMarkData() {
        String[] Data2;
        Data2 = new String[]{MsgTime, MsgType, MsgRead, MsgMark, MsgPush};
        return Data2;
    }

    public String[] getTimeData() {
        String[] Data3;
        Data3 = new String[]{BbomEffective, FirstTestDate, SampleFinishDate, MidTestDate, VolProDate, StorageDate};
        return Data3;
    }

    public String[] getTimeBPData() {
        String[] Data4;
        Data4 = new String[]{BbomEffectiveBP, FirstTestDateBP, SampleFinishDateBP, MidTestDateBP, VolProDateBP, StorageDateBP};
        return Data4;
    }
}
