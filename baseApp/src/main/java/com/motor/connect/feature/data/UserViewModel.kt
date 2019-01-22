package com.motor.connect.feature.data

import android.os.Handler
import android.util.Log
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class UserViewModel(mView: MainAearView?, mModel: BaseModel)
    : BaseViewModel<MainAearView, BaseModel>(mView, mModel) {

    var dataArea: MutableList<AreaModel> = mutableListOf()

    override fun initViewModel() {

    }

    /**
     * class CreatePlanViewModel(mView: CreatePlanView?, mModel: BaseModel)
    : BaseViewModel<CreatePlanView, BaseModel>(mView, mModel)
     */

//    @get:Bindable
//    var dataArea: MutableList<AreaModel> = mutableListOf()
//        private set(value) {
//            field = value
//
////            notifyPropertyChanged(BR.dataArea)
//            mView?.updateUI()
//        }

    private val updateInterval = 1000L
    private val updateHandler = Handler()

//    private var updateRunnable: Runnable = object : Runnable {
//        override fun run() {
//            initData(isUser)
//            updateHandler.postDelayed(this, updateInterval)
//        }
//    }

    fun initData(isFirst: Boolean?) {
        Log.d("hqdat", "===  isFirst =   $isFirst")
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
//        updateHandler.removeCallbacks(updateRunnable)
    }
}