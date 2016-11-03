package com.example.konka.workbench.activity.message;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
 * Created by Wangfan on 2016-10-17.
 * 消息数据库类
 */
public class MessageDBHelper {
    /** 表是否打开*/
    private static boolean mIsOpen = false;
    /** 文件存储路径*/
    private static String mFileDir = new String();
    /** 存储数据库*/
    private static SQLiteDatabase mMyMessageDB;
    /** 界面*/
    private Context mContext;
    /** 标识
     * Push表推送
     * Msg表消息
     */
    private String mStatus;
    /** 版本标记 升级时用*/
    private String mVersion;
    /** 标准列数*/
    private static final int mColunmCount =21;
    /** 列名数组 顺序排列*/
    private static final String[] mColumnName= new String[]{
            "MsgID", "ProjectName", "State","ReportNumber",
            "MsgTime", "MsgType", "MsgRead", "MsgMark", "MsgPush",
            "bomEffective", "firstTestDate", "sampleFinishDate", "midTestDate", "volProDate", "storageDate",
            "bomEffectiveBP", "firstTestDateBP", "sampleFinishDateBP", "midTestDateBP", "volProDateBP", "storageDateBP",
    };
    /** 当前可分配的MessageID*/
    private static int mUsableMessageID = -1;

    private static String mUpdateType;//更新类型
    /** 一些常量*/
    public static final String ALL_MESSAGE = "_ALL_MESSAGE";
    public static final String VALID_MESSAGE = "_VALID_MESSAGE";
    public static final String PUSH_MESSAGE = "_PUSH_MESSAGE";
    public static final String MSG_UPDATE = "_MSG_UPDATE";
    public static final String PUSH_UPDATE = "_PUSH_UPDATE";
    public static final String VISIBLE_MESSAGE = "_VISIBLE_MESSAGE";

    private static boolean UpdateBDfinishMark;
    private static boolean UpdateVSfinishMark;

    private static MessageReadListener mReadListener;

    private static int mTotal;
    private static List<Version> mStateList = new ArrayList<Version>(); //软件版本集合（数据传递用）
    private static final FindListener<Version> mFindVersionListener = new FindListener<Version>(){
        private final String stringforhide = "加一个变量可以隐藏";
        @Override
        public void done(List<Version> versionlist, BmobException e) {
            if (e == null) {
                Version version = versionlist.get(versionlist.size() - 1);//得到最后一个元素
                //pj2=version.getFromProject();
                System.out.println("version:"+version.getState()+version.getFromProject().getObjectId());
                mStateList.add(version);
                if(mStateList.size()==mTotal){
                    updateVSfinish();
                    System.out.println("VS更新完成");
                }
            } else {
                Log.d("bombfindlistener更新失败", e.getMessage());
            }
        }
    };

    private static final FindListener<Project> mFindProjectListener = new FindListener<Project>() {
        private final String stringforhide = "加一个变量可以隐藏";
        @Override
        public void done(List<Project> list, BmobException e) {
            if (e == null) {
                mTotal = 0;
                mStateList.clear();//清空软件版本集合
                for (Project project : list) {
                    //Project project = list.get(projectnum);
                    System.out.println("project:" + project.getProjectName() + project.getObjectId());

                    BmobQuery<Version> query = new BmobQuery<Version>();
                    BmobQuery<Project> innerQuery = new BmobQuery<Project>();

                    innerQuery.addWhereEqualTo("objectId", project.getObjectId());//版本关联项目
                    query.addWhereMatchesQuery("fromProject", "Project", innerQuery);

                    query.setLimit(50);
                    query.findObjects(mFindVersionListener);

                    mTotal++;
                }
            }else {
                Log.d("Version更新失败", e.getMessage());
            }
        }
    };

