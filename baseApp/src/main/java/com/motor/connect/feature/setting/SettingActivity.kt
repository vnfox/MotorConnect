package com.motor.connect.feature.setting

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.SettingViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.setting.schedule.SettingScheduleActivity


class SettingActivity : BaseViewActivity<SettingViewBinding, SettingViewModel>(), SettingView {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    private val viewModel = SettingViewModel(this, BaseModel())

    override fun createViewModel(): SettingViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingViewModel): SettingViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_view)
        mBinding.viewModel = mViewModel

        return mBinding
    }

    fun gotoPreviousScreen(v: View) {
        actionLeft()
    }

    fun openSettingScheduler(v: View) {
        SettingScheduleActivity.show(this)
    }

    fun openConfigScreen(v: View) {
        showUnderConstruction("Config system")
    }

    fun openNotedScreen(v: View) {
        showUnderConstruction("Note")
    }

    fun openHowToUseScreen(v: View) {
//        HowToUseActivity.show(this)
        showUnderConstruction()
    }

    fun openHelpFeedbackScreen(v: View) {
//        HelpFeedbackActivity.show(this)
//        showUnderConstruction()
        // onSendSms()
        var smsPhone = "0947818171"
        var smsContent = "sms testing 01"

        sendSMS(smsPhone, smsContent)

    }

    private fun handleDeleteMessage(number: String, message: String) {
        // val handler = Handler()
        showLoadingView("Delete message ...")
        handler.postDelayed(Runnable {

            deleteSMS(this, number, message)
            hideLoadingView()
            this.finish()
        }, 3000)
    }

    private fun deleteSMS(ctx: Context, number: String, message: String) {
        try {
            val ADDRESS_COLUMN_NAME = "address"
            val DATE_COLUMN_NAME = "date"
            val BODY_COLUMN_NAME = "body"
            val ID_COLUMN_NAME = "_id"

            val uriSms = Uri.parse("content://sms")
            val c = ctx.contentResolver.query(uriSms,
                    arrayOf("_id", "thread_id", "address", "person", "date", "body"), null, null, null)


            // Defines selection criteria for the rows you want to delete
            val mSelectionClause = "$ADDRESS_COLUMN_NAME = ? AND $BODY_COLUMN_NAME = ? AND $DATE_COLUMN_NAME = ?"


            Log.i("hqdat", "c count......" + c!!.count)
            if (c != null && c.moveToFirst()) {
                do {

                    val id = c.getLong(0)
                    val threadId = c.getLong(1)
                    val address = c.getString(2)
                    val body = c.getString(5)
                    val date = c.getString(3)

                    val mSelectionArgs = arrayOfNulls<String>(3)
                    mSelectionArgs[0] = address
                    mSelectionArgs[1] = body
                    mSelectionArgs[2] = threadId.toString()

                    if (message == body && address == number) {
                        Log.d("hqdat", "Deleting SMS with id: $threadId")
                        val rows = ctx.contentResolver.delete(Uri.parse("content://sms/$id"),
                                null,
                                null)
                        //ctx.contentResolver.delete()
                        Log.d("hqdat", "Delete success......... rows: $rows")
                        Log.d("hqdat", "Delete success......... body: $body")
                    }
                } while (c.moveToNext())
            }

        } catch (e: Exception) {
            Log.e("hqdat", e.toString())
            Log.e("hqdat", e.message)
        }
    }

    //---sends an SMS message to another device---

    private fun sendSMS(phoneNumber: String, message: String) {
        val SENT = "SMS_SENT"
        val DELIVERED = "SMS_DELIVERED"

        val sentPI = PendingIntent.getBroadcast(this, 0,
                Intent(SENT), 0)

        val deliveredPI = PendingIntent.getBroadcast(this, 0,
                Intent(DELIVERED), 0)

        //---when the SMS has been sent---
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Toast.makeText(baseContext, "SMS sent",
                                Toast.LENGTH_SHORT).show()
                        Log.d("hqdat", "........  SMS sent")

                        //delete message
                        handleDeleteMessage(phoneNumber, message)
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                        Toast.makeText(baseContext, "Generic failure",
                                Toast.LENGTH_SHORT).show()
                        Log.d("hqdat", "........  Generic failure")
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE -> {
                        Toast.makeText(baseContext, "No service",
                                Toast.LENGTH_SHORT).show()
                        Log.d("hqdat", "........  No Service")
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {
                        Toast.makeText(baseContext, "Null PDU",
                                Toast.LENGTH_SHORT).show()
                        Log.d("hqdat", "........  Null PDU")
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                        Toast.makeText(baseContext, "Radio off",
                                Toast.LENGTH_SHORT).show()
                        Log.d("hqdat", "........  Radio off")
                    }
                }
            }
        }, IntentFilter(SENT))

        //---when the SMS has been delivered---
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Toast.makeText(baseContext, "SMS delivered",
                                Toast.LENGTH_SHORT).show()

                        Log.d("hqdat", "........  SMS delivered")
                        // Update UI
                    }
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(baseContext, "SMS not delivered",
                                Toast.LENGTH_SHORT).show()
                        Log.d("hqdat", "........  SMS not delivered")
                        //Update UI
                    }
                }
            }
        }, IntentFilter(DELIVERED))

        val sms = SmsManager.getDefault()
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
    }
}