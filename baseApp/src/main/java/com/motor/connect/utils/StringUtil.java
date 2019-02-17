package com.motor.connect.utils;

import com.motor.connect.feature.model.ScheduleModel;

import org.jetbrains.annotations.NotNull;

public class StringUtil {

    public static String getFirstItem(String value) {
        String[] array = value.split(" ");
        return array[0];
    }

    public static String getScheduleWorking(String value) {
        StringBuilder result = new StringBuilder();
        String[] array = value.split(" ");
        result.append("Lich dang tuoi").append("\n");
        //getFirstItem & check current time
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

    public static String prepareSmsSettingScheduleContent(String prefix, String password, String areaId, String content) {
        //Dn1234 prefix
        StringBuilder result = new StringBuilder();
        result.append(prefix);
        if (!password.isEmpty())
            result.append(password);
        result.append(" ");
        result.append(areaId).append(" ");
        result.append(content);
        return result.toString().trim();
    }

    public static String prepareSmsStopAllSchedule(String password) {
        StringBuilder result = new StringBuilder();
        result.append(MotorConstants.AreaCode.PREFIX_STOP);
        if (!password.isEmpty())
            result.append(password);
        result.append(" ");
        result.append(MotorConstants.AreaCode.STOP_ALL);
        return result.toString().trim();
    }

    public static String prepareSmsStopSchedule(String password, String areaId) {
        StringBuilder result = new StringBuilder();
        result.append(MotorConstants.AreaCode.PREFIX_STOP);
        if (!password.isEmpty())
            result.append(password);
        result.append(" ");
        result.append(areaId);
        return result.toString().trim();
    }

    public static String prepareSmsReviewSchedule(String password, String areaId) {
        StringBuilder result = new StringBuilder();
        result.append(MotorConstants.AreaCode.PREFIX_REVIEW_SCHEDULER);
        if (!password.isEmpty())
            result.append(password);
        result.append(" ");
        result.append(areaId);
        return result.toString().trim();
    }

    @NotNull
    public static String prepareSmsVanAreaUsed(String password, @NotNull String areaId, int countVan, @NotNull String vanUsed) {
        StringBuilder result = new StringBuilder();
        result.append(MotorConstants.AreaCode.PREFIX_VAN_USED);
        if (!password.isEmpty())
            result.append(password);
        result.append(" ");
        result.append(areaId).append(" ");
        result.append("0" + countVan).append(" ");
        result.append(vanUsed);
        return result.toString().trim();
    }

    public static String getScheduleOneDay(ScheduleModel schedule) {
        StringBuilder result = new StringBuilder();
        result.append("Lịch tưới").append("\n");
        result.append(getSchedule(schedule));
        return result.toString();
    }

    public static String getScheduleTwoDay(ScheduleModel schedule1, ScheduleModel schedule2) {
        StringBuilder result = new StringBuilder();
        result.append("Lịch tưới").append("\n");
        result.append(getSchedule(schedule1));
        result.append("\n");
        result.append(getSchedule(schedule2));
        return result.toString();
    }

    public static String getScheduleThreeDay(ScheduleModel schedule1, ScheduleModel schedule2, ScheduleModel schedule3) {
        StringBuilder result = new StringBuilder();
        result.append("Lịch tưới").append("\n");
        result.append(getSchedule(schedule1));
        result.append("\n");
        result.append(getSchedule(schedule2));
        result.append("\n");
        result.append(getSchedule(schedule3));
        return result.toString();
    }

    private static String getSchedule(ScheduleModel scheduleModel) {
        StringBuilder result = new StringBuilder();
        result.append(" * Bắt đầu ").append(scheduleModel.getTimeSchedule().replace(":", " giờ "))
                .append(" - tưới ").append(scheduleModel.getTimeRun())
                .append(" phút");
        return result.toString();
    }
}
