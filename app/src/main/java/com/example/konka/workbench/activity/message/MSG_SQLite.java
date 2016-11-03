package com.example.konka.workbench.activity.message;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.konka.workbench.activity.message.MSG_SQLite_line;
import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;
import com.example.konka.workbench.domain.Version;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by HP on 2016-9-26.
 */
public class MSG_SQLite {

    final private String File_Dir;        //文件存储路径
    private SQLiteDatabase MyProject_DB;  //数据库
    private Context context;             //界面
    private String statue;               //标识


    private String SQL_version ="";//数据库版本标记 更新时候用
    final private int standercolunmcount =21;//标准列数，改动表时改动次项
    //标准列名
    final private String[] standercolumnname= new String[]{
            "Bomb_time String",
            "Project_Name String",
            "State String",

            "Msg_time String",
            "Msg_type String",//消息类型
            "Msg_read String",//读消息标记
            "Msg_mark String", //有效性标记
            "Msg_push String",//推送标记

            "bom_effective String",
            "firstTestDate String",
            "sampleFinishDate String",
            "midTestDate String",
            "volProDate String",
            "storageDate String",

            "bom_effective_bp String",
            "firstTestDate_bp String",
            "sampleFinishDate_bp String",
            "midTestDate_bp String",
            "volProDate_bp String",
            "storageDate_bp String",

            "ReportNumber String"
    };

    private int total;

    private boolean updatevsfinishmark;
    private boolean updatebdfinishmark;

    private List<Version> statelist = new ArrayList<Version>(); //软件版本集合（数据传递用）

    private cn.bmob.v3.listener.FindListener<Version> bombfindlistener = new cn.bmob.v3.listener.FindListener<Version>(){
        //Project pj2=new Project();
        @Override
        public void done(List<Version> versionlist, BmobException e) {
            if (e == null) {
                Version version = versionlist.get(versionlist.size() - 1);//得到最后一个元素
                //pj2=version.getFromProject();
                System.out.println("version:"+version.getState()+version.getFromProject().getObjectId());
                statelist.add(version);
                if(statelist.size()==total){
                    VSfinish();
                    System.out.println("VS更新完成");
                }
            } else {
                Log.d("bombfindlistener更新失败", e.getMessage());
            }
        }
    };

    private cn.bmob.v3.listener.FindListener<Project> bombfindlistener2 = new cn.bmob.v3.listener.FindListener<Project>(){
        @Override
        public void done(List<Project> list, BmobException e) {
            if (e == null) {
                total = 0;
                statelist.clear();//清空软件版本集合
                for (Project project : list) {
                    //Project project = list.get(projectnum);
                    System.out.println("project:" + project.getProjectName() + project.getObjectId());

                    BmobQuery<Version> query = new BmobQuery<Version>();
                    BmobQuery<Project> innerQuery = new BmobQuery<Project>();

                    innerQuery.addWhereEqualTo("objectId", project.getObjectId());//版本关联项目
                    query.addWhereMatchesQuery("fromProject", "Project", innerQuery);

                    query.setLimit(50);
                    query.findObjects(bombfindlistener);

                    total++;
                }
            }else {
                Log.d("bombfindlistener2更新失败", e.getMessage());
            }
        }
    };


    public MSG_SQLite(Context context,String status){
        File_Dir = context.getFilesDir().toString()+"/my_project.db3";//SQLite路径
        //System.out.println(File_Dir);
        MyProject_DB = SQLiteDatabase.openOrCreateDatabase(File_Dir,null);

        //判断是否存在表,不存在就建立新表，
        Cursor cursor = MyProject_DB.rawQuery("select * from sqlite_master where type='table' AND name='MyProject_inf'",null);
        cursor.moveToFirst();
        //System.out.println("表数目:"+cursor.getCount());
        if(cursor.getCount()==0){
            System.out.println("不存在表");
            CreateTable();
        }
        else {
            Cursor cursor2 = MyProject_DB.rawQuery("select * from MyProject_inf",null);
            cursor2.moveToFirst();
            int columncount = cursor2.getColumnCount();
            System.out.println("表列数："+columncount);
            //DelTable();
            //CreateTable();
            if(columncount<standercolunmcount)
            {
                System.out.println("表有变化，进行更新");
                if(standercolunmcount==21)
                    SQL_version = "v0.21";//变更为0.21版本数据库
                for (int j=columncount;j<standercolunmcount;j++){
                    MyProject_DB.execSQL("alter table MyProject_inf add "+standercolumnname[j]);
                    MyProject_DB.execSQL("update MyProject_inf set "+standercolumnname[j].replace(" String","")+"=''");//设置初值为空字符串
                }
            }
            cursor2.close();
        }
        this.context = context;
        this.statue = status;
        //SetStart();
        //Update_BD(context,status);
        cursor.close();
    }


