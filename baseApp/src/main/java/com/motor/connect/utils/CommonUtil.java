package com.motor.connect.utils;

import com.motor.connect.feature.model.AreaModel;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommonUtil {

    public static Boolean checkPhoneContainArea(String phone) {
        phone = StringUtil.comparePrefixPhone(phone);
        List<AreaModel> dataArea;
        dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST);

        for (int i = 0; i < dataArea.size(); i++)
            return phone.contains(dataArea.get(i).getAreaPhone());
        return false;
    }
}