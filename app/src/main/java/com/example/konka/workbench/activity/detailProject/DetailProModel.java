package com.example.konka.workbench.activity.detailProject;

import android.util.Log;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;
import com.example.konka.workbench.domain.Version;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ChenXiaotao on 2016-10-17.
 */
public class DetailProModel implements IDetailProModel {
    /**
     * 根据objectId查找项目
     *
     * @param projectId
     * @param detailProListener
     */
    @Override
    public void findProject(String projectId, final OnDetailProListener detailProListener) {
        new BmobQuery<Project>().getObject(projectId, new QueryListener<Project>() {
            @Override
            public void done(Project p, BmobException e) {
                if (e == null) {
                    detailProListener.findProjectSuccess(p);
                } else {
                    if (e.getErrorCode() == 9016) {
                        detailProListener.noNetwork();//无网络连接
                    }
                    Log.d("DeatailProModel", e.getMessage());
                }

            }
        });
    }

    /**
     * 判断是否是项目成员
     *
     * @param project
     * @param userId
     * @param detailProListener
     */
    @Override
    public void isProjectMember(final Project project, String userId, final OnDetailProListener detailProListener) {
        BmobQuery<User> innerQuery = new BmobQuery<User>();
        BmobQuery<Project> query = new BmobQuery<Project>();
        innerQuery.addWhereEqualTo("objectId", userId);//设置内部查询的条件
        query.addWhereMatchesQuery("userList", "_User", innerQuery);//添加内部查询
        query.setLimit(50);
        query.findObjects(new FindListener<Project>() {
            @Override
            public void done(List<Project> list, BmobException e) {
                if (e == null) {
                    for (Project project1 : list) {
                        if (project.getObjectId().equals(project1.getObjectId())) {//比较用户的项目中是否有该项目
                            detailProListener.isProjectMemberSuccess();//执行操作
                            break;
                        }
                    }
                } else {
                    if (e.getErrorCode() == 9016) {
                        detailProListener.noNetwork();//无网络连接
                    }
                    Log.d("项目人员判断失败", e.getMessage());
                }
            }
        });
    }

    /**
     * 根据projectId 查找该项目的所有版本
     *
     * @param projectId
     * @param detailProListener
     */
    @Override
    public void getVersions(String projectId, final OnDetailProListener detailProListener) {
        BmobQuery<Version> query = new BmobQuery<Version>();
        BmobQuery<Project> innerQuery = new BmobQuery<Project>();
        innerQuery.addWhereEqualTo("objectId", projectId);
        query.addWhereMatchesQuery("fromProject", "Project", innerQuery);
        query.findObjects(new FindListener<Version>() {
            @Override
            public void done(List<Version> list, BmobException e) {
                if (e == null) {
                    detailProListener.getLastVersionSuccess(list);
                } else {
                    if (e.getErrorCode() == 9016) {
                        detailProListener.noNetwork();//无网络连接
                    }
                    Log.d("最新软件版本显示失败", e.getMessage());
                }
            }
        });
    }

    @Override
    public void deleteProject(Project project, final List<Version> versionList, final OnDetailProListener detailProListener) {
        project.delete(project.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    BmobBatch batch = new BmobBatch();
                    List<BmobObject> bmobObjectList = new ArrayList<BmobObject>();
                    bmobObjectList.addAll(versionList);
                    batch.deleteBatch(bmobObjectList).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if (e == null) {
                                detailProListener.deleteProjectSuccess();
                            } else {
                                if (e.getErrorCode() == 9016) {
                                    detailProListener.noNetwork();//无网络连接
                                }
                                Log.d("软件版本删除失败", e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });

                } else {
                    Log.d("项目删除失败", e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void exitProject(Project project, User user, final OnDetailProListener detailProListener) {
        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        project.setUserList(relation);
        project.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    detailProListener.exitProjectSuccess();
                } else {
                    if (e.getErrorCode() == 9016) {
                        detailProListener.noNetwork();//无网络连接
                    }
                    Log.d("退出项目失败", e.getMessage());
                }
            }
        });
    }

}