    public void SetStart(){
        this.updatevsfinishmark = false;
        this.updatebdfinishmark = false;
    }
    public boolean isBDfinnish(){
        return this.updatebdfinishmark;
    }
    public boolean isVSfinnish(){
        return this.updatevsfinishmark;
    }

    private void BDfinish(){
        this.updatebdfinishmark = true;
    }
    private void VSfinish(){
        this.updatevsfinishmark = true;
    }

    public void Update_VS() {
        BmobQuery<Project> query = new BmobQuery<Project>();
        BmobQuery<User> innerQuery = new BmobQuery<User>();

        User user = BmobUser.getCurrentUser(User.class);//取得当前用户

        innerQuery.addWhereEqualTo("objectId", user.getObjectId());//项目关联用户
        query.addWhereMatchesQuery("userList", "_User", innerQuery);

        query.setLimit(50);//如果不加上这条语句，默认返回10条数据
        query.findObjects(bombfindlistener2);
    }

    public void Update_BD(){
        BmobQuery<Project> query = new BmobQuery<Project>();
        BmobQuery<User> innerQuery=new BmobQuery<User>();

        User user = BmobUser.getCurrentUser(User.class);//取得当前用户

        innerQuery.addWhereEqualTo("objectId",user.getObjectId());//项目关联用户
        query.addWhereMatchesQuery("userList","_User",innerQuery);

        query.setLimit(50);//如果不加上这条语句，默认返回10条数据

        query.findObjects(new FindListener<Project>() {
            boolean haveeff;        //存在有效项
            String projectName;     //项目名
            String bomb_time;      //Bomb云生效时间
            String reportnumber;   //主观测试报告编号
            //Project pj;

            String timeid;
            String timebpid;

            String[] bases = new String[3];
            String[] times = new String[6];
            String[] added = new String[1];
            //String[] timesbp = new String[6];

            MSG_SQLite_line local; //本地SQL行
            MSG_SQLite_line download = new MSG_SQLite_line(false);//BOMB云加载的SQL行

            @Override
            public void done(List<Project> list, BmobException e) {
                if (e == null) {
                    if(statue.equals("msg"))
                        Toast.makeText(context,"更新完成", Toast.LENGTH_SHORT).show();

                    for(Project project:list){//遍历我的项目
                        download.initialize(false);//复原download
                        projectName = project.getProjectName();      //项目名
                        bomb_time=project.getCreatedAt();          //bomb创建时间
                        reportnumber = project.getReportNumber();
                        if(reportnumber.isEmpty()){
                            reportnumber = "null";
                        }

                        //System.out.println(statelist.get(listi));

                        bases[0] = bomb_time;
                        bases[1] = projectName;
                        for (Version vs:statelist){
                            //pj=vs.getFromProject();
                            if(vs.getFromProject().getObjectId().equals(project.getObjectId())){
                                bases[2] = vs.getState();
                                break;
                            }
                        }

                        added[0] = reportnumber;
                        //System.out.println("主观评价报告:"+reportnumber);

                        download.SetBaseData(bases);
                        download.SetAddedData(added);

                        times[0] = project.getBom_effective();
                        times[1] = project.getFirstTestDate();
                        times[2] = project.getSampleFinishDate();
                        times[3] = project.getMidTestDate();
                        times[4] = project.getVolProDate();
                        times[5] = project.getStorageDate();

                        download.SetTimeData(times);

                        System.out.println(bases[0]+bases[1]+bases[2]+",主观评价报告:"+added[0]);

                        local = download.SearchFromSQL("eff",MyProject_DB);//利用下载数据在本地数据库中寻找有效数据

                        if(statue.equals("push"))
                            download.SetMessagePush("1");

                        if (!local.isLocal()){//本地状态为空说明为新记录
                            download.SetMessageType("11");//设置消息类型为新项目消息
                            download.InsertToSQLite(MyProject_DB);//在SQL数据库中更新
                        }
                        else if(!local.GetBaseData("all").equals(download.GetBaseData("all"))){//查询到有效消息 但与下载的消息不一致
                            haveeff = false;//当前不存在有效项
                            download.SetLocal(true);//将最后一条更新的download设为本地（本地消息才可以UPDATE）
                            if(!local.GetBaseData("state").equals(download.GetBaseData("state"))){
                                haveeff = true;//存在有效项
                                download.SetMessageType("21");
                                download.InsertToSQLite(MyProject_DB);
                            }
                            if(!local.GetBaseData("report").equals(download.GetBaseData("report"))){
                                if (haveeff) {//如果存在有效项
                                    download.UpdateLine(MyProject_DB, "Msg_mark", "0");//将之前的有效项设为无效
                                }
                                if(download.GetPushData()[4].equals("null")){//下载到报告编号为空，不做提示，防止更新时推送
                                    download.SetMessageType("0");//无类型消息
                                    download.SetMessagePush("0");//不推送
                                    download.SetMessageRead("2");//不显示消息
                                }
                                else if(local.GetPushData()[4].equals("null")){
                                    download.SetMessageType("22");
                                }
                                else {
                                    download.SetMessageType("23");
                                }
                                download.InsertToSQLite(MyProject_DB);
                            }

                            local.UpdateLine(MyProject_DB,"Msg_mark","0");//本地原消息设为无效
                        }
                        else if(!(timeid = GetTimeChangeId(local,download)).equals("000000")){//与下载的消息一致 但是项目时间节点发生改变
                            haveeff = false;//当前不存在有效项
                            download.SetLocal(true);//设为本地才可以修改
                            for (int i=0;i<6;i++){
                                if(timeid.charAt(i)=='1'){
                                    if(haveeff){
                                        download.UpdateLine(MyProject_DB, "Msg_mark", "0");//将之前的有效项设为无效
                                    }
                                    haveeff=true;
                                    download.SetMessageType("4"+Integer.toString(i+1));
                                    download.InsertToSQLite(MyProject_DB);//在SQL数据库中更新
                                }
                            }
                            local.UpdateLine(MyProject_DB,"Msg_mark","0");//本地原消息设为无效
                            System.out.println("项目时间节点改变");
                        }
                        else if(!(timebpid = GetTimeBPId(local,download)).equals("000000")){//与下载的消息一致 但是距离生效时间发生改变
                            haveeff = false;//当前不存在有效项
                            download.SetLocal(true);//设为本地才可以修改
                            for (int i=0;i<6;i++){
                                if(timebpid.charAt(i)=='1'){
                                    if(haveeff){
                                        download.UpdateLine(MyProject_DB, "Msg_mark", "0");//将之前的有效项设为无效
                                    }
                                    haveeff=true;
                                    download.SetMessageType("3"+Integer.toString(i+1));
                                    download.InsertToSQLite(MyProject_DB);//在SQL数据库中更新
                                }
                            }
                            local.UpdateLine(MyProject_DB,"Msg_mark","0");//本地原消息设为无效
                            System.out.println("生效时间改变");
                        }
                        else {
                            if(SQL_version.equals("v0.21")){
                                System.out.println("数据库变更为v0.21版本（第一次更新时提示）");
                                String versiontype = local.GetMsgType();
                                switch (versiontype){
                                    case "0":break;
                                    case "1":{local.UpdateLine(MyProject_DB,"Msg_type","11");break;}
                                    case "2":{local.UpdateLine(MyProject_DB,"Msg_type","21");break;}
                                    case "3":{local.UpdateLine(MyProject_DB,"Msg_type","31");break;}
                                    case "4":{local.UpdateLine(MyProject_DB,"Msg_type","32");break;}
                                    case "5":{local.UpdateLine(MyProject_DB,"Msg_type","33");break;}
                                    case "6":{local.UpdateLine(MyProject_DB,"Msg_type","34");break;}
                                    case "7":{local.UpdateLine(MyProject_DB,"Msg_type","35");break;}
                                    case "8":{local.UpdateLine(MyProject_DB,"Msg_type","36");break;}
                                }
                            }else {
                                System.out.println("无新数据");
                            }
                            continue;//没有改动不做任何修改
                        }

                    }
                } else {
                    Log.d("更新失败", e.getMessage());
                }
                System.out.println("BD更新完成");
                BDfinish();
            }
        });
        //return "updated";
    }

