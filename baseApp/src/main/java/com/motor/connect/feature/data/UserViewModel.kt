package com.motor.connect.feature.data

import android.util.Log
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class UserViewModel(mView: MainAreaView?, mModel: BaseModel)
    : BaseViewModel<MainAreaView, BaseModel>(mView, mModel) {

    var dataArea: MutableList<AreaModel> = mutableListOf()

    override fun initViewModel() {
        genFakeData()
    }

    private fun genFakeData() {
        val model = AreaModel()
        model.areaId = "01"
        model.areaName = "Area Data kv1"
        model.areaPhone = "0974818171"
        model.areaStatus = "dang hoat dong"
        model.areaType = "Admin"
        model.areaSchedule = "03 0601 030 1100 060 1600 090 01"
        model.areaVans = getAreaVans("5 Van")
        model.timeRemain = 30 // count from schedule
        model.timeReminder = 60
        model.areaScheduleRepeat = "01"

        dataArea.add(model)

        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, dataArea)
    }

    private fun getAreaVans(vanSelected: String): List<VanModel>? {

        var areaVans: MutableList<VanModel> = mutableListOf()
        val van = VanModel()
        var numVan = vanSelected.substring(0, 1).toInt()

        for (i in 1..numVan) {
            van.vanId = "0$i"
            van.vanStatus = false

            areaVans.add(van)
        }
        return areaVans
    }

    fun initData(isFirst: Boolean?) {
        if (isFirst!!) {
            mView?.showEmptyView()
        } else {
            dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
            mView?.updateUI(dataArea)
        }
    }

    fun updateData() {
        dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        mView?.updateUI(dataArea)
    }

    fun stopUpdates() {

    }
}