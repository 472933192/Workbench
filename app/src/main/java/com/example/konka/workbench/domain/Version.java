package com.example.konka.workbench.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;

/**
 * Created by HP on 2016-9-13.
 * 软件版本
 */
public class Version extends BmobObject {
    private String versionName;     //版本名
    private Project fromProject;     //软件版本所属项目
    private String state;//软件版本状态

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Project getFromProject() {
        return fromProject;
    }

    public void setFromProject(Project fromProject) {
        this.fromProject = fromProject;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
