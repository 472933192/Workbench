package com.example.konka.workbench.util;

/**
 * Created by HP on 2016-9-23.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.konka.workbench.R;

public class DateTimePickDialogUtil implements OnDateChangedListener {

    private DatePicker datePicker;
    private AlertDialog ad;
    private String dateTime;
    private Activity activity;


    public DateTimePickDialogUtil(Activity activity) {

        this.activity = activity;
    }

    public void init(DatePicker datePicker) {

        Calendar calendar = Calendar.getInstance();

        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
    }

    public AlertDialog dateTimePicKDialog(final Button inputDate) {
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.activity_datetime, null);//动态加载布局，实现弹窗效果
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);

        init(datePicker);

        ad = new AlertDialog.Builder(activity)

                .setView(dateTimeLayout)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        onDateChanged(null, 0, 0, 0);
                        inputDate.setText(dateTime);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        inputDate.setText("");
                    }
                }).show();

        return ad;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();//获得日历实例

        calendar.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        dateTime = sdf.format(calendar.getTime());
        //Toast.makeText(activity, dateTime, Toast.LENGTH_LONG).show();
    }
}