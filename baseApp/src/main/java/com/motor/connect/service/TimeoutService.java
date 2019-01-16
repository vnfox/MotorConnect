package com.motor.connect.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class TimeoutService extends JobService {
    public static final String SCHEDULE_TAG_IDLE_TIMER = "SCHEDULE_TAG_IDLE_TIMER";

    public static final String SCHEDULE_TAG_COUNT_DOWN_TIMER = "SCHEDULE_TAG_COUNT_DOWN_TIMER";

    public static final String EXTRA_TRIGGER_TIME = "EXTRA_TRIGGER_TIME";

    public static final String EXTRA_START_TIME = "EXTRA_START_TIME";


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        // ignore
        return false;
    }
}