    private static final FindListener<Project> mFindProjectListenerForUpdate = new FindListener<Project>() {
        boolean havechange;        //存在有效项
        boolean localupdate;    //本地消息是否更新
        String projectName;     //项目名
        String reportNumber;   //主观测试报告编号

        String timeid;
        String timebpid;

        String[] bases = new String[3];
        String[] times = new String[6];

        MessageDBrow local; //本地SQL行
        MessageDBrow download = new MessageDBrow();//BOMB云加载的SQL行

        @Override
        public void done(List<Project> list, BmobException e) {
            //if (e == null) {
            //if(statue.equals("msg"))
            //    Toast.makeText(context,"更新完成", Toast.LENGTH_SHORT).show();

            for(Project project:list){//遍历我的项目
                download.setMessageDBrow(false);//复原download
                projectName = project.getProjectName();      //项目名
                reportNumber = project.getReportNumber();
                if(reportNumber.isEmpty()){
                    reportNumber = "null";
                }
                bases[0] = projectName;
                bases[2] = reportNumber+"X";
                for (Version vs:mStateList){
                    //pj=vs.getFromProject();
                    if(vs.getFromProject().getObjectId().equals(project.getObjectId())){
                        bases[1] = vs.getState();
                        break;
                    }
                }
                times[0] = project.getBom_effective();
                times[1] = project.getFirstTestDate();
                times[2] = project.getSampleFinishDate();
                times[3] = project.getMidTestDate();
                times[4] = project.getVolProDate();
                times[5] = project.getStorageDate();

                download.setMessageDBrow(false,null,bases,times);

                System.out.println("BOMB云得到数据:"+bases[0]+bases[1]+bases[2]);

                local = searchMessage(download);//利用下载数据在本地数据库中寻找有效数据

                if(mUpdateType.equals(PUSH_UPDATE))
                    download.setMessagePush("1");

                if ( local == null ){//本地状态为空说明为新记录
                    download.setMessageType("11");//设置消息类型为新项目消息
                    insertToTable(download);//在SQL数据库中更新
                    continue;//新记录不进行后续操作
                }
                havechange = false;//当前未变动
                localupdate = false;//本地消息未更新过
                if(!local.getRowBaseString(MessageDBrow.BASE_ALL).equals(download.getRowBaseString(MessageDBrow.BASE_ALL))){//查询到有效消息 但与下载的消息不一致
                    if(!local.getRowBaseString(MessageDBrow.BASE_STATE).equals(download.getRowBaseString(MessageDBrow.BASE_STATE))){
                        havechange = true;//发生变动
                        download.setMessageType("21");
                        insertToTable(download);
                        System.out.println("软件版本状态发生改变");
                    }
                    if(!local.getRowBaseString(MessageDBrow.BASE_REPORT).equals(download.getRowBaseString(MessageDBrow.BASE_REPORT))){
                        if (havechange) {//如果有变动，产生新变动时要将原来的变动设为无效
                            updateBDrow(download,"MsgMark","0");
                            download.setMessageID(Integer.toString(MessageDBHelper.getUsableMessageID()));//获得新消息id
                            download.setMessageLocal(false);//设置不为本地（不为本地消息才能插入）
                        }
                        if(download.getRowBaseString(MessageDBrow.BASE_REPORT_SINGLE).equals("nullX")){//下载到报告编号为空，不做提示，防止更新时推送
                            download.setMessageType("0");//无类型消息
                            download.setMessagePush("0");//不推送
                            download.setMessageRead("2");//不显示消息
                        }
                        else if(local.getRowBaseString(MessageDBrow.BASE_REPORT_SINGLE).equals("nullX")){
                            download.setMessageType("22");
                        }
                        else {
                            download.setMessageType("23");
                        }
                        havechange = true;
                        insertToTable(download);
                        System.out.println("主观评价报告发生改变");
                    }
                    updateBDrow(local,"MsgMark","0");
                    localupdate = true;
                }
                if(!(timeid = MessageDBtimer.getTimeChangeId(local,download)).equals("000000")){//与下载的消息一致 但是项目时间节点发生改变

                    for (int i=0;i<6;i++){
                        if(timeid.charAt(i)=='1'){
                            if(havechange){//如果有变动，产生新变动时要将原来的变动设为无效
                                updateBDrow(download,"MsgMark","0");//将之前项设为无效
                                download.setMessageID(Integer.toString(MessageDBHelper.getUsableMessageID()));//获得新消息id
                                download.setMessageLocal(false);//设置不为本地（不为本地消息才能插入）
                            }
                            havechange=true;
                            download.setMessageType("4"+Integer.toString(i+1));
                            insertToTable(download);//在SQL数据库中更新
                        }
                    }
                    if (!localupdate) {
                        updateBDrow(local, "MsgMark", "0");//本地原消息设为无效
                        localupdate = true;
                    }
                    System.out.println("项目时间节点改变");
                }
                if(!(timebpid = MessageDBtimer.getTimeBPChangeId(local,download)).equals("000000")){//与下载的消息一致 但是距离生效时间发生改变
                    for (int i=0;i<6;i++){
                        if(timebpid.charAt(i)=='1'){
                            if(havechange){
                                updateBDrow(download,"MsgMark","0");
                                download.setMessageID(Integer.toString(MessageDBHelper.getUsableMessageID()));//获得新消息id
                                download.setMessageLocal(false);//设置不为本地（不为本地消息才能插入）
                            }
                            havechange=true;
                            download.setMessageType("3"+Integer.toString(i+1));
                            insertToTable(download);//在SQL数据库中更新
                        }
                    }
                    if (!localupdate) {
                        updateBDrow(local, "MsgMark", "0");//本地原消息设为无效
                    }
                    System.out.println("生效时间改变");
                }
                if(!havechange){//如果没有改变
                    System.out.println("无新数据");
                    continue;//没有改动不做任何修改
                }

            }
            // } else {
            //     Log.d("更新失败", e.getMessage());
            // }
            System.out.println("BD更新完成");
            updateBDfinish();
        }
    };

