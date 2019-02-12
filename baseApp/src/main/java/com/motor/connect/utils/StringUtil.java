package com.motor.connect.utils;

public class StringUtil {

    public static String[] getCheckSchedule(String string) {
        String[] array = string.split(" ");
        return array;
    }

    public static String getCountWorkingDay(String value) {
        String[] array = value.split(" ");
        return array[0];
    }

    public static String getScheduleWorkingOneDay(String value) {
        StringBuilder result = new StringBuilder();
        String[] array = value.split(" ");

        result.append("Lich tuoi").append("\n");

        result.append(" * Bat dau ").append(getHourMinutes(array[1])).append(" - tuoi ").append(getMinutes(array[2]));
        return result.toString();
    }

    public static String getScheduleWorkingTwoDay(String value) {
        StringBuilder result = new StringBuilder();
        String[] array = value.split(" ");

        result.append("Lich tuoi").append("\n");

        result.append(" * Bat dau ").append(getHourMinutes(array[1])).append(" - tuoi ").append(getMinutes(array[2])).append("\n");

        result.append(" * Bat dau ").append(getHourMinutes(array[3])).append(" - tuoi ").append(getMinutes(array[4]));
        return result.toString();
    }

    public static String getScheduleWorkingThreeDay(String value) {
        StringBuilder result = new StringBuilder();
        String[] array = value.split(" ");

        result.append("Lich tuoi").append("\n");

        result.append(" * Bat dau ").append(getHourMinutes(array[1])).append(" - tuoi ").append(getMinutes(array[2])).append("\n");

        result.append(" * Bat dau ").append(getHourMinutes(array[3])).append(" - tuoi ").append(getMinutes(array[4])).append("\n");

        result.append(" * Bat dau ").append(getHourMinutes(array[5])).append(" - tuoi ").append(getMinutes(array[6]));
        return result.toString();
    }

    public static String getScheduleWorking(String value) {
        StringBuilder result = new StringBuilder();
        String[] array = value.split(" ");
        result.append("Lich dang tuoi").append("\n");
        //getCountWorkingDay & check current time
        if (array[0].contentEquals(EnumHelper.ScheduleDays.ONE_DAY.getKey())) {
            result.append(" * Bat dau ").append(getHourMinutes(array[1])).append(" - tuoi ").append(getMinutes(array[2]));
            return result.toString();
        } else if (array[0].contentEquals(EnumHelper.ScheduleDays.TWO_DAY.getKey())) {
            result.append(" * Bat dau ").append(getHourMinutes(array[3])).append(" - tuoi ").append(getMinutes(array[4]));
            return result.toString();
        } else if (array[0].contentEquals(EnumHelper.ScheduleDays.THREE_DAY.getKey())) {
            result.append(" * Bat dau ").append(getHourMinutes(array[5])).append(" - tuoi ").append(getMinutes(array[6]));
            return result.toString();
        }
        return result.toString();
    }

    public static String[] getScheduleRunning(String value) {
        String[] array = value.replace("Lich dang tuoi\n", "").replace(" * ", "")
                .split(" - ");
        return array;
    }

    private static String getHourMinutes(String value) {
        StringBuilder result = new StringBuilder();
        result.append(value.substring(0, 2)).append(" gio ");
        result.append(value.substring(2, 4)).append(" phut");
        return result.toString();
    }

    private static String getMinutes(String value) {
        StringBuilder result = new StringBuilder();
        result.append(value.substring(1, 3)).append(" phut");
        return result.toString();
    }
}
