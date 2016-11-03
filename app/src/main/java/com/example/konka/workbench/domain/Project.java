package com.example.konka.workbench.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by HP on 2016-9-13.
 * 项目类
 */
public class Project extends BmobObject implements Serializable{
    private String projectName;      //项目名
    private String type;            //机型
    private String platform;        //平台
    private String series;          //系列
    private String resolution;      //分辨率
    private String character;       //屏特性
    private String localDimming;    //动态背光技术
    private String tuner;           //Tuner
    private String demo;             //Demo
    private String port;             //端子
    private String bom_effective;   //BOM生效时间
    private String firstTestDate;   //初始下机时间
    private String sampleFinishDate;//样评完成时间
    private String midTestDate;     //中试下机时间
    private String volProDate;      //开始批量时间
    private String storageDate;     //批量入库时间
    private String demAddress;      //需求地址
    private String softwareName;    //软件名
    private String reportNumber;    //报告编号
    private String bom;             //BOM
    private String withScreen;      //配屏

    public BmobRelation getUserList() {
        return userList;
    }

    public void setUserList(BmobRelation userList) {
        this.userList = userList;
    }

    //与用户类的多对多关联
    private BmobRelation userList;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getLocalDimming() {
        return localDimming;
    }

    public void setLocalDimming(String localDimming) {
        this.localDimming = localDimming;
    }

    public String getTuner() {
        return tuner;
    }

    public void setTuner(String tuner) {
        this.tuner = tuner;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getBom_effective() {
        return bom_effective;
    }

    public void setBom_effective(String bom_effective) {
        this.bom_effective = bom_effective;
    }

    public String getFirstTestDate() {
        return firstTestDate;
    }

    public void setFirstTestDate(String firstTestDate) {
        this.firstTestDate = firstTestDate;
    }

    public String getSampleFinishDate() {
        return sampleFinishDate;
    }

    public void setSampleFinishDate(String sampleFinishDate) {
        this.sampleFinishDate = sampleFinishDate;
    }

    public String getMidTestDate() {
        return midTestDate;
    }

    public void setMidTestDate(String midTestDate) {
        this.midTestDate = midTestDate;
    }

    public String getVolProDate() {
        return volProDate;
    }

    public void setVolProDate(String volProDate) {
        this.volProDate = volProDate;
    }

    public String getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(String storageDate) {
        this.storageDate = storageDate;
    }

    public String getDemAddress() {
        return demAddress;
    }

    public void setDemAddress(String demAddress) {
        this.demAddress = demAddress;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getBom() {
        return bom;
    }

    public void setBom(String bom) {
        this.bom = bom;
    }

    public String getWithScreen() {
        return withScreen;
    }

    public void setWithScreen(String withScreen) {
        this.withScreen = withScreen;
    }

}
