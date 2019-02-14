package com.motor.connect.utils;

import com.feature.area.BuildConfig;

public class MotorConstants {

    public static final String KEY_PUT_AREA_DETAIL = "PUT_AREA";
    public static final String KEY_PUT_AREA_LIST = "PUT_AREA_LIST";
    public static final String KEY_EDIT_AREA = "EDIT_DATA_AREA";
    public static final String KEY_POSITION = "KEY_POSITION";
    public static final String FIRST_USED = "FIRST_USED";
    public static final String KEY_TRIGGER_DATA = "KEY_TRIGGER_DATA";
    public static final String KEY_VANS_USED = "KEY_VANS_USED";

    public static final int MSG_UNCOLOR_START = 0;
    public static final int MSG_UNCOLOR_STOP = 1;
    public static final int MSG_COLOR_START = 2;
    public static final int MSG_COLOR_STOP = 3;

    public static final String MESSENGER_INTENT_KEY
            = BuildConfig.APPLICATION_ID + ".MESSENGER_INTENT_KEY";
    public static final String WORK_DURATION_KEY =
            BuildConfig.APPLICATION_ID + ".WORK_DURATION_KEY";

    public static final int PERMISSION_REQUEST_CODE = 101;


    public class AreaCode {
        private AreaCode() {
            // do nothing
        }


        public static final String PREFIX_REPEAT = "De";
        public static final String PREFIX_NONE_REPEAT = "Dn";
        public static final String PREFIX_STOP = "Do";
        public static final String PREFIX_CHANGE_PASSWORD = "Pass";
        public static final String PREFIX_SET_DEFAULT = "Re";
        public static final String PREFIX_REVIEW_SCHEDULER = "Dr";
        public static final String PREFIX_AUTO_SEND_SCHEDULER = "Ds";
        public static final String PREFIX_VAN_USED = "Dv";
        public static final String PREFIX_OPEN = "Co";
        public static final String STOP_ALL = "00";

    }

    public class VanCode {
        private VanCode() {
            // do nothing
        }


        public static final int VAN_CODE_1 = 1000;
        public static final int VAN_CODE_2 = 1001;


    }

}
