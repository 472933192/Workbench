package com.example.konka.workbench.activity.detailProEdit;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;

/**
 * Created by xiaotao on 2016-10-18.
 */
public interface IDetailProEditView {
    /**
     * 取得当前的软件版本名
     * @return String
     */
    String getNewVersionName();

    /**
     * 取得当前的软件状态
     * @return String
     */
    String getState();

    /**
     * 取得未修改前项目最新软件版本
     * @return Version
     */
    Version getLastVersion();

    /**
     * 取得项目信息
     * @return
     */
    Project getProject();
/*
    *//**
     * 弹出添加新的软件版本的信息
     *//*
    void showAddNewVersionMsg();

    *//**
     * 弹出软件版本更新成功的信息
     *//*
    void updateVersionSuccess();*/

    /**
     * 弹出项目信息更新成功的信息
     */
    void updateProjectSuccess();

    void showNoNetwork();
}
