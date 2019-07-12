package com.motor.connect.feature.details

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.StringUtil
import com.orhanobut.hawk.Hawk

class AreaDetailViewModel(mView: AreaDetailView?, mModel: BaseModel)
	: BaseViewModel<AreaDetailView, BaseModel>(mView, mModel) {
	
	var model = AreaModel()
	
	override fun initViewModel() {
		val areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		model = areaModels[pos]
		//get data
		mView?.viewLoaded()
		
		updateAreaInfo(model)
		Hawk.put(MotorConstants.KEY_VANS_USED, model.areaVans)
	}
	
	private fun updateAreaInfo(model: AreaModel) {
		val schedules = getScheduleWorking()
		mView?.viewAreaInfo(model, schedules)
	}
	
	fun updateInfoMotor() {
		val schedules = getScheduleWorking()
		mView?.updateInfoMotor(schedules, getVansUsed(model.areaVans))
	}
	
	private fun getVansUsed(areaVans: MutableList<VanModel>): String {
		val result = StringBuilder()
		areaVans.forEachIndexed { index, vanModel ->
			if (vanModel.vanStatus) {
				val position = index + 1
				if (position < 10) {
					result.append("0$position").append(" ")
				} else {
					result.append(position).append(" ")
				}
			}
		}
		return result.toString()
	}
	
	private fun getScheduleWorking(): String {
		var result = ""
		if (model.areaVans == null || model.areaVans.isEmpty())
			return "Chưa cài đặt lịch tưới"
		
		model.areaVans.forEach {
			if (it.schedule.isNotEmpty()) {
				result += StringUtil.getScheduleArea(it.schedule, it.vanId, it.duration)
			}
		}
		return result!!
	}
}