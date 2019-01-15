package com.motor.connect.feature.setting

import com.motor.connect.base.view.IBaseView

/**
 * Created by dathuynh on 8/23/18.
 */
interface SettingView : IBaseView {

    fun actionLeft()

    fun showSettingArea()

    fun showConfigSystem()

    fun showReminder()

    fun showHowToUse()

    fun showHelpFeedback()

}