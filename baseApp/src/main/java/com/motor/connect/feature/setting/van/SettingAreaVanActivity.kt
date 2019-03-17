package com.motor.connect.feature.setting.van

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.widget.GridLayoutManager
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.SettingAreaVanViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.PermissionUtils
import com.motor.connect.utils.StringUtil
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.setting_area_van_view.*


class SettingAreaVanActivity : BaseViewActivity<SettingAreaVanViewBinding, SettingAreaVanViewModel>(), SettingAreaVanView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingAreaVanActivity::class.java))
        }
    }

    private lateinit var needPermissions: MutableList<String>
    private var vanUsed = StringBuilder()
    private var countVan = 0

    private val viewModel = SettingAreaVanViewModel(this, BaseModel())
    private var adapter: SettingAreaVanAdapter? = null

    override fun createViewModel(): SettingAreaVanViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingAreaVanViewModel): SettingAreaVanViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_area_van_view)
        mBinding.viewModel = mViewModel

        //Adapter item click
        adapter = SettingAreaVanAdapter { areaModel, position ->
            //nothing
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        viewModel.initViewModel()

        return mBinding
    }

    override fun viewLoaded(areaVans: MutableList<VanModel>) {
        adapter?.setData(areaVans)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun actionClose(v: View) {
        actionLeft()
    }

    fun setupVanUsed(v: View) {
        var listVans = adapter?.getDataView()

        for (i in 0 until listVans!!.size) {
            if (listVans!![i].vanStatus) {
                vanUsed.append(listVans!![i].vanId).append(" ")
                countVan++
            }
        }
        //Update data
        viewModel.updateDataArea(listVans)

        //No send sms
        setupVanUsedDetail(vanUsed.toString())
    }

    private fun setupVanUsedDetail(smsContent: String) {
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
            MotorConstants.PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSendSms(vanUsed.toString())
            }
        }
    }

    private fun onSendSms(vanUsed: String) {
        //Send sms in background
        showLoadingView(getString(R.string.sms_sending))
        val smsNumber = viewModel.getPhoneNumber()
        val smsText = StringUtil.prepareSmsVanAreaUsed(viewModel.getPassWordArea(), viewModel.getAreaId(), countVan, vanUsed)
        Log.d("hqdat", ">>>>>  smsText   $smsText")
        val smsManager = SmsManager.getDefault()
        var pStatus: Int = 0

        Thread(Runnable {
            while (pStatus < MotorConstants.TIME_PROGRESS) {
                pStatus += 1
                handler.post {
                    if (pStatus == MotorConstants.TIME_PROGRESS) {
                        hideLoadingView()
                        //Send sms
                        //smsManager.sendTextMessage(smsNumber, null, smsText, null, null)
                        backPreviousScreen()
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

    private fun backPreviousScreen() {
        //Trigger Data
        shef!!.setUpdateData(MotorConstants.KEY_EDIT_AREA, true)
        actionLeft()
        this.finish()
    }
}