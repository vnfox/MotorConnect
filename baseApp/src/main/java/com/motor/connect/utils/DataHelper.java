package com.motor.connect.utils;

import com.motor.connect.feature.model.AreaModel;
import com.orhanobut.hawk.Hawk;

import java.util.List;

public class DataHelper {

    private DataHelper() {
    }

    public static List<AreaModel> getListAreas() {
        List<AreaModel> areaModels;
        areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST);
        return areaModels;
    }

    public static void updateListArea(AreaModel model) {
        List<AreaModel> areaModels;
        areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST);
        areaModels.add(model);
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels);
    }

    public static AreaModel getArea(int position) {
        List<AreaModel> areaModels;
        areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST);
        return areaModels.get(position);
    }
}
