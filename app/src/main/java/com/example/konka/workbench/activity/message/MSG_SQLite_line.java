package com.example.konka.workbench.activity.message;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HP on 2016-9-26.
 */
public class MSG_SQLite_line {//SQL行操作
    private String Bomb_time;//Bomb云记录的时间
    private String Project_name;//项目名
    private String State;//软件状态

    private String Msg_time;//消息生成时间
    private String Msg_type;//消息类型 0无类型 1新项目消息 2项目变更消息 3BOM提醒消息
    private String Msg_read;//读删标记 0未读 1已读 2隐藏
    private String Msg_mark;//消息标记 0无效 1有效 null空(查找返回空值的时候使用)
    private String Msg_push;//推送标记 0为不推送 1为推送 默认为0

    final private int eff_num = 8;//以上为构建有效项,这些项将在初始化时指定
    private boolean local;//本地性

    private String bom_effective;   //BOM生效时间
    private String firstTestDate;   //初始下机时间
    private String sampleFinishDate;//样评完成时间
    private String midTestDate;     //中试下机时间
    private String volProDate;      //开始批量时间
    private String storageDate;     //批量入库时间

    //距离时间
    private String bom_effective_bp;   //BOM生效
    private String firstTestDate_bp;   //初始下机
    private String sampleFinishDate_bp;//样评完成
    private String midTestDate_bp;     //中试下机
    private String volProDate_bp;      //开始批量
    private String storageDate_bp;     //批量入库

    final private int addedcount = 1;//添加列数，有添加新列时更改此值
    private String ReportNumber;//主观评价报告编号



    MSG_SQLite_line(boolean bl){
        Msg_time = NowTime();//消息生成时间 默认当前时间
        Msg_type = "0";//消息类型 0无类型 1新项目消息 2项目变更消息 3时间提醒消息 默认0
        Msg_read = "0";//读删标记 0未读 1已读 2隐藏 默认未读
        Msg_mark = "1";//消息标记 0无效 1有效 null空(查找返回空值的时候使用) 默认有效
        Msg_push = "0";//推送标记 0为不推送 1为推送 默认为0
        local = bl;//默认不为本地
    }

    public void initialize(boolean bl){
        if(!bl) {
            Msg_time = NowTime();//消息生成时间 默认当前时间
            Msg_type = "0";//消息类型 0无类型 1新项目消息 2项目变更消息 3时间提醒消息 默认0
            Msg_read = "0";//读删标记 0未读 1已读 2隐藏 默认未读
            Msg_mark = "1";//消息标记 0无效 1有效 null空(查找返回空值的时候使用) 默认有效
            Msg_push = "0";//推送标记 0为不推送 1为推送 默认为0
            local = bl;//默认不为本地
        }
    }

    public void SetBaseData(String[]strings){
        Bomb_time=strings[0];
        Project_name = strings[1];
        State = strings[2];
    }

    public void SetIDData(String[]strings){
        Msg_time = strings[0];
        Msg_type = strings[1];
        Msg_read = strings[2];
        Msg_mark = strings[3];
        Msg_push = strings[4];
    }

    public void SetTimeData(String[]strings){
        bom_effective = strings[0];   //BOM生效时间
        firstTestDate = strings[1];   //初始下机时间
        sampleFinishDate = strings[2];//样评完成时间
        midTestDate = strings[3];     //中试下机时间
        volProDate = strings[4];      //开始批量时间
        storageDate = strings[5];     //批量入库时间

        bom_effective_bp = GetBeforePresent(bom_effective);
        firstTestDate_bp = GetBeforePresent(firstTestDate);
        sampleFinishDate_bp = GetBeforePresent(sampleFinishDate);
        midTestDate_bp = GetBeforePresent(midTestDate);
        volProDate_bp = GetBeforePresent(volProDate);
        storageDate_bp = GetBeforePresent(storageDate);

        System.out.println(storageDate_bp);

    }

    public void SetAddedData(String[]strings){
        ReportNumber = strings[0];
    }

    public void reSetTimeBP(){
        bom_effective_bp = GetBeforePresent(bom_effective);
        firstTestDate_bp = GetBeforePresent(firstTestDate);
        sampleFinishDate_bp = GetBeforePresent(sampleFinishDate);
        midTestDate_bp = GetBeforePresent(midTestDate);
        volProDate_bp = GetBeforePresent(volProDate);
        storageDate_bp = GetBeforePresent(storageDate);
    }

