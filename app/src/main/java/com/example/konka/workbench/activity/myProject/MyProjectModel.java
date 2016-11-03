package com.example.konka.workbench.activity.myProject;

import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public class MyProjectModel implements IMyProjectModel {
    @Override
    public void findMyProject(final OnMyProjectListener myProjectListener) {
        BmobQuery<Project> query = new BmobQuery<Project>();
        BmobQuery<User> innerQuery = new BmobQuery<User>();
        User user = BmobUser.getCurrentUser(User.class);//取得当前用户
        innerQuery.addWhereEqualTo("objectId", user.getObjectId());
        query.addWhereMatchesQuery("userList", "_User", innerQuery);
        query.setLimit(50);//如果不加上这条语句，默认返回10条数据
        query.order("-updatedAt");
        query.findObjects(new FindListener<Project>() {
            @Override
            public void done(List<Project> list, BmobException e) {
                if (e == null) {
                    myProjectListener.findMyProjectSuccess(list);
                } else {
                    if (e.getErrorCode() == 9016) {
                        myProjectListener.noNetwork();//无网络连接
                    } else {
                        Log.d("bmob", "查询我的项目失败");
                    }
                }
            }
        });
    }

    @Override
    public void getProject(String projectId, final OnMyProjectListener myProjectListener) {
        new BmobQuery<Project>().getObject(projectId, new QueryListener<Project>() {
            @Override
            public void done(Project p, BmobException e) {
                if (e == null) {
                    myProjectListener.getProjectSuccess(p);
                } else if (e.getErrorCode() == 9016) {
                    myProjectListener.noNetwork();//无网络连接
                }
            }
        });
    }

    @Override
    public void findProjectCondition(List<ArrayList<String>> selectedData, final OnMyProjectListener myProjectListener) {
        BmobQuery<Project> query = new BmobQuery<>();
        List<BmobQuery<Project>> queries = new ArrayList<>();

        if (selectedData.get(0).size() > 0) {
            queries.add(new BmobQuery<Project>().addWhereContainedIn("type", selectedData.get(0)));
        }
        if (selectedData.get(1).size() > 0) {
            queries.add(new BmobQuery<Project>().addWhereContainedIn("platform", selectedData.get(1)));
        }
        if (selectedData.get(2).size() > 0) {
            queries.add(new BmobQuery<Project>().addWhereContainedIn("bom", selectedData.get(2)));
        }
        if (selectedData.get(3).size() > 0) {
            queries.add(new BmobQuery<Project>().addWhereContainedIn("withScreen", selectedData.get(3)));
        }
        if (queries.size() != 0) {
            query.and(queries);
        }
        BmobQuery<User> innerQuery = new BmobQuery<User>();
        User user = BmobUser.getCurrentUser(User.class);//取得当前用户
        innerQuery.addWhereEqualTo("objectId", user.getObjectId());
        query.addWhereMatchesQuery("userList", "_User", innerQuery);
        query.setLimit(50);//如果不加上这条语句，默认返回10条数据
        query.findObjects(new FindListener<Project>() {
            @Override
            public void done(List<Project> list, BmobException e) {
                if (e == null) {
                    Log.d("AllProjectModel", "条件查询成功" + list.size());
                    myProjectListener.projectFilterSuccess(list);
                } else if (e.getErrorCode() == 9016) {
                    myProjectListener.noNetwork();//无网络连接
                }
            }
        });
    }
}