    /** 打开表函数
     * 参数为任意一个activity
     * */
    public static void openTable(Context context){
        if(mIsOpen)//表格打开不操作
            return;

        if(mFileDir.isEmpty()) {
            mFileDir = context.getFilesDir().toString() + "/my_project.db3";//SQLite路径
            mMyMessageDB = SQLiteDatabase.openOrCreateDatabase(mFileDir, null);
        }

        //判断是否存在表,不存在就建立新表，
        Cursor cursor = mMyMessageDB.rawQuery("select * from sqlite_master where type='table' AND name='MyMessage'",null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            System.out.println("不存在表");
            createTable();
            mUsableMessageID = 1;//新表重新开始赋值
        }
        else {
            Cursor cursor2 = mMyMessageDB.rawQuery("select * from MyMessage",null);
            cursor2.moveToFirst();
            int columncount = cursor2.getColumnCount();
            System.out.println("表列数："+columncount);
            //DelTable();
            //CreateTable();
            if(columncount<mColunmCount)
            {
                System.out.println("表有变化，进行更新");
                for (int j=columncount;j<mColunmCount;j++){
                    mMyMessageDB.execSQL("alter table MyMessage add "+mColumnName[j]+" String");
                    mMyMessageDB.execSQL("update MyMessage set "+mColumnName[j]+"=''");//设置初值为空字符串
                }
            }
            if(mUsableMessageID==-1){
                if (cursor2.getCount()==0){
                    mUsableMessageID = 1;
                }else {
                    cursor2.moveToLast();
                    mUsableMessageID = Integer.parseInt(cursor2.getString(0)) + 1;
                }
            }
            cursor2.close();
        }
        cursor.close();
        mIsOpen = true;
    }

    /** 创建表函数*/
    private static void createTable(){
        String addstring = "";
        for (int i=0;i<mColunmCount-1;i++){
            addstring = addstring+mColumnName[i]+" String,";
        }
        addstring = addstring+mColumnName[mColunmCount-1]+" String)";//最后一个加反括号
        mMyMessageDB.execSQL("create table MyMessage(" +addstring);
        System.out.println("创建新表");
    }

    /** 删除表函数*/
    public static void delTable(){
        mMyMessageDB.execSQL("drop table MyMessage");
    }

    /** 删除表函数*/
    public static void clearTable(){
        mMyMessageDB.execSQL("delete from MyMessage");
    }


