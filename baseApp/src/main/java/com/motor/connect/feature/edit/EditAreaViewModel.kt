package com.motor.connect.feature.edit

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class EditAreaViewModel(mView: EditAreaView?, mModel: BaseModel)
    : BaseViewModel<EditAreaView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
        val model = Hawk.get<AreaModel>(MotorConstants.KEY_PUT_AREA_DETAIL)

        mView?.viewLoaded(model)
    }

    fun updateDataArea(dataModel: AreaModel) {
        Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, dataModel)
        var position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        var areaModels: MutableList<AreaModel> = mutableListOf()

        areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        areaModels[position] = dataModel
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)

        mView?.backDetailScreen()
    }
}