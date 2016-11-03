package com.example.konka.workbench.activity.message;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Wangfan on 2016-10-17.
 * 消息时间类
 */
public class MessageDBtimer {
    /**时间数据
     * 格式为 yyyy-MM-dd */
    private String mTime;

    MessageDBtimer(){
        mTime = getNowTime();
    }
    MessageDBtimer(String standerTime){
        mTime = standerTime;
    }

    /**获得当前时间函数
     * 返回标准格式的当前时间
     * */
    public static String getNowTime(){
        Date objdate=new Date();//获得系统当前时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//设置时间格式
        String date=sdf.format(objdate);
        return date;
    }

    /**获得当前时间距离某一时间函数
     * 参数要求为格式为 yyyy-MM-dd 否则一律返回 _Unset
     * 输出为String类型数字 可以直接转换为int型 表示当前距离输入时间的天数
     * 注意：当前时间距离输入时间差2年及以上时会有几天的误差 慎用；
     * */
    public static String getBeforePresent(String date_record){
        if(!isTime(date_record))
            return MessageDBrow.UNSET;
        String date_now = getNowTime();

        int[] i_now,i_record;
        i_now = changeTimeType(date_now);
        i_record = changeTimeType(date_record);

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

    private static boolean isTime(String DateRecord){
        if(DateRecord.length()<10)
            return false;
        else if(DateRecord.charAt(4)=='-'&&DateRecord.charAt(7)=='-')
            return true;
        else
            return false;
    }

    /**获得年月日函数
     * 输入标准格式时间
     * 返回一个int型数组 数组第一个元素为年 第二个为月 第三个为日
     * */
    private static int[] changeTimeType(String StanderTime){
        int year,month,day;
        year = Integer.parseInt(StanderTime.substring(0,4));
        month = Integer.parseInt(StanderTime.substring(5,7));
        day = Integer.parseInt(StanderTime.substring(8));
        int[] i={year,month,day};
        return i;
    }//将XXXX-XX-XX类型的时间String类型数据换成int型数组


    public static String getTimeBPChangeId(MessageDBrow local,MessageDBrow download){
        String[] strings1 = local.getRowStringArray(MessageDBrow.TIME_BP_ARRAY);
        String[] strings2 = download.getRowStringArray(MessageDBrow.TIME_BP_ARRAY);
        String string = "";
        for(int i=0;i<6;i++){
            if((!strings1[i].equals(strings2[i]))&&strings2[i].equals("2"))//变化并且仅差两天
                string = string+"1";
            else
                string = string+"0";
        }
        return string;
    }

    public static String getTimeChangeId(MessageDBrow local,MessageDBrow download){
        String[] strings1 = local.getRowStringArray(MessageDBrow.TIME_ARRAY);
        String[] strings2 = download.getRowStringArray(MessageDBrow.TIME_ARRAY);
        String string = "";
        for(int i=0;i<6;i++){
            if(!strings1[i].equals(strings2[i]))//时间不相等
                string = string+"1";
            else
                string = string+"0";
        }
        return string;
    }


}
