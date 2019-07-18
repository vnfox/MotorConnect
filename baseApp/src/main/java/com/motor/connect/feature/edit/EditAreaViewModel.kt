package com.motor.connect.feature.edit

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.getVanId
import com.orhanobut.hawk.Hawk

class EditAreaViewModel(mView: EditAreaView?, mModel: BaseModel)
	: BaseViewModel<EditAreaView, BaseModel>(mView, mModel) {
	
	var dataArea: MutableList<AreaModel> = mutableListOf()
	var vansUsed: MutableList<VanModel> = mutableListOf()
	
	override fun initViewModel() {
		var position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		vansUsed = dataArea[position].areaVans
		
		mView?.viewLoaded(dataArea[position])
	}
	
	fun getNumberVanUsed(): Int {
		var position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		vansUsed = dataArea[position].areaVans
		return vansUsed.size
	}
	
	//Refactor update data
	fun updateDataArea(dataModel: AreaModel) {
		Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, dataModel)
		var position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		
		dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		dataModel.areaVans = vansUsed
		dataArea[position] = dataModel
		Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, dataArea)
		
		mView?.backDetailScreen()
	}
	
	fun updateVansUsed(isAddVan: Boolean, size: Int) {
		if (isAddVan) {
			val van = VanModel()
			van.vanId = getVanId(size)
			van.vanStatus = true
			vansUsed.add(van)
		} else {
			this.vansUsed.removeAt(size)
		}
		var position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
		dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
		dataArea[position].areaVans = vansUsed
		Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, dataArea)
	}
}