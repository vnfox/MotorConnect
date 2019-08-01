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
		selectNavigateUi(false)
	}
	
	private fun selectNavigateUi(agenda: Boolean?) {
		when {
			agenda!! -> mView?.fetchDataAgenda(model.areaVans)
			else -> mView?.fetchDataManual(model.areaVans)
		}
		//set default data
		clearDataSet(agenda)
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
	
	fun updateDataChange(position: Int) {
		areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		model.areaVans[position] = Hawk.get<VanModel>(MotorConstants.KEY_PUT_VAN_MODEL)
		areaModels[pos] = model
		Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
	}
	
	fun updateAgendaWorking(isAgenda: Boolean) {
		areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		
		model = areaModels[pos]
		model.agenda = isAgenda
		areaModels[pos] = model
		Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
		
		//Update UI
		selectNavigateUi(isAgenda)
	}
	
	fun prepareDataSendSms() {
		val vanModels: MutableList<VanModel> = Hawk.get(MotorConstants.KEY_PUT_LIST_VAN_MODEL)
		var items: MutableList<VanModel> = mutableListOf()
		if (model.agenda) {
			vanModels.forEach {
				if (!it.duration.isNullOrEmpty() && it.schedule.isNotEmpty()
						&& !it.duration.contentEquals("00")) {
					items.add(it)
				}
			}
			mView?.prepareDataForAgenda(items)
		} else {
			vanModels.forEach {
				if (it.manual) {
					items.add(it)
				}
			}
			mView?.prepareDataForManual(items)
		}
	}
	
	fun getDataZoneAvailable(items: MutableList<VanModel>): Pair<MutableList<VanModel>, MutableList<VanModel>> {
		var zone1: MutableList<VanModel> = mutableListOf()
		var zone2: MutableList<VanModel> = mutableListOf()
		
		items.forEachIndexed { index, element ->
			when {
				index < 8 -> zone1.add(element)
				else -> zone2.add(element)
			}
		}
		return Pair(zone1, zone2)
	}
}