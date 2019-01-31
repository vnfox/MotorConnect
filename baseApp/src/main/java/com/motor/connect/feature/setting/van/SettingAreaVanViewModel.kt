package com.motor.connect.feature.setting.van

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class SettingAreaVanViewModel(mView: SettingAreaVanView?, mModel: BaseModel)
    : BaseViewModel<SettingAreaVanView, BaseModel>(mView, mModel)  {

    override fun initViewModel() {


        //Get data
        mView?.viewLoaded()

        //Hawk.put(MotorConstants.KEY_VANS_USED, model.areaVans)
    }
}