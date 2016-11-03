package com.example.konka.workbench.activity.message;

/**
 * Created by Wangfan on 2016-10-17.
 * 消息表行操作类
 */
public class MessageDBrow {
    private String mMsgID;//消息ID 对应唯一消息
    private String mProjectName;//项目名
    private String mState;//软件状态
    private String mReportNumber;//主观评价报告编号

    private String mMsgTime;//消息生成时间
    private String mMsgType;//消息类型 0无类型 1新项目消息 2项目变更消息 3BOM提醒消息
    private String mMsgRead;//读删标记 0未读 1已读 2隐藏
    private String mMsgMark;//消息标记 0无效 1有效 null空(查找返回空值的时候使用)
    private String mMsgPush;//推送标记 0为不推送 1为推送 默认为0

    private String mBbomEffective;   //BOM生效时间
    private String mFirstTestDate;   //初始下机时间
    private String mSampleFinishDate;//样评完成时间
    private String mMidTestDate;     //中试下机时间
    private String mVolProDate;      //开始批量时间
    private String mStorageDate;     //批量入库时间

    private String mBbomEffectiveBP;   //BOM生效
    private String mFirstTestDateBP;   //初始下机
    private String mSampleFinishDateBP;//样评完成
    private String mMidTestDateBP;     //中试下机
    private String mVolProDateBP;      //开始批量
    private String mStorageDateBP;     //批量入库

    private boolean mLocal;//本地性

    public static final String ALL_DATA = "_ALL_DATA";
    public static final String BASE_ALL = "_BASE_ALL";
    public static final String BASE_STATE = "_BASE_STATE";
    public static final String BASE_REPORT = "_BASE_REPORT";
    public static final String BASE_ID_SINGLE = "_BASE_ID_SINGLE";
    public static final String BASE_REPORT_SINGLE = "_BASE_REPORT_SINGLE";
    public static final String BASE_NAME_SINGLE = "_BASE_NAME_SINGLE";
    public static final String MARK_ARRAY = "_MARK_ARRAY";
    public static final String BASE_ARRAY = "_BASE_ARRAY";
    public static final String TIME_ARRAY = "_TIME_ARRAY";
    public static final String TIME_BP_ARRAY = "_TIME_BP_ARRAY";
    public static final String MARK_MARK_SINGLE = "_MARK_MARK_SINGLE";
    public static final String MARK_TYPE_SINGLE = "_MARK_TYPE_SINGLE";
    public static final String UNSET = "_UNSET";



    public void setMessageDBrow(boolean Local){
        setMessageDBrow(Local,null);
    }
    public void setMessageDBrow(boolean Local,String[] marks){
        setMessageDBrow(Local,marks,null);
    }
    public void setMessageDBrow(boolean Local,String[] marks,String[] bases){
        setMessageDBrow(Local,marks,bases,null);
    }
    public void setMessageDBrow(boolean Local,String[] marks,String[] bases,String[] times){
        setMessageDBrow(Local,marks,bases,times,null);
    }
    public void setMessageDBrow(boolean Local,String[] marks,String[] bases,String[] times,String[] timebps){
        mLocal = Local;
        if(marks==null){
            mMsgTime = MessageDBtimer.getNowTime();//消息生成时间 默认当前时间
            mMsgType = "0";//消息类型 0无类型 1新项目消息 2项目变更消息 3时间提醒消息 默认0
            mMsgRead = "0";//读删标记 0未读 1已读 2隐藏 默认未读
            mMsgMark = "1";//消息标记 0无效 1有效 null空(查找返回空值的时候使用) 默认有效
            mMsgPush = "0";//推送标记 0为不推送 1为推送 默认为0
        }else {
            mMsgTime = marks[0];
            mMsgType = marks[1];
            mMsgRead = marks[2];
            mMsgMark = marks[3];
            mMsgPush = marks[4];
        }

        if(bases==null){
            mMsgID = Integer.toString(MessageDBHelper.getUsableMessageID());
            mProjectName = UNSET;
            mState = UNSET;
            mReportNumber = UNSET;
        }else if(bases.length==3) {
            mProjectName = bases[0];
            mState = bases[1];
            mReportNumber = bases[2];
        }else {
            mMsgID = bases[0];
            mProjectName = bases[1];
            mState = bases[2];
            mReportNumber = bases[3];
        }

        if(times==null){
            mBbomEffective = UNSET;
            mFirstTestDate = UNSET;   //初始下机时间
            mSampleFinishDate = UNSET;//样评完成时间
            mMidTestDate = UNSET;     //中试下机时间
            mVolProDate = UNSET;      //开始批量时间
            mStorageDate = UNSET;     //批量入库时间
        }else {
            mBbomEffective = times[0];
            mFirstTestDate = times[1];
            mSampleFinishDate = times[2];
            mMidTestDate = times[3];
            mVolProDate = times[4];
            mStorageDate = times[5];
        }

        if(timebps==null){
            mBbomEffectiveBP = MessageDBtimer.getBeforePresent(mBbomEffective);
            mFirstTestDateBP = MessageDBtimer.getBeforePresent(mFirstTestDate);   //初始下机时间
            mSampleFinishDateBP = MessageDBtimer.getBeforePresent(mSampleFinishDate);//样评完成时间
            mMidTestDateBP = MessageDBtimer.getBeforePresent(mMidTestDate);     //中试下机时间
            mVolProDateBP = MessageDBtimer.getBeforePresent(mVolProDate);      //开始批量时间
            mStorageDateBP = MessageDBtimer.getBeforePresent(mStorageDate);     //批量入库时间
        }else {
            mBbomEffectiveBP = timebps[0];
            mFirstTestDateBP = timebps[1];
            mSampleFinishDateBP = timebps[2];
            mMidTestDateBP = timebps[3];
            mVolProDateBP = timebps[4];
            mStorageDateBP = timebps[5];
        }



    }
    public void setMessageDBrow(MessageDBrow messageDBrow){
        setMessageDBrow(messageDBrow.isLocal(),
                messageDBrow.getRowStringArray(MARK_ARRAY),
                messageDBrow.getRowStringArray(BASE_ARRAY),
                messageDBrow.getRowStringArray(TIME_ARRAY),
                messageDBrow.getRowStringArray(TIME_BP_ARRAY));
    }

