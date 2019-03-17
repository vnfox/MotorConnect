package com.motor.connect.feature.main

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class UserViewModel(mView: MainAreaView?, mModel: BaseModel)
    : BaseViewModel<MainAreaView, BaseModel>(mView, mModel) {

    var dataArea: MutableList<AreaModel> = mutableListOf()

    override fun initViewModel() {
    }

    fun initData(isFirst: Boolean?) {
        dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        if (isFirst!! || dataArea.isEmpty()) {
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
        dataArea.clear()
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, dataArea)
    }
}