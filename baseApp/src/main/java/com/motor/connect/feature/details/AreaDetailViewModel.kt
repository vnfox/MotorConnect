package com.motor.connect.feature.details

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class AreaDetailViewModel(mView: AreaDetailView?, mModel: BaseModel)
    : BaseViewModel<AreaDetailView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
        mView?.showLoadingView()
        //get data
        mView?.viewLoaded()

        updateAreaInfo()

        //get more info
//        mView?.viewMotorInfo(model)
        mView?.viewMotorWorking()


    }

    private fun updateAreaInfo() {
        val model = Hawk.get<AreaModel>(MotorConstants.KEY_PUT_AREA)

        mView?.viewMotorInfo(model)
    }
}