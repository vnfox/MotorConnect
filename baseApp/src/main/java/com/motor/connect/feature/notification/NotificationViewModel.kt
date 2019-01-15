package com.motor.connect.feature.notification

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class NotificationViewModel(mView: NotificationView?, mModel: BaseModel)
    : BaseViewModel<NotificationView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
        mView!!.viewLoaded()
    }

    fun actionLeft() = mView?.actionLeft()
}