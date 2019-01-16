package com.motor.connect.utils;

public class MotorConstants {

    public static final String KEY_PUT_AREA = "PUT_AREA";
    public static final String KEY_PUT_AREA_LIST = "PUT_AREA_LIST";


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

    }

    public class VanCode {
        private VanCode() {
            // do nothing
        }

        public static final int VAN_CODE_1 = 1000;
        public static final int VAN_CODE_2 = 1001;


    }

}
