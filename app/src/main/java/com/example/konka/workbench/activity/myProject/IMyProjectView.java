package com.example.konka.workbench.activity.myProject;

import com.example.konka.workbench.domain.Project;

import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public interface IMyProjectView {
    /**
     * 展示我的项目列表
     * @param list
     */
    void initMyProject(List<Project> list);

    /**
     * 刷新被修改的项目的信息
     */
    void refreshProject(Project p);

    /**
     * 筛选成功，更新视图
     */
    void filterRefresh(List<Project> list);

    void showNoNetwork();
}
