package com.motor.connect.feature.details

import com.motor.connect.base.view.actionbar.ActionBarView

interface AreaDetailView : ActionBarView {

    fun viewLoaded()

    fun viewMotorWorking()

    fun viewMotorInfo()
}