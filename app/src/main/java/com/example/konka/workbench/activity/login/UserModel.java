package com.example.konka.workbench.activity.login;

import android.util.Log;

import com.example.konka.workbench.domain.User;
import com.example.konka.workbench.activity.login.IUserModel;
import com.example.konka.workbench.activity.login.OnLoginListener;
import com.example.konka.workbench.activity.login.OnRegisterListener;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ChenXiaotao on 2016-10-13.
 */
public class UserModel implements IUserModel {

    @Override
    public void login(String userName, String password, final OnLoginListener loginListener) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    loginListener.loginSuccess();//登陆成功
                } else {
                    if(e.getErrorCode()==9016){
                    loginListener.noNetwork();//无网络连接
                    }else{
                    loginListener.loginFailed();//登陆失败
                    }
                }
            }
        });
    }

    @Override
    public void register(String userName, String password, final OnRegisterListener registerListener) {
        User user=new User();//创建一个User类的对象，名称为user
        user.setUsername(userName);
        user.setPassword(password);
        user.signUp(new SaveListener<User >() {
            @Override
            public void done(User user, BmobException e){
                if(e==null ){
                    registerListener.registerSuccess(user);//注册成功
                }else{
                    if(e.getErrorCode()==9016){
                        registerListener.noNetwork();//无网络连接
                    }else {
                        registerListener.registerFailed(e);//注册失败
                    }
                }
            }
        });
    }
}
