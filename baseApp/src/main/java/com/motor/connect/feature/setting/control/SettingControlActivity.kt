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
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.databinding.SettingControlViewBinding
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.*
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.action_bar_view.*
import kotlinx.android.synthetic.main.setting_control_view.*


class SettingControlActivity : BaseViewActivity<SettingControlViewBinding, SettingControlViewModel>(),
		SettingControlView {
	
	companion object {
		fun show(context: Context) {
			context.startActivity(Intent(context, SettingControlActivity::class.java))
		}
	}
	
	private lateinit var needPermissions: MutableList<String>
	private var smsContent = StringBuilder()
	private var isSpontaneous: Boolean = false
	private lateinit var timeManual: String
	
	private val viewModel = SettingControlViewModel(this, BaseModel())
	private var manualAdapter: SettingControlAdapter? = null
	
	override fun createViewModel(): SettingControlViewModel {
		viewModel.mView = this
		return viewModel
	}
	
	override fun createDataBinding(mViewModel: SettingControlViewModel): SettingControlViewBinding {
		mBinding = DataBindingUtil.setContentView(this, R.layout.setting_control_view)
		mBinding.viewModel = mViewModel
		
		txt_title.text = getString(R.string.setting_control_title)
		btn_action_right.text = getString(R.string.setting_control_apply)
		//Adapter item click
		manualAdapter = SettingControlAdapter()
		
		//rc_control.adapter = agendaAdapter
		rc_control.layoutManager = GridLayoutManager(this, 1)
		viewModel.initViewModel()
		return mBinding
	}
	
	override fun fetchData(areaVans: List<VanModel>) {
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
		btn_manual.background = getDrawable(R.drawable.bg_button_selected)
		btn_spontaneous.background = getDrawable(R.drawable.bg_button_unselected)
		viewModel.updateAgendaWorking()
	}
	
	fun spontaneousControl(v: View) {
		isSpontaneous = true
		btn_manual.background = getDrawable(R.drawable.bg_button_unselected)
		btn_spontaneous.background = getDrawable(R.drawable.bg_button_selected)
		viewModel.clearDataSet(false)
		handler.post {
			onSetDurationForSpontaneous()
		}
		try {
			// Sleep for 200 milliseconds.
			// Just to display the progress slowly
			Thread.sleep(100)
		} catch (e: InterruptedException) {
			e.printStackTrace()
		}
	}
	
	override fun prepareDataSMS(items: MutableList<VanModel>) {
		bit_mask = 0
		smsContent.setLength(0)
		if (items.isEmpty()) {
			showDialogWarning(getString(R.string.sms_select_wave))
			return
		}
		
		items.forEach {
			getZoneAvailable(it.vanId.toInt())
		}
		var password = decimal2ATSSexagesimal(MotorConstants.PASSWORD_DEFAULT)
		var zoneAvailable: String = getAvailableATS(bit_mask)
		
		if (isSpontaneous) {
			smsContent.append(MotorConstants.AreaCode.PREFIX_DO)
			smsContent.append(password)
			smsContent.append(zoneAvailable)
			smsContent.append(getTimeSpontaneousATS(timeManual))
		} else {
			smsContent.append(MotorConstants.AreaCode.PREFIX_DM)
			smsContent.append(password)
			smsContent.append(zoneAvailable)
			smsContent.append("001")
		}
		
		Log.d("hqdat","======== sms  ====    $smsContent")
		//checkGrantedPermissionSms(smsContent.toString())
	}
	//=============== Spontaneous =======================
	
	private val positiveClick = { _: DialogInterface, _: Int ->
		viewModel.updateAgendaWorking()
	}
	
	private val negativeClick = { _: DialogInterface, _: Int ->
		timeManual = ""
		viewModel.updateAgendaWorking()
	}
	
	private fun onSetDurationForSpontaneous() {
		val items = resources.getStringArray(R.array.times_spontaneous)
		AlertDialog.Builder(this)
				.setTitle(getString(R.string.setting_time_working))
				.setSingleChoiceItems(items, 0) { _, i ->
					val result = items[i].toString()
					timeManual = result
				}
				.setPositiveButton(getString(R.string.btn_chon), DialogInterface.OnClickListener(positiveClick))
				.setNegativeButton(getString(R.string.btn_huy), DialogInterface.OnClickListener(negativeClick))
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
						showUnderConstruction(getString(R.string.sms_sent))
						smsContent.setLength(0)
					}
					SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
						showUnderConstruction(getString(R.string.sms_send_failed))
						smsContent.setLength(0)
					}
					SmsManager.RESULT_ERROR_NO_SERVICE -> {
						showUnderConstruction(getString(R.string.sms_send_failed))
						smsContent.setLength(0)
					}
					SmsManager.RESULT_ERROR_NULL_PDU -> {
						showUnderConstruction(getString(R.string.sms_send_failed))
						smsContent.setLength(0)
					}
					SmsManager.RESULT_ERROR_RADIO_OFF -> {
						showUnderConstruction(getString(R.string.sms_send_failed))
						smsContent.setLength(0)
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
	
	private fun showDialogWarning(message: String) {
		AlertDialog.Builder(this)
				.setTitle(getString(R.string.sms_warning_title))
				.setMessage(message)
				.setPositiveButton(getString(R.string.btn_ok), null)
				.show()
	}
}