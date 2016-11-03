package com.example.konka.workbench.activity.myProject;

import com.example.konka.workbench.domain.Project;

import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public interface OnMyProjectListener {
    /**
     * 查询我的项目成功，则执行操作
     */
    void findMyProjectSuccess(List<Project> list);

    /**
     * 查询项目成功
     */
    void getProjectSuccess(Project p);

    /**
     * 筛选成功
     * @param list
     */
    void projectFilterSuccess(List<Project> list);

    void noNetwork();
}
