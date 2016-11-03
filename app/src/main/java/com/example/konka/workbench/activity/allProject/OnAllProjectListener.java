package com.example.konka.workbench.activity.allProject;

import com.example.konka.workbench.domain.Project;

import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public interface OnAllProjectListener {
    /**
     * 查询所有项目成功，则执行操作
     */
    void findAllProjectSuccess(List<Project> list);

    /**
     * 查询项目成功，则执行操作
     */
    void getProjectSuccess(Project p);
    /**
     * 筛选成功
     */
    void projectFilterSuccess(List<Project> list);

    void noNetwork();
}
