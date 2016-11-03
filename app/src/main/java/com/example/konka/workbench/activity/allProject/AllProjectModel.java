package com.example.konka.workbench.activity.allProject;

import android.util.Log;

import com.example.konka.workbench.domain.Project;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public class AllProjectModel implements IAllProjectModel {
    @Override
    public void findAllProject(final OnAllProjectListener allProjectListener) {
        BmobQuery<Project> query = new BmobQuery<Project>();
        query.setLimit(50);//如果不加上这条语句，默认返回10条数据
        query.order("-updatedAt");
        query.findObjects(new FindListener<Project>() {
            @Override
            public void done(List<Project> list, BmobException e) {
                if (e == null) {
                    allProjectListener.findAllProjectSuccess(list);
                } else {
                    if (e.getErrorCode() == 9016) {
                        allProjectListener.noNetwork();//无网络连接
                    }
                    Log.d("查询所有项目失败", e.getMessage());
                }
            }
        });
    }

    @Override
    public void getProject(String projectId, final OnAllProjectListener allProjectListener) {
        new BmobQuery<Project>().getObject(projectId, new QueryListener<Project>() {
            @Override
            public void done(Project p, BmobException e) {
                if (e == null) {
                    allProjectListener.getProjectSuccess(p);
                } else if (e.getErrorCode() == 9016) {
                    allProjectListener.noNetwork();//无网络连接
                }
            }
        });
    }

    /**
     * 条件查询，查询包含该子项的数据
     * list存储顺序
     * 0：机型，1：平台，2：bom，3:配屏
     */
    @Override
    public void findProjectCondition(List<ArrayList<String>> selectedData, final OnAllProjectListener allProjectListener) {
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
        query.setLimit(50);
        query.findObjects(new FindListener<Project>() {
            @Override
            public void done(List<Project> list, BmobException e) {
                if (e == null) {
                    Log.d("AllProjectModel", "条件查询成功" + list.size());
                    allProjectListener.projectFilterSuccess(list);
                } else if (e.getErrorCode() == 9016) {
                    allProjectListener.noNetwork();//无网络连接
                }
            }
        });
    }
}
