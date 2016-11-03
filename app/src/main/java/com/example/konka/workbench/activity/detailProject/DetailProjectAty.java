package com.example.konka.workbench.activity.detailProject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.konka.workbench.activity.detailProEdit.DetailProEditAty;
import com.example.konka.workbench.activity.memberOfProject.MemberOfProAty;
import com.example.konka.workbench.R;
import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;
import com.example.konka.workbench.util.ContextValue;

/**
 * Created by xiaotao on 2016-9-18.
 * 展示具体某个项目，需判断用户是否属于项目人员,从而展示不同的布局给用户
 */
public class DetailProjectAty extends Activity implements IDetailProjectView {
    /*保存项目id,用于传递到另一个活动*/
    private String mprojectId;
    /*用于保存软件最新版本*/
    private Version mlastVersion;
    private DetailProPresenter detailProPresenter = new DetailProPresenter(this);
    //项目属性
    private TextView projectName;
    private TextView type;
    private TextView platform;
    private TextView series;
    private TextView localDimming;
    private TextView resolution;
    private TextView character;
    private TextView tuner;
    private TextView demo;
    private TextView port;
    private TextView withScreen;
    private TextView bom;
    private TextView bom_effective;
    private TextView demAddress;
    private TextView softwareName;
    private TextView version;
    private TextView reportNumber;
    private TextView firstTestDate;
    private TextView sampleFinishDate;
    private TextView minTestDate;
    private TextView volProDate;
    private TextView storageDate;
    private TextView state;

    private boolean flag = false;//判断该项目控件是否已实例化,默认为未实例化

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_demo);
        mprojectId = getIntent().getStringExtra(ContextValue.PROJECT_ID);
        detailProPresenter.findProject(mprojectId);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ContextValue.PROJECT_ID, mprojectId);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*更新成功*/
            case ContextValue.PROJECT_OPERATE:
                if (resultCode == RESULT_OK) {
                    detailProPresenter.findProject(mprojectId);
                }
        }
    }

    /**
     * 实例化控件
     */
    @Override
    public void initView() {
        projectName = (TextView) findViewById(R.id.myProject_demo);
        type = (TextView) findViewById(R.id.type_demo);
        platform = (TextView) findViewById(R.id.platform_demo);
        series = (TextView) findViewById(R.id.series_demo);
        localDimming = (TextView) findViewById(R.id.localDimming_demo);
        resolution = (TextView) findViewById(R.id.resolution_demo);
        character = (TextView) findViewById(R.id.character_demo);
        tuner = (TextView) findViewById(R.id.tuner_demo);
        demo = (TextView) findViewById(R.id.demo_demo);
        port = (TextView) findViewById(R.id.port_demo);
        withScreen = (TextView) findViewById(R.id.withScreen_demo);
        bom = (TextView) findViewById(R.id.bom_demo);
        bom_effective = (TextView) findViewById(R.id.BOMTime_demo);
        demAddress = (TextView) findViewById(R.id.demAddress_demo);
        softwareName = (TextView) findViewById(R.id.softwareName_demo);
        version = (TextView) findViewById(R.id.softwareVersion_demo);
        reportNumber = (TextView) findViewById(R.id.reportNumber_demo);
        firstTestDate = (TextView) findViewById(R.id.firstTestDate_demo);
        minTestDate = (TextView) findViewById(R.id.minTestDate_demo);
        volProDate = (TextView) findViewById(R.id.volProDate_demo);
        sampleFinishDate = (TextView) findViewById(R.id.sampleFinishDate_demo);
        storageDate = (TextView) findViewById(R.id.storageDate_demo);
        state = (TextView) findViewById(R.id.state_demo);
        flag = true;//项目更新后不再实例化控件
    }

    /**
     * 为控件赋值
     *
     * @param project
     */
    @Override
    public void setValues(Project project) {
        //为控件赋值
        projectName.setText(project.getProjectName());
        type.setText(project.getType());
        platform.setText(project.getPlatform());
        series.setText(project.getSeries());
        localDimming.setText(project.getLocalDimming());
        resolution.setText(project.getResolution());
        character.setText(project.getCharacter());
        storageDate.setText(project.getStorageDate());
        sampleFinishDate.setText(project.getSampleFinishDate());
        volProDate.setText(project.getVolProDate());
        minTestDate.setText(project.getMidTestDate());
        firstTestDate.setText(project.getFirstTestDate());
        softwareName.setText(project.getSoftwareName());
        demAddress.setText(project.getDemAddress());
        bom_effective.setText(project.getBom_effective());
        bom.setText(project.getBom());
        withScreen.setText(project.getWithScreen());
        port.setText(project.getPort());
        demo.setText(project.getDemo());
        tuner.setText(project.getTuner());
        reportNumber.setText(project.getReportNumber());
        detailProPresenter.getLastVersion(project.getObjectId());//显示最新版本的软件和状态
    }

    /**
     * 用户是项目成员，实例化隐藏的按钮和设置按钮监听事件
     */
    @Override
    public void showHideButton() {
        //显示编辑和删除按钮，以及它们的点击事件
        final LinearLayout layout = (LinearLayout) findViewById(R.id.linear_gone);
        layout.setVisibility(View.VISIBLE);//是项目成员，则显示操作视图
        Button edit = (Button) findViewById(R.id.edit);//编辑按钮
        Button delete = (Button) findViewById(R.id.delete);
        Button showMembers = (Button) findViewById(R.id.show_member);
        Button quit = (Button) findViewById(R.id.delete_member);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProjectAty.this, DetailProEditAty.class);
                intent.putExtra(ContextValue.PROJECT_DATA, detailProPresenter.getmProject());
                intent.putExtra(ContextValue.VERSION_DATA, mlastVersion);
                startActivityForResult(intent, ContextValue.PROJECT_OPERATE);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailProjectAty.this);
                dialog.setTitle(R.string.delete_title);
                dialog.setMessage(R.string.delete_message);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        detailProPresenter.deleteProject();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        showMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProjectAty.this, MemberOfProAty.class);
                intent.putExtra(ContextValue.PROJECT_ID, mprojectId);
                startActivity(intent);
            }
        });
        //删除User与project的多对多关联
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailProPresenter.exitProject();
            }
        });
    }

    /**
     * 保存最新软件版本，为控件赋值
     *
     * @param v
     */
    @Override
    public void setLastVersion(Version v) {
        mlastVersion = v;
        version.setText(v.getVersionName());
        state.setText(v.getState());
    }

    /**
     * 为flag添加get方法
     *
     * @return
     */
    @Override
    public boolean getFlag() {
        return flag;
    }

    /**
     * 删除项目成功后，返回到我的项目
     */
    @Override
    public void deleteProjectSuccess() {
        Toast.makeText(DetailProjectAty.this, "项目删除成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(ContextValue.PROJECT_ID, mprojectId);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 成功退出项目后，返回我的项目
     */
    @Override
    public void exitProjectSuccess() {
        Toast.makeText(DetailProjectAty.this, "成功退出项目", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(ContextValue.PROJECT_ID, mprojectId);
        setResult(ContextValue.PROJECT_EXIT, intent);
        finish();
    }

    @Override
    public void showNoNetwork() {
        Toast.makeText(this, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
    }
}
