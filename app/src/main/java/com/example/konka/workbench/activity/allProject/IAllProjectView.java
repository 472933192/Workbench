package com.example.konka.workbench.activity.allProject;

import com.example.konka.workbench.domain.Project;

import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public interface IAllProjectView {
    /**
     * 显示所有项目
     */
    void initProjects(List<Project> list);

    /**
     * 在项目列表里更新修改的项目信息
     *
     * @param p
     */
    void refreshProject(Project p);

    /**
     * 筛选成功，更新视图
     */
    void filterRefresh(List<Project> list);

    void showNoNetwork();
}
