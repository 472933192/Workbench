package com.example.konka.workbench.activity.newproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.konka.workbench.R;
import com.example.konka.workbench.util.ContextValue;
import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;
import com.example.konka.workbench.util.DateTimePickDialogUtil;


/**
 * Created by xiaotao on 2016-9-13.
 * 新建项目
 */
public class NewProjectAty extends Activity implements INewProjectView {
  //  private static final String TAG="NewProjectAty";
    private Project project;
    private Version version;
    private NewProjectPresenter newProjectPresenter = new NewProjectPresenter(this);
    private EditText myPro_name;
    private EditText type;
    private EditText platform;
    private Spinner series;
    private Spinner localDimming;
    private EditText resolution;
    private EditText character;
    private EditText tuner;
    private EditText demo;
    private EditText port;
    private Button bom_begin;
    private Button sampleEndDate;
    private Button beginTestDate;
    private Button midTestDate;
    private Button volProDate;
    private Button storageDate;
    private EditText demAddress;
    private EditText bom;
    private EditText softwareName;
    private EditText softwareVersion;
    private Spinner  state;
    private EditText withScreen;
    private EditText reportNumber;
    private Button preserve_Button;
    private Button cancel_Button;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        initView();

        time_Setting();

        initListener();

    }

    private void initView() {
        myPro_name = (EditText) findViewById(R.id.myProject_edit);//项目名
        type = (EditText) findViewById(R.id.type_edit);//型号
        platform = (EditText) findViewById(R.id.platform_edit);//平台
        series = (Spinner) findViewById(R.id.series_spinner);//系列
        localDimming = (Spinner) findViewById(R.id.localDimming_spinner);//背光技术
        state = (Spinner) findViewById(R.id.state_spinner);
        resolution = (EditText) findViewById(R.id.resolution_edit);//分辨率
        character = (EditText) findViewById(R.id.character_edit);//屏特性
        tuner = (EditText) findViewById(R.id.tuner_edit);
        demo = (EditText) findViewById(R.id.demo_edit);
        port = (EditText) findViewById(R.id.port_edit);
        withScreen = (EditText) findViewById(R.id.withScreen_edit);//配平
        bom = (EditText) findViewById(R.id.bom_edit);
        bom_begin = (Button) findViewById(R.id.BOMTime_edit);//bom生效时间
        demAddress = (EditText) findViewById(R.id.demAddress_edit);//
        softwareName = (EditText) findViewById(R.id.softwareName_edit);
        softwareVersion = (EditText) findViewById(R.id.softwareVersion_edit);
        reportNumber = (EditText) findViewById(R.id.reportNumber_edit);
        beginTestDate = (Button) findViewById(R.id.firstTestDate_edit);
        sampleEndDate = (Button) findViewById(R.id.sampleFinishDate_edit);//样评完成时间
        midTestDate = (Button) findViewById(R.id.minTestDate_edit);//
        volProDate = (Button) findViewById(R.id.volProDate_edit);//开始批量时间
        storageDate = (Button) findViewById(R.id.storageDate_edit);//批量入库时间

        preserve_Button = (Button) findViewById(R.id.preserve_button);
        cancel_Button = (Button) findViewById(R.id.cancel_button);
    }

    public void initListener() {

        preserve_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNullOrEmpty(myPro_name.getText().toString())) {
                    Toast.makeText(NewProjectAty.this, "项目名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNullOrEmpty(type.getText().toString())) {
                    Toast.makeText(NewProjectAty.this, "机型不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNullOrEmpty(platform.getText().toString())) {
                    Toast.makeText(NewProjectAty.this, "平台不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNullOrEmpty(withScreen.getText().toString())) {
                    Toast.makeText(NewProjectAty.this, "配屏不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNullOrEmpty(bom.getText().toString())) {
                    Toast.makeText(NewProjectAty.this, "BOM不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNullOrEmpty(softwareVersion.getText().toString())) {
                    Toast.makeText(NewProjectAty.this, "软件版本不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                project = new Project();
                project.setProjectName(myPro_name.getText().toString().trim());
                project.setPlatform(platform.getText().toString().toLowerCase().replace(" ", ""));
                project.setType(type.getText().toString().toUpperCase().replace(" ", ""));
                project.setSeries(series.getSelectedItem().toString());
                project.setLocalDimming(localDimming.getSelectedItem().toString());
                project.setResolution(resolution.getText().toString().trim());
                project.setCharacter(character.getText().toString().trim());
                project.setTuner(tuner.getText().toString().trim());
                project.setDemo(demo.getText().toString().trim());
                project.setPort(port.getText().toString().trim());
                project.setBom(bom.getText().toString().toUpperCase().replace(" ", ""));
                project.setBom_effective(bom_begin.getText().toString());
                project.setSampleFinishDate(sampleEndDate.getText().toString());
                project.setFirstTestDate(beginTestDate.getText().toString());
                project.setMidTestDate(midTestDate.getText().toString());
                project.setVolProDate(midTestDate.getText().toString());
                project.setStorageDate(storageDate.getText().toString());
                project.setDemAddress(demAddress.getText().toString().trim());
                project.setSoftwareName(softwareName.getText().toString().trim());
                project.setWithScreen(withScreen.getText().toString().replace(" ", ""));
                project.setReportNumber(reportNumber.getText().toString().trim());
                project.setVolProDate(volProDate.getText().toString());
                version = new Version();
                version.setVersionName(softwareVersion.getText().toString().toUpperCase().replace(" ", ""));
                version.setState(state.getSelectedItem().toString());//从表单获取
                newProjectPresenter.save(project,version);      //保存新建项目
            }
        });

        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//销毁当前活动

            }
        });
    }

    @Override
    public void createProjectSuccess(String projectId) {
        Toast.makeText(NewProjectAty.this,"创建新项目"+projectId+"成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        project.setObjectId(projectId);//设置id,确保用户修改该项目时能够保存。
        intent.putExtra(ContextValue.PROJECT_DATA,project);//传递新增的project，用于更新视图
        setResult(RESULT_OK,intent);//返回数据给上一个活动
        finish();//销毁当前活动，会回调上一个活动的onActivityResult()方法
    }

    private boolean isNullOrEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    private void time_Setting(){

        bom_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        NewProjectAty.this);
                dateTimePicKDialog.dateTimePicKDialog(bom_begin);
            }
        });

        beginTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog = new DateTimePickDialogUtil(NewProjectAty.this);
                dateTimePickDialog.dateTimePicKDialog(beginTestDate);
            }
        });

        sampleEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog = new DateTimePickDialogUtil(NewProjectAty.this);
                dateTimePickDialog.dateTimePicKDialog(sampleEndDate);
            }
        });

        midTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog =new DateTimePickDialogUtil(NewProjectAty.this);
                dateTimePickDialog.dateTimePicKDialog(midTestDate);
            }
        });

        volProDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog = new DateTimePickDialogUtil(NewProjectAty.this);
                dateTimePickDialog.dateTimePicKDialog(volProDate);
            }
        });

        storageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog =new DateTimePickDialogUtil(NewProjectAty.this);
                dateTimePickDialog.dateTimePicKDialog(storageDate);
            }
        });
    }
}


