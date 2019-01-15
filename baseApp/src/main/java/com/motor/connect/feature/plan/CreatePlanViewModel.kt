package com.motor.connect.feature.plan

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class CreatePlanViewModel(mView: CreatePlanView?, mModel: BaseModel)
    : BaseViewModel<CreatePlanView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
        mView!!.showLoadingView()
    }

    fun actionLeft() = mView?.actionLeft()

    fun actionRight() = mView?.actionRight()

    fun typeYourIdea() = mView?.typeYourIdea()

    fun setTimeDuration() = mView?.setTimeDuration()

    fun setUpActionBar() = mView?.setUpActionBar()

    fun inviteFriends() = mView?.inviteFriends()

}