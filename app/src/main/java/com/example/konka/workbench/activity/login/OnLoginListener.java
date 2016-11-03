package com.example.konka.workbench.activity.login;

/**
 * Created by ChenXiaotao on 2016-10-13.
 * 操作的结果，对应的反馈
 */
public interface OnLoginListener {
    /**
     * 登陆成功
     */
    void loginSuccess();

    /**
     * 登陆失败
     */
    void loginFailed();

    /**
     * 无网络连接
     */
    void noNetwork();
}