    public void Outlineupdate() {//线下更新
        MSG_SQLite_line[] effmsg = MSG_SQLite_line.SearchAllEff(MyProject_DB);
        MSG_SQLite_line counttimesql;
        String timebpid;
        boolean haveeff;
        for (int i = 0; i < effmsg.length; i++) {
            if(!effmsg[i].isLocal())//返回的消息不为本地直接跳过
                continue;
            counttimesql = effmsg[i];
            counttimesql.reSetTimeBP();
            if (!(timebpid = GetTimeBPId(effmsg[i], counttimesql)).equals("000000")) {//与下载的消息一致 但是距离生效时间发生改变
                counttimesql.SetMessagePush("1");//设为推送
                haveeff = false;//当前不存在有效项
                counttimesql.SetLocal(true);//设为本地才可以修改
                for (int j=0;j<6;j++){
                    if(timebpid.charAt(j)=='1'){
                        if(haveeff){
                            counttimesql.UpdateLine(MyProject_DB, "Msg_mark", "0");//将之前的有效项设为无效
                        }
                        haveeff=true;
                        counttimesql.SetMessageType("3"+Integer.toString(j+1));
                        counttimesql.InsertToSQLite(MyProject_DB);//在SQL数据库中更新
                    }
                }
                effmsg[i].UpdateLine(MyProject_DB,"Msg_mark","0");//本地原消息设为无效
            }
            counttimesql.initialize(false);
        }
        System.out.println("线下更新完成");
    }

