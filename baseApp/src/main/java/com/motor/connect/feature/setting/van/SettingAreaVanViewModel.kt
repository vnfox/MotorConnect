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

    var model = AreaModel()

    override fun initViewModel() {
        val areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)

        model = areaModels[pos]

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

    fun updateDataChange(position: Int) {
        val areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)

        model.areaVans[position] = Hawk.get<VanModel>(MotorConstants.KEY_PUT_VAN_MODEL)

        areaModels[pos] = model
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
    }

    fun updateDataRepeatChange(position: Int, repeat: RepeatModel) {
        val areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        val vanModel = Hawk.get<VanModel>(MotorConstants.KEY_PUT_VAN_MODEL)

        vanModel.repeatModel = repeat
        model.areaVans[position] = vanModel

        areaModels[pos] = model
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)

    }
}