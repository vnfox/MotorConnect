package com.motor.connect.feature.main

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.WeatherModel

/**
 * Created by dathuynh on 8/23/18.
 */
interface MainAreaView : ActionBarView {

    fun updateUI(dataArea: MutableList<AreaModel>)

    fun showEmptyView()

    fun updateWeatherInfo(weather: WeatherModel)
}