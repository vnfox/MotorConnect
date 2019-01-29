package com.motor.connect.feature.details

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.AreaModel

interface AreaDetailView : ActionBarView {

    fun viewLoaded()

    fun viewMotorWorking()

    fun viewMotorInfo(model: AreaModel)
}