package com.example.konka.workbench.activity.login;

import com.example.konka.workbench.domain.User;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by ChenXiaotao on 2016-10-13.
 */
public class UserPresenter {
    private IUserModel userModel;
    private ILoginView loginView;
    private IRegisterView registerView;

    public UserPresenter(ILoginView loginView) {
        this.loginView = loginView;
        this.userModel=new UserModel();
    }

    public UserPresenter(IRegisterView registerView) {
        this.registerView = registerView;
        this.userModel=new UserModel();
    }

    /**
     * 登陆
     */
    public void login(){
        userModel.login(loginView.getUserName(), loginView.getPassword(), new OnLoginListener() {
            @Override
            public void loginSuccess() {
                loginView.toMainActivity();
            }

            @Override
            public void loginFailed() {
                loginView.showFailedError();
            }

            @Override
            public void noNetwork() {
                loginView.showNoNetwork();
            }
        });
    }

    /**
     * 注册
     */
    public void register() {
        userModel.register(registerView.getUserName(), registerView.getPassword(), new OnRegisterListener() {
            @Override
            public void registerSuccess(User user) {
                registerView.toLoginActivity(user);
            }

            @Override
            public void registerFailed(BmobException e) {
                registerView.showFailedError(e);
            }

            @Override
            public void noNetwork() {
                registerView.showNoNetwork();
            }
        });
    }
}