    public void SetTimeData2(String[]strings1,String[]strings2){
        bom_effective = strings1[0];   //BOM生效时间
        firstTestDate = strings1[1];   //初始下机时间
        sampleFinishDate = strings1[2];//样评完成时间
        midTestDate = strings1[3];     //中试下机时间
        volProDate = strings1[4];      //开始批量时间
        storageDate = strings1[5];     //批量入库时间

        bom_effective_bp = strings2[0];   //BOM生效时间
        firstTestDate_bp = strings2[1];   //初始下机时间
        sampleFinishDate_bp = strings2[2];//样评完成时间
        midTestDate_bp = strings2[3];     //中试下机时间
        volProDate_bp = strings2[4];      //开始批量时间
        storageDate_bp = strings2[5];     //批量入库时间
    }

    public void SetAllData(String[]strings){
        int i;
        String[] basedata = new String[3];
        String[] IDdata = new String[5];
        String[] timedata = new String[6];
        String[] timebpdata = new String[6];
        String[] addeddate = new String[addedcount];

        for(i=0;i<3;i++){
            basedata[i] = strings[i];
        }
        for(i=0;i<5;i++){
            IDdata[i] = strings[i+3];
        }
        for(i=0;i<6;i++){
            timedata[i] = strings[i+8];
        }
        for(i=0;i<6;i++){
            timebpdata[i] = strings[i+14];
        }
        for(i=0;i<addedcount;i++){
            addeddate[i] = strings[i+20];
        }
        SetBaseData(basedata);
        SetIDData(IDdata);
        SetTimeData2(timedata,timebpdata);
        SetAddedData(addeddate);
    }

    public void SetMessageType(String st){
        if(   st.equals("11")
                ||st.equals("21") ||st.equals("22") ||st.equals("23")
                ||st.equals("31") ||st.equals("32") ||st.equals("33") ||st.equals("34") ||st.equals("35") ||st.equals("36")
                ||st.equals("41") ||st.equals("42") ||st.equals("43") ||st.equals("44") ||st.equals("45") ||st.equals("46")
                ||st.equals("0"))
            Msg_type = st;
        else
            Msg_type = "0";
    }//设置消息类型

    private String NowTime(){
        //设置时间
        Date objdate=new Date();//获得系统当前时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//设置时间格式
        String date=sdf.format(objdate);
        return date;
    }//设置当前时间


    public void SetMessagePush(String string){
        Msg_push = string;
    }//设置消息是否推送

    public void SetMessageRead(String string){
        Msg_read = string;
    }//设置读删标记

    public void SetMessageMark(String string){
        Msg_mark = string;
    }//设置消息标记

    public void SetLocal(boolean bl){
        local = bl;
    }



    public String  GetMark(){
        return this.Msg_mark;
    }

    public String GetBeforePresent(String date_record){
        if(!isTime(date_record))
            return "fall";
        Date objdate=new Date();//获得系统当前时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//设置时间格式
        String date_now = sdf.format(objdate);

        int[] i_now,i_record;
        i_now = ChangeTimeType(date_now);
        i_record = ChangeTimeType(date_record);

        //System.out.println("daynow:"+i_now[0]+i_now[1]+i_now[2]);
        //System.out.println("dayrec:"+i_record[0]+i_record[1]+i_record[2]);

        int year_now=i_now[0],month_now=i_now[1],day_now=i_now[2];
        int year_record=i_record[0],month_record=i_record[1],day_record=i_record[2];

        int day = 0;
        int[] daytable = {day,day=day+31,day=day+28,day=day+31,day=day+30,day=day+31,
                day=day+30,day=day+31,day=day+31,day=day+30,day=day+31,day=day+30,day=day+31};
        day = 0;
        int[] daytable_R = {day,day=day+31,day=day+29,day=day+31,day=day+30,day=day+31,
                day=day+30,day=day+31,day=day+31,day=day+30,day=day+31,day=day+30,day=day+31};

        if((year_record%100!=0&&year_record%4==0)||year_record%400==0){//生效时间为瑞年
            int days_now = daytable_R[month_now-1]+day_now;
            int days_record = daytable_R[month_record-1]+day_record;
            if (year_record-year_now>=1)
                days_record = days_record+365;
            return Integer.toString(days_record - days_now);
        }
        else {
            int days_now = daytable[month_now-1]+day_now;
            int days_record = daytable[month_record-1]+day_record;
            if (year_record-year_now>=1) {
                if ((year_now % 100 != 0 && year_now % 4 == 0) || year_now % 400 == 0)//今年是瑞年
                    days_record = days_record + 366;
                else
                    days_record = days_record + 365;
            }
            return Integer.toString(days_record - days_now);
        }
    }//设置距离某一时间值距离的时间（XXXX-XX-XX）

    public String GetMsgTime(){
        return Msg_time;
    }//获得消息生成

