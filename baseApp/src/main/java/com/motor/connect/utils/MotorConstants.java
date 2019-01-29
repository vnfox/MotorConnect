package com.motor.connect.utils;

import com.feature.area.BuildConfig;

public class MotorConstants {

    public static final String KEY_PUT_AREA = "PUT_AREA";
    public static final String KEY_UPDATE_AREA = "UPDATE_AREA";
    public static final String KEY_PUT_AREA_LIST = "PUT_AREA_LIST";
    public static final String KEY_UPDATE = "PUT_UPDATE";
    public static final String FIRST_USED = "FIRST_USED";
    public static final String KEY_TRIGGER_DATA = "KEY_TRIGGER_DATA";

    public static final int MSG_UNCOLOR_START = 0;
    public static final int MSG_UNCOLOR_STOP = 1;
    public static final int MSG_COLOR_START = 2;
    public static final int MSG_COLOR_STOP = 3;

    public static final String MESSENGER_INTENT_KEY
            = BuildConfig.APPLICATION_ID + ".MESSENGER_INTENT_KEY";
    public static final String WORK_DURATION_KEY =
            BuildConfig.APPLICATION_ID + ".WORK_DURATION_KEY";

    // Todo remove UI Timeout
    public static final int TIMEOUT_IDLE_DURATION_IN_SECONDS = 120;
    public static final int TIMEOUT_COUNT_DOWN_30_SECONDS = 30;
    public static final int TIMEOUT_API_REQUEST_IN_SECONDS = 60;
    public static final int TIMEOUT_COUNT_DOWN_SESSION_EXPIRED_POP_UP = 15;


    public class AreaCode {
        private AreaCode() {
            // do nothing
        }

        public static final int AREA_CODE_1 = 1000;
        public static final int AREA_CODE_2 = 1001;

        public static final String PREFIX_REPEAT = "De";
        public static final String PREFIX_NONE_REPEAT = "Dn";

    }
    public class VanCode {
        private VanCode() {
            // do nothing
        }

        public static final int VAN_CODE_1 = 1000;
        public static final int VAN_CODE_2 = 1001;


    }

}
