package com.motor.connect.feature.notification

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.SmsModel

interface NotificationView : ActionBarView {

    fun updateUI(smsReceivers: MutableList<SmsModel>)

    fun showEmptyView()
}