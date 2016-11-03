package com.example.konka.workbench.activity.detailProEdit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import com.example.konka.workbench.R;
import com.example.konka.workbench.util.ContextValue;
import com.example.konka.workbench.util.DateTimePickDialogUtil;
import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;

/**
 * Created by xiaotao on 2016-9-19.
 * 编辑项目内容
 */
public class DetailProEditAty extends Activity implements View.OnClickListener,IDetailProEditView{
    /*项目信息*/
    private Project mProject;
    /*项目最新软件版本信息*/
    private Version mlastVersion;
    /*保存按钮*/
    private Button preserve_Button;
    /*取消按钮*/
    private Button cancel_Button;
    /*实例化控制器*/
    private ProEditPresenter proEditPresenter = new ProEditPresenter(this);

    //项目属性
    private EditText projectName;
    private EditText type;
    private EditText platform;
    private Spinner series;
    private Spinner localDimming;
    private EditText resolution;
    private EditText character;
    private EditText tuner;
    private EditText demo;
    private EditText port;
    private EditText withScreen;
    private EditText bom;
    private Button bom_effective;
    private EditText demAddress;
    private EditText softwareName;
    private EditText version;
    private EditText reportNumber;
    private Button firstTestDate;
    private Button sampleFinishDate;
    private Button midTestDate;
    private Button volProDate;
    private Button storageDate;
    private Spinner state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        mProject= (Project) getIntent().getSerializableExtra(ContextValue.PROJECT_DATA);
        mlastVersion= (Version) getIntent().getSerializableExtra(ContextValue.VERSION_DATA);
        initView();
        time_Setting();
        preserve_Button.setOnClickListener(this);
        cancel_Button.setOnClickListener(this);
    }

    /**
     * 实例化控件并赋值
     */
    private void initView(){
        projectName= (EditText) findViewById(R.id.myProject_edit);
        type= (EditText) findViewById(R.id.type_edit);
        platform = (EditText) findViewById(R.id.platform_edit);
        series= (Spinner) findViewById(R.id.series_spinner);
        localDimming = (Spinner) findViewById(R.id.localDimming_spinner);
        resolution = (EditText) findViewById(R.id.resolution_edit);
        character = (EditText) findViewById(R.id.character_edit);
        tuner = (EditText) findViewById(R.id.tuner_edit);
        port = (EditText) findViewById(R.id.port_edit);
        withScreen = (EditText) findViewById(R.id.withScreen_edit);
        bom = (EditText) findViewById(R.id.bom_edit);
        bom_effective = (Button) findViewById(R.id.BOMTime_edit);
        demAddress = (EditText) findViewById(R.id.demAddress_edit);
        softwareName = (EditText) findViewById(R.id.softwareName_edit);
        version= (EditText) findViewById(R.id.softwareVersion_edit);
        reportNumber = (EditText) findViewById(R.id.reportNumber_edit);
        firstTestDate = (Button) findViewById(R.id.firstTestDate_edit);
        midTestDate = (Button) findViewById(R.id.minTestDate_edit);
        volProDate = (Button) findViewById(R.id.volProDate_edit);
        sampleFinishDate = (Button) findViewById(R.id.sampleFinishDate_edit);
        storageDate = (Button) findViewById(R.id.storageDate_edit);
        demo = (EditText) findViewById(R.id.demo_edit);
        state = (Spinner) findViewById(R.id.state_spinner);
        //按钮
        preserve_Button = (Button) findViewById(R.id.preserve_button);
        cancel_Button = (Button) findViewById(R.id.cancel_button);

        //为控件赋值
        projectName.setText(mProject.getProjectName());
        type.setText(mProject.getType());
        platform.setText(mProject.getPlatform());
        resolution.setText(mProject.getResolution());
        character.setText(mProject.getCharacter());
        storageDate.setText(mProject.getStorageDate());
        sampleFinishDate.setText(mProject.getSampleFinishDate());
        volProDate.setText(mProject.getVolProDate());
        midTestDate.setText(mProject.getMidTestDate());
        firstTestDate.setText(mProject.getFirstTestDate());
        version.setText(mlastVersion.getVersionName());
        softwareName.setText(mProject.getSoftwareName());
        demAddress.setText(mProject.getDemAddress());
        bom_effective.setText(mProject.getBom_effective());
        bom.setText(mProject.getBom());
        withScreen.setText(mProject.getWithScreen());
        port.setText(mProject.getPort());
        demo.setText(mProject.getDemo());
        tuner.setText(mProject.getTuner());
        reportNumber.setText(mProject.getReportNumber());
        storageDate.setText(mProject.getStorageDate());
        volProDate.setText(mProject.getVolProDate());
        //设置spinner 默认选中的值
        setSpinnerItemSelectedByValue(series,mProject.getSeries());
        setSpinnerItemSelectedByValue(localDimming,mProject.getSeries());
        setSpinnerItemSelectedByValue(state,mlastVersion.getState());

    }

    /**
     * 设置时间
     */
    private void time_Setting(){

        bom_effective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        DetailProEditAty.this);
                dateTimePicKDialog.dateTimePicKDialog(bom_effective);
            }
        });

        firstTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog = new DateTimePickDialogUtil(DetailProEditAty.this);
                dateTimePickDialog.dateTimePicKDialog(firstTestDate);
            }
        });

        sampleFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog = new DateTimePickDialogUtil(DetailProEditAty.this);
                dateTimePickDialog.dateTimePicKDialog(sampleFinishDate);
            }
        });

        midTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog =new DateTimePickDialogUtil(DetailProEditAty.this);
                dateTimePickDialog.dateTimePicKDialog(midTestDate);
            }
        });

        volProDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog = new DateTimePickDialogUtil(DetailProEditAty.this);
                dateTimePickDialog.dateTimePicKDialog(volProDate);
            }
        });

        storageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePickDialog =new DateTimePickDialogUtil(DetailProEditAty.this);
                dateTimePickDialog.dateTimePicKDialog(storageDate);
            }
        });
    }

    /**
     * 使spinner显示该value
     * @param spinner
     * @param value
     */
    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                finish();
            break;
            case R.id.preserve_button:
                Log.d("preserve", "yunxing");
                Log.d("项目名", projectName.getText().toString().trim());
                mProject.setProjectName(projectName.getText().toString().trim());
                mProject.setPlatform(platform.getText().toString().toLowerCase().replace(" ",""));
                mProject.setType(type.getText().toString().toUpperCase().replace(" ",""));
                mProject.setSeries(series.getSelectedItem().toString());
                mProject.setLocalDimming(localDimming.getSelectedItem().toString());
                mProject.setResolution(resolution.getText().toString().trim());
                mProject.setCharacter(character.getText().toString().trim());
                mProject.setTuner(tuner.getText().toString().trim());
                mProject.setDemo(demo.getText().toString().trim());
                mProject.setPort(port.getText().toString().trim());
                mProject.setWithScreen(withScreen.getText().toString().replace(" ",""));
                mProject.setBom(bom.getText().toString().toUpperCase().replace(" ",""));
                mProject.setBom_effective(bom_effective.getText().toString());
                mProject.setDemAddress(demAddress.getText().toString().trim());
                mProject.setSoftwareName(softwareName.getText().toString().trim());
                mProject.setReportNumber(reportNumber.getText().toString().trim());
                mProject.setFirstTestDate(firstTestDate.getText().toString());
                mProject.setMidTestDate(midTestDate.getText().toString());
                mProject.setSampleFinishDate(sampleFinishDate.getText().toString());
                mProject.setVolProDate(volProDate.getText().toString());
                mProject.setStorageDate(storageDate.getText().toString());
                //更新项目内容
                proEditPresenter.updateProject();
        }
    }

    @Override
    public String getNewVersionName() {
        return version.getText().toString().toUpperCase().replace(" ","");
    }

    @Override
    public String getState() {
        return state.getSelectedItem().toString();
    }

    @Override
    public Version getLastVersion() {
        return mlastVersion;
    }

    @Override
    public Project getProject() {
        return mProject;
    }
    @Override
    public void updateProjectSuccess() {
        Toast.makeText(DetailProEditAty.this,"项目信息更新成功",Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();//销毁活动
    }

    @Override
    public void showNoNetwork() {
        Toast.makeText(this, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
    }
}
