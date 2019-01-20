package com.motor.connect.feature.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Handler
import com.android.databinding.library.baseAdapters.BR
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class UserViewModel : BaseObservable() {

    @get:Bindable
    var dataArea: MutableList<AreaModel> = mutableListOf()
        private set(value) {
            field = value
            notifyPropertyChanged(BR.dataArea)
        }

    private val updateInterval = 1000L
    private val updateHandler = Handler()

    private var updateRunnable: Runnable = object : Runnable {
        override fun run() {
            updateList()
            updateHandler.postDelayed(this, updateInterval)
        }
    }

    fun updateList() {
        dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        notifyPropertyChanged(BR.dataArea)
    }

    fun stopUpdates() {
        updateHandler.removeCallbacks(updateRunnable)
    }
}