package com.motor.connect.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.motor.connect.utils.CommonUtil;
import com.motor.connect.utils.MotorConstants;
import com.motor.connect.utils.dialog.DialogActivity;
import com.orhanobut.hawk.Hawk;

public class SMSReceiver extends BroadcastReceiver {

    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = null;
        if (bundle != null) {
            format = bundle.getString("format");
        }
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                Log.d("hqdat", ">>>>   phone    " + msgs[i].getOriginatingAddress());

                Hawk.put(MotorConstants.KEY_SMS_RECEIVER, strMessage);
                showDialogMessage(context);
//                if (CommonUtil.checkPhoneContainArea(msgs[i].getOriginatingAddress())) {
//                    Hawk.put(MotorConstants.KEY_SMS_RECEIVER, strMessage);
//                    showDialogMessage(context);
//                }
            }
        }
    }

    private void showDialogMessage(Context context) {
        //start activity which has dialog
        Intent i = new Intent(context, DialogActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
