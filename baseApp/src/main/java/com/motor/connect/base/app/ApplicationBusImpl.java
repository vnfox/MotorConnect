package com.motor.connect.base.app;

import com.motor.connect.feature.model.AreaModel;

import java.util.List;

public class ApplicationBusImpl implements ApplicationBus {

    private List<AreaModel> areaModels;
    private AreaModel model;

    @Override
    public boolean isInit() {
        return false;
    }

    @Override
    public void setListAreas(List<AreaModel> areaModels) {
        this.areaModels = areaModels;
    }

    @Override
    public List<AreaModel> getListAreas() {
        return areaModels;
    }

    @Override
    public void setArea(AreaModel area) {
        this.model = area;
    }

    @Override
    public AreaModel getArea() {
        return model;
    }
}