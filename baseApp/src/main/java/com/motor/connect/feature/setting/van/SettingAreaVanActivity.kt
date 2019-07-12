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
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.motor.connect.R
import com.motor.connect.databinding.SettingAreaVanViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
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
	private var smsContent = StringBuilder()
	
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
		smsContent.setLength(0)
		var dataZone = viewModel.getDataZone()
		var timeSchedule = StringBuilder()

		dataZone.forEach {
			getZoneAvailable(it.vanId.toInt())
			timeSchedule.append(getTimeScheduleAndDurationATS(it.schedule, it.duration))
			timeSchedule.append(getScheduleRepeat(it.repeatModel))
		}
		
		var zoneAvailable: String = getAvailableATS(bit_mask)
		var password = decimal2ATSSexagesimal(MotorConstants.PASSWORD_DEFAULT) // ATS password
		smsContent.append(MotorConstants.AreaCode.PREFIX_DE)
		smsContent.append(password)
		smsContent.append(zoneAvailable)
		smsContent.append(timeSchedule)
		
		Log.d("hqdat", "\n================  Scheduler SMS Content ========\n ===>>>>>>>    $smsContent")

		checkGrantedPermissionSms(smsContent.toString())
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
				
				onSendSms(smsContent.toString())
			}
		}
	}
	
	//---sends an SMS message to another device---
	
	private fun onSendSms( message: String) {
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
						Log.d("hqdat", "........  SMS sent")
						backPreviousScreen()
						showUnderConstruction("SMS Sent")
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
	
	private fun backPreviousScreen() {
		//Trigger Data
		shef!!.setUpdateData(MotorConstants.KEY_EDIT_AREA, true)
		
		actionLeft()
		this.finish()
	}
}
