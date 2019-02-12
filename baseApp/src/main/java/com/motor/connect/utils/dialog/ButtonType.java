package com.motor.connect.utils.dialog;

public enum ButtonType {
    POSITIVE(0), NEGATIVE(1);

    private int mId;

    ButtonType(int id) {
        this.mId = id;
    }

    public int getId() {
        return mId;
    }
}