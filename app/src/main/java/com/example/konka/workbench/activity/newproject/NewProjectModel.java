package com.example.konka.workbench.activity.newproject;

import android.util.Log;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;
import com.example.konka.workbench.domain.Version;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xiaotao on 2016-10-17.
 */
public class NewProjectModel implements INewProjectModel {
    @Override
    public void save(Project project, final Version version, final OnNewProjectListener projectListener) {
        project.save(new SaveListener<String>() {
            @Override
            public void done(final String projectId, BmobException e) {
                if (e == null) {
                    //与用户关联
                    User user= BmobUser.getCurrentUser(User.class);
                    BmobRelation relation = new BmobRelation();
                    relation.add(user);
                    Project p=new Project();
                    p.setObjectId(projectId);
                    p.setUserList(relation);
                    p.update(new UpdateListener() {//更新user,使其与project关联
                        @Override
                        public void done(BmobException e) {
                            final Project fromProject=new Project();
                            fromProject.setObjectId(projectId);
                            version.setFromProject(fromProject);//设置一对多关联，软件版本与项目
                            version.save(new SaveListener<String>() {
                                @Override
                                public void done(String objectId, BmobException e) {
                                    if(e==null){
                                        projectListener.createProjectSuccess(projectId);
                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }
                    });
                } else {
                    Log.i("bmob","创建项目失败："+e.getMessage()+","+e.getErrorCode());
                }
                }
        });
    }
}
