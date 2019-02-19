package com.motor.connect.feature.setting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.SettingViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.setting.help.HelpFeedbackActivity
import com.motor.connect.feature.setting.howuse.HowToUseActivity
import com.motor.connect.feature.setting.schedule.SettingScheduleActivity
import com.motor.connect.utils.PermissionUtils
import io.reactivex.annotations.NonNull
import java.util.*


class SettingActivity : BaseViewActivity<SettingViewBinding, SettingViewModel>(), SettingView {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    lateinit var needPermissions: MutableList<String>

    private val viewModel = SettingViewModel(this, BaseModel())

    override fun createViewModel(): SettingViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingViewModel): SettingViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_view)
        mBinding.viewModel = mViewModel

        verifyAppPermission()
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
        HowToUseActivity.show(this)
    }

    fun openHelpFeedbackScreen(v: View) {
        HelpFeedbackActivity.show(this)
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
            1 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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