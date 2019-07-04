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
	
	/*private fun onSendSms(smsContent: String) {
		//Send sms in background
		showLoadingView(getString(R.string.sms_sending))
		val smsNumber = viewModel.getPhoneNumber()
		Log.d("hqdat", ">>>>>  smsText   $smsContent")
		val smsManager = SmsManager.getDefault()
		var pStatus: Int = 0
		
		Thread(Runnable {
			while (pStatus < MotorConstants.TIME_PROGRESS) {
				pStatus += 1
				handler.post {
					if (pStatus == MotorConstants.TIME_PROGRESS) {
						hideLoadingView()
						//Send sms
						smsManager.sendTextMessage(smsNumber, null, smsContent, null, null)
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
	}*/
	
	
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
						Toast.makeText(baseContext, "SMS sent",
								Toast.LENGTH_SHORT).show()
						Log.d("hqdat", "........  SMS sent")
						backPreviousScreen()
						//delete message
						//handleDeleteMessage(phoneNumber, message)
					}
					SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
						Toast.makeText(baseContext, "Generic failure",
								Toast.LENGTH_SHORT).show()
						Log.d("hqdat", "........  Generic failure")
					}
					SmsManager.RESULT_ERROR_NO_SERVICE -> {
						Toast.makeText(baseContext, "No service",
								Toast.LENGTH_SHORT).show()
						Log.d("hqdat", "........  No Service")
					}
					SmsManager.RESULT_ERROR_NULL_PDU -> {
						Toast.makeText(baseContext, "Null PDU",
								Toast.LENGTH_SHORT).show()
						Log.d("hqdat", "........  Null PDU")
					}
					SmsManager.RESULT_ERROR_RADIO_OFF -> {
						Toast.makeText(baseContext, "Radio off",
								Toast.LENGTH_SHORT).show()
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
						Toast.makeText(baseContext, "SMS delivered",
								Toast.LENGTH_SHORT).show()
						
						Log.d("hqdat", "........  SMS delivered")
						// Update UI
					}
					Activity.RESULT_CANCELED -> {
						Toast.makeText(baseContext, "SMS not delivered",
								Toast.LENGTH_SHORT).show()
						Log.d("hqdat", "........  SMS not delivered")
						//Update UI
					}
				}
			}
		}, IntentFilter(DELIVERED))
		
		val sms = SmsManager.getDefault()
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
	}
	
	private fun handleDeleteMessage(number: String, message: String) {
		// val handler = Handler()
		showLoadingView("Delete message ...")
		handler.postDelayed(Runnable {
			
			deleteSMS(this, number, message)
			hideLoadingView()
			
			backPreviousScreen()
			this.finish()
		}, 3000)
	}
	
	private fun deleteSMS(ctx: Context, number: String, message: String) {
		try {
			val ADDRESS_COLUMN_NAME = "address"
			val DATE_COLUMN_NAME = "date"
			val BODY_COLUMN_NAME = "body"
			val ID_COLUMN_NAME = "_id"
			
			val uriSms = Uri.parse("content://sms")
			val c = ctx.contentResolver.query(uriSms,
					arrayOf("_id", "thread_id", "address", "person", "date", "body"), null, null, null)
			
			
			// Defines selection criteria for the rows you want to delete
			val mSelectionClause = "$ADDRESS_COLUMN_NAME = ? AND $BODY_COLUMN_NAME = ? AND $DATE_COLUMN_NAME = ?"
			
			
			Log.i("hqdat", "c count......" + c!!.count)
			if (c != null && c.moveToFirst()) {
				do {
					
					val id = c.getLong(0)
					val threadId = c.getLong(1)
					val address = c.getString(2)
					val body = c.getString(5)
					val date = c.getString(3)
					
					val mSelectionArgs = arrayOfNulls<String>(3)
					mSelectionArgs[0] = address
					mSelectionArgs[1] = body
					mSelectionArgs[2] = threadId.toString()
					
					if (message == body && address == number) {
						Log.d("hqdat", "Deleting SMS with id: $threadId")
						val rows = ctx.contentResolver.delete(Uri.parse("content://sms/$id"),
								null,
								null)
						//ctx.contentResolver.delete()
						Log.d("hqdat", "Delete success......... rows: $rows")
						Log.d("hqdat", "Delete success......... body: $body")
					}
				} while (c.moveToNext())
			}
			
		} catch (e: Exception) {
			Log.e("hqdat", e.toString())
			Log.e("hqdat", e.message)
		}
	}
	
	private fun backPreviousScreen() {
		//Trigger Data
		shef!!.setUpdateData(MotorConstants.KEY_EDIT_AREA, true)
		
		actionLeft()
		this.finish()
	}
}
