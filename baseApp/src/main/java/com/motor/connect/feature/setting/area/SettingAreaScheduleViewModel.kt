package com.motor.connect.feature.setting.area

import android.util.Log
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.ScheduleModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.StringUtil
import com.orhanobut.hawk.Hawk
import java.text.SimpleDateFormat
import java.util.*

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
        var areaModels: MutableList<AreaModel> = mutableListOf()
        var position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        areaModels[position] = model
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)


        //Todo Sample => will remove
        calculateCurrentProgress(time1_start, StringUtil.getFirstItem(time1_run))
    }

    fun prepareScheduleTwo(time1_start: String, time1_run: String,
                           time2_start: String, time2_run: String, repeat: String) {
        var scheduleList: MutableList<ScheduleModel> = mutableListOf()
        scheduleList.add(getScheduleModel(time1_start, time1_run))
        scheduleList.add(getScheduleModel(time2_start, time2_run))

        //Update Detail
        model.schedule = scheduleList
        model.scheduleRepeat = repeat
        Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, model)

        //Update List Data
        var areaModels: MutableList<AreaModel> = mutableListOf()
        var position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        areaModels[position] = model
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
    }

    fun prepareScheduleThree(time1_start: String, time1_run: String,
                             time2_start: String, time2_run: String,
                             time3_start: String, time3_run: String, repeat: String) {
        var scheduleList: MutableList<ScheduleModel> = mutableListOf()
        scheduleList.add(getScheduleModel(time1_start, time1_run))
        scheduleList.add(getScheduleModel(time2_start, time2_run))
        scheduleList.add(getScheduleModel(time3_start, time3_run))

        //Update Detail
        model.schedule = scheduleList
        model.scheduleRepeat = repeat
        Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, model)

        //Update List Data
        var areaModels: MutableList<AreaModel> = mutableListOf()
        var position = Hawk.get<Int>(MotorConstants.KEY_POSITION)
        areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        areaModels[position] = model
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)
    }

    private fun getScheduleModel(time_start: String, time_run: String): ScheduleModel {
        var schedule1 = ScheduleModel()
        schedule1.timeSchedule = time_start
        schedule1.timeRun = StringUtil.getFirstItem(time_run)
        return schedule1
    }

    private fun calculateCurrentProgress(time: String, minutes: String): Int {
        //get Current time
        val dateFormat = SimpleDateFormat("HH:mm")
        val date = Date()

        val startTime = time.replace(":", "").trim().toInt()
        val currentTime = dateFormat.format(date).replace(":", "").toInt()
        val endTime = getEndTime(time, minutes).toInt()

        Log.d("hqdat", ">>>> startTime  $startTime")
        Log.d("hqdat", ">>>> currentTime  $currentTime")
        Log.d("hqdat", ">>>> EndTime  $endTime")
        if (currentTime > endTime)
            return 0

        return (currentTime - startTime) * 60
    }

    private fun getEndTime(time: String, min: String): String {
        val array = time.split(":")
        var hour = array[0].toInt()
        var minutes = array[1].trim().toInt() + min.toInt()

        Log.d("hqdat", ">>>> minutes  $minutes")

        if (minutes > 60) {
            minutes -= 60
            hour++
        } else if (minutes == 60) {
            minutes = 0
            hour++
            minutes.toString() + "0"
        }

        return hour.toString() + minutes.toString()
    }
}