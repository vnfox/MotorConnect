package com.motor.connect.feature.home

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class HomeViewModel(mView: HomeView?, mModel: BaseModel)
    : BaseViewModel<HomeView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
        mView!!.showLoadingView()
    }

    fun showAddArea() = mView?.showAddArea()

    fun showHomePlanning() = mView?.showHomePlanning()

    fun showSetting() = mView?.showSetting()

    fun showNotification() = mView?.showNotification()




}