package com.example.konka.workbench.activity.detailProEdit;


import android.util.Log;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by xiaotao on 2016-10-18.
 *
 */
public class DetailProEditModel implements IDetailProEditModel {
    @Override
    public void update(Project p, final OnProEditListener proEditListener) {
        p.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    proEditListener.updateProjectSuccess();
                } else {
                    if (e.getErrorCode() == 9016) {
                        proEditListener.noNetwork();//无网络连接
                    }
                    Log.d("DetailProEditModel", "项目更新失败！");
                }
            }
        });
    }

    @Override
    public void addNewVersion(Version v, final OnProEditListener proEditListener) {
        v.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    proEditListener.addNewVersionSuccess();
                } else {
                    if (e.getErrorCode() == 9016) {
                        proEditListener.noNetwork();//无网络连接
                    }
                }
            }
        });
    }

    @Override
    public void updateVesrion(Version v, final OnProEditListener proEditListener) {
        v.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null) {
               proEditListener.updateVersionSuccess();
                }else if (e.getErrorCode() == 9016) {
                        proEditListener.noNetwork();//无网络连接
                    }
            }
        });
    }
}
