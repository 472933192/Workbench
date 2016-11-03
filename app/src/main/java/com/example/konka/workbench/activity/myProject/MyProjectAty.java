package com.example.konka.workbench.activity.myProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.konka.workbench.R;
import com.example.konka.workbench.activity.detailProject.DetailProjectAty;
import com.example.konka.workbench.activity.newproject.NewProjectAty;
import com.example.konka.workbench.adapter.ExpandableGridAdapter;
import com.example.konka.workbench.adapter.ProjectAdapter;
import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.util.ContextValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 我的项目
 */
public class MyProjectAty extends Activity implements IMyProjectView {

    private ProjectAdapter madapter;//适配器
    private ListView mlistview;
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mFilterListView;
    private List<Project> mprojectList=new ArrayList<Project>();//装入适配器的list集合
    /*临时数据*/
    private List<HashSet<String>> temp = new ArrayList<HashSet<String>>();
    /*所有筛选的选项*/
    private List<ArrayList<String>> item = new ArrayList<ArrayList<String>>();
    /*选中的选项*/
    private List<ArrayList<String>> selectedData = new ArrayList<ArrayList<String>>();
    private MyProjectPresenter myProjectPresenter = new MyProjectPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myproject_page);
        initListener();
        myProjectPresenter.findMyProjects();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mydrawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//关闭手势滑动
        mFilterListView = (ExpandableListView) findViewById(R.id.mymenu_listView);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            temp.add(new HashSet<String>());
            item.add(new ArrayList<String>());
            selectedData.add(new ArrayList<String>());
        }
    }

    private void initListener() {
        ImageButton add_btn = (ImageButton) findViewById(R.id.add_btn);
        Button filter = (Button) findViewById(R.id.myfilter);
        Button confirm = (Button) findViewById(R.id.myok_bn);
        Button reset = (Button) findViewById(R.id.mycancel_bn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProjectAty.this, NewProjectAty.class);
                startActivityForResult(intent, ContextValue.PROJECT_ADD);
            }
        });
        /*展示筛选菜单*/
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(findViewById(R.id.mymenu_layout));
                ExpandableGridAdapter adapter = new ExpandableGridAdapter(MyProjectAty.this, item, selectedData);
                mFilterListView.setAdapter(adapter);
            }
        });
        /*确定按钮，查询筛选后的项目列表*/
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProjectPresenter.projectFilter(selectedData);
            }
        });
        /*重置按钮，清空选中的数据，重新查询所有项目*/
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<selectedData.size();i++) {
                    selectedData.get(i).clear();
                }
                showListView(mprojectList);
                mDrawerLayout.closeDrawer(findViewById(R.id.mymenu_layout));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {   //判断数据来源
            case ContextValue.PROJECT_ADD:
                if (resultCode == RESULT_OK) {//判断处理结果是否成功
                    Project project = (Project) data.getSerializableExtra(ContextValue.PROJECT_DATA);
                    mprojectList.add(0, project);
                    madapter.notifyDataSetChanged();
                    mlistview.setSelection(mprojectList.size());
                }
                break;
            case ContextValue.PROJECT_OPERATE://返回删除结果，更新视图
                if (resultCode == RESULT_OK) {
                    String projectId = data.getStringExtra(ContextValue.PROJECT_ID);
                    for (Project p : mprojectList) {
                        if (p.getObjectId().equals(projectId)) {
                            mprojectList.remove(p);
                            break;
                        }
                    }
                    madapter.notifyDataSetChanged();
                } else if (resultCode == RESULT_CANCELED) {
                    String projectId = data.getStringExtra(ContextValue.PROJECT_ID);
                    myProjectPresenter.getProject(projectId);
                } else if (resultCode == ContextValue.PROJECT_EXIT) {
                    String projectId = data.getStringExtra(ContextValue.PROJECT_ID);
                    for (Project project : mprojectList) {
                        if (projectId.equals(project.getObjectId())) {
                            mprojectList.remove(project);
                            break;
                        }
                    }
                    madapter.notifyDataSetChanged();
                }
        }
    }

    /**
     * 展示项目列表，监听点击事件
     *
     * @param myList
     */
    private void showListView(final List<Project> myList) {
        madapter = new ProjectAdapter(MyProjectAty.this, R.layout.mypro_item, myList);
        mlistview = (ListView) findViewById(R.id.lv_mypro);
        mlistview.setAdapter(madapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//子项点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project project = myList.get(position);
                Intent intent = new Intent(MyProjectAty.this, DetailProjectAty.class);
                intent.putExtra(ContextValue.PROJECT_ID, project.getObjectId());
                startActivityForResult(intent, ContextValue.PROJECT_OPERATE);
            }
        });
    }


    /**
     * 加载所有项目,并初始化存储筛选项的list集合
     * list存储顺序
     * 0：机型，1：平台，2：bom，3:配屏
     */
    @Override
    public void initMyProject(List<Project> list) {
        mprojectList=list;
        showListView(mprojectList);
        for (Project p : mprojectList) {
            temp.get(0).add(p.getType());
            temp.get(1).add(p.getPlatform());
            temp.get(2).add(p.getBom());
            temp.get(3).add(p.getWithScreen());
        }
        item.get(0).addAll(temp.get(0));
        item.get(1).addAll(temp.get(1));
        item.get(2).addAll(temp.get(2));
        item.get(3).addAll(temp.get(3));
        temp = null;//赋值为空，回收内存
    }

    @Override
    public void refreshProject(Project p) {
        for (Project project : mprojectList) {
            if (p.getObjectId().equals(project.getObjectId())) {
                project.setProjectName(p.getProjectName());
                project.setType(p.getType());
                project.setPlatform(p.getPlatform());
                project.setBom(p.getBom());
                project.setWithScreen(p.getWithScreen());
                break;
            }
        }
        madapter.notifyDataSetChanged();
    }

    @Override
    public void filterRefresh(List<Project> list) {
        showListView(list);
        mDrawerLayout.closeDrawer(findViewById(R.id.mymenu_layout));//关闭侧滑菜单
    }

    @Override
    public void showNoNetwork() {
        Toast.makeText(MyProjectAty.this,"无网络连接，请检查您的手机网络.",Toast.LENGTH_SHORT).show();
    }
}

