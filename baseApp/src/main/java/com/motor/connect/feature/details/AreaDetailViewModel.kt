package com.motor.connect.feature.details

import android.util.Log
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.StringUtil
import com.orhanobut.hawk.Hawk

class AreaDetailViewModel(mView: AreaDetailView?, mModel: BaseModel)
    : BaseViewModel<AreaDetailView, BaseModel>(mView, mModel) {

    var model = AreaModel()

    override fun initViewModel() {
        val areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        val pos = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        model = areaModels[pos]
        //get data
        mView?.viewLoaded()

        updateAreaInfo(model)
        Hawk.put(MotorConstants.KEY_VANS_USED, model.areaVans)
    }

    private fun updateAreaInfo(model: AreaModel) {
        val schedules = getScheduleWorking()

        mView?.viewAreaInfo(model, schedules)
    }

    fun updateInfoMotor() {
        //val schedule = getScheduleWorking()
        val schedule = "Need to refactor"
        mView?.updateInfoMotor(model.areaStatus, getVansUsed(model.areaVans), schedule)
    }

    fun checkScheduleWorking() {

    }

    private fun getVansUsed(areaVans: MutableList<VanModel>): String {
        val result = StringBuilder()
        for (i in 0 until areaVans.size) {
            if (areaVans[i].vanStatus) {
                result.append(areaVans[i].vanId).append(" ")
            }
        }
        return result.toString()
    }

    private fun getScheduleWorking(): String {
        var result = ""

        if (model.areaVans == null || model.areaVans.isEmpty())
            return "Chưa cài đặt lịch tưới"

        model.areaVans.forEach {
            if (it.schedule.isNotEmpty()) {
                Log.d("hqdat", "=====  >>>>>>   1   $result")
                result += StringUtil.getScheduleArea(it.schedule, it.vanId, it.duration)

                Log.d("hqdat", "=====  >>>>>>   2   $result")
            }
        }
        return result!!
    }

    fun getPassWordArea(): String {
        return model.password
    }

    fun getAreaId(): String {
        return model.areaId
    }
}