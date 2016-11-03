package com.example.konka.workbench.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.konka.workbench.R;
import com.example.konka.workbench.activity.message.MessageDBHelper;
import com.example.konka.workbench.activity.message.MessageDBrow;

import java.util.List;

/**
 * Created by HP on 2016-10-18.
 */
public class MessageAdapterForTest extends ArrayAdapter<MessageDBrow> {

    private int mResourceId;
    private String mMsgTime;
    private String mProjectName;
    private String mState;
    private String mReportNumber;
    private String mType;
    /*
        private String bom_effective;   //BOM生效
        private String firstTestDate;   //初始下机
        private String sampleFinishDate;//样评完成
        private String midTestDate;     //中试下机
        private String volProDate;      //开始批量
        private String storageDate;     //批量入库
    */
    private String mBomEffectiveBP;   //BOM生效
    private String mFirstTestDateBP;   //初始下机
    private String mSampleFinishDateBP;//样评完成
    private String mMidTestDateBP;     //中试下机
    private String mVolProDateBP;      //开始批量
    private String mStorageDateBP;     //批量入库

    private Context mAct;
    private boolean mNotRead;

    public MessageAdapterForTest(Context context, int resource, List<MessageDBrow> objects) {
        super(context, resource, objects);
        mResourceId=resource;
        mAct = context;
    }

    public void setData(MessageDBrow msg){
        String[] strings = msg.getRowStringArray(MessageDBrow.ALL_DATA);
        mNotRead = msg.isNotRead();
        
        mProjectName = strings[1];
        mState = strings[2];
        mReportNumber = strings[3];
        mReportNumber = mReportNumber.substring(0,mReportNumber.length()-1);//把报告编号最后一个结尾符X去掉（为了保证1.0情况下不变成int型而保存时缩写为1而设立）

        mMsgTime = strings[4];
        mType = strings[5];

        for(int i=15;i<=20;i++){
            if(strings[i].equals(MessageDBrow.UNSET))
                strings[i] = "待定";
            else if(Integer.parseInt(strings[i])<0)
                strings[i] = "已结束";
            else if(Integer.parseInt(strings[i])==0)
                strings[i] = "为今天";
            else{
                if (mNotRead)
                    strings[i] = "为："+strings[i-6]+"（还有"+strings[i]+"天）";
                else
                    strings[i] = "为："+strings[i-6];
            }

        }
        mBomEffectiveBP = strings[15];
        mFirstTestDateBP = strings[16];
        mSampleFinishDateBP = strings[17];
        mMidTestDateBP = strings[18];
        mVolProDateBP = strings[19];
        mStorageDateBP = strings[20];

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageDBrow msg = getItem(position);
        setData(msg);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
        } else {
            view=convertView;
        }
        TextView name=(TextView)view.findViewById(R.id.lv_message_name);
        TextView time=(TextView)view.findViewById(R.id.lv_message_time);
        TextView content=(TextView)view.findViewById(R.id.lv_message_content);

        if(mNotRead){//如果没有读过 设置字体加粗
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

        String textString;
        switch (mType){
            /*case "0":{
            */
            case "11":{
                textString = "有一个名为'"+mProjectName+"'的新项目\n"+
                        "BOM生效时间"+mBomEffectiveBP+"\n"+
                        "初试下机时间"+mFirstTestDateBP+"\n"+
                        "样评完成时间"+mSampleFinishDateBP+"\n"+
                        "中试下机时间"+mMidTestDateBP+"\n"+
                        "开始批量时间"+mVolProDateBP+"\n"+
                        "批量入库时间"+mStorageDateBP;
                name.setText("新项目通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "21":{
                String case21="";
                if(mState.equals("合格定版"))
                    case21 = "\n请提交主观评价报告";
                textString = "项目'"+mProjectName+"'软件状态变更为:"+mState+case21;
                name.setText("软件状态变更通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "22":{
                textString = "项目'"+mProjectName+"'主观评价报告已提交\n"
                        +"报告编号为:"+mReportNumber+"\n"
                        +"请将软件入库";
                name.setText("软件入库通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "23":{
                textString = "项目'"+mProjectName+"'主观评价报告已更改\n"
                        +"报告编号更改为:"+mReportNumber+"\n"
                        +"请将软件入库";
                name.setText("软件入库通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "31":{
                textString = "距离项目'"+mProjectName+"'BOM生效时间还有两天";
                name.setText("BOM生效时间通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "32":{
                textString = "距离项目'"+mProjectName+"'初始下机时间还有两天";
                name.setText("初始下机时间通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "33":{
                textString = "距离项目'"+mProjectName+"'样评完成时间还有两天";
                name.setText("样评完成时间通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "34":{
                textString = "距离项目'"+mProjectName+"'中试下机时间还有两天";
                name.setText("中试下机时间通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "35":{
                textString = "距离项目'"+mProjectName+"'开始批量时间还有两天";
                name.setText("开始批量时间通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "36":{
                textString = "距离项目'"+mProjectName+"'批量入库时间还有两天";
                name.setText("批量入库时间通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "41":{
                textString = "项目'"+mProjectName+"'BOM生效时间改为"+mBomEffectiveBP;
                name.setText("时间变更通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "42":{
                textString = "项目'"+mProjectName+"'初始下机时间改为"+mFirstTestDateBP;
                name.setText("时间变更通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "43":{
                textString = "项目'"+mProjectName+"'样评完成时间改为"+mFirstTestDateBP;
                name.setText("时间变更通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "44":{
                textString = "项目'"+mProjectName+"'中试下机时间改为"+mMidTestDateBP;
                name.setText("时间变更通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "45":{
                textString ="项目'"+mProjectName+"'开始批量时间改为"+mVolProDateBP;
                name.setText("时间变更通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
            case "46":{
                textString ="项目'"+mProjectName+"'批量入库时间改为"+mStorageDateBP;
                name.setText("时间变更通知");
                content.setText(textString);
                time.setText(mMsgTime);
                break;
            }
        }

        // mProjectName.setText(project.getmProjectName());
        //  type.setText(project.getType());
        // platform.setText(project.getPlatform());
        return view;
    }

    public void deletItemFromMessageDB(int position){
        MessageDBrow item = getItem(position);
        MessageDBHelper.deleteMessage(item);
    }


}
