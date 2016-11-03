package com.example.konka.workbench.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.konka.workbench.R;
import com.example.konka.workbench.activity.message.MSG_SQLite_line;

import java.util.List;

/**
 * Created by HP on 2016-9-14.
 * message适配器
 */
public class MessageAdapter extends ArrayAdapter<MSG_SQLite_line> {
    private int resourceId;
    private String msgtime;
    private String projectname;
    private String state;
    private String reportnumber;
    private String tpye;

    private String bom_effective_bp;   //BOM生效
    private String firstTestDate_bp;   //初始下机
    private String sampleFinishDate_bp;//样评完成
    private String midTestDate_bp;     //中试下机
    private String volProDate_bp;      //开始批量
    private String storageDate_bp;     //批量入库

    private Context act;


    private boolean notread;

    public MessageAdapter(Context context, int resource, List<MSG_SQLite_line> objects) {
        super(context, resource, objects);
        resourceId=resource;
        act = context;
    }

    public void SetData(MSG_SQLite_line msg){
        String[] strings = msg.GetMsgData();
        msgtime = strings[0];
        projectname = strings[1];
        state = strings[2];
        tpye = strings[3];

        for(int i=4;i<=9;i++){
            if(Integer.parseInt(strings[i])<0)
                strings[i] = "已结束";
            else if(Integer.parseInt(strings[i])==0)
                strings[i] = "为今天";
            else
                strings[i] = "还有"+strings[i]+"天";
        }
        bom_effective_bp = strings[4];
        firstTestDate_bp = strings[5];
        sampleFinishDate_bp = strings[6];
        midTestDate_bp = strings[7];
        volProDate_bp = strings[8];
        storageDate_bp = strings[9];

        reportnumber = strings[10];

        notread = msg.IsNotRead();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MSG_SQLite_line msg = getItem(position);
        SetData(msg);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view=convertView;
        }
        TextView name=(TextView)view.findViewById(R.id.lv_message_name);
        TextView time=(TextView)view.findViewById(R.id.lv_message_time);
        TextView content=(TextView)view.findViewById(R.id.lv_message_content);

        if(notread){//如果没有读过 设置字体加粗
            TextPaint nametp = name.getPaint();
            nametp.setFakeBoldText(true);
            TextPaint contenttp = content.getPaint();
            contenttp.setFakeBoldText(true);
            TextPaint timetp = time.getPaint();
            timetp.setFakeBoldText(true);
        }else {
            TextPaint nametp = name.getPaint();
            nametp.setFakeBoldText(false);
            TextPaint contenttp = content.getPaint();
            contenttp.setFakeBoldText(false);
            TextPaint timetp = time.getPaint();
            timetp.setFakeBoldText(false);
        }

        switch (tpye){
            /*case "0":{
                name.setText("已过期通知");
                switch (msg.getMessageid().charAt(1)){
                    case '1':{content.setText("有一个名为\""+msg.getProjectname()+"\"的新项目");break;}
                    case '2':{content.setText("名为\""+msg.getProjectname()+"\"的项目发生了变动");break;}
                    case '3':{content.setText("项目\""+msg.getProjectname()+"\"的BOM生效时间提醒");break;}
                }
                time.setText(msg.getMessagetime());
                break;
            }
            */
            case "11":{
                name.setText("新项目通知");
                content.setText("有一个名为'"+projectname+"'的新项目\n"+
                        "BOM生效时间"+bom_effective_bp+"\n"+
                        "初试下机时间"+firstTestDate_bp+"\n"+
                        "样评完成时间"+sampleFinishDate_bp+"\n"+
                        "中试下机时间"+midTestDate_bp+"\n"+
                        "开始批量时间"+volProDate_bp+"\n"+
                        "批量入库时间"+storageDate_bp);
                time.setText(msgtime);
                break;
            }
            case "21":{
                String case21="";
                if(state.equals("合格定版"))
                    case21 = "\n请提交主观评价报告";
                name.setText("软件状态变更通知");
                content.setText("项目'"+projectname+"'软件状态变更为:"+state+case21);
                time.setText(msgtime);
                break;
            }
            case "22":{
                name.setText("软件入库通知");
                content.setText("项目'"+projectname+"'主观评价报告已提交\n"
                        +"报告编号为:"+reportnumber+"\n"
                        +"请将软件入库");
                time.setText(msgtime);
                break;
            }
            case "23":{
                name.setText("软件入库通知");
                content.setText("项目'"+projectname+"'主观评价报告已更改\n"
                        +"报告编号更改为:"+reportnumber+"\n"
                        +"请将软件入库");
                time.setText(msgtime);
                break;
            }
            case "31":{
                name.setText("BOM生效时间通知");
                content.setText("距离项目'"+projectname+"'BOM生效时间还有两天");
                time.setText(msgtime);
                break;
            }
            case "32":{
                name.setText("初始下机时间通知");
                content.setText("距离项目'"+projectname+"'初始下机时间还有两天");
                time.setText(msgtime);
                break;
            }
            case "33":{
                name.setText("样评完成时间通知");
                content.setText("距离项目'"+projectname+"'样评完成时间还有两天");
                time.setText(msgtime);
                break;
            }
            case "34":{
                name.setText("中试下机时间通知");
                content.setText("距离项目'"+projectname+"'中试下机时间还有两天");
                time.setText(msgtime);
                break;
            }
            case "35":{
                name.setText("开始批量时间通知");
                content.setText("距离项目'"+projectname+"'开始批量时间还有两天");
                time.setText(msgtime);
                break;
            }
            case "36":{
                name.setText("批量入库时间通知");
                content.setText("距离项目'"+projectname+"'批量入库时间还有两天");
                time.setText(msgtime);
                break;
            }
        }

       // projectname.setText(project.getProjectName());
      //  type.setText(project.getType());
       // platform.setText(project.getPlatform());
        return view;
    }


    //在SQL表中删除position位项
    public void delItemFromSQL(int position){
        MSG_SQLite_line item = getItem(position);
        String File_Dir = act.getFilesDir().toString()+"/my_project.db3";//SQLite路径
        //System.out.println(File_Dir);
        SQLiteDatabase MyProject_DB = SQLiteDatabase.openOrCreateDatabase(File_Dir,null);
        if (item.GetMark().equals("0")){//无效消息直接删除
            item.DeleteLine(MyProject_DB);
        }
        else {//有效消息设置为隐藏
            item.UpdateLine(MyProject_DB,"Msg_read","2");
        }
    }

}
