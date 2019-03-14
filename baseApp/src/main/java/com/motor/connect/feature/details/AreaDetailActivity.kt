package com.motor.connect.feature.details

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Handler
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.feature.area.R
import com.feature.area.databinding.DetailViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.edit.EditAreaActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.setting.area.SettingAreaScheduleActivity
import com.motor.connect.feature.setting.control.SettingControlActivity
import com.motor.connect.feature.setting.van.SettingAreaVanActivity
import com.motor.connect.utils.DialogHelper
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.PermissionUtils
import com.motor.connect.utils.StringUtil
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.detail_view.*


class AreaDetailActivity : BaseViewActivity<DetailViewBinding, AreaDetailViewModel>(), AreaDetailView, DialogHelper.AlertDialogListener {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AreaDetailActivity::class.java))
        }
    }

    private val viewModel = AreaDetailViewModel(this, BaseModel())
    private var workingProgress: CircularProgressIndicator? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null

    private var bottomSheetDialog: BottomSheetDialog? = null

    private var maxProgress: Int = 1800
    private var currentProgress: Int = 0

    private lateinit var needPermissions: MutableList<String>
    private var smsPhone = String()
    private var smsContent = String()

    private var alertDialogHelper: DialogHelper? = null

    override fun createViewModel(): AreaDetailViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: AreaDetailViewModel): DetailViewBinding {

        mBinding = DataBindingUtil.setContentView(this, R.layout.detail_view)
        mBinding.viewModel = mViewModel

        //Setup Account Bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.mipmap.ic_back_white)
        toolbar.setNavigationOnClickListener {
            actionLeft()
        }
        workingProgress = findViewById(R.id.working_progress)

        //Setup info
        showLoadingView(getString(R.string.sms_loading))
        viewModel.initViewModel()

        //Setup CollapsingToolbarLayout View
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        collapsingToolbarLayout?.title = getString(R.string.detail_title)

        return mBinding
    }

    override fun onResume() {
        super.onResume()
        if (shef!!.getUpdateData(MotorConstants.KEY_EDIT_AREA) || shef!!.getUpdateData(MotorConstants.KEY_TRIGGER_DATA)) {
            shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, false)
            viewModel.initViewModel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        workingProgress = null
        viewModel.destroy()
    }

    @SuppressLint("SetTextI18n")
    override fun updateAreaInfoWhenEdit(model: AreaModel) {
        smsPhone = model.areaPhone
        txt_area_name.text = model.areaName
        txt_area_phone.text = model.areaPhone
        txt_area_van.text = getString(R.string.detail_van_total) + model.areaVans.size.toString()
    }

    override fun viewLoaded() {
        loadBackdrop()
    }

    @SuppressLint("SetTextI18n")
    override fun viewAreaInfo(model: AreaModel, schedules: String) {
        smsPhone = model.areaPhone
        txt_area_name.text = model.areaName
        txt_area_phone.text = model.areaPhone

        //should show van open
        txt_area_van.text = String.format(getString(R.string.detail_van_total), model.areaVans.size.toString())
        txt_area_scheduler.text = schedules
        txt_area_detail.text = model.areaDetails

        hideLoadingView()
        //update Motor info
        viewModel.updateInfoMotor()
    }

    @SuppressLint("SetTextI18n")
    override fun updateInfoMotor(areaStatus: String?, vansUsed: String, schedule: String) {
        info_container.visibility = View.VISIBLE

        txt_area_status.text = String.format(getString(R.string.detail_status), areaStatus)
        txt_area_van_used.text = String.format(getString(R.string.detail_van_open), vansUsed)
        txt_schedule_working.text = schedule

        //update Motor working
        viewModel.checkScheduleWorking()
    }

    @SuppressLint("SetTextI18n")
    override fun viewMotorWorking(timeStart: String, time: String, maxValue: Int, currentTime: Int) {
        txt_area_status.text = getString(R.string.detail_status_working)
        working_container.visibility = View.VISIBLE

        txt_time_start.text = getString(R.string.detail_time_start) + StringUtil.getTimeRunning(timeStart)
        txt_time_working.text = String.format(getString(R.string.detail_time_running), time)
        txt_van.text = txt_area_van.text

        currentProgress = currentTime
        maxProgress = maxValue
        workingProgress?.maxProgress = maxValue.toDouble()
        workingProgress?.setCurrentProgress(currentTime.toDouble())
        workingProgress?.setProgressTextAdapter(timeText)

        onWorkingProgress()
    }

    override fun updateViewMotorStopWorking() {
        working_container.visibility = View.GONE
    }

    private val timeText = CircularProgressIndicator.ProgressTextAdapter { time ->
        var time = time
        time %= 3600.0

        val minutes = (time / 60).toInt()
        val seconds = (time % 60).toInt()
        val sb = StringBuilder()
        if (minutes < 10) {
            sb.append(0)
        }
        sb.append(minutes).append(":")
        if (seconds < 10) {
            sb.append(0)
        }
        sb.append(seconds)
        sb.toString()
    }

    private fun loadBackdrop() {
        val imageView = findViewById<ImageView>(R.id.backdrop)
        Glide.with(this).load(BackDrop.getRandomBackDrop()).apply(RequestOptions.centerCropTransform()).into(imageView)
    }

    fun selectSettingView(v: View) {
        bottomSheetDialog = BottomSheetDialog(this)
        val sheetView = this.layoutInflater.inflate(R.layout.dialog_bottom_sheet_view, null)
        bottomSheetDialog?.setContentView(sheetView)
        bottomSheetDialog?.show()
    }

    fun setupScheduleArea(v: View) {
        bottomSheetDialog?.dismiss()
        SettingAreaVanActivity.show(this)
    }

    fun controlAgenda(v: View) {
        bottomSheetDialog?.dismiss()
        SettingControlActivity.show(this)
    }

    fun editInfoArea(v: View) {
        EditAreaActivity.show(this)
        bottomSheetDialog?.dismiss()
    }

    private fun showDialogConfirm(title: String, description: String) {

        alertDialogHelper = DialogHelper(this)
        alertDialogHelper?.showAlertDialog(title, description, getString(R.string.btn_accept), getString(R.string.btn_huy), false)
    }

    override fun onPositiveClick() {
        //Send Sms
        setupSchedulerDetail(smsContent)
    }

    override fun onNegativeClick() {
        //do nothing
    }

    private fun onWorkingProgress() {
        Thread(Runnable {
            while (currentProgress < maxProgress) {
                currentProgress += 1

                handler.post {
                    workingProgress?.setCurrentProgress(currentProgress.toDouble())
                }
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }

    private fun setupSchedulerDetail(smsContent: String) {
        //Send SMS
        if (PermissionUtils.isGranted(this,
                        Manifest.permission.SEND_SMS)) {
            //Setup van used
            onSendSms(smsContent)
        } else {
            //Add permission
            needPermissions.add(Manifest.permission.SEND_SMS)
            PermissionUtils.isPermissionsGranted(this, needPermissions.toTypedArray(), MotorConstants.PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            MotorConstants.PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSendSms(smsContent)
            }
        }
    }

    private fun onSendSms(smsContent: String) {
        showLoadingView(getString(R.string.sms_sending))
        //Send sms in background
        var pStatus: Int = 0
        val smsManager = SmsManager.getDefault()

        Thread(Runnable {
            while (pStatus < MotorConstants.TIME_PROGRESS) {
                pStatus += 1
                handler.post {
                    if (pStatus == MotorConstants.TIME_PROGRESS) {
                        hideLoadingView()
                        //Send sms
                        smsManager.sendTextMessage(smsPhone, null, smsContent, null, null)
                    }
                }
                try {
                    // Sleep for 200 milliseconds.
                    // Just to display the progress slowly
                    Thread.sleep(100) //thread will take approx 3 seconds to finish
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }
}