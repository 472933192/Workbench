package com.example.konka.workbench.activity.memberOfProject;

import android.os.Message;
import android.util.Log;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by xiaotao on 2016-10-19.
 */
public class MemOfProModel implements IMemOfProModel {
    @Override
    public void findAllUser(String projectId, final OnMemOfProListener memOfProListener) {
        BmobQuery<User> query = new BmobQuery<User>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        Project project = new Project();
        project.setObjectId(projectId);
        //执行查询方法
        query.addQueryKeys("username");
        query.addWhereRelatedTo("userList", new BmobPointer(project));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> users, BmobException e) {
                if (e == null) {
                    memOfProListener.findAllUserSuccess(users);
                } else {
                    if (e.getErrorCode() == 9016) {
                        memOfProListener.noNetwork();//无网络连接
                    }
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