    public String GetMsgType(){
        return Msg_type;
    }

    public String GetBaseData(String string){
        if(string.equals("all"))
            return Project_name+State+ReportNumber;
        else if(string.equals("state"))
            return Project_name+State;
        else if(string.equals("report"))
            return Project_name+ReportNumber;
        else
            return "";
    }//获得基本数据输出

    public String[] GetTime() {
        return new String[]{
                bom_effective,    //BOM生效
                firstTestDate,   //初始下机
                sampleFinishDate,//样评完成
                midTestDate,     //中试下机
                volProDate,      //开始批量
                storageDate     //批量入库;
        };//获得时间
    }

    public String[] GetTimeBP() {
        return new String[]{
                bom_effective_bp,    //BOM生效
                firstTestDate_bp,   //初始下机
                sampleFinishDate_bp,//样评完成
                midTestDate_bp,     //中试下机
                volProDate_bp,      //开始批量
                storageDate_bp     //批量入库;
        };//获得距离BOM生效时间差
    }

    public String[] GetPushData(){
        return new String[]{Bomb_time,Project_name,State,Msg_type,ReportNumber};
    }

    public String GetPushBPtime(int i){
        String []strings = new String[]{
                bom_effective_bp,    //BOM生效
                firstTestDate_bp,   //初始下机
                sampleFinishDate_bp,//样评完成
                midTestDate_bp,     //中试下机
                volProDate_bp,      //开始批量
                storageDate_bp     //批量入库;
        };
        return strings[i];
    }

    public String[] GetMsgData(){
        return new String[]{Msg_time,Project_name,State,Msg_type,bom_effective_bp, firstTestDate_bp,
        sampleFinishDate_bp,midTestDate_bp,volProDate_bp,storageDate_bp,bom_effective, firstTestDate,
                sampleFinishDate,midTestDate,volProDate,storageDate,ReportNumber};
    }


    private boolean isTime(String st){
        if(st.length()<10)
            return false;
        else if(st.charAt(4)=='-'&&st.charAt(7)=='-')
            return true;
        else
            return false;
    }//是否为XXXX-XX-XX类型的时间数据

    private int[] ChangeTimeType(String st){
        int year,month,day;
        year = Integer.parseInt(st.substring(0,4));
        month = Integer.parseInt(st.substring(5,7));
        day = Integer.parseInt(st.substring(8));
        int[] i={year,month,day};
        return i;
    }//将XXXX-XX-XX类型的时间String类型数据换成int型数组

    public boolean IsNotRead(){
        if(Msg_read.equals("0"))
            return true;
        else
            return false;
    }//消息是否未读

    public void InsertToSQLite(SQLiteDatabase db){
        String[] strings = new String[]{
                Bomb_time,//Bomb云记录的时间
                Project_name,//项目名
                State,//软件状保留位

                Msg_time,//消息生成时间
                Msg_type,//消息类型 0无类型 1新项目消息 2项目变更消息 3BOM提醒消息
                Msg_read,//读删标记 0未读 1已读 2隐藏
                Msg_mark,//消息标记 0无效 1有效 null空(查找返回空值的时候使用)
                Msg_push,//推送标记 0为不推送 1为推送 默认为0

                bom_effective,  //BOM生效时间
                firstTestDate,   //初始下机时间
                sampleFinishDate,//样评完成时间
                midTestDate,     //中试下机时间
                volProDate,      //开始批量时间
                storageDate,     //批量入库时间

                bom_effective_bp,   //BOM生效
                firstTestDate_bp,   //初始下机
                sampleFinishDate_bp,//样评完成
                midTestDate_bp,     //中试下机
                volProDate_bp,      //开始批量
                storageDate_bp,     //批量入库

                ReportNumber        //报告编号
        };
        db.execSQL("insert into MyProject_inf values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",strings);
    }//将本条数据上传到指定SQL数据库中

    public void UpdateLine(SQLiteDatabase db,String columnname,String newdata){
        if (local){//为本地消息才能修改
            db.execSQL("update MyProject_inf set "+columnname+"=? " +
                    "where Bomb_time=? " +
                    "and Project_Name=? " +
                    "and State=? " +
                    "and ReportNumber=? " +
                    "and Msg_time =? " +
                    "and Msg_type = ? " +
                    "and Msg_read =? " +
                    "and Msg_mark =? " +
                    "and Msg_push =?",new String[]{newdata,Bomb_time,Project_name,State,ReportNumber,Msg_time,Msg_type,Msg_read,Msg_mark,Msg_push});
            //System.out.println(Msg_time+","+Msg_type+","+Msg_read+","+Msg_mark+","+Msg_push);
        }
    }//


