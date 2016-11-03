package com.example.konka.workbench.activity.newproject;

import com.example.konka.workbench.domain.Project;

/**
 * Created by xiaotao on 2016-10-17.
 */
public interface INewProjectView {
    /**
     * 创建项目成功，返回到上一个活动
     */
    void createProjectSuccess(String projectId);
}
