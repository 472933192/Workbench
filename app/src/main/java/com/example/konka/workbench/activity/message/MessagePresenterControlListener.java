package com.example.konka.workbench.activity.message;

/**
 * 消息服务操作监听器
 * 在Service中使用，用于监听ATY对Service的一些操作
 * Created by HP on 2016-10-20.
 */
public interface MessagePresenterControlListener {
    public abstract void onChangeStatue(String statue);
}
