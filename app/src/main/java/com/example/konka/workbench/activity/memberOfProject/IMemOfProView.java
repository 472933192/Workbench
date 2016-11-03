package com.example.konka.workbench.activity.memberOfProject;

import com.example.konka.workbench.domain.User;

import java.util.List;

/**
 * Created by xiaotao on 2016-10-19.
 */
public interface IMemOfProView {
    /**
     * 展示项目的所有成员
     * @param users
     */
    void showAllMember(List<User> users);

    void showNoNetwork();
}
