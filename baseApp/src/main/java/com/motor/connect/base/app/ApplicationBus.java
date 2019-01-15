package com.motor.connect.base.app;

import com.motor.connect.feature.model.AreaModel;

import java.util.List;

public interface ApplicationBus {

    boolean isInit();

    void setListAreas(List<AreaModel> areaModels);

    List<AreaModel> getListAreas();


    void setArea(AreaModel area);

    AreaModel getArea();
}
