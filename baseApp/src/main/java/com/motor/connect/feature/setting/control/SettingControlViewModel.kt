package com.motor.connect.feature.setting.control

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class SettingControlViewModel(mView: SettingControlView?, mModel: BaseModel)
	: BaseViewModel<SettingControlView, BaseModel>(mView, mModel) {
	
	var areaModels: MutableList<AreaModel> = mutableListOf()
	var model = AreaModel()
	
	override fun initViewModel() {
		areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		
		model = areaModels[pos]
		selectNavigateUi()
	}
	
	private fun selectNavigateUi() {
		mView?.fetchData(model.areaVans)
		//set default data
//		clearDataSet(agenda)
	}
	
	fun getPhoneNumber(): String {
		return model.areaPhone
	}
	
	fun clearDataSet(agenda: Boolean) {
		val vanModels: MutableList<VanModel> = Hawk.get(MotorConstants.KEY_PUT_LIST_VAN_MODEL)
		vanModels.forEach {
			it.manual = false
		}
		model.areaVans.forEach {
			it.manual = false
		}
		model.agenda = agenda
		Hawk.put(MotorConstants.KEY_PUT_LIST_VAN_MODEL, vanModels)
	}
	
	fun updateAgendaWorking() {
		areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		
		model = areaModels[pos]
		areaModels[pos] = model
		Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
		
		//Update UI
		selectNavigateUi()
	}
	
	fun prepareDataSendSms() {
		val vanModels: MutableList<VanModel> = Hawk.get(MotorConstants.KEY_PUT_LIST_VAN_MODEL)
		var items: MutableList<VanModel> = mutableListOf()
		vanModels.forEach {
			if (it.manual) {
				items.add(it)
			}
		}
		mView?.prepareDataSMS(items)
	}
}