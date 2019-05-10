package com.motor.connect.feature.model;

import java.util.ArrayList;
import java.util.List;

public class VanModel {

    private String vanId;
    private Boolean vanStatus;
    private List<String> schedule = new ArrayList<>();
    private String duration;
    private Boolean isManual = false;
    private RepeatModel repeatModel = new RepeatModel();


    public String getVanId() {
        return vanId;
    }

    public void setVanId(String vanId) {
        this.vanId = vanId;
    }

    public Boolean getVanStatus() {
        return vanStatus;
    }

    public void setVanStatus(Boolean vanStatus) {
        this.vanStatus = vanStatus;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<String> schedule) {
        this.schedule = schedule;
    }

    public Boolean getManual() {
        return isManual;
    }

    public void setManual(Boolean manual) {
        isManual = manual;
    }

    public RepeatModel getRepeatModel() {
        return repeatModel;
    }

    public void setRepeatModel(RepeatModel repeatModel) {
        this.repeatModel = repeatModel;
    }
}