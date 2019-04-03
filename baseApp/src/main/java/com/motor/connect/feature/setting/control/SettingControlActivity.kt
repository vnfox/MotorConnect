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
        SettingControlView, SettingControlAdapter.ItemListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingControlActivity::class.java))
        }
    }

    private lateinit var needPermissions: MutableList<String>
    private var vanUsed = StringBuilder()
    private var countVan = 0

    private val viewModel = SettingControlViewModel(this, BaseModel())
    private var adapter: SettingControlAdapter? = null

    override fun createViewModel(): SettingControlViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingControlViewModel): SettingControlViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_control_view)
        mBinding.viewModel = mViewModel

        txt_title.text = "Control"
        btn_action_right.text = "Apply"
        //Adapter item click
        adapter = SettingControlAdapter(this)
        rc_control.adapter = adapter
        rc_control.layoutManager = GridLayoutManager(this, 1)

        viewModel.initViewModel()

        return mBinding
    }

    override fun viewLoaded(areaVans: MutableList<VanModel>, agenda: Boolean) {
        adapter?.setData(areaVans)
        rc_control.adapter?.notifyDataSetChanged()

        if (agenda) {
            btn_manual.background = getDrawable(R.drawable.bg_button_unselected)
            btn_agenda.background = getDrawable(R.drawable.bg_button_selected)
        } else {
            btn_manual.background = getDrawable(R.drawable.bg_button_selected)
            btn_agenda.background = getDrawable(R.drawable.bg_button_unselected)
        }
    }

    fun actionLeft(v: View) {
        actionLeft()
    }

    fun actionRight(v: View) {
        showUnderConstruction()
        //Save data
        //Todo set up schedule
    }

    fun manualControl(v: View) {
        showUnderConstruction()
        btn_manual.background = getDrawable(R.drawable.bg_button_selected)
        btn_agenda.background = getDrawable(R.drawable.bg_button_unselected)
        viewModel.updateAgendaWorking(false)
    }

    fun agendaControl(v: View) {
        showUnderConstruction()
        btn_agenda.background = getDrawable(R.drawable.bg_button_selected)
        btn_manual.background = getDrawable(R.drawable.bg_button_unselected)
        viewModel.updateAgendaWorking(true)
    }

    override fun onSetDuration(position: Int, holder: SettingControlAdapter.ItemViewHolder) {
        var items = resources.getStringArray(R.array.times_working)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.setting_time_working))
                .setSingleChoiceItems(items, 0) { onDialogClicked, i ->

                    var result = items[i].toString()
                    holder.timeWorking.text = items[i].toString()

                    //Notify UI
                    adapter?.updateDuration(position, holder, result.split(" ")[0])
                    viewModel.updateDataChange(position)

                }
                .setPositiveButton(getString(R.string.btn_chon), null)
                .setNegativeButton(getString(R.string.btn_huy), null)
                .show()

    }

    //Todo re-used later
    /*fun setupVanUsed(v: View) {
        var listVans = adapter?.getDataView()

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