package com.motor.connect.feature.setting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.SettingViewBinding
import com.motor.connect.base.view.BaseActivity
import com.motor.connect.utils.PermissionUtils
import io.reactivex.annotations.NonNull
import java.util.*
import android.provider.ContactsContract


class SettingActivity : BaseActivity() {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    lateinit var needPermissions: MutableList<String>

    private val viewModel = SettingViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: SettingViewBinding = DataBindingUtil.setContentView(this, R.layout.setting_view)

        binding.viewModel = viewModel

        viewModel.startUpdates()

        verifyAppPermission()


        val onClose = findViewById<ImageView>(R.id.action_left)
        onClose?.setOnClickListener {
            actionLeft()
        }

    }

    fun openSettingScheduler(v: View) {
        Toast.makeText(this, "=== openSettingScheduler====", Toast.LENGTH_LONG).show()
    }

    fun openConfigScreen(v: View) {
        Toast.makeText(this, "=== openConfigScreen ====", Toast.LENGTH_LONG).show()
    }

    fun openNotedScreen(v: View) {
        Toast.makeText(this, "=== openNotedScreen ====", Toast.LENGTH_LONG).show()
    }


    fun openHowToUseScreen(v: View) {
        Toast.makeText(this, "=== openHowToUseScreen ====", Toast.LENGTH_LONG).show()
    }


    fun openHelpFeedbackScreen(v: View) {
        Toast.makeText(this, "=== openHelpFeedbackScreen ====", Toast.LENGTH_LONG).show()
    }


    //============= Todo check remove =======================
    private fun verifyAppPermission() {
        needPermissions = ArrayList()

        if (!PermissionUtils.isGranted(this,
                        Manifest.permission.SEND_SMS)) {
            needPermissions.add(Manifest.permission.SEND_SMS)
        }
    }

    private fun checkPermission(sendSms: String): Boolean {

        val checkpermission = ContextCompat.checkSelfPermission(this, sendSms)
        return checkpermission == PackageManager.PERMISSION_GRANTED
    }

    fun grantPermissions() {
        ActivityCompat.requestPermissions(this,
                needPermissions.toTypedArray(), 101)
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "=== permission  Accept ====", Toast.LENGTH_LONG).show()
                val smsNumber = "0947818171"
                val smsText = "Send sms test app SmsManager"

                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(smsNumber, null, smsText, null, null)
            }
        }
    }

    fun onSendSms() {
        if (checkPermission(Manifest.permission.SEND_SMS)) {
            Toast.makeText(this, "=== permission  Accept ====", Toast.LENGTH_LONG).show()

            //Send sms in background Work wells
            val smsNumber = "0947818171"
            val smsText = "Send sms test app SmsManager"

            val smsManager = SmsManager.getDefault()
            //smsManager.sendTextMessage(smsNumber, null, smsText, null, null)
        } else {
            Toast.makeText(this, "=== permission  Denied ====", Toast.LENGTH_LONG).show()
        }
    }
}