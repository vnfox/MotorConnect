package com.motor.connect.feature.setting.control

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.telephony.SmsManager
import android.util.Log
import android.view.View
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
	private var smsContent1 = StringBuilder()
	private var smsContent2 = StringBuilder()
	private var isSpontaneous: Boolean = false
	private lateinit var timeManual: String
	
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
	}
	
	override fun fetchDataManual(areaVans: List<VanModel>) {
		rc_control.adapter = manualAdapter
		rc_control.layoutManager = GridLayoutManager(this, 1)
		manualAdapter?.setData(areaVans)
		rc_control.adapter?.notifyDataSetChanged()
	}
	
	fun actionLeft(v: View) {
		actionLeft()
	}
	
	fun actionRight(v: View) {
		viewModel.prepareDataSendSms()
	}
	
	fun manualControl(v: View) {
		isSpontaneous = false
		btn_agenda.background = getDrawable(R.drawable.bg_button_unselected)
		btn_manual.background = getDrawable(R.drawable.bg_button_selected)
		btn_spontaneous.background = getDrawable(R.drawable.bg_button_unselected)
		viewModel.updateAgendaWorking(false)
	}
	
	fun spontaneousControl(v: View) {
		isSpontaneous = true
		btn_agenda.background = getDrawable(R.drawable.bg_button_unselected)
		btn_manual.background = getDrawable(R.drawable.bg_button_unselected)
		btn_spontaneous.background = getDrawable(R.drawable.bg_button_selected)
		
		handler.post {
			onSetDurationForSpontaneous()
		}
		try {
			// Sleep for 200 milliseconds.
			// Just to display the progress slowly
			Thread.sleep(200) //thread will take approx 3 seconds to finish
		} catch (e: InterruptedException) {
			e.printStackTrace()
		}
	}
	
	fun agendaControl(v: View) {
		isSpontaneous = false
		btn_agenda.background = getDrawable(R.drawable.bg_button_selected)
		btn_manual.background = getDrawable(R.drawable.bg_button_unselected)
		btn_spontaneous.background = getDrawable(R.drawable.bg_button_unselected)
		viewModel.updateAgendaWorking(true)
	}
	
	override fun prepareDataForManual(items: MutableList<VanModel>) {
		// Make sure smsContent1 clear
		smsContent1.setLength(0)
		items.forEach {
			Log.d("hqdat", "================ Manual VAN ID ==========>>>>>>>    ${it.vanId}")
			getZoneAvailable(it.vanId.toInt())
		}
		var password = decimal2ATSSexagesimal(MotorConstants.PASSWORD_DEFAULT)
		var zoneAvailable: String = getAvailableATS(bit_mask)
		
		if (isSpontaneous) {
			smsContent1.append(MotorConstants.AreaCode.PREFIX_DO)
			smsContent1.append(password)
			smsContent1.append(zoneAvailable)
			smsContent1.append(getTimeSpontaneousATS(timeManual))
		} else {
			smsContent1.append(MotorConstants.AreaCode.PREFIX_DM)
			smsContent1.append(password)
			smsContent1.append(zoneAvailable)
			smsContent1.append("001")
		}
		
		Log.d("hqdat", "================ Manual SMS Content ==========>>>>>>>    $smsContent1")
		checkGrantedPermissionSms(smsContent1.toString())
	}
	
	//=============== Spontaneous =======================
	
	private val positiveClick = { _: DialogInterface, _: Int ->
		viewModel.updateAgendaWorking(false)
	}
	
	private val negativeClick = { _: DialogInterface, _: Int ->
		timeManual = ""
		viewModel.updateAgendaWorking(false)
	}
	
	private fun onSetDurationForSpontaneous() {
		var items = resources.getStringArray(R.array.times_spontaneous)
		AlertDialog.Builder(this)
				.setTitle(getString(R.string.setting_time_working))
				.setSingleChoiceItems(items, 0) { _, i ->
					var result = items[i].toString()
					
					timeManual = result
				}
				.setPositiveButton(getString(R.string.btn_chon), DialogInterface.OnClickListener(positiveClick))
				.setNegativeButton(getString(R.string.btn_huy), DialogInterface.OnClickListener(negativeClick))
				.show()
	}
	
	override fun prepareDataForAgenda(items: MutableList<VanModel>) {
		smsContent1.setLength(0)
		smsContent2.setLength(0)
		var password = decimal2ATSSexagesimal(MotorConstants.PASSWORD_DEFAULT)
		val (round1, round2) = viewModel.getDataZoneAvailable(items)
		
		when {
			round2.isEmpty() -> {  //====== Support 8 Wave ==================
				val (timeSchedule, zoneAvailable) = getTimeScheduleAndZoneAvailable(round1)
				
				smsContent1.append(MotorConstants.AreaCode.PREFIX_DN)
				smsContent1.append(password)
				
				smsContent1.append(zoneAvailable)
				smsContent1.append(timeSchedule)
				Log.d("hqdat", "================ Agenda SMS Content 1 =======\n ===>>>>>>>    $smsContent1")
				checkGrantedPermissionSms(smsContent1.toString())
			}
			else -> {  //======= Support 16 Wave ==================
				val (timeSchedule, zoneAvailable) = getTimeScheduleAndZoneAvailable(round1)
				smsContent1.append(MotorConstants.AreaCode.PREFIX_DH)
				smsContent1.append(password)
				
				smsContent1.append(zoneAvailable)
				smsContent1.append(timeSchedule)
				checkGrantedPermissionSms(smsContent1.toString())
				
				bit_mask = 0
				val (timeSchedule2, zoneAvailable2) = getTimeScheduleAndZoneAvailable(round2)
				smsContent2.append(MotorConstants.AreaCode.PREFIX_DN)
				smsContent2.append(password)
				
				smsContent2.append(zoneAvailable2)
				smsContent2.append(timeSchedule2)
				
				Log.d("hqdat", "================ Agenda SMS Content 1 =======\n ===>>>>>>>    $smsContent1")
				Log.d("hqdat", "================ Agenda SMS Content 2 =======\n ===>>>>>>>    $smsContent2")
			}
		}
	}
	
	private fun getTimeScheduleAndZoneAvailable(dataZone: MutableList<VanModel>): Pair<String, String> {
		var timeSchedule = StringBuilder()
		dataZone.forEach {
			getZoneAvailable(it.vanId.toInt())
			timeSchedule.append(getTimeScheduleAndDurationATS(it.schedule, it.duration))
		}
		var zoneAvailable: String = getAvailableATS(bit_mask)
		return Pair(timeSchedule.toString(), zoneAvailable)
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
				onSendSms(smsContent1.toString())
			}
		}
	}
	
	private fun handelSendSmsRound2(smsMessage: String) {
		handler.post {
			onSendSms(smsMessage)
		}
		try {
			// Sleep for 200 milliseconds.
			// Just to display the progress slowly
			Thread.sleep(200) //thread will take approx 3 seconds to finish
		} catch (e: InterruptedException) {
			e.printStackTrace()
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
						showUnderConstruction(getString(R.string.sms_sent))
						smsContent1.setLength(0)
						if (smsContent2.isNotEmpty()) {
							handelSendSmsRound2(smsContent2.toString())
							smsContent2.setLength(0)
						}
					}
					SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
						showUnderConstruction(getString(R.string.sms_send_failed))
					}
					SmsManager.RESULT_ERROR_NO_SERVICE -> {
						showUnderConstruction(getString(R.string.sms_send_failed))
					}
					SmsManager.RESULT_ERROR_NULL_PDU -> {
						showUnderConstruction(getString(R.string.sms_send_failed))
					}
					SmsManager.RESULT_ERROR_RADIO_OFF -> {
						showUnderConstruction(getString(R.string.sms_send_failed))
					}
				}
			}
		}, IntentFilter(SENT))
		
		//---when the SMS has been delivered---
		registerReceiver(object : BroadcastReceiver() {
			override fun onReceive(arg0: Context, arg1: Intent) {
				when (resultCode) {
					Activity.RESULT_OK -> {
						showUnderConstruction(getString(R.string.sms_delivered))
					}
					Activity.RESULT_CANCELED -> {
						showUnderConstruction(getString(R.string.sms_not_delivered))
					}
				}
			}
		}, IntentFilter(DELIVERED))
		
		val sms = SmsManager.getDefault()
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
	}
}