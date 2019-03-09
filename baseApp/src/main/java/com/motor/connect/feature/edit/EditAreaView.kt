package com.motor.connect.feature.edit

import com.motor.connect.base.view.actionbar.ActionBarView
import com.motor.connect.feature.model.AreaModel

interface EditAreaView : ActionBarView {

    fun viewLoaded(model: AreaModel?)

    fun backDetailScreen()
}