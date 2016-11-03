package com.example.konka.workbench.activity.login;

/**
 * Created by ChenXiaotao on 2016-10-13.
 */
public interface IUserModel {
    /**
     * 登陆
     */
     void login(String userName, String password,OnLoginListener loginListener);

    /**
     * 注册
     */
    void register(String userName,String password,OnRegisterListener registerListener);
}
