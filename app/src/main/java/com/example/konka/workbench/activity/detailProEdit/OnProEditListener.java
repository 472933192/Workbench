package com.example.konka.workbench.activity.detailProEdit;

/**
 * Created by xiaotao on 2016-10-18.
 */
public interface OnProEditListener {
    /**
     * 软件更新成功
     */
    void updateProjectSuccess();

    /**
     * 添加新的软件版本成功
     */
    void addNewVersionSuccess();

    /**
     * 更新软件版本信息成功
     */
    void updateVersionSuccess();

    void noNetwork();
}