    /** 更新软件版本状态函数*/
    public static void updateVersion() {
        BmobQuery<Project> query = new BmobQuery<Project>();
        BmobQuery<User> innerQuery = new BmobQuery<User>();

        User user = BmobUser.getCurrentUser(User.class);//取得当前用户

        innerQuery.addWhereEqualTo("objectId", user.getObjectId());//项目关联用户
        query.addWhereMatchesQuery("userList", "_User", innerQuery);

        query.setLimit(100);//如果不加上这条语句，默认返回10条数据
        query.findObjects(mFindProjectListener);

    }
    /** 更新表函数*/
    public static void updateTable(final String updateType){
        BmobQuery<Project> query = new BmobQuery<Project>();
        BmobQuery<User> innerQuery=new BmobQuery<User>();

        User user = BmobUser.getCurrentUser(User.class);//取得当前用户

        innerQuery.addWhereEqualTo("objectId",user.getObjectId());//项目关联用户
        query.addWhereMatchesQuery("userList","_User",innerQuery);

        query.setLimit(100);//如果不加上这条语句，默认返回10条数据

        mUpdateType = updateType;
        query.findObjects(mFindProjectListenerForUpdate);
    }
    /** 离线更新函数*/
    public static void outLineUpdate() {//线下更新
        MessageDBrow[] effmsg = searchMessage(MessageDBHelper.VALID_MESSAGE);//搜索所有有效消息
        MessageDBrow counttimesql = new MessageDBrow();
        String timebpid;
        boolean haveeff;
        for (int i = 0; i < effmsg.length; i++) {
            if(!effmsg[i].isLocal())//返回的消息不为本地直接跳过
                continue;
            counttimesql.setMessageDBrow(effmsg[i]);
            counttimesql.resetTimeBP();
            if (!(timebpid = MessageDBtimer.getTimeBPChangeId(effmsg[i], counttimesql)).equals("000000")) {//与下载的消息一致 但是距离生效时间发生改变
                counttimesql.setMessageID(Integer.toString(MessageDBHelper.getUsableMessageID()));//时间发生变更说明是新消息，得到新ID
                counttimesql.setMessageLocal(false);//修改新ID时变更为非本地
                counttimesql.setMessagePush("1");//设为推送

                haveeff = false;//不含前置
                for (int j=0;j<6;j++){
                    if(timebpid.charAt(j)=='1'){
                        if(haveeff){
                            updateBDrow(counttimesql,"MsgMark","0");//将之前项设为无效
                            counttimesql.setMessageID(Integer.toString(MessageDBHelper.getUsableMessageID()));//获得新消息id
                            counttimesql.setMessageLocal(false);//设置不为本地（不为本地消息才能插入）
                        }
                        haveeff=true;
                        counttimesql.setMessageType("3"+Integer.toString(j+1));
                        insertToTable(counttimesql);//在SQL数据库中更新
                    }
                }
                updateBDrow(effmsg[i],"MsgMark","0");//本地原消息设为无效
            }
        }
        System.out.println("线下更新完成");
    }

    /** 得到有效ID函数 每次分配一个ID就会自动往后加1*/
    public static int getUsableMessageID(){
        int usableID = mUsableMessageID;
        mUsableMessageID++;
        return usableID;
    }

    public static void insertToTable(MessageDBrow messageDBrow){
        if(!messageDBrow.isLocal()) {//不为本地消息才能插入
            mMyMessageDB.execSQL("insert into MyMessage values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", messageDBrow.getRowStringArray(MessageDBrow.ALL_DATA));
            messageDBrow.setMessageLocal(true);//插入以后就为本地消息了
            if(mReadListener!=null){//如果发现插入了新消息 回调阅读监听器
                mReadListener.onChangeRead();
            }
        }else {
            System.out.println("本地消息无法插入数据库");
        }
    }

