package com.motor.connect.feature.setting.van

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.TextView
import com.motor.connect.R
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.databinding.SettingAreaVanViewBinding
import com.motor.connect.feature.model.RepeatModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.*
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
	private var currentPosition = 0
	private var smsContentRound1 = StringBuilder()
	private var smsContentRound2 = StringBuilder()
	
	private val viewModel = SettingAreaVanViewModel(this, BaseModel())
	private var adapter: SettingAreaVanAdapter? = null
	
	override fun createViewModel(): SettingAreaVanViewModel {
		viewModel.mView = this
		return viewModel
	}
	
	override fun createDataBinding(mViewModel: SettingAreaVanViewModel): SettingAreaVanViewBinding {
		mBinding = DataBindingUtil.setContentView(this, R.layout.setting_area_van_view)
		mBinding.viewModel = mViewModel
		
		txt_title.text = getString(R.string.text_schedule)
		btn_action_right.text = getString(R.string.btn_save)
		
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
		bit_mask = 0
		smsContentRound1.setLength(0)
		smsContentRound2.setLength(0)
		
		var password = decimal2ATSSexagesimal(MotorConstants.PASSWORD_DEFAULT)
		val (round1, round2) = viewModel.getDataZoneAvailable()
		
		when {
			round2.isEmpty() -> {  // Support 8 Wave
				//round 1
				val (timeSchedule, zoneAvailable) = getTimeScheduleAndZoneAvailable(round1)
				
				smsContentRound1.append(MotorConstants.AreaCode.PREFIX_DE)
				smsContentRound1.append(password)
				smsContentRound1.append(zoneAvailable)
				smsContentRound1.append(timeSchedule)
				checkGrantedPermissionSms(smsContentRound1.toString())
			}
			else -> { // Support 16 Wave
				val (timeSchedule, zoneAvailable) = getTimeScheduleAndZoneAvailable(round1)
				
				smsContentRound1.append(MotorConstants.AreaCode.PREFIX_DG)
				smsContentRound1.append(password)
				smsContentRound1.append(zoneAvailable)
				smsContentRound1.append(timeSchedule)
				checkGrantedPermissionSms(smsContentRound1.toString())
				
				//======== Round2 =================
				bit_mask = 0
				val (timeSchedule2, zoneAvailable2) = getTimeScheduleAndZoneAvailable(round2)
				smsContentRound2.append(MotorConstants.AreaCode.PREFIX_DE)
				smsContentRound2.append(password)
				smsContentRound2.append(zoneAvailable2)
				smsContentRound2.append(timeSchedule2)
			}
		}
		Log.d("hqdat", "================  Scheduler SMS Round 1 ========\n ===>>>>>>>    $smsContentRound1")
		Log.d("hqdat", "================  Scheduler SMS Round 2 ========\n ===>>>>>>>    $smsContentRound2")
	}
	
	private fun getTimeScheduleAndZoneAvailable(dataZone: MutableList<VanModel>): Pair<String, String> {
		var timeSchedule = StringBuilder()
		
		dataZone.forEach {
			getZoneAvailable(it.vanId.toInt())
			Log.d("hqdat", "================  VAN ID ===========>>>>>>>    ${it.vanId}")
			timeSchedule.append(getTimeScheduleAndDurationATS(it.schedule, it.duration))
			timeSchedule.append(getScheduleRepeat(it.repeatModel))
		}
		
		var zoneAvailable: String = getAvailableATS(bit_mask)
		return Pair(timeSchedule.toString(), zoneAvailable)
	}
	
	private fun getScheduleRepeat(repeatModel: RepeatModel): String {
		var scheduleRepeat = 0
		for (i in 0..6) {
			scheduleRepeat += getDayOfWeek(i, repeatModel)
		}
		return getDayOfWeekATS(scheduleRepeat)
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
					result = if (result.contentEquals("None")) "00" else {
						result.split(" ")[0]
					}
					//Notify UI
					adapter?.updateDuration(position, holder, positionItem, result)
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
	
	private fun checkGrantedPermissionSms(smsContent: String) {
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
				
				onSendSms(smsContentRound1.toString())
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
						smsContentRound1.setLength(0)
						if(smsContentRound2.isNotEmpty()){
							handelSendSmsRound2(smsContentRound2.toString())
							smsContentRound2.setLength(0)
						} else {
							backPreviousScreen()
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
						// Update UI
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
	
	private fun backPreviousScreen() {
		//Trigger Data
		shef!!.setUpdateData(MotorConstants.KEY_EDIT_AREA, true)
		actionLeft()
		this.finish()
	}
}
