package com.motor.connect.feature.setting.control

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.motor.connect.R
import com.motor.connect.databinding.SettingControlViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.*
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
	private var smsContent = StringBuilder()
	
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
	
	override fun onSetDuration(position: Int, holder: SettingControlAgendaAdapter.ItemViewHolder) {
		var items = resources.getStringArray(R.array.times_working)
		AlertDialog.Builder(this)
				.setTitle(getString(R.string.setting_time_working))
				.setSingleChoiceItems(items, 0) { _, i ->
					var result = items[i].toString()
					holder.timeWorking.text = items[i].toString()
					
					result = if (result.contentEquals("None")) "00" else {
						result.split(" ")[0]
					}
					//Notify UI
					agendaAdapter?.updateDuration(position, holder, result)
					viewModel.updateDataChange(position)
				}
				.setPositiveButton(getString(R.string.btn_chon), null)
				.setNegativeButton(getString(R.string.btn_huy), null)
				.show()
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
		// Make sure smsContent clear
		smsContent.setLength(0)
		items.forEach {
			getZoneAvailable(it.vanId.toInt())
		}
		var password = decimal2ATSSexagesimal(MotorConstants.PASSWORD_DEFAULT)
		var zoneAvailable: String = getAvailableATS(bit_mask)
		
		smsContent.append(MotorConstants.AreaCode.PREFIX_DM)
		smsContent.append(password)
		smsContent.append(zoneAvailable)
		smsContent.append("001")
		Log.d("hqdat", "================ Manual SMS Content =======\n ===>>>>>>>    $smsContent")
		checkGrantedPermissionSms(smsContent.toString())
	}
	
	override fun prepareDataForAgenda(items: MutableList<VanModel>) {
		smsContent.setLength(0)
		var timeSchedule = StringBuilder()
		items.forEach {
			getZoneAvailable(it.vanId.toInt())
			timeSchedule.append(getTimeScheduleAndDurationATS(it.schedule, it.duration))
		}
		
		var password = decimal2ATSSexagesimal(MotorConstants.PASSWORD_DEFAULT)
		var zoneAvailable: String = getAvailableATS(bit_mask)
		
		smsContent.append(MotorConstants.AreaCode.PREFIX_DN)
		smsContent.append(password)
		
		smsContent.append(zoneAvailable)
		smsContent.append(timeSchedule)
		
		// setup send sms
		Log.d("hqdat", "================ Agenda SMS Content =======\n ===>>>>>>>    $smsContent")
		checkGrantedPermissionSms(smsContent.toString())
	}
	
	private fun getTimeScheduleAndDurationATS(schedule: List<String>, duration: String): String {
		val result = StringBuilder()
		var schedule = schedule.reversed()
		result.append(schedule.size)
		
		schedule.forEachIndexed { index, element ->
			result.append(getScheduleTimeATS(element))
			result.append(getTimeDurationATS(duration))
		}
		return result.toString()
	}
	
	//Check Grant permission
	private fun checkGrantedPermissionSms(smsContent: String) {
		if (PermissionUtils.isGranted(this,
						Manifest.permission.SEND_SMS)) {
			onSendSms(smsContent)
		} else {
			needPermissions.add(Manifest.permission.SEND_SMS)
			PermissionUtils.isPermissionsGranted(this, needPermissions.toTypedArray(), MotorConstants.PERMISSION_REQUEST_CODE)
		}
	}
	
	override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
		when (requestCode) {
			MotorConstants.PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				onSendSms(smsContent.toString())
			}
		}
	}
	
	//---sends an SMS message to another device---
	
	private fun onSendSms(message: String) {
		val SENT = "SMS_SENT"
		val DELIVERED = "SMS_DELIVERED"
		val phoneNumber = viewModel.getPhoneNumber()
		
		val sentPI = PendingIntent.getBroadcast(this, 0,
				Intent(SENT), 0)
		
		val deliveredPI = PendingIntent.getBroadcast(this, 0,
				Intent(DELIVERED), 0)
		showLoadingView(getString(R.string.sms_sending))
		
		//---when the SMS has been sent---
		registerReceiver(object : BroadcastReceiver() {
			override fun onReceive(arg0: Context, arg1: Intent) {
				hideLoadingView()
				when (resultCode) {
					Activity.RESULT_OK -> {
						showUnderConstruction("SMS Sent")
						Log.d("hqdat", "........  SMS sent")
					}
					SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
						showUnderConstruction("SMS sent failed")
						Log.d("hqdat", "........  Generic failure")
					}
					SmsManager.RESULT_ERROR_NO_SERVICE -> {
						showUnderConstruction("SMS sent failed")
						Log.d("hqdat", "........  No Service")
					}
					SmsManager.RESULT_ERROR_NULL_PDU -> {
						showUnderConstruction("SMS sent failed")
						Log.d("hqdat", "........  Null PDU")
					}
					SmsManager.RESULT_ERROR_RADIO_OFF -> {
						showUnderConstruction("SMS sent failed")
						Log.d("hqdat", "........  Radio off")
					}
				}
			}
		}, IntentFilter(SENT))
		
		//---when the SMS has been delivered---
		registerReceiver(object : BroadcastReceiver() {
			override fun onReceive(arg0: Context, arg1: Intent) {
				when (resultCode) {
					Activity.RESULT_OK -> {
						showUnderConstruction("SMS delivered")
						Log.d("hqdat", "........  SMS delivered")
						// Update UI
					}
					Activity.RESULT_CANCELED -> {
						showUnderConstruction("SMS not delivered")
						Log.d("hqdat", "........  SMS not delivered")
					}
				}
			}
		}, IntentFilter(DELIVERED))
		
		val sms = SmsManager.getDefault()
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
	}
}