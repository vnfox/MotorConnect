package com.motor.connect.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * Created by dathuynh on 8/27/18.
 */

public class DialogHelper {

    Context context;
    AlertDialog alertDialog = null;
    AlertDialogListener callBack;
    Activity current_activity;

    public DialogHelper(Context context) {
        this.context = context;
        this.current_activity = (Activity) context;
        callBack = (AlertDialogListener) context;
    }

    /**
     * Displays the AlertDialog with 3 Action buttons
     * <p>
     * you can set cancelable property
     *
     * @param title
     * @param message
     * @param positive
     * @param negative
     * @param neutral
     * @param isCancelable
     */
    public void showAlertDialog(String title, String message, String positive, String negative, String neutral, boolean isCancelable) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(current_activity);

        if (!TextUtils.isEmpty(title))
            alertDialogBuilder.setTitle(title);
        if (!TextUtils.isEmpty(message))
            alertDialogBuilder.setMessage(message);

        if (!TextUtils.isEmpty(positive)) {
            alertDialogBuilder.setPositiveButton(positive,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            callBack.onPositiveClick();
                            alertDialog.dismiss();
                        }
                    });
        }
        if (!TextUtils.isEmpty(negative)) {
            alertDialogBuilder.setNegativeButton(negative,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            callBack.onNegativeClick();
                            alertDialog.dismiss();
                        }
                    });
        }

        alertDialogBuilder.setCancelable(isCancelable);


        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        if (TextUtils.isEmpty(negative))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
    }

    /**
     * Displays the AlertDialog with positive action button only
     * <p>
     * you can set cancelable property
     *
     * @param title
     * @param message
     * @param positive
     * @param isCancelable
     */
    public void showAlertDialog(String title, String message, String positive, boolean isCancelable) {
        showAlertDialog(title, message, positive, "", "", isCancelable);
    }

    /**
     * Displays the AlertDialog with positive action button only
     * <p>
     * cancelable property is false (Default)
     *
     * @param title
     * @param message
     * @param positive
     */
    public void showAlertDialog(String title, String message, String positive) {
        showAlertDialog(title, message, positive, "", "", false);
    }


    /**
     * Displays the AlertDialog with positive & negative buttons
     * <p>
     * you can set cancelable property
     *
     * @param title
     * @param message
     * @param positive
     * @param negative
     * @param isCancelable
     */

    public void showAlertDialog(String title, String message, String positive, String negative, boolean isCancelable) {
        showAlertDialog(title, message, positive, negative, "", isCancelable);
    }

    /**
     * Displays the AlertDialog with positive & negative buttons
     * <p>
     * cancelable property is false (Default)
     *
     * @param title
     * @param message
     * @param positive
     * @param negative
     */
    public void showAlertDialog(String title, String message, String positive, String negative) {
        showAlertDialog(title, message, positive, negative, "", false);
    }

    /**
     * Displays the AlertDialog with 3 Action buttons
     * <p>
     * cancelable property is false (Default)
     *
     * @param title
     * @param message
     * @param positive
     * @param negative
     * @param neutral
     */
    public void showAlertDialog(String title, String message, String positive, String negative, String neutral) {
        showAlertDialog(title, message, positive, negative, neutral, false);
    }

    public interface AlertDialogListener {
        void onPositiveClick();

        void onNegativeClick();

    }
}
