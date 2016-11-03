package com.example.konka.workbench.activity.detailProject;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;

/**
 * Created by ChenXiaotao on 2016-10-17.
 */
public interface IDetailProjectView {
    /**
     * 实例化控件
     */
    void initView();

    /**
     * 为控件赋值
     */
    void setValues(Project project);

    /**
     * 显示隐藏的按钮，用于操作项目
     */
    void showHideButton();

    /**
     *  //显示最新的软件版本和状态
     */
    void setLastVersion(Version version);

    /**
     * 取得flag，用于判断控件是否已实例化
     */
    boolean getFlag();

    /**
     * 删除项目成功，弹出提示
     */
    void deleteProjectSuccess();
    /**
     *退出项目成功，弹出提示
     */
    void exitProjectSuccess();

    void showNoNetwork();
}
