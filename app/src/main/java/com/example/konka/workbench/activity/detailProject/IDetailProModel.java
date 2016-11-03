package com.example.konka.workbench.activity.detailProject;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;
import com.example.konka.workbench.domain.Version;

import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-17.
 */
public interface IDetailProModel {
    /**
     * 查询项目内容
     */
    void findProject(String projectId,OnDetailProListener detailProListener);

    /**
     * 查询该项目与用户是否有关联关系
     * @param project
     * @param userId
     * @param detailProListener
     */
    void isProjectMember(Project project,String userId,OnDetailProListener detailProListener);

    /**
     * 取得项目的所有软件版本
     * @param projectId
     * @param detailProListener 处理操作结果
     */
    void getVersions(String projectId,OnDetailProListener detailProListener);

    /**
     * 删除该项目
     * @param p
     * @param versionList
     * @param detailProListener
     */
    void deleteProject(Project p, List<Version> versionList, OnDetailProListener detailProListener);

    /**
     * 删除用户与该项目的关联关系
     * @param p
     * @param user
     * @param detailProListener
     */
    void exitProject(Project p, User user,OnDetailProListener detailProListener);
}
