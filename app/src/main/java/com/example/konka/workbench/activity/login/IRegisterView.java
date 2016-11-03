package com.example.konka.workbench.activity.login;

import com.example.konka.workbench.domain.User;

import cn.bmob.v3.exception.BmobException;

/**
 * @author ChenXiaotao
 */
public interface IRegisterView {
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
     * 取得用户输入的确认密码
     * @return
     */
    String getConfigPassword();

    /**
     * 弹出注册成功信息，返回到登陆界面
     * @param user
     */
    void toLoginActivity(User user);

    /**
     * 弹出注册失败的信息
     * @param e
     */
    void showFailedError(BmobException e);
    /*弹出无网络连接的信息*/
    void showNoNetwork();
}
