package com.motor.connect.feature.setting.agenda

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.VanModel

interface SettingAgendaView : ActionBarView {

    fun viewLoaded(areaVans: MutableList<VanModel>)
}