package com.example.konka.workbench.activity.login;

import com.example.konka.workbench.domain.User;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by ChenXiaotao on 2016-10-13.
 */
public interface OnRegisterListener {
    /**
     * 注册成功
     */
    void registerSuccess(User user);

    /**
     * 注册失败
     */
    void registerFailed(BmobException e);

    /**
     * 无网络连接
     */
    void noNetwork();
}