    public void DeleteLine(SQLiteDatabase db){
        if (local){//为本地消息才能修改
            db.execSQL("delete from MyProject_inf " +
                    "where Bomb_time=? " +
                    "and Project_Name=? " +
                    "and State=? " +
                    "and ReportNumber=? " +
                    "and Msg_time =? " +
                    "and Msg_type = ? " +
                    "and Msg_read =? " +
                    "and Msg_mark =? " +
                    "and Msg_push =?",new String[]{Bomb_time,Project_name,State,ReportNumber,Msg_time,Msg_type,Msg_read,Msg_mark,Msg_push});
            System.out.println("删除成功");
        }
    }//


    public MSG_SQLite_line SearchFromSQL(String string, SQLiteDatabase MyProject_DB){
        Cursor cursor;
        MSG_SQLite_line msg;
        if(string.equals("eff")){//搜索有效数据
            cursor = MyProject_DB.rawQuery("select * from MyProject_inf where Project_Name=? and Msg_mark='1'",new String[]{Project_name});
            cursor.moveToFirst();
            if(cursor.getCount()==0){//没有得到结果或者得到大于一条有效结果
                System.out.println(Project_name+"未找到有效消息");
                cursor.close();
                return new MSG_SQLite_line(false);//返回一条不为本地的消息
            }
            else if(cursor.getCount()>1){
                System.out.println(Project_name+"有效消息数过大");
                for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                    System.out.println(cursor.getString(0)+cursor.getString(1)+cursor.getString(2)+cursor.getString(3)+cursor.getString(4)+cursor.getString(5)+cursor.getString(6)+cursor.getString(7)+cursor.getString(20));
                }
                cursor.close();
                return new MSG_SQLite_line(false);//返回一条不为本地的消息
            }
            else {
                msg = new MSG_SQLite_line(true);
                String[] bases = new String[3];
                for(int i=0;i<3;i++){
                    bases[i]=cursor.getString(i);
                }

                String[] ids = new String[5];
                for(int i=0;i<5;i++){
                    ids[i]=cursor.getString(i+3);
                }


                String[] times = new String[6];
                for(int i=0;i<6;i++){
                    times[i]=cursor.getString(i+8);
                }

                String[] timesbp = new String[6];
                for(int i=0;i<6;i++){
                    timesbp[i]=cursor.getString(i+14);
                }

                String[] added = new String[addedcount];
                for(int i=0;i<addedcount;i++){
                    added[i]=cursor.getString(i+20);
                }

                msg.SetBaseData(bases);
                msg.SetIDData(ids);
                msg.SetTimeData2(times,timesbp);
                msg.SetAddedData(added);
                cursor.close();
                return msg;
            }
        }
        else return msg=new MSG_SQLite_line(false);
    }//以这条消息作为基准在SQL数据库中查找对应消息

    public static MSG_SQLite_line[] SearchAllEff(SQLiteDatabase MyProject_DB) {
        Cursor cursor;
        MSG_SQLite_line msg;

        cursor = MyProject_DB.rawQuery("select * from MyProject_inf where Msg_mark='1'", null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            cursor.close();
            MSG_SQLite_line[] msg_line = new MSG_SQLite_line[1];
            msg = new MSG_SQLite_line(false);
            msg_line[0] = msg;
            return msg_line;//返回一条不为本地的消息
        } else {
            MSG_SQLite_line[] msg_line = new MSG_SQLite_line[cursor.getCount()];
            int j = 0;
            final int ADD_NUMBER = 1;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                msg = new MSG_SQLite_line(true);
                String[] bases = new String[3];
                for (int i = 0; i < 3; i++) {
                    bases[i] = cursor.getString(i);
                }

                String[] ids = new String[5];
                for (int i = 0; i < 5; i++) {
                    ids[i] = cursor.getString(i + 3);
                }

                String[] times = new String[6];
                for (int i = 0; i < 6; i++) {
                    times[i] = cursor.getString(i + 8);
                }

                String[] timesbp = new String[6];
                for (int i = 0; i < 6; i++) {
                    timesbp[i] = cursor.getString(i + 14);
                }

                String[] added = new String[ADD_NUMBER];
                for(int i=0;i<ADD_NUMBER;i++){
                    added[i]=cursor.getString(i+20);
                }

                msg.SetBaseData(bases);
                msg.SetIDData(ids);
                msg.SetTimeData2(times, timesbp);
                msg.SetAddedData(added);

                msg_line[j] = msg;
                j++;
            }

            System.out.println("有效消息总数"+j);
            cursor.close();
            return msg_line;
        }
    }

    public boolean isLocal(){
        return local;
    }

}