    public void setMessagePush(String pushmark){
        if(pushmark.equals("1")||pushmark.equals("0"))
            this.mMsgPush = pushmark;
        else
            this.mMsgPush = "0";
    }

    public void setMessageRead(String readmark){
        if(readmark.equals("1")||readmark.equals("0")||readmark.equals("2"))
            this.mMsgRead = readmark;
        else
            this.mMsgRead = "0";
    }

    public void setMessageType(String type){
        if(   type.equals("11")
                ||type.equals("21") ||type.equals("22") ||type.equals("23")
                ||type.equals("31") ||type.equals("32") ||type.equals("33") ||type.equals("34") ||type.equals("35") ||type.equals("36")
                ||type.equals("41") ||type.equals("42") ||type.equals("43") ||type.equals("44") ||type.equals("45") ||type.equals("46")
                ||type.equals("0"))
            this.mMsgType = type;
        else
            this.mMsgType = "0";
    }

    public void setMessageLocal(boolean local){
        this.mLocal = local;
    }

    public void setMessageID(String newID){
        this.mMsgID = newID;
    }

    public void resetTimeBP(){
        mBbomEffectiveBP = MessageDBtimer.getBeforePresent(mBbomEffective);
        mFirstTestDateBP = MessageDBtimer.getBeforePresent(mFirstTestDate);
        mSampleFinishDateBP = MessageDBtimer.getBeforePresent(mSampleFinishDate);
        mMidTestDateBP = MessageDBtimer.getBeforePresent(mMidTestDate);
        mVolProDateBP = MessageDBtimer.getBeforePresent(mVolProDate);
        mStorageDateBP = MessageDBtimer.getBeforePresent(mStorageDate);
    }

    public String[] getRowStringArray(String type){
        switch (type){
            case ALL_DATA:{
                String[] strings = new String[]{
                        this.mMsgID, this.mProjectName, this.mState, this.mReportNumber,
                        this.mMsgTime, this.mMsgType, this.mMsgRead, this.mMsgMark, this.mMsgPush,
                        this.mBbomEffective,
                        this.mFirstTestDate,
                        this.mSampleFinishDate,
                        this.mMidTestDate,
                        this.mVolProDate,
                        this.mStorageDate,
                        this.mBbomEffectiveBP,
                        this.mFirstTestDateBP,
                        this.mSampleFinishDateBP,
                        this.mMidTestDateBP,
                        this.mVolProDateBP,
                        this.mStorageDateBP
                };
                return strings;
            }
            case TIME_ARRAY:{
                String[] strings = new String[]{
                        this.mBbomEffective,
                        this.mFirstTestDate,
                        this.mSampleFinishDate,
                        this.mMidTestDate,
                        this.mVolProDate,
                        this.mStorageDate,
                };
                return strings;
            }
            case TIME_BP_ARRAY:{
                String[] strings = new String[]{
                        this.mBbomEffectiveBP,
                        this.mFirstTestDateBP,
                        this.mSampleFinishDateBP,
                        this.mMidTestDateBP,
                        this.mVolProDateBP,
                        this.mStorageDateBP,
                };
                return strings;
            }
            case MARK_ARRAY:{
                String[] strings = new String[]{
                        this.mMsgTime, this.mMsgType, this.mMsgRead, this.mMsgMark, this.mMsgPush
                };
                return strings;
            }
            case BASE_ARRAY:{
                String[] strings = new String[]{
                        this.mMsgID, this.mProjectName, this.mState, this.mReportNumber
                };
                return strings;
            }
        }
        return null;
    }

    public String getRowMarkString(String type){
        String string;
        switch (type){
            case MARK_MARK_SINGLE:{
                string = this.mMsgMark;
                return string;
            }
            case MARK_TYPE_SINGLE:{
                string = this.mMsgType;
                return string;
            }
        }
        return null;
    }

    public String getRowBaseString(String type){
        String string;
        switch (type){
            case BASE_NAME_SINGLE:{
                string = this.mProjectName;
                return string;
            }
            case BASE_ID_SINGLE:{
                string = this.mMsgID;
                return string;
            }
            case BASE_ALL:{
                string = this.mProjectName + this.mState + this.mReportNumber;
                return string;
            }
            case BASE_STATE:{
                string = this.mProjectName + this.mState;
                return string;
            }
            case BASE_REPORT:{
                string = this.mProjectName + this.mReportNumber;
                return string;
            }
            case BASE_REPORT_SINGLE:{
                string = this.mReportNumber;
                return string;
            }
        }
        return null;
    }

    public boolean isLocal(){
        return this.mLocal;
    }

    public boolean isNotRead(){
        if(this.mMsgRead.equals("0")){
            return true;
        }else {
            return false;
        }
    }
}
