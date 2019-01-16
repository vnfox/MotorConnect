package com.motor.connect.feature.notification

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.motor.connect.feature.model.SmsModel

class NotificationViewModel : BaseObservable() {

    @get:Bindable
    var smsReceivers: MutableList<SmsModel> = mutableListOf()
        private set(value) {
            field = value
            notifyPropertyChanged(BR.dataArea)
        }

    fun startUpdates() {
        initData()
    }

    private fun initData() {


    }
}