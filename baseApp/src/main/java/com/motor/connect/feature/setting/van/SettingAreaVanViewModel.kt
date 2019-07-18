package com.motor.connect.feature.setting.van

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.RepeatModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class SettingAreaVanViewModel(mView: SettingAreaVanView?, mModel: BaseModel)
	: BaseViewModel<SettingAreaVanView, BaseModel>(mView, mModel) {
	
	var dataArea: MutableList<AreaModel> = mutableListOf()
	var vansUsed: MutableList<VanModel> = mutableListOf()
	var model = AreaModel()
	
	override fun initViewModel() {
		dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		
		model = dataArea[pos]
		vansUsed = dataArea[pos].areaVans
		mView?.viewLoaded(dataArea[pos].areaVans)
	}
	
	fun getPhoneNumber(): String {
		return model.areaPhone
	}
	
	fun updateDataChange(position: Int) {
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		vansUsed[position] = Hawk.get<VanModel>(MotorConstants.KEY_PUT_VAN_MODEL)
		dataArea[pos].areaVans = vansUsed
		Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, dataArea)
	}
	
	fun updateDataRepeatChange(position: Int, repeat: RepeatModel) {
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		//Todo check if data empty
		vansUsed[position] = Hawk.get<VanModel>(MotorConstants.KEY_PUT_VAN_MODEL)
		
		vansUsed[position].repeatModel = repeat
		dataArea[pos].areaVans = vansUsed
		Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, dataArea)
	}
	
	fun getDataZoneAvailable(): Pair<MutableList<VanModel>, MutableList<VanModel>> {
		var zone1: MutableList<VanModel> = mutableListOf()
		var zone2: MutableList<VanModel> = mutableListOf()
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		
		dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		var data = dataArea[pos]
		data.areaVans.forEachIndexed { index, element ->
			if (element.schedule.isNotEmpty() && element.duration != null) {
				element.vanId = (index + 1).toString()
				when {
					index < 8 -> zone1.add(element)
					else -> zone2.add(element)
				}
			}
		}
		return Pair(zone1, zone2)
	}
}