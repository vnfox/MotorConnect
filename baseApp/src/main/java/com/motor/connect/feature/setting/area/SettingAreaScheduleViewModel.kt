package com.motor.connect.feature.setting.area

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.ScheduleModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.StringUtil
import com.orhanobut.hawk.Hawk

class SettingAreaScheduleViewModel(mView: SettingAreaScheduleView?, mModel: BaseModel)
    : BaseViewModel<SettingAreaScheduleView, BaseModel>(mView, mModel) {

    var model = AreaModel()

    override fun initViewModel() {
        //Check data => show UI
        model = Hawk.get<AreaModel>(MotorConstants.KEY_PUT_AREA_DETAIL)
        mView?.viewLoaded()
    }

    fun getAreaId(): String {
        return model.areaId
    }

    fun getAreaPhone(): String {
        return model.areaPhone
    }

    fun getAreaPassWord(): String {
        return model.password
    }

    fun prepareScheduleOne(time1_start: String, time1_run: String, repeat: String) {
        var scheduleList: MutableList<ScheduleModel> = mutableListOf()
        scheduleList.add(getScheduleModel(time1_start, time1_run))

        //Update Detail
        model.schedule = scheduleList
        model.scheduleRepeat = repeat
        Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, model)

        //Update List Data
        var areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        val position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        areaModels[position] = model
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
    }

    fun prepareScheduleTwo(time1_start: String, time1_run: String,
                           time2_start: String, time2_run: String, repeat: String) {
        val scheduleList: MutableList<ScheduleModel> = mutableListOf()
        scheduleList.add(getScheduleModel(time1_start, time1_run))
        scheduleList.add(getScheduleModel(time2_start, time2_run))

        //Update Detail
        model.schedule = scheduleList
        model.scheduleRepeat = repeat
        Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, model)

        //Update List Data
        var areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        val position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        areaModels[position] = model
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
    }

    fun prepareScheduleThree(time1_start: String, time1_run: String,
                             time2_start: String, time2_run: String,
                             time3_start: String, time3_run: String, repeat: String) {
        val scheduleList: MutableList<ScheduleModel> = mutableListOf()
        scheduleList.add(getScheduleModel(time1_start, time1_run))
        scheduleList.add(getScheduleModel(time2_start, time2_run))
        scheduleList.add(getScheduleModel(time3_start, time3_run))

        //Update Detail
        model.schedule = scheduleList
        model.scheduleRepeat = repeat
        Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, model)

        //Update List Data
        var areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        val position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        areaModels[position] = model
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
    }

    private fun getScheduleModel(time_start: String, time_run: String): ScheduleModel {
        val schedule1 = ScheduleModel()
        schedule1.timeSchedule = time_start
        schedule1.timeRun = StringUtil.getFirstItem(time_run)
        return schedule1
    }
}