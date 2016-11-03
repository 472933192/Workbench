package com.example.konka.workbench.activity.allProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXiaotao on 2016-10-20.
 */
public interface IAllProjectModel {
    /**
     * 查找所有项目
     */
    void findAllProject(OnAllProjectListener onAllProjectListener);

    /**
     * 查找项目
     */
    void getProject(String projectId, OnAllProjectListener allProjectListener);
    /**
     *条件查询，查询包含该子项的数据
     * list存储顺序
     * 0：机型，1：平台，2：bom，3:配屏
     */
    void findProjectCondition(List<ArrayList<String>> selectedData,OnAllProjectListener allProjectListener);
}
