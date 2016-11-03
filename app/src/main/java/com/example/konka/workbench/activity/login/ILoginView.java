package com.example.konka.workbench.activity.login;

/**
 * @author Chenxiaotao
 */
public interface ILoginView {
    /**
     * 取得用户输入的用户名
     * @return
     */
    String getUserName();

    /**
     * 取得用户输入的密码
     * @return
     */
    String getPassword();

    /**
     * 弹出登陆成功的信息，跳转到主页面
     */
    void toMainActivity();

    /**
     * 弹出登陆失败的信息
     */
    void showFailedError();

    /**
     * 弹窗提示无网络连接
     */
    void showNoNetwork();
}
