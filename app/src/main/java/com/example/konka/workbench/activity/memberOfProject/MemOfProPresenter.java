package com.example.konka.workbench.activity.memberOfProject;

import com.example.konka.workbench.domain.User;

import java.util.List;

/**
 * Created by xiaotao on 2016-10-19.
 */
public class MemOfProPresenter implements OnMemOfProListener {
    private IMemOfProModel memOfProModel;
    private IMemOfProView memOfProView;

    public MemOfProPresenter(IMemOfProView memOfProView) {
        this.memOfProView = memOfProView;
        this.memOfProModel = new MemOfProModel();
    }

    public void initAllUser(String projectId) {
        memOfProModel.findAllUser(projectId, this);
    }

    @Override
    public void findAllUserSuccess(List<User> users) {
        memOfProView.showAllMember(users);
    }

    @Override
    public void noNetwork() {
        memOfProView.showNoNetwork();
    }
}
