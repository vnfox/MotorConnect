package com.motor.connect.feature.add

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.ScheduleModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class AddAreaViewModel(mView: AddAreaView?, mModel: BaseModel)
    : BaseViewModel<AddAreaView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
    }

    fun saveDataArea(isFirstUsed: Boolean?, dataModel: AreaModel) {
        //Default schedule empty
        val schedule: MutableList<ScheduleModel> = mutableListOf()

        var id = "01"
        var areaModels: MutableList<AreaModel> = mutableListOf()
        if (!isFirstUsed!!) {
            areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)

            id = if (areaModels.size < 9)
                "0" + areaModels.size + 1
            else
                (areaModels.size + 1).toString()
        }
        dataModel.areaId = id
        dataModel.areaSchedule = ""
        dataModel.schedule = schedule
        areaModels.add(dataModel)
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
        Hawk.put(MotorConstants.KEY_PUT_LIST_VAN_MODEL, dataModel.areaVans)

        mView?.goBackMainScreen()
    }
}