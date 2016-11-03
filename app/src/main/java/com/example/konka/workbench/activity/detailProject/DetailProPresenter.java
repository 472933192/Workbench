package com.example.konka.workbench.activity.detailProject;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;
import com.example.konka.workbench.domain.Version;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by ChenXiaotao on 2016-10-17.
 */
public class DetailProPresenter implements OnDetailProListener {
    private IDetailProModel detailProModel;
    private IDetailProjectView detailProjectView;
    private Project mProject;
    private List<Version> mVersionList;//保存多个版本，为删除做准备
    private User mUser = BmobUser.getCurrentUser(User.class);

    public DetailProPresenter(IDetailProjectView detailProjectView) {
        this.detailProjectView = detailProjectView;
        this.detailProModel = new DetailProModel();
    }

    /**
     * 查找项目
     *
     * @param projectId
     */
    public void findProject(String projectId) {
        detailProModel.findProject(projectId, this);
    }

    /**
     * 删除项目，并删除项目关联的软件版本
     */
    public void deleteProject() {
        detailProModel.deleteProject(mProject, mVersionList, this);
    }

    public void exitProject() {
        detailProModel.exitProject(mProject, mUser, this);
    }

    /**
     * 查找项目成功，执行操作
     *
     * @param p
     */
    @Override
    public void findProjectSuccess(Project p) {
        mProject = p;
        if (!detailProjectView.getFlag()) {
            detailProjectView.initView();//初始化实例
            detailProjectView.setValues(p);//为实例赋值
            detailProModel.isProjectMember(p, mUser.getObjectId(), this);
        }
        detailProjectView.setValues(p);
    }

    @Override
    public void isProjectMemberSuccess() {
        detailProjectView.showHideButton();
    }

    public void getLastVersion(String mProjectId) {
        detailProModel.getVersions(mProjectId, this);
    }

    /**
     * 查询该项目的所有软件版本成功，则比较出最新的软件版本
     *
     * @param list
     */
    @Override
    public void getLastVersionSuccess(List<Version> list) {
        mVersionList = list;
        Version v = list.get(0);
        for (Version version : list) {
            if ((v.getVersionName().compareToIgnoreCase(version.getVersionName())) < 0) {
                v = version;
            }
        }
        /*显示最新的软件版本和状态
        保存最新软件版本*/
        detailProjectView.setLastVersion(v);
    }

    @Override
    public void deleteProjectSuccess() {
        detailProjectView.deleteProjectSuccess();
    }

    @Override
    public void exitProjectSuccess() {
        detailProjectView.exitProjectSuccess();
    }

    @Override
    public void noNetwork() {
        detailProjectView.showNoNetwork();
    }

    public Project getmProject() {
        return mProject;
    }
}
