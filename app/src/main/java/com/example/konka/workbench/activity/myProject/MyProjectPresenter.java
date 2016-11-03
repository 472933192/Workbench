package com.example.konka.workbench.activity.myProject;

import com.example.konka.workbench.domain.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public class MyProjectPresenter implements OnMyProjectListener {
    private IMyProjectModel myProjectModel;
    private IMyProjectView myProjectView;

    public MyProjectPresenter(IMyProjectView myProjectView) {
        this.myProjectView = myProjectView;
        this.myProjectModel = new MyProjectModel();
    }

    public void findMyProjects() {
        myProjectModel.findMyProject(this);
    }

    public void projectFilter(List<ArrayList<String>> selectedData) {
        myProjectModel.findProjectCondition(selectedData,this);
    }
    @Override
    public void findMyProjectSuccess(List<Project> list) {
        myProjectView.initMyProject(list);
    }

    @Override
    public void getProjectSuccess(Project p) {
        myProjectView.refreshProject(p);
    }

    @Override
    public void projectFilterSuccess(List<Project> list) {
        myProjectView.filterRefresh(list);
    }

    @Override
    public void noNetwork() {
        myProjectView.showNoNetwork();
    }

    public void getProject(String projectId) {
        myProjectModel.getProject(projectId, this);
    }

}
