package com.motor.connect.feature.setting.control

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.VanModel

interface SettingControlView : ActionBarView {

    fun fetchDataAgenda(areaVans: List<VanModel>)

    fun fetchDataManual(areaVans: List<VanModel>)

    fun prepareDataForAgenda(items: MutableList<VanModel>)

    fun prepareDataForManual(items: MutableList<VanModel>)
}