package com.example.konka.workbench.activity.detailProject;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;

import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-17.
 */
public interface OnDetailProListener {
    /**
     * 如果查询项目内容成功，则执行该操作
     * @param p
     */
    void findProjectSuccess(Project p);

    /**
     * 如果用户是项目成员，则执行该操作
     */
    void isProjectMemberSuccess();

    /**
     * 获取项目最新软件版本成功，则
     * @param list
     */
    void getLastVersionSuccess(List<Version> list);

    /**
     * 删除项目成功，则
     */
    void deleteProjectSuccess();

    /**
     * 退出项目成功，则
     */
    void exitProjectSuccess();

    void noNetwork();
}
