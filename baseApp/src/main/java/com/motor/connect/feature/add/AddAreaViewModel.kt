package com.motor.connect.feature.add

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class AddAreaViewModel(mView: com.motor.connect.feature.add.AddAreaView?, mModel: BaseModel)
    : BaseViewModel<com.motor.connect.feature.add.AddAreaView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
        mView!!.viewLoaded()
    }

    fun actionLeft() = mView?.actionLeft()
}