package com.example.konka.workbench.activity.memberOfProject;

import com.example.konka.workbench.domain.User;

import java.util.List;

/**
 * Created by xiaotao on 2016-10-19.
 */
public interface OnMemOfProListener {
    /**
     * 查询项目所有成员成功，则执行操作
     * @param users
     */
    void findAllUserSuccess(List<User> users);

    void noNetwork();
}
