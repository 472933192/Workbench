package com.example.konka.workbench.activity.addMember;

import com.example.konka.workbench.domain.User;

import java.util.List;

/**
 * Created by xiaotao on 2016-10-19.
 */
public interface OnAddMemListener {
    /**
     * 查找非项目成员的所有用户成功，则执行操作
     * @param users
     */
    void findOtherMemSuccess(List<User> users);

    /**
     * 添加项目成员成功，则执行操作
     */
    void addRelationSuccess();

    void noNetwork();
}
