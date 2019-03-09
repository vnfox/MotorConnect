package com.motor.connect.utils;

import com.motor.connect.feature.model.AreaModel;
import com.orhanobut.hawk.Hawk;

import java.util.List;

public class CommonUtil {

    public static Boolean checkPhoneContainArea(String phone) {
        phone = StringUtil.comparePrefixPhone(phone);
        List<AreaModel> dataArea;
        dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST);

        for (int i = 0; i < dataArea.size() - 1; i++)
            return phone.contains(dataArea.get(i).getAreaPhone());
        return false;
    }
}