package com.motor.connect.feature.setting.van

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.TextView
import com.feature.area.R
import com.feature.area.databinding.SettingAreaVanViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.model.RepeatModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.PermissionUtils
import com.motor.connect.utils.StringUtil
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.action_bar_view.*
import kotlinx.android.synthetic.main.setting_area_van_view.*
import java.text.SimpleDateFormat
import java.util.*


class SettingAreaVanActivity : BaseViewActivity<SettingAreaVanViewBinding, SettingAreaVanViewModel>(), SettingAreaVanView, SettingAreaVanAdapter.ItemListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingAreaVanActivity::class.java))
        }
    }

    private lateinit var needPermissions: MutableList<String>
    private var vanUsed = StringBuilder()
    private var countVan = 0
    private var currentPosition = 0

    private val viewModel = SettingAreaVanViewModel(this, BaseModel())
    private var adapter: SettingAreaVanAdapter? = null

    override fun createViewModel(): SettingAreaVanViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingAreaVanViewModel): SettingAreaVanViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_area_van_view)
        mBinding.viewModel = mViewModel

        txt_title.text = "Schedule"
        btn_action_right.text = "Save"

        adapter = SettingAreaVanAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        viewModel.initViewModel()

        return mBinding
    }

    override fun viewLoaded(areaVans: MutableList<VanModel>) {
        adapter?.setData(areaVans)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun actionLeft(v: View) {
        actionLeft()

        shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, true)
    }

    fun actionRight(v: View) {
        showUnderConstruction()
        // Setup sms
        //Get data
    }

    //============================================

    override fun onAddSchedule(position: Int, stepSchedule: Int, holder: SettingAreaVanAdapter.ItemViewHolder) {
        adapter?.updateScheduleAdded(stepSchedule + 1, holder)

        awaitingUpdateDataChange(position)
    }

    override fun onRemoveSchedule(position: Int, stepSchedule: Int, holder: SettingAreaVanAdapter.ItemViewHolder) {
        adapter?.updateScheduleAdded(stepSchedule - 1, holder)

        awaitingUpdateDataChange(position)
    }

    private fun awaitingUpdateDataChange(position: Int) {
        handler.post {
            viewModel.updateDataChange(position)
        }
        try {
            // Sleep for 200 milliseconds.
            // Just to display the progress slowly
            Thread.sleep(100) //thread will take approx 3 seconds to finish
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onSetDuration(position: Int, holder: SettingAreaVanAdapter.ItemViewHolder, positionItem: Int) {
        var items = resources.getStringArray(R.array.times_working)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.setting_time_working))
                .setSingleChoiceItems(items, 0) { onDialogClicked, i ->

                    var result = items[i].toString()
                    holder.duration.text = items[i].toString()

                    //Notify UI
                    adapter?.updateDuration(position, holder, positionItem, result.split(" ")[0])
                    viewModel.updateDataChange(position)
                    if (currentPosition != position) {
                        awaitNotifyItemChange(currentPosition)
                        currentPosition = position
                    }
                }
                .setPositiveButton(getString(R.string.btn_chon), null)
                .setNegativeButton(getString(R.string.btn_huy), null)
                .show()
    }

    override fun onSchedule(position: Int, textView: TextView, positionItem: Int) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            textView.text = SimpleDateFormat("HH:mm").format(cal.time)

            Log.d("hqdat", "========= textView.text position ====  $textView.text")
            //Update data & notify UI
            adapter?.updateTimeSchedule(position, textView, positionItem, textView.text.toString())
            viewModel.updateDataChange(position)
            if (currentPosition != position) {
                currentPosition = position
                awaitNotifyItemChange(position)
            }
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun awaitNotifyItemChange(position: Int) {
        handler.post {
            adapter?.notifyItemChanged(position)
        }
        try {
            // Sleep for 200 milliseconds.
            // Just to display the progress slowly
            Thread.sleep(100) //thread will take approx 3 seconds to finish
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onCheckRepeat(position: Int, repeat: RepeatModel) {
            viewModel.updateDataRepeatChange(position, repeat)
    }

    //====== Start Set time schedule ==============





    //====== End Set time schedule ==============

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
