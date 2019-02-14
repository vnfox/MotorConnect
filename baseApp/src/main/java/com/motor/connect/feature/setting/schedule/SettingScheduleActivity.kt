package com.motor.connect.feature.setting.schedule

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.SettingScheduleActivityBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.add.AddAreaActivity
import com.motor.connect.feature.details.AreaDetailActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.setting.area.SettingAreaScheduleActivity
import com.motor.connect.utils.DialogHelper
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.PermissionUtils
import com.motor.connect.utils.StringUtil
import com.orhanobut.hawk.Hawk
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.setting_schedule_activity.*


class SettingScheduleActivity : BaseViewActivity<SettingScheduleActivityBinding, SettingScheduleViewModel>(),
        SettingScheduleView, DialogHelper.AlertDialogListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingScheduleActivity::class.java))
        }
    }

    private val viewModel = SettingScheduleViewModel(this, BaseModel())
    private var adapter: SettingScheduleAdapter? = null

    private var alertDialogHelper: DialogHelper? = null
    private lateinit var needPermissions: MutableList<String>

    override fun createViewModel(): SettingScheduleViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingScheduleViewModel): SettingScheduleActivityBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_schedule_activity)
        mBinding.viewModel = mViewModel

        //Adapter item click
        adapter = SettingScheduleAdapter { areaModel, position ->

            //Setting schedule area
            Hawk.put(MotorConstants.KEY_POSITION, position)
            Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, areaModel)
            SettingAreaScheduleActivity.show(this)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val isUser = shef?.getFirstUserPref(MotorConstants.FIRST_USED)
        viewModel.initData(isUser)
        return mBinding
    }

    fun onBackSettingScreen(v: View) {
        actionLeft()
    }

    override fun showEmptyView() {
        setting_container.visibility = View.GONE
        txt_empty.visibility = View.VISIBLE

    }

    override fun updateUI(dataArea: MutableList<AreaModel>) {
        setting_container.visibility = View.VISIBLE
        txt_empty.visibility = View.GONE
        adapter?.setData(dataArea)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        if (shef!!.getTriggerData(MotorConstants.KEY_TRIGGER_DATA)) {
            viewModel.updateData()
            shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, false)
        }
    }

    override fun onDestroy() {
        viewModel.stopUpdates()
        super.onDestroy()
    }

    fun openEmptyScreen(v: View) {
        AddAreaActivity.show(this)
    }

    fun stopScheduleAllArea(v: View) {
        alertDialogHelper = DialogHelper(this)
        alertDialogHelper?.showAlertDialog(getString(R.string.sms_warning),
                getString(R.string.setting_schedule_off_description),
                getString(R.string.btn_accept), getString(R.string.btn_huy), false)
    }

    override fun onPositiveClick() {
        //Send Sms
        setupSchedulerDetail()
    }

    override fun onNegativeClick() {
        //do nothing
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            MotorConstants.PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSendSms()
            }
        }
    }

    private fun setupSchedulerDetail() {
        if (PermissionUtils.isGranted(this,
                        Manifest.permission.SEND_SMS)) {
            //Setup van used
            onSendSms()
        } else {
            //Add permission
            needPermissions.add(Manifest.permission.SEND_SMS)
            PermissionUtils.isPermissionsGranted(this, needPermissions.toTypedArray(), MotorConstants.PERMISSION_REQUEST_CODE)
        }
    }

    private fun onSendSms() {
        var smsPhone = viewModel.getPhoneArea()
        var smsContent = StringUtil.prepareSmsStopAllSchedule(viewModel.getPassWordArea())
        //Send sms in background
        Log.d("hqdat", ">>> smsNumber  $smsPhone")
        Log.d("hqdat", ">>> smsContent  $smsContent")

        //Todo open comment when completed
        val smsManager = SmsManager.getDefault()
//        smsManager.sendTextMessage(smsPhone, null, smsContent, null, null)
    }
}
