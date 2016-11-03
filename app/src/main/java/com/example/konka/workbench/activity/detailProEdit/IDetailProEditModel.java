package com.example.konka.workbench.activity.detailProEdit;

import com.example.konka.workbench.domain.Project;
import com.example.konka.workbench.domain.Version;

/**
 * @author ChenXiaotao
 *
 */
public interface IDetailProEditModel {
    /**
     * 更新项目
     * @param p
     * @param proEidtListener
     */
    void update(Project p, OnProEditListener proEidtListener);

    /**
     * 添加新的软件版本
     * @param v
     * @param proEidtListener
     */
    void addNewVersion(Version v, OnProEditListener proEidtListener);

    /**
     * 更新软件版本的状态
     * @param v
     * @param proEidtListener
     */
    void updateVesrion(Version v, OnProEditListener proEidtListener);
}
