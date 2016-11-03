package com.example.konka.workbench.activity.newproject;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;

/**
 * Created by xiaotao on 2016-10-17.
 */
public class NewProjectPresenter {
    private INewProjectModel newProjectModel;
    private INewProjectView newProjectView;

    public NewProjectPresenter(INewProjectView newProjectView) {
        this.newProjectView = newProjectView;
        this.newProjectModel=new NewProjectModel();
    }
    /**
     * 创建项目
     */
    public void save(Project project,Version version){
        newProjectModel.save(project,version,new OnNewProjectListener() {
            @Override
            public void createProjectSuccess(String projectId) {
                newProjectView.createProjectSuccess(projectId);
            }
        });
    }
}