    /**
     * 排序后插入函数
     */
    public static void resortByIDAndInsert(MessageDBrow[] messageDBrows){
        MessageDBrow row;
        int ID1,ID2,i,j;
        for (i=0;i<messageDBrows.length;i++){
            for(j=1;j<messageDBrows.length-i;j++){
                ID1 = Integer.parseInt(messageDBrows[j-1].getRowBaseString(MessageDBrow.BASE_ID_SINGLE));
                ID2 = Integer.parseInt(messageDBrows[j].getRowBaseString(MessageDBrow.BASE_ID_SINGLE));
                if(ID1<=ID2){
                    row = messageDBrows[j-1];
                    messageDBrows[j-1] = messageDBrows[j];
                    messageDBrows[j] = row;
                }
            }
            MessageDBHelper.insertToTable(messageDBrows[j-1]);
        }
        mUsableMessageID = Integer.parseInt(messageDBrows[0].getRowBaseString(MessageDBrow.BASE_ID_SINGLE))+1;//重设可用ID
        System.out.println("当前可用ID为："+mUsableMessageID);
    }

    public static void updateBDrow(MessageDBrow messageDBrow,String columnname,String newdata){
        if (messageDBrow.isLocal()) {
            Cursor cursor = mMyMessageDB.rawQuery("select * from MyMessage where MsgID=?", new String[]{messageDBrow.getRowBaseString(MessageDBrow.BASE_ID_SINGLE)});
            if(cursor.getCount()==0){
                System.out.println("未找到对应ID，更新失败");
                return;
            }else if (cursor.getCount()>1){
                System.out.println("找到重复的消息ID，更新失败");
                return;
            }else {
                mMyMessageDB.execSQL("update MyMessage set "+columnname+"=? where MsgID=? ",new String[]{newdata,messageDBrow.getRowBaseString(MessageDBrow.BASE_ID_SINGLE)});
                if(columnname.equals("MsgRead")&&mReadListener!=null){//如果发现修改了阅读标记 回调阅读监听器
                    mReadListener.onChangeRead();
                }
            }
            cursor.close();
        }else {
            System.out.println("不为本地，无法在数据库中更新");
        }
    }

    /**
     * 删除项函数
     * 如果ID不对 则删除失败
     * @param messageDBrow 要在表中删除的行
     */
    public static void deleteMessage(MessageDBrow messageDBrow){
        Cursor cursor;
        String ID = messageDBrow.getRowBaseString(MessageDBrow.BASE_ID_SINGLE);
        System.out.println(ID);
        cursor = mMyMessageDB.rawQuery("select * from MyMessage where MsgID = ?",new String[]{ID});
        cursor.moveToFirst();
        if( cursor.getCount()==0 ){
            System.out.println("该ID不在message表内，删除失败");
        }else {
            if(messageDBrow.getRowMarkString(MessageDBrow.MARK_MARK_SINGLE).equals("0")){//如果是无效的直接删除
                mMyMessageDB.execSQL("delete from MyMessage where MsgID=?",new String[]{ID});
            }else {
                updateBDrow(messageDBrow,"MsgRead","2");//有效消息设为不可见
                mReadListener.onChangeRead();//读写变化回调
            }
            System.out.println("删除成功");
        }
        cursor.close();

    }

    /**
     * 自动删除函数，删除表中无效并且处于删除状态的项目
     */
    public static void autoDelete(){
        mMyMessageDB.execSQL("delete from MyMessage where MsgMark='0' and MsgRead = '2'");
        System.out.println("自动清理数据库");
    }

