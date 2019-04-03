package com.motor.connect.feature.model;

import java.util.List;

public class AreaModel {

    private String areaId;
    private String areaName;
    private String areaType;
    private String areaPhone;
    private String areaDetails;
    private String areaSchedule;
    private String areaStatus = "Chưa hoạt động";
    private List<VanModel> areaVans;
    private List<ScheduleModel> schedule;
    private String scheduleRepeat;
    private String password = "1234";
    private Boolean isAgenda = false;


    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getAreaPhone() {
        return areaPhone;
    }

    public void setAreaPhone(String areaPhone) {
        this.areaPhone = areaPhone;
    }

    public String getAreaDetails() {
        return areaDetails;
    }

    public void setAreaDetails(String areaDetails) {
        this.areaDetails = areaDetails;
    }

    public String getAreaSchedule() {
        return areaSchedule;
    }

    public void setAreaSchedule(String areaSchedule) {
        this.areaSchedule = areaSchedule;
    }

    public String getAreaStatus() {
        return areaStatus;
    }

    public void setAreaStatus(String areaStatus) {
        this.areaStatus = areaStatus;
    }

    public List<VanModel> getAreaVans() {
        return areaVans;
    }

    public void setAreaVans(List<VanModel> areaVans) {
        this.areaVans = areaVans;
    }

    public String getScheduleRepeat() {
        return scheduleRepeat;
    }

    public void setScheduleRepeat(String scheduleRepeat) {
        this.scheduleRepeat = scheduleRepeat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ScheduleModel> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleModel> schedule) {
        this.schedule = schedule;
    }

    public Boolean getAgenda() {
        return isAgenda;
    }

    public void setAgenda(Boolean agenda) {
        isAgenda = agenda;
    }
}
