package com.motor.connect.feature.setting.control

import android.util.Log
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
		mView?.fetchData(model.areaVans)
	}
	
	private fun selectNavigateUi() {
		val vanModels: MutableList<VanModel> = Hawk.get(MotorConstants.KEY_PUT_LIST_VAN_MODEL)
		mView?.fetchData(vanModels)
	}
	
	fun getPhoneNumber(): String {
		return model.areaPhone
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
		var items: MutableList<VanModel> = mutableListOf()
		model.areaVans.forEachIndexed { index, vanModel ->
			if(vanModel.manual){
				items.add(vanModel)
			}
		}
		mView?.prepareDataSMS(items)
	}
	
	fun updateDataChange(position: Int, checked: Boolean) {
		var vanModels: MutableList<VanModel> = model.areaVans
		vanModels[position].manual = checked
		Hawk.put(MotorConstants.KEY_PUT_LIST_VAN_MODEL, vanModels)
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		model.areaVans = vanModels
		areaModels[pos] = model
		Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
	}
}