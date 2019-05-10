package com.motor.connect.feature.setting.control

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import com.feature.area.R
import com.feature.area.databinding.SettingControlViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.PermissionUtils
import com.motor.connect.utils.StringUtil
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.action_bar_view.*
import kotlinx.android.synthetic.main.setting_control_view.*


class SettingControlActivity : BaseViewActivity<SettingControlViewBinding, SettingControlViewModel>(),
        SettingControlView, SettingControlAgendaAdapter.ItemListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingControlActivity::class.java))
        }
    }

    private lateinit var needPermissions: MutableList<String>
    private var vanUsed = StringBuilder()
    private var countVan = 0

    private val viewModel = SettingControlViewModel(this, BaseModel())
    private var agendaAdapter: SettingControlAgendaAdapter? = null
    private var manualAdapter: SettingControlManualAdapter? = null

    override fun createViewModel(): SettingControlViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingControlViewModel): SettingControlViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_control_view)
        mBinding.viewModel = mViewModel

        txt_title.text = getString(R.string.setting_controll_title)
        btn_action_right.text = getString(R.string.setting_controll_apply)
        //Adapter item click
        agendaAdapter = SettingControlAgendaAdapter(this)
        manualAdapter = SettingControlManualAdapter()

        rc_control.adapter = agendaAdapter
        rc_control.layoutManager = GridLayoutManager(this, 1)

        viewModel.initViewModel()

        return mBinding
    }

    override fun fetchDataAgenda(areaVans: List<VanModel>) {
        rc_control.adapter = agendaAdapter
        rc_control.layoutManager = GridLayoutManager(this, 1)
        agendaAdapter?.setData(areaVans)
        rc_control.adapter?.notifyDataSetChanged()

        btn_manual.background = getDrawable(R.drawable.bg_button_unselected)
        btn_agenda.background = getDrawable(R.drawable.bg_button_selected)
    }

    override fun fetchDataManual(areaVans: List<VanModel>) {
        rc_control.adapter = manualAdapter
        rc_control.layoutManager = GridLayoutManager(this, 1)
        manualAdapter?.setData(areaVans)
        rc_control.adapter?.notifyDataSetChanged()

        btn_manual.background = getDrawable(R.drawable.bg_button_selected)
        btn_agenda.background = getDrawable(R.drawable.bg_button_unselected)
    }

    fun actionLeft(v: View) {
        actionLeft()
    }

    fun actionRight(v: View) {
        viewModel.prepareDataSendSms()
    }

    fun manualControl(v: View) {
        viewModel.updateAgendaWorking(false)
    }

    fun agendaControl(v: View) {
        viewModel.updateAgendaWorking(true)
    }

    override fun prepareDataForManual(items: MutableList<VanModel>) {
        showUnderConstruction("prepareDataForManual $items")
        //prepare sms
    }

    override fun prepareDataForAgenda(items: MutableList<VanModel>) {
        showUnderConstruction("prepareDataForAgenda   $items")
                //prepare sms
    }

    override fun onSetDuration(position: Int, holder: SettingControlAgendaAdapter.ItemViewHolder) {
        var items = resources.getStringArray(R.array.times_working)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.setting_time_working))
                .setSingleChoiceItems(items, 0) { _, i ->
                    var result = items[i].toString()
                    holder.timeWorking.text = items[i].toString()

                    //Notify UI
                    agendaAdapter?.updateDuration(position, holder, result.split(" ")[0])
                    viewModel.updateDataChange(position)
                }
                .setPositiveButton(getString(R.string.btn_chon), null)
                .setNegativeButton(getString(R.string.btn_huy), null)
                .show()

    }

    //Todo re-used later
    /*fun setupVanUsed(v: View) {
        var listVans = agendaAdapter?.getDataView()

        for (i in 0 until listVans!!.size) {
            if (listVans!![i].vanStatus) {
                vanUsed.append(listVans!![i].vanId).append(" ")
                countVan++
            }
        }
        //Send SMS
        viewModel.updateDataArea(listVans)
        setupVanUsedDetail(vanUsed.toString())
    }*/

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
                        smsManager.sendTextMessage(smsNumber, null, smsText, null, null)
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