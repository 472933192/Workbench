package com.example.konka.workbench.activity.addMember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.konka.workbench.R;
import com.example.konka.workbench.util.ContextValue;
import com.example.konka.workbench.adapter.UserNumAdapter;
import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

/**
 * Created by xiaotao on 2016-9-22.
 * 项目成员添加页面
 */
public class AddMemAty extends Activity implements IAddMemView{
    private AddMemPresenter addMemPresenter = new AddMemPresenter(this);

    private UserNumAdapter adapter;//适配器
    private List<User> userList;//装入适配器的list集合
    private List<User> userData;//存储项目已有成员
    private String projectId;//项目id
    private ListView listview;
    private final List<User> getList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_member);
        projectId = getIntent().getStringExtra(ContextValue.PROJECT_ID);
        userData = (List<User>) getIntent().getSerializableExtra(ContextValue.DATA_LIST);
        listview = (ListView) findViewById(R.id.project_member);
        addMemPresenter.initOtherMem();
        initListener();
    }

    private void initListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//子项点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checktv = (CheckedTextView) view.findViewById(R.id.user_Num_cho);
                if(checktv.isChecked()){
                    checktv.setChecked(false);
                    getList.remove(userList.get(position));
                }else{
                    checktv.setChecked(true);
                    getList.add(userList.get(position));
                }
            }
        });
        Button addMembers= (Button) findViewById(R.id.btn_ok);
        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMemPresenter.addRelation();
            }
        });

    }

    @Override
    public void showOtherMem(List<User> users) {
        userList=users;
        adapter = new UserNumAdapter(AddMemAty.this, R.layout.activity_project_member_item, userList);
        listview.setAdapter(adapter);//异步更新视图
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public List<User> getUserList() {
        return userData;
    }

    @Override
    public List<User> getSelectedUsers() {
        return getList;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public void toMemOfProAty() {
        Toast.makeText(AddMemAty.this, "添加项目成员成功", Toast.LENGTH_SHORT).show();
        //返回上一个活动，携带getList集合里的数据
        Intent intent=new Intent();
        intent.putExtra(ContextValue.DATA_LIST, (Serializable)getList);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void showNoNetwork() {
        Toast.makeText(this, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
    }
}
