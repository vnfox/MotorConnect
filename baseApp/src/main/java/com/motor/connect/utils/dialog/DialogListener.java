package com.motor.connect.utils.dialog;

public interface DialogListener {
    void onPositiveClick(int from);

    void onNegativeClick(int from);

    void onNeutralClick(int from);
}