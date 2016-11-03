package com.example.konka.workbench.activity.memberOfProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.konka.workbench.R;
import com.example.konka.workbench.activity.addMember.AddMemAty;
import com.example.konka.workbench.util.ContextValue;
import com.example.konka.workbench.adapter.UserAdapter;
import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by xiaotao on 2016-9-22.
 * 项目所有成员展示画面
 */
public class MemberOfProAty extends Activity implements IMemOfProView {
    private UserAdapter adapter;//适配器
    private List<User> userList;//装入适配器的list集合
    private String projectId;//该项目的id
    private ListView listview;

    private MemOfProPresenter memOfProPresenter = new MemOfProPresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_show);
        listview = (ListView) findViewById(R.id.member_lv);
        projectId = getIntent().getStringExtra(ContextValue.PROJECT_ID);
        memOfProPresenter.initAllUser(projectId);
        initListener();
    }

    private void initListener() {
        ImageButton addMember = (ImageButton) findViewById(R.id.add_member_bn);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberOfProAty.this, AddMemAty.class);
                //传输项目已有成员的id，为后面的查询做准备
                List<String> userData = new ArrayList<String>();
                for (User user : userList) {
                    userData.add(user.getObjectId());
                }
                intent.putExtra(ContextValue.PROJECT_ID, projectId);
                intent.putExtra(ContextValue.DATA_LIST, (Serializable) userData);
                startActivityForResult(intent, ContextValue.USER_ADD);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ContextValue.USER_ADD:
                if (resultCode == RESULT_OK) {
                    List<User> addUserList = (List<User>) data.getSerializableExtra(ContextValue.DATA_LIST);//取得添加的user
                    userList.addAll(addUserList);
                    adapter.notifyDataSetChanged();//刷新视图
                    listview.setSelection(userList.size());//定位到最后一项
                }
        }
    }


    @Override
    public void showAllMember(List<User> users) {
        userList = users;
        adapter = new UserAdapter(MemberOfProAty.this, R.layout.activity_member_show_item, userList);
        listview.setAdapter(adapter);//异步更新视图
    }

    @Override
    public void showNoNetwork() {
        Toast.makeText(this, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
    }
}
