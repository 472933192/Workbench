package com.example.konka.workbench.activity.addMember;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by xiaotao on 2016-10-19.
 */
public class AddMemPresenter implements OnAddMemListener {
    private IAddMemModel addMemModel;
    private IAddMemView addMemView;

    public AddMemPresenter(IAddMemView addMemView) {
        this.addMemView = addMemView;
        this.addMemModel = new AddMemModel();
    }
    /*查找不是项目成员的所有用户*/
    public void initOtherMem() {
        addMemModel.findOtherMem(addMemView.getUserList(),this);
    }
    public void addRelation() {
        List<BmobObject> projectList = new ArrayList<BmobObject>();
        BmobRelation relation = new BmobRelation();;
        for (User user : addMemView.getSelectedUsers()) {//设置要添加的用户与项目关联起来
            Project p=new Project();
            p.setObjectId(addMemView.getProjectId());
            relation.add(user);
            p.setUserList(relation);
            projectList.add(p);
        }
        addMemModel.addRelation(projectList,this);
    }
    @Override
    public void findOtherMemSuccess(List<User> users) {
        addMemView.showOtherMem(users);
    }

    @Override
    public void addRelationSuccess() {
        addMemView.toMemOfProAty();
    }

    @Override
    public void noNetwork() {
        addMemView.showNoNetwork();
    }
}
