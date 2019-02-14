package com.motor.connect.feature.setting.schedule

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class SettingScheduleViewModel(mView: SettingScheduleView?, mModel: BaseModel)
    : BaseViewModel<SettingScheduleView, BaseModel>(mView, mModel) {

    var dataArea: MutableList<AreaModel> = mutableListOf()

    override fun initViewModel() {

    }

    fun initData(isFirst: Boolean?) {
        if (isFirst!!) {
            mView?.showEmptyView()
        } else {
            dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
            mView?.updateUI(dataArea)
        }
    }

    fun updateData() {
        dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        mView?.updateUI(dataArea)
    }

    fun stopUpdates() {

    }
}