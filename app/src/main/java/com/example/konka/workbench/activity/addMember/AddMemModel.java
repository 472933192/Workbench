package com.example.konka.workbench.activity.addMember;

import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.User;
import com.example.konka.workbench.util.ContextValue;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

/**
 * Created by xiaotao on 2016-10-19.
 */
public class AddMemModel implements IAddMemModel {

    @Override
    public void findOtherMem(List<User> userList, final OnAddMemListener addMemListener) {
        BmobQuery<User > query = new BmobQuery<User>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);//执行查询方法
        query.addQueryKeys("objectId,username");
        query.addWhereNotContainedIn("objectId",userList);
        //查询不在该项目的所有成员
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> users, BmobException e) {
                if(e==null){
                    addMemListener.findOtherMemSuccess(users);
                }else{
                    if (e.getErrorCode() == 9016) {
                        addMemListener.noNetwork();//无网络连接
                    }
                    Log.i("查找用户失败：",e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void addRelation(List<BmobObject> projectList, final OnAddMemListener addMemListener) {
        new BmobBatch().updateBatch(projectList).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    for(int i=0;i<list.size();i++){
                        BatchResult result = list.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                            Log.d("第"+i+"个数据批量更新成功",i+"");
                        }else{
                            Log.d("第"+i+"个数据批量更新失败：",ex.getMessage()+","+ex.getErrorCode());
                        }
                    }
                    addMemListener.addRelationSuccess();
                }else{
                    if (e.getErrorCode() == 9016) {
                        addMemListener.noNetwork();//无网络连接
                    }
                    Log.d("添加项目成员失败", e.getMessage());
                }
            }
        });
    }
}
