package com.motor.connect.utils;

public class EnumHelper {

    public enum ScheduleDays {
        ONE_DAY("01", "ngay tuoi 1 lan"),
        TWO_DAY("02", "ngay tuoi 2 lan"),
        THREE_DAY("03", "ngay tuoi 3 lan"),
        UNKNOWN("00", "None");

        private final String mKey;
        private final String mValue;

        ScheduleDays(String key, String value) {
            mKey = key;
            mValue = value;
        }

        public static ScheduleDays getFromValue(String key) {
            for (ScheduleDays c : ScheduleDays.values()) {
                if (c.getKey().equals(key))
                    return c;
            }
            return UNKNOWN;
        }

        public String getKey() {
            return mKey;
        }

        public String getValue() {
            return mValue;
        }
    }

    public enum ScheduleRepeats {
        ONE_DAY("01", "1 ngay tuoi 1 lan"),
        TWO_DAY("02", "2 ngay tuoi 1 lan"),
        THREE_DAY("03", "3 ngay tuoi 1 lan"),
        UNKNOWN("00", "None");

        private final String mKey;
        private final String mValue;

        ScheduleRepeats(String key, String value) {
            mKey = key;
            mValue = value;
        }

        public static ScheduleRepeats getFromValue(String key) {
            for (ScheduleRepeats c : ScheduleRepeats.values()) {
                if (c.getKey().equals(key))
                    return c;
            }
            return UNKNOWN;
        }

        public String getKey() {
            return mKey;
        }

        public String getValue() {
            return mValue;
        }
    }
}
