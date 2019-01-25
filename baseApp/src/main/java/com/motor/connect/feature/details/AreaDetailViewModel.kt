package com.motor.connect.feature.details

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class AreaDetailViewModel(mView: AreaDetailView?, mModel: BaseModel)
    : BaseViewModel<AreaDetailView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
        //get data
        mView?.viewLoaded()

        //get more info

        mView?.viewMotorInfo()

        mView?.viewMotorWorking()
    }
}