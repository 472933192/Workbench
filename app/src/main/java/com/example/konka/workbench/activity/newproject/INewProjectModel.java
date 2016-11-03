package com.example.konka.workbench.activity.newproject;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;

/**
 * Created by xiaotao on 2016-10-17.
 */
public interface INewProjectModel {
    /**
     * 创建新的项目
     * @param project
     */
    void save(Project project, Version version, OnNewProjectListener projectListener);

}
