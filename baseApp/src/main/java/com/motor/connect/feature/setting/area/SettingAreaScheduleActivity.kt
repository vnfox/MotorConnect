package com.motor.connect.feature.setting.area

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.SettingAreaScheduleViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.utils.EnumHelper
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.PermissionUtils
import com.motor.connect.utils.StringUtil
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.setting_area_schedule_view.*
import kotlinx.android.synthetic.main.setting_area_schedule_view.view.*
import java.text.SimpleDateFormat
import java.util.*


class SettingAreaScheduleActivity : BaseViewActivity<SettingAreaScheduleViewBinding, SettingAreaScheduleViewModel>(), SettingAreaScheduleView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingAreaScheduleActivity::class.java))
        }
    }

    lateinit var needPermissions: MutableList<String>

    private val viewModel = SettingAreaScheduleViewModel(this, BaseModel())

    private var prefix = "Dn1234"
    private var repeat = " "
    private var selectSchedule = "01"
    private var smsContent = ""

    override fun createViewModel(): SettingAreaScheduleViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingAreaScheduleViewModel): SettingAreaScheduleViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_area_schedule_view)
        mBinding.viewModel = mViewModel

        viewModel.initViewModel()
        return mBinding
    }

    override fun viewLoaded() {
        // Default
        rd_schedule1.isChecked = true
        rd_schedule_loop_none.isChecked = true
        second_container.visibility = View.GONE
        third_container.visibility = View.GONE
    }

    fun actionClose(v: View) {
        actionLeft()
    }

    fun selectSchedule1(v: View) {
        selectSchedule = "01"
        second_container.visibility = View.GONE
        third_container.visibility = View.GONE
    }

    fun selectSchedule2(v: View) {
        selectSchedule = "02"
        second_container.visibility = View.VISIBLE
        third_container.visibility = View.GONE
    }

    fun selectSchedule3(v: View) {
        selectSchedule = "03"
        second_container.visibility = View.VISIBLE
        third_container.visibility = View.VISIBLE
    }

    //Setup Loop days
    fun selectLoopNone(v: View) {
        repeat = " "
        prefix = "Dn1234"
    }

    fun selectLoop1(v: View) {
        repeat = "01"
        prefix = "De1234"
    }

    fun selectLoop2(v: View) {
        repeat = "02"
        prefix = "De1234"
    }

    fun selectLoop3(v: View) {
        repeat = "03"
        prefix = "De1234"
    }

    fun selectTimeStart1(v: View) {
        showSelectTimeStartDialog(v.txt_time1_start)
    }

    fun selectTimeWorking1(v: View) {
        showSelectTimeWorkingDialog(v.txt_time1_run)
    }

    fun selectTimeStart2(v: View) {
        showSelectTimeStartDialog(v.txt_time2_start)
    }

    fun selectTimeWorking2(v: View) {
        showSelectTimeWorkingDialog(v.txt_time2_run)
    }

    fun selectTimeStart3(v: View) {
        showSelectTimeStartDialog(v.txt_time3_start)
    }

    fun selectTimeWorking3(v: View) {
        showSelectTimeWorkingDialog(v.txt_time3_run)
    }

    // action setup scheduler
    fun setupSchedule(v: View) {

        smsContent = getSmsContent()
        Log.d("hqdat", ">>> smsContent  $smsContent")

        if (PermissionUtils.isGranted(this,
                        Manifest.permission.SEND_SMS)) {
            //Setup Scheduler
            onSendSms(smsContent)
        } else {
            //Add permission
            needPermissions.add(Manifest.permission.SEND_SMS)
            PermissionUtils.isPermissionsGranted(this, needPermissions.toTypedArray(), MotorConstants.PERMISSION_REQUEST_CODE)
        }
    }

    private fun getSmsContent(): String {
        when (selectSchedule) {
            EnumHelper.ScheduleDays.ONE_DAY.key -> {
                //De1234 01 -> 01 0601 030 01
                smsContent = StringUtil.getSmsContentScheduleOneDays(selectSchedule, txt_time1_start.text.toString(), txt_time1_run.text.toString(), repeat)
            }
            EnumHelper.ScheduleDays.TWO_DAY.key -> {
                smsContent = StringUtil.getSmsContentScheduleTowDays(selectSchedule, txt_time1_start.text.toString(), txt_time1_run.text.toString(),
                        txt_time2_start.text.toString(), txt_time2_run.text.toString(), repeat)
            }
            EnumHelper.ScheduleDays.THREE_DAY.key -> {
                smsContent = StringUtil.getSmsContentScheduleThreeDays(selectSchedule, txt_time1_start.text.toString(), txt_time1_run.text.toString(),
                        txt_time2_start.text.toString(), txt_time2_run.text.toString(),
                        txt_time3_start.text.toString(), txt_time3_run.text.toString(), repeat)
            }
        }

        return smsContent
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            MotorConstants.PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                onSendSms(smsContent)
            }
        }
    }

    private fun onSendSms(content: String) {
        //Dn1234 lich tuoi cho 1 ngay

        //Area_id 01
        //Send sms in background
        val smsNumber = viewModel.getAreaPhone()

        //viewModel.getAreaId() + content
        val smsText = StringUtil.prepareSmsContent(prefix, viewModel.getAreaId(), content)

        Log.d("hqdat", ">>> onSendSms Phone  $smsNumber")
        Log.d("hqdat", ">>> onSendSms Content  $smsText")

        val smsManager = SmsManager.getDefault()
//        smsManager.sendTextMessage(smsNumber, null, smsText, null, null)
    }

    private fun showSelectTimeStartDialog(txt_time: TextView) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            txt_time.text = SimpleDateFormat("HH:mm").format(cal.time)

        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun showSelectTimeWorkingDialog(txt_time: TextView) {
        var items = resources.getStringArray(R.array.times_working)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.setting_time_working))
                .setSingleChoiceItems(items, 0) { onDialogClicked, i ->

                    txt_time.text = items[i].toString()

                }
                .setPositiveButton(getString(R.string.btn_chon), null)
                .setNegativeButton(getString(R.string.btn_huy), null)
                .show()
    }

}