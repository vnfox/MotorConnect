package com.motor.connect.feature.setting.schedule

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.motor.connect.R
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.databinding.SettingScheduleActivityBinding
import com.motor.connect.feature.add.AddAreaActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.setting.van.SettingAreaVanActivity
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.setting_schedule_activity.*


class SettingScheduleActivity : BaseViewActivity<SettingScheduleActivityBinding, SettingScheduleViewModel>(),
		SettingScheduleView {
	
	companion object {
		fun show(context: Context) {
			context.startActivity(Intent(context, SettingScheduleActivity::class.java))
		}
	}
	
	private val viewModel = SettingScheduleViewModel(this, BaseModel())
	private var adapter: SettingScheduleAdapter? = null
	
	override fun createViewModel(): SettingScheduleViewModel {
		viewModel.mView = this
		return viewModel
	}
	
	override fun createDataBinding(mViewModel: SettingScheduleViewModel): SettingScheduleActivityBinding {
		mBinding = DataBindingUtil.setContentView(this, R.layout.setting_schedule_activity)
		mBinding.viewModel = mViewModel
		
		//Adapter item click
		adapter = SettingScheduleAdapter { areaModel, position ->
			
			//Setting schedule area
			Hawk.put(MotorConstants.KEY_POSITION, position)
			Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, areaModel)
			SettingAreaVanActivity.show(this)
		}
		
		recyclerView.adapter = adapter
		recyclerView.layoutManager = GridLayoutManager(this, 2)
		
		val isUser = shef?.getFirstUserPref(MotorConstants.FIRST_USED)
		viewModel.initData(isUser)
		return mBinding
	}
	
	fun onBackSettingScreen(v: View) {
		actionLeft()
	}
	
	override fun showEmptyView() {
		txt_empty.visibility = View.VISIBLE
	}
	
	override fun updateUI(dataArea: MutableList<AreaModel>) {
		txt_empty.visibility = View.GONE
		adapter?.setData(dataArea)
		recyclerView.adapter?.notifyDataSetChanged()
	}
	
	override fun onResume() {
		super.onResume()
		if (shef!!.getTriggerData(MotorConstants.KEY_TRIGGER_DATA)) {
			viewModel.updateData()
			shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, false)
		}
	}
	
	override fun onDestroy() {
		viewModel.stopUpdates()
		super.onDestroy()
	}
	
	fun openEmptyScreen(v: View) {
		AddAreaActivity.show(this)
	}
}
