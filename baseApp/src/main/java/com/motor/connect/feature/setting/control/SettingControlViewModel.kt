package com.motor.connect.feature.setting.control

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class SettingControlViewModel(mView: SettingControlView?, mModel: BaseModel)
    : BaseViewModel<SettingControlView, BaseModel>(mView, mModel) {

    var model = AreaModel()

    override fun initViewModel() {
        model = Hawk.get<AreaModel>(MotorConstants.KEY_PUT_AREA_DETAIL)
        //Update UIS
        mView?.viewLoaded(model.areaVans)
    }

    fun getPhoneNumber(): String {
        return model.areaPhone
    }

    fun getAreaId(): String {
        return model.areaId
    }

    fun getPassWordArea(): String {
        return model.password
    }

    fun updateDataArea(listVans: List<VanModel>) {
        var areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        val position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        model.areaVans = listVans
        areaModels[position] = model
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
    }
}