package com.motor.connect.utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

import static java.util.Calendar.getInstance;

/**
 * Created by dathuynh on 8/27/18.
 */

public class DialogHelper extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int munites = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(),
                hour, munites, DateFormat.is24HourFormat(getActivity()));

    }
}
