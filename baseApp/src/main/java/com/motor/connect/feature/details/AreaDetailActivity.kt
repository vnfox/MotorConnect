package com.motor.connect.feature.details

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.motor.connect.R
import com.motor.connect.databinding.DetailViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.edit.EditAreaActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.setting.control.SettingControlActivity
import com.motor.connect.feature.setting.van.SettingAreaVanActivity
import com.motor.connect.utils.MotorConstants
import kotlinx.android.synthetic.main.detail_view.*


class AreaDetailActivity : BaseViewActivity<DetailViewBinding, AreaDetailViewModel>(), AreaDetailView {
	
	
	companion object {
		fun show(context: Context) {
			context.startActivity(Intent(context, AreaDetailActivity::class.java))
		}
	}
	
	private val viewModel = AreaDetailViewModel(this, BaseModel())
	private var workingProgress: CircularProgressIndicator? = null
	private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
	private var bottomSheetDialog: BottomSheetDialog? = null
	
	override fun createViewModel(): AreaDetailViewModel {
		viewModel.mView = this
		return viewModel
	}
	
	override fun createDataBinding(mViewModel: AreaDetailViewModel): DetailViewBinding {
		
		mBinding = DataBindingUtil.setContentView(this, R.layout.detail_view)
		mBinding.viewModel = mViewModel
		
		//Setup Account Bar
		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		toolbar.setNavigationIcon(R.mipmap.ic_back_white)
		toolbar.setNavigationOnClickListener {
			actionLeft()
		}
		workingProgress = findViewById(R.id.working_progress)
		
		//Setup info
		showLoadingView(getString(R.string.sms_loading))
		viewModel.initViewModel()
		
		//Setup CollapsingToolbarLayout View
		collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
		collapsingToolbarLayout?.title = getString(R.string.detail_title)
		
		return mBinding
	}
	
	override fun onResume() {
		super.onResume()
		if (shef!!.getUpdateData(MotorConstants.KEY_EDIT_AREA) || shef!!.getUpdateData(MotorConstants.KEY_TRIGGER_DATA)) {
			shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, false)
			viewModel.initViewModel()
		}
	}
	
	override fun onDestroy() {
		super.onDestroy()
		workingProgress = null
		viewModel.destroy()
	}
	
	override fun updateAreaInfoWhenEdit(model: AreaModel) {
		txt_area_name.text = model.areaName
		txt_area_phone.text = model.areaPhone
		txt_area_van.text = getString(R.string.detail_van_total) + model.areaVans.size.toString()
	}
	
	override fun viewLoaded() {
		loadBackdrop()
	}
	
	override fun viewAreaInfo(model: AreaModel, schedules: String) {
		txt_area_name.text = model.areaName
		txt_area_phone.text = model.areaPhone
		
		//should show van open
		txt_area_van.text = String.format(getString(R.string.detail_van_total), model.areaVans.size.toString())
		txt_area_scheduler.text = schedules
		txt_area_detail.text = model.areaDetails
		
		//update Motor info
		hideLoadingView()
		viewModel.updateInfoMotor()
	}
	
	override fun updateInfoMotor(areaStatus: String?, vansUsed: String) {
		txt_area_status.text = String.format(getString(R.string.detail_status), areaStatus)
		txt_area_van_used.text = String.format(getString(R.string.detail_van_open), vansUsed)
	}
	
	private fun loadBackdrop() {
		val imageView = findViewById<ImageView>(R.id.backdrop)
		Glide.with(this).load(BackDrop.getRandomBackDrop()).apply(RequestOptions.centerCropTransform()).into(imageView)
	}
	
	fun selectSettingView(v: View) {
		bottomSheetDialog = BottomSheetDialog(this)
		val sheetView = this.layoutInflater.inflate(R.layout.dialog_bottom_sheet_view, null)
		bottomSheetDialog?.setContentView(sheetView)
		bottomSheetDialog?.show()
	}
	
	fun setupScheduleArea(v: View) {
		bottomSheetDialog?.dismiss()
		SettingAreaVanActivity.show(this)
	}
	
	fun controlAgenda(v: View) {
		bottomSheetDialog?.dismiss()
		SettingControlActivity.show(this)
	}
	
	fun editInfoArea(v: View) {
		EditAreaActivity.show(this)
		bottomSheetDialog?.dismiss()
	}
}