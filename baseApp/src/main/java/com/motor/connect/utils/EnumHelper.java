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

   public enum  DayOfWeek {
       Monday("01", 1),
       Tuesday("02", 2),
       Wednesday("03", 4),
       Thursday("04", 8),
       Friday("05", 16),
       Saturday("06", 32),
       Sunday("07", 64),
       UNKNOWN("00", 0);

       DayOfWeek(String key, int value) {
           mKey = key;
           mValue = value;
       }

       private final String mKey;
       private final int mValue;

       public String getKey() {
           return mKey;
       }

       public int getValue() {
           return mValue;
       }

       public static DayOfWeek getFromValue(String key) {
           for (DayOfWeek c : DayOfWeek.values()) {
               if (c.getKey().equals(key))
                   return c;
           }
           return UNKNOWN;
       }
    }

    public enum  ValveValue {
        Valve1("01", 1),
        Valve2("02", 2),
        Valve3("03", 4),
        Valve4("04", 8),
        Valve5("05", 16),
        Valve6("06", 32),
        Valve7("07", 64),
        Valve8("08", 128),
        UNKNOWN("00", 0);

        ValveValue(String key, int value) {
            mKey = key;
            mValue = value;
        }

        private final String mKey;
        private final int mValue;

        public String getKey() {
            return mKey;
        }

        public int getValue() {
            return mValue;
        }

        public static ValveValue getFromValue(String key) {
            for (ValveValue c : ValveValue.values()) {
                if (c.getKey().equals(key))
                    return c;
            }
            return UNKNOWN;
        }
    }
}