    private String GetTimeBPId(MSG_SQLite_line local, MSG_SQLite_line download){
        String[] strings1 = local.GetTimeBP();
        String[] strings2 = download.GetTimeBP();
        String string = "";
        for(int i=0;i<6;i++){
            if((!strings1[i].equals(strings2[i]))&&strings1[i].equals("2"))//变化并且仅差两天
                string = string+"1";
            else
                string = string+"0";
        }
        return string;
    }

    private String GetTimeChangeId(MSG_SQLite_line local, MSG_SQLite_line download){
        String[] strings1 = local.GetTime();
        String[] strings2 = download.GetTime();
        String string = "";
        for(int i=0;i<6;i++){
            if(!strings1[i].equals(strings2[i]))//时间不相等
                string = string+"1";
            else
                string = string+"0";
        }
        return string;
    }


    public SQLiteDatabase GetDataBase(){
        return MyProject_DB;
    }

    //获得所有需要推送的消息
    public MSG_SQLite_line[] GetAllPush(){
        Cursor cursor = MyProject_DB.rawQuery("select * from MyProject_inf where Msg_push='1'",null);//要有效且需要推送的
        String[] strings = new String[cursor.getColumnCount()];
        MSG_SQLite_line[] msg = new MSG_SQLite_line[cursor.getCount()];
        int j=0;
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            for(int i=0;i<cursor.getColumnCount();i++){
                strings[i]=cursor.getString(i);
            }
            msg[j] = new MSG_SQLite_line(true);
            msg[j].SetAllData(strings);
            j++;
        }
        cursor.close();
        System.out.println(msg.length);
        return msg;
    }

    //获得所有需要显示的消息
    public MSG_SQLite_line[] GetAllMsg(){
        Cursor cursor = MyProject_DB.rawQuery("select * from MyProject_inf where Msg_read='0' or Msg_read='1'",null);
        //System.out.println(cursor.getColumnCount());
        //System.out.println(cursor.getCount());

        String[] strings = new String[cursor.getColumnCount()];
        MSG_SQLite_line[] msg = new MSG_SQLite_line[cursor.getCount()];
        int j=0;
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            for(int i=0;i<cursor.getColumnCount();i++){
                strings[i]=cursor.getString(i);
            }
            msg[j] = new MSG_SQLite_line(true);
            msg[j].SetAllData(strings);
            j++;
        }
        cursor.close();
        return msg;
    }

    /*public void SetAllPushed(){
        MyProject_DB.execSQL("update MyProject_inf set Msg_push='0' where Msg_push='1'");
    }*/

    public void DelAll(){
        DelTable();
        CreateTable();
        //MyProject_DB.execSQL("delete from MyProject_inf where Msg_mark='0'");//删除所有无效消息
        //MyProject_DB.execSQL("update MyProject_inf set Msg_read='2'");//隐藏所有消息
    }

    private void DelTable(){
        MyProject_DB.execSQL("drop table MyProject_inf");
    }

    private void CreateTable(){
        String addstring = "";
        for (int i=0;i<standercolunmcount-1;i++){
            addstring = addstring+standercolumnname[i]+",";
        }
        addstring = addstring+standercolumnname[standercolunmcount-1]+")";//最后一个加反括号
        MyProject_DB.execSQL("create table MyProject_inf(" +addstring);
        System.out.println("创建新表");
    }

    public void CloseBD(){
        MyProject_DB.close();
    }

    public void OpenBD(){
        MyProject_DB = SQLiteDatabase.openOrCreateDatabase(File_Dir,null);
    }



}

