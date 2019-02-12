package com.motor.connect.feature.setting.area

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import com.feature.area.R
import com.feature.area.databinding.SettingAreaScheduleViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
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
        second_container.visibility = View.GONE
        third_container.visibility = View.GONE
    }

    fun selectSchedule2(v: View) {
        second_container.visibility = View.VISIBLE
        third_container.visibility = View.GONE
    }

    fun selectSchedule3(v: View) {
        second_container.visibility = View.VISIBLE
        third_container.visibility = View.VISIBLE
    }

    //Setup Loop days
    fun selectLoopNone(v: View) {

    }

    fun selectLoop1(v: View) {

    }

    fun selectLoop2(v: View) {

    }

    fun selectLoop3(v: View) {

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

    fun setupSchedule(v: View) {

    }

    private fun showSelectTimeStartDialog(txt_time: TextView) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            txt_time.text = SimpleDateFormat(" HH:mm").format(cal.time)

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