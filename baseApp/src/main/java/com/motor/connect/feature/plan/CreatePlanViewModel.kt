package com.motor.connect.feature.plan

import android.os.Handler
import android.util.Log
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class CreatePlanViewModel(mView: CreatePlanView?, mModel: BaseModel)
    : BaseViewModel<CreatePlanView, BaseModel>(mView, mModel) {

    private val updateInterval = 1000L
    private val updateHandler = Handler()

    override fun initViewModel() {
        mView!!.showLoadingView()
    }

    fun actionLeft() = mView?.actionLeft()

    fun typeYourIdea() = mView?.typeYourIdea()

    fun setTimeDuration() = mView?.setTimeDuration()

    fun inviteFriends() = mView?.inviteFriends()

    fun checkUpdateUI() {
        Log.d("hqdat", "== Progress Change >>>>>> ")

        mView?.updateUI()
        getString()
    }

    private fun getString() {
        Log.d("hqdat", "== in updateUI >>>>>> ")
    }
}