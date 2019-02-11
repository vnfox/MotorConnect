package com.motor.connect.feature.setting.van

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.VanModel

interface SettingAreaVanView : ActionBarView {

    fun viewLoaded(areaVans: MutableList<VanModel>)
}