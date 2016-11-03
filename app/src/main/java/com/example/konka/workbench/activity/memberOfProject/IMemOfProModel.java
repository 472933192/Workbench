package com.example.konka.workbench.activity.memberOfProject;

import com.example.konka.workbench.activity.addMember.OnAddMemListener;

/**
 * Created by HP on 2016-10-19.
 */
public interface IMemOfProModel {
    /**
     * 查询该项目的所有成员
     */
    void findAllUser(String projectId, OnMemOfProListener memOfProListener);
}
