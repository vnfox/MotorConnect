package com.motor.connect.feature.setting.area

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class SettingAreaScheduleViewModel(mView: SettingAreaScheduleView?, mModel: BaseModel)
    : BaseViewModel<SettingAreaScheduleView, BaseModel>(mView, mModel) {

    var model = AreaModel()
    override fun initViewModel() {
        //Check data => show UI
        model = Hawk.get<AreaModel>(MotorConstants.KEY_PUT_AREA_DETAIL)
        mView?.viewLoaded()
    }

    fun getAreaId(): String {
        return model.areaId
    }

    fun getAreaPhone(): String {
        return model.areaPhone
    }

    fun getAreaPassWord(): String {
        return model.password
    }
}