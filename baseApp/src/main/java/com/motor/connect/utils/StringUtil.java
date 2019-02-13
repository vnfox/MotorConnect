package com.motor.connect.utils;

import org.jetbrains.annotations.NotNull;

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

    public static String getScheduleRepeatDay(String value) {
        StringBuilder result = new StringBuilder();
        String[] array = value.split(" ");

        if (array[array.length - 1].length() == 2) {
            result.append("Tuới ");
            result.append(array[array.length - 1]).append(" ngày/lần");
            return result.toString();
        }
        return "Lịch tưới trong ngày";
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

    //smsContent = selectSchedule + " " + txt_time1_start.text + " " + txt_time1_run.text + " " + repeat

    public static String getSmsContentScheduleOneDays(String scheduleDay, String time1, String time1_run, String repeat) {
        String[] run = time1_run.split(" ");

        StringBuilder result = new StringBuilder();
        result.append(scheduleDay).append(" ");
        result.append(time1.replace(":", "")).append(" ");
        result.append(run[0]).append(" ");
        result.append(repeat);

        return result.toString().trim();
    }

    public static String getSmsContentScheduleTowDays(String scheduleDay, String time1, String time1_run,
                                                      String time2, String time2_run, String repeat) {
        String[] run1 = time1_run.split(" ");
        String[] run2 = time2_run.split(" ");

        StringBuilder result = new StringBuilder();
        result.append(scheduleDay).append(" ");

        result.append(time1.replace(":", "")).append(" ");
        result.append(run1[0]).append(" ");

        result.append(time2.replace(":", "")).append(" ");
        result.append(run2[0]).append(" ");

        result.append(repeat);

        return result.toString().trim();
    }

    public static String getSmsContentScheduleThreeDays(String scheduleDay, String time1, String time1_run,
                                                        String time2, String time2_run,
                                                        String time3, String time3_run, String repeat) {
        String[] run1 = time1_run.split(" ");
        String[] run2 = time2_run.split(" ");
        String[] run3 = time3_run.split(" ");


        StringBuilder result = new StringBuilder();
        result.append(scheduleDay).append(" ");

        result.append(time1.replace(":", "")).append(" ");
        result.append(run1[0]).append(" ");

        result.append(time2.replace(":", "")).append(" ");
        result.append(run2[0]).append(" ");

        result.append(time3.replace(":", "")).append(" ");
        result.append(run3[0]).append(" ");

        result.append(repeat);

        return result.toString().trim();
    }

    public static String prepareSmsContent(String prefix, String areaId, String content) {
        StringBuilder result = new StringBuilder();
        result.append(prefix).append(" ");
        result.append(areaId).append(" ");
        result.append(content);
        return result.toString().trim();
    }
}
