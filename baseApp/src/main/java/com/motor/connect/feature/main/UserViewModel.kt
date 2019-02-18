package com.motor.connect.feature.main

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
        //0352248726
//        genFakeData("01")
    }

    private fun genFakeData(id: String) {
        val model = AreaModel()

        when (id) {  //02783920657927473
            "01" -> {
                model.areaSchedule = "01 0601 030 01"
                model.areaPhone = "0352248726"
            }
            "02" -> {
                model.areaSchedule = "02 0601 030 1600 090 02"
                model.areaPhone = "0947818171"
            }
            "03" -> {
                model.areaSchedule = "03 0601 030 1100 060 1720 030 03"
                model.areaPhone = "+9221"
            }
            "04" -> {
                model.areaSchedule = "01 0601 030"
                model.areaPhone = "0907021007"
            }
            "05" -> {
                model.areaSchedule = "02 0601 030 1600 090"
                model.areaPhone = "0942983969"
            }
            "06" -> {
                model.areaSchedule = "03 0601 030 1100 060 1600 090"
                model.areaPhone = "0982060035"
            }
            "07" -> {
                model.areaSchedule = ""
                model.areaPhone = "195"
            }
        }

        model.areaId = id
        model.areaName = "Area Data $id"

        model.areaStatus = ""
        model.areaType = "Admin"
        model.areaVans = getAreaVans("5 Van")
        // count from schedule
        model.scheduleRepeat = ""
        model.areaDetails = "Khu vuon buoi 2 nam tuoi"

        dataArea.add(model)

        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, dataArea)
    }

    private fun getAreaVans(vanSelected: String): List<VanModel>? {

        var areaVans: MutableList<VanModel> = mutableListOf()

        var numVan = vanSelected.substring(0, 1).toInt()

        for (i in 1..numVan) {
            val van = VanModel()
            van.vanId = "0$i"
            van.vanStatus = true
            if (i == 3)
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