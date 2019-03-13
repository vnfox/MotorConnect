package com.motor.connect.feature.setting.help

import com.motor.connect.base.view.actionbar.ActionBarView

interface HelpFeedbackView : ActionBarView {

    fun viewLoaded(url: String)

}