package com.motor.connect.feature.setting.van

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class SettingAreaVanViewModel(mView: SettingAreaVanView?, mModel: BaseModel)
    : BaseViewModel<SettingAreaVanView, BaseModel>(mView, mModel) {


    override fun initViewModel() {

        val model = Hawk.get<AreaModel>(MotorConstants.KEY_PUT_AREA)

        //Update UIS
        mView?.viewLoaded(model.areaVans)

    }

    //Save data
    fun updateDataModel(areaVans: MutableList<VanModel>) {

    }
}