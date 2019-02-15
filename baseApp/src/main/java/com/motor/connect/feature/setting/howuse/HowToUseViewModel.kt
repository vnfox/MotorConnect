package com.motor.connect.feature.setting.howuse

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel

class HowToUseViewModel(view: HowToUseView?, model: BaseModel) : BaseViewModel<HowToUseView, BaseModel>(view, model) {

    override fun initViewModel() {
        var url = "file:///android_asset/how_to_use.html"

        mView?.viewLoaded(url)
    }
}