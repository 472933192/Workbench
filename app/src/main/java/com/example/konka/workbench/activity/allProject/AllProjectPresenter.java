package com.example.konka.workbench.activity.allProject;

import com.example.konka.workbench.domain.Project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public class AllProjectPresenter implements OnAllProjectListener {
    private IAllProjectModel allProjectModel;
    private IAllProjectView allProjectView;

    public AllProjectPresenter(IAllProjectView allProjectView) {
        this.allProjectView = allProjectView;
        this.allProjectModel = new AllProjectModel();
    }

    public void findAllProject() {
        allProjectModel.findAllProject(this);
    }
    public void getProject(String projectId) {
        allProjectModel.getProject(projectId, this);
    }
    /*根据选中筛选项，查询数据*/
    public void projectFilter(List<ArrayList<String>> selectedData) {
        allProjectModel.findProjectCondition(selectedData,this);
    }
    @Override
    public void findAllProjectSuccess(List<Project> list) {
        allProjectView.initProjects(list);
    }

    @Override
    public void getProjectSuccess(Project p) {
        allProjectView.refreshProject(p);
    }

    @Override
    public void projectFilterSuccess(List<Project> list) {
        allProjectView.filterRefresh(list);
    }

    @Override
    public void noNetwork() {
        allProjectView.showNoNetwork();
    }

}
