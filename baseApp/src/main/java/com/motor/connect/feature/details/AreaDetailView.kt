package com.motor.connect.feature.details

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.AreaModel

interface AreaDetailView : ActionBarView {

    fun viewLoaded()

    fun viewMotorWorking(s: String, s1: String, maxValue: Int, currentTime: Int)

    fun viewAreaInfo(model: AreaModel, schedules: String)

    fun updateInfoMotor(areaStatus: String?, vansUsed: String, scheduleWorking: String?)
}