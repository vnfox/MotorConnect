package com.motor.connect.feature.setting.help

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class HelpFeedbackViewModel(view: HelpFeedbackView?, model: BaseModel) : BaseViewModel<HelpFeedbackView, BaseModel>(view, model) {

    override fun initViewModel() {
        var url = "file:///android_asset/help_feedback.html"

        mView?.viewLoaded(url)
    }
}