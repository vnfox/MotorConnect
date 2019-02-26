package com.motor.connect.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.motor.connect.feature.details.AreaDetailActivity;
import com.motor.connect.feature.main.MainActivity;
import com.motor.connect.utils.MotorConstants;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class OutgoingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //check the flag
        if (MotorConstants.IsProgramRunning) {
            //open your activity immediately after a call
            Intent intent1 = new Intent(context, AreaDetailActivity.class);
            intent1.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            MotorConstants.IsProgramRunning = false;
        }
    }
}
