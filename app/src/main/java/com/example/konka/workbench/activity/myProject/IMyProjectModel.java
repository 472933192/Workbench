package com.example.konka.workbench.activity.myProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public interface IMyProjectModel {
    /**
     * 查询我的项目
     */
    void findMyProject(OnMyProjectListener myProjectListener);

    /**
     * 查询项目
     * @param projectId
     * @param myProjectListener
     */
    void getProject(String projectId, OnMyProjectListener myProjectListener);

    /**
     *条件查询，查询包含该子项的数据
     * list存储顺序
     * 0：机型，1：平台，2：bom，3:配屏
     */
    void findProjectCondition(List<ArrayList<String>> selectedData, OnMyProjectListener myProjectListener);
}
