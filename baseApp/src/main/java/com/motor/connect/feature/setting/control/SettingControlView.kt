package com.motor.connect.feature.setting.control

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.VanModel

interface SettingControlView : ActionBarView {
    
    fun fetchData(areaVans: List<VanModel>)
    
    fun prepareDataSMS(items: MutableList<VanModel>)
}