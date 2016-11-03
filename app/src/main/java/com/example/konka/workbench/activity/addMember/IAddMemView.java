package com.example.konka.workbench.activity.addMember;

import com.example.konka.workbench.domain.User;

import java.util.List;

/**
 * Created by xiaotao on 2016-10-19.
 */
public interface IAddMemView {
    /**
     * 显示非项目成员的所有用户
     */
    void showOtherMem(List<User> users);

    /**
     * 取得项目所有成员
     */
    List<User> getUserList();
    /**
     * 取得被选中的所有用户
     */
    List<User> getSelectedUsers();

    /**
     * 取得项目id
     */
    String getProjectId();

    /**
     * 添加项目成员成功，返回到上一个活动
     */
    void toMemOfProAty();

    void showNoNetwork();
}
