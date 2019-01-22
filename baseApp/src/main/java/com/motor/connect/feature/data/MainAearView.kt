package com.motor.connect.feature.data

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.AreaModel

/**
 * Created by dathuynh on 8/23/18.
 */
interface MainAearView : ActionBarView {

    fun updateUI(dataArea: MutableList<AreaModel>)

    fun showEmptyView()
}