    /** 查找函数
     * 参数为类型值
     * 注意：消息表都是越新插入的消息在越后面，这里返回的结果都是越新的在越前面
     * */
    public static MessageDBrow[] searchMessage(String tpye){
        Cursor cursor;
        MessageDBrow[] messageDBrows;
        if(tpye.equals(VALID_MESSAGE)){
            cursor = mMyMessageDB.rawQuery("select * from MyMessage where MsgMark='1'",null);
            messageDBrows=new MessageDBrow[cursor.getCount()];
            int iDBrows = 0;
            for (cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
                messageDBrows[iDBrows] = cursorToMessageDBrow(cursor);
                iDBrows++;
            }
            cursor.close();
            return messageDBrows;
        }
        else if (tpye.equals(PUSH_MESSAGE)){
            cursor = mMyMessageDB.rawQuery("select * from MyMessage where MsgMark='1' and MsgPush='1'",null);
            messageDBrows=new MessageDBrow[cursor.getCount()];
            int iDBrows = 0;
            for (cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
                messageDBrows[iDBrows] = cursorToMessageDBrow(cursor);
                iDBrows++;
            }
            cursor.close();
            return messageDBrows;
        }
        else if (tpye.equals(VISIBLE_MESSAGE)){
            cursor = mMyMessageDB.rawQuery("select * from MyMessage where MsgRead<>'2'",null);//MsgRead=2时为隐藏
            messageDBrows=new MessageDBrow[cursor.getCount()];
            int iDBrows = 0;
            for (cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
                messageDBrows[iDBrows] = cursorToMessageDBrow(cursor);
                iDBrows++;
            }
            cursor.close();
            return messageDBrows;
        }
        else if(tpye.equals(ALL_MESSAGE)){
            cursor = mMyMessageDB.rawQuery("select * from MyMessage",null);
            messageDBrows=new MessageDBrow[cursor.getCount()];
            int iDBrows = 0;
            for (cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
                messageDBrows[iDBrows] = cursorToMessageDBrow(cursor);
                iDBrows++;
            }
            cursor.close();
            return messageDBrows;
        }else {
            return null;
        }
    }
    /** 查找函数
     * 参数为下载的消息行
     * 返回本地对应的消息行，如果本地不存在对应消息 返回空，如果本地存在对应的消息大于一条 返回一个不为本地的消息行
     * */
    public static MessageDBrow searchMessage(MessageDBrow downloadDBrow){
        Cursor cursor;
        MessageDBrow localDBrow;
        cursor = mMyMessageDB.rawQuery("select * from MyMessage where ProjectName=? and MsgMark='1'",new String[]{downloadDBrow.getRowBaseString(MessageDBrow.BASE_NAME_SINGLE)});

        if(cursor.getCount()==0){
            cursor.close();
            return null;
        } else if (cursor.getCount()>1) {
            localDBrow = new MessageDBrow();
            localDBrow.setMessageDBrow(false);
            cursor.close();
            return localDBrow;
        }else {
            cursor.moveToFirst();
            localDBrow = cursorToMessageDBrow(cursor);
            cursor.close();
            return localDBrow;
        }
    }

    private static MessageDBrow cursorToMessageDBrow(Cursor cursor){
        String[] bases = new String[4];
        for (int i=0;i<4;i++){
            bases[i] = cursor.getString(i);
        }

        String[] marks = new String[5];
        for (int i=0;i<5;i++){
            marks[i] = cursor.getString(i+4);
        }


        String[] times = new String[6];
        for (int i=0;i<6;i++){
            times[i] = cursor.getString(i+9);
        }

        String[] timebps = new String[6];
        for (int i=0;i<6;i++){
            timebps[i] = cursor.getString(i+15);
        }

        MessageDBrow messageDBrow = new MessageDBrow();
        messageDBrow.setMessageDBrow(true,marks,bases,times,timebps);
        return messageDBrow;
    }

    public static int getNoReadNum(){
        Cursor cursor;
        cursor = mMyMessageDB.rawQuery("select * from MyMessage where MsgRead='0'",null);//MsgRead=2时为隐藏
        int numofnoread = cursor.getCount();
        cursor.close();
        return numofnoread;
    }
    public static void setReadListener(MessageReadListener listener){
        mReadListener = listener;
    }

    public static void startUpdate(){
        UpdateBDfinishMark = false;
        UpdateVSfinishMark = false;
    }
    public static boolean isUpdateBDfinish(){
        return UpdateBDfinishMark;
    }
    public static boolean isUpdateVSfinish(){
        return UpdateVSfinishMark;
    }
    private static void updateVSfinish(){
        UpdateVSfinishMark = true;
    }
    private static void updateBDfinish(){
        UpdateBDfinishMark = true;
    }

}
