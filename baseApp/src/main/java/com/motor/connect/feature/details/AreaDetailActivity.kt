package com.motor.connect.feature.details

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
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

    private val maxProgress: Int = 1800
    private var currentProgress: Int = 0
    private val handler = Handler()

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
        toolbar.setNavigationIcon(R.mipmap.ic_back)
        toolbar.setNavigationOnClickListener {

            actionLeft()
        }

        workingProgress = findViewById(R.id.working_progress)

        //Setup info
        viewModel.initViewModel()

        //Setup CollapsingToolbarLayout View
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        collapsingToolbarLayout?.title = "Details View"

        return mBinding
    }

    override fun onResume() {
        super.onResume()
        if (shef!!.getUpdateData(MotorConstants.KEY_EDIT_AREA)) {
            viewModel.reloadDataWhenEdit()
        }
    }

    override fun updateAreaInfoWhenEdit(model: AreaModel) {
        smsPhone = model.areaPhone
        txt_area_name.text = model.areaName
        txt_area_phone.text = model.areaPhone
        txt_area_van.text = "So van " + model.areaVans.size.toString()
    }

    override fun viewLoaded() {
        loadBackdrop()
    }

    override fun viewAreaInfo(model: AreaModel, schedules: String) {
        smsPhone = model.areaPhone
        txt_area_name.text = model.areaName
        txt_area_phone.text = model.areaPhone
        txt_area_van.text = "So van " + model.areaVans.size.toString()
        txt_area_scheduler.text = schedules

        //update Motor info
        viewModel.updateInfoMotor()
    }

    @SuppressLint("SetTextI18n")
    override fun updateInfoMotor(areaStatus: String?, vansUsed: String, scheduleWorking: String?) {
        info_container.visibility = View.VISIBLE

        txt_area_status.text = "Trang thai $areaStatus"
        txt_area_van_used.text = "Van su dung $vansUsed"
        txt_schedule_working.text = scheduleWorking

        //update Motor working
        viewModel.updateMotorWorking(scheduleWorking)
    }

    override fun viewMotorWorking(timeStart: String, time: String, maxValue: Int, currentTime: Int) {
        if (currentTime == 0) {
            working_container.visibility = View.GONE
        } else {
            working_container.visibility = View.VISIBLE

            txt_time_start.text = timeStart
            txt_time_working.text = time
            txt_van.text = txt_area_van_used.text

            Log.d("hqdat", ">>>> currentTime.toDouble()  " + currentTime.toDouble())

            currentProgress = currentTime

            workingProgress?.maxProgress = maxValue.toDouble()
            workingProgress?.setCurrentProgress(currentTime.toDouble())
            workingProgress?.setProgressTextAdapter(timeText)

            onWorkingProgress()
        }
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
        //Todo Will get list Backdrop
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).apply(RequestOptions.centerCropTransform()).into(imageView)
    }


    fun selectSettingView(v: View) {
        bottomSheetDialog = BottomSheetDialog(this)
        val sheetView = this.layoutInflater.inflate(R.layout.dialog_bottom_sheet_view, null)
        bottomSheetDialog?.setContentView(sheetView)
        bottomSheetDialog?.show()
    }

    fun setupScheduleArea(v: View) {
        bottomSheetDialog?.dismiss()
        SettingAreaScheduleActivity.show(this)
    }

    fun reviewScheduleArea(v: View) {
        smsContent = StringUtil.prepareSmsReviewSchedule(viewModel.getPassWordArea(), viewModel.getAreaId())

        showDialogConfirm(getString(R.string.sms_warning_title), getString(R.string.sms_review_schedule_content))
        bottomSheetDialog?.dismiss()
    }

    fun setupVanUsedArea(v: View) {
        bottomSheetDialog?.dismiss()
        SettingAreaVanActivity.show(this)
    }

    fun scheduleStopArea(v: View) {
        smsContent = StringUtil.prepareSmsStopSchedule(viewModel.getPassWordArea(), viewModel.getAreaId())

        showDialogConfirm(getString(R.string.sms_warning_title), getString(R.string.sms_stop_schedule_content))
        bottomSheetDialog?.dismiss()
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
        //Send sms in background
        Toast.makeText(this, "=== onSendSms ====  $smsContent", Toast.LENGTH_LONG).show()
        Log.d("hqdat", ">>> smsNumber  $smsPhone")
        Log.d("hqdat", ">>> smsContent  $smsContent")

        //Todo open comment when completed
        val smsManager = SmsManager.getDefault()
//        smsManager.sendTextMessage(smsPhone, null, smsContent, null, null)
    }
}