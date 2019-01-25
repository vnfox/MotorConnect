package com.motor.connect.feature.setting.area

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class SettingAreaScheduleViewModel(mView: SettingAreaScheduleView?, mModel: BaseModel)
    : BaseViewModel<SettingAreaScheduleView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
        //Check data => show UI
        mView?.viewLoaded()
    }
}