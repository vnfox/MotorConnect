package com.motor.connect.feature.setting

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class SettingViewModel(mView: SettingView?, mModel: BaseModel)
    : BaseViewModel<SettingView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
        mView!!.viewLoaded()
    }

    fun actionLeft() = mView?.actionLeft()

    fun showSettingArea() = mView?.showSettingArea()

    fun showConfigSystem() = mView?.showConfigSystem()

    fun showReminder() = mView?.showReminder()

    fun showHowToUse() = mView?.showReminder()

    fun showHelpFeedback() = mView?.showConfigSystem()

}