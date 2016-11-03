package com.example.konka.workbench.activity.addMember;

import com.example.konka.workbench.domain.User;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by xiaotao on 2016-10-19.
 */
public interface IAddMemModel {
    /**
     * 查找不与该项目存在关联关系的用户
     */
    void findOtherMem(List<User> userList,OnAddMemListener addMemListener);

    /**
     * 批量添加用户为该项目成员
     * @param projectList
     * @param addMemListener
     */
    void addRelation(List<BmobObject> projectList, OnAddMemListener addMemListener);
}
