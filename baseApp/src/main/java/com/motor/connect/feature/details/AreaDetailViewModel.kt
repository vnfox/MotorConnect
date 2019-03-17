package com.motor.connect.feature.details

import android.util.Log
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.ScheduleModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.EnumHelper
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.StringUtil
import com.orhanobut.hawk.Hawk
import java.text.SimpleDateFormat
import java.util.*

class AreaDetailViewModel(mView: AreaDetailView?, mModel: BaseModel)
    : BaseViewModel<AreaDetailView, BaseModel>(mView, mModel) {

    var model = AreaModel()

    override fun initViewModel() {

        model = Hawk.get<AreaModel>(MotorConstants.KEY_PUT_AREA_DETAIL)
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
        val schedule = getScheduleWorking()
        mView?.updateInfoMotor(model.areaStatus, getVansUsed(model.areaVans), schedule)
    }

    fun checkScheduleWorking() {
        if (model.schedule == null || model.schedule.isEmpty()) {
            return
        }
        when {
            model.schedule.size == 1 -> {
                calculateProgressOneDay(model.schedule[0])
            }
            model.schedule.size == 2 -> {
                calculateProgressTwoDay(model.schedule[0], model.schedule[1])
            }
            model.schedule.size == 3 -> {
                calculateProgressThreeDay(model.schedule[0], model.schedule[1], model.schedule[2])
            }
        }
    }

    private fun calculateProgressOneDay(schedule: ScheduleModel) {
        val dateFormat = SimpleDateFormat("HH:mm")
        val date = Date()

        var currentTime = dateFormat.format(date).replace(":", "").toInt()
        val startTime = schedule.timeSchedule.replace(":", "").trim().toInt()
        val endTime = getEndTime(schedule.timeSchedule, StringUtil.getFirstItem(schedule.timeRun)).toInt()
        val maxTime = StringUtil.getFirstItem(schedule.timeRun).toInt()

        Log.d("hqdat", ">>>> startTime  $startTime")
        Log.d("hqdat", ">>>> currentTime  $currentTime")
        Log.d("hqdat", ">>>> EndTime  $endTime")

        if (!(startTime >= currentTime || currentTime >= endTime)) {
            currentTime -= startTime
            mView?.viewMotorWorking(schedule.timeSchedule, schedule.timeRun, maxTime * 60, currentTime * 60)
        } else {
            mView?.updateViewMotorStopWorking()
        }
    }

    private fun calculateProgressTwoDay(schedule1: ScheduleModel, schedule2: ScheduleModel) {
        val dateFormat = SimpleDateFormat("HH:mm")
        val date = Date()

        var currentTime = dateFormat.format(date).replace(":", "").toInt()
        val startTime1 = schedule1.timeSchedule.replace(":", "").trim().toInt()
        val endTime1 = getEndTime(schedule1.timeSchedule, StringUtil.getFirstItem(schedule1.timeRun)).toInt()

        val startTime2 = schedule2.timeSchedule.replace(":", "").trim().toInt()
        val endTime2 = getEndTime(schedule2.timeSchedule, StringUtil.getFirstItem(schedule2.timeRun)).toInt()

        if (!(startTime1 >= currentTime || currentTime >= endTime1)) {
            currentTime -= startTime1
            val maxTime = StringUtil.getFirstItem(schedule1.timeRun).toInt()
            mView?.viewMotorWorking(schedule1.timeSchedule, schedule1.timeRun, maxTime * 60, currentTime * 60)

        } else if (!(startTime2 >= currentTime || currentTime >= endTime2)) {
            currentTime -= startTime1
            val maxTime = StringUtil.getFirstItem(schedule2.timeRun).toInt()
            mView?.viewMotorWorking(schedule2.timeSchedule, schedule2.timeRun, maxTime * 60, currentTime * 60)
        } else {
            mView?.updateViewMotorStopWorking()
        }
    }

    private fun calculateProgressThreeDay(schedule1: ScheduleModel, schedule2: ScheduleModel, schedule3: ScheduleModel) {
        val dateFormat = SimpleDateFormat("HH:mm")
        val date = Date()

        var currentTime = dateFormat.format(date).replace(":", "").toInt()
        val startTime1 = schedule1.timeSchedule.replace(":", "").trim().toInt()
        val endTime1 = getEndTime(schedule1.timeSchedule, StringUtil.getFirstItem(schedule1.timeRun)).toInt()

        val startTime2 = schedule2.timeSchedule.replace(":", "").trim().toInt()
        val endTime2 = getEndTime(schedule2.timeSchedule, StringUtil.getFirstItem(schedule2.timeRun)).toInt()

        val startTime3 = schedule3.timeSchedule.replace(":", "").trim().toInt()
        val endTime3 = getEndTime(schedule3.timeSchedule, StringUtil.getFirstItem(schedule3.timeRun)).toInt()

        if (!(startTime1 >= currentTime || currentTime >= endTime1)) {
            currentTime -= startTime1
            val maxTime = StringUtil.getFirstItem(schedule1.timeRun).toInt()
            mView?.viewMotorWorking(schedule1.timeSchedule, schedule1.timeRun, maxTime * 60, currentTime * 60)

        } else if (!(startTime2 >= currentTime || currentTime >= endTime2)) {
            currentTime -= startTime2
            val maxTime = StringUtil.getFirstItem(schedule2.timeRun).toInt()
            mView?.viewMotorWorking(schedule2.timeSchedule, schedule2.timeRun, maxTime * 60, currentTime * 60)
        } else if (!(startTime3 >= currentTime || currentTime >= endTime3)) {
            currentTime -= startTime3
            val maxTime = StringUtil.getFirstItem(schedule3.timeRun).toInt()
            mView?.viewMotorWorking(schedule3.timeSchedule, schedule3.timeRun, maxTime * 60, currentTime * 60)
        } else {
            mView?.updateViewMotorStopWorking()
        }
    }

    private fun getEndTime(time: String, min: String): String {
        val array = time.split(":")
        var hour = array[0].toInt()
        var minutes = array[1].trim().toInt() + min.toInt()
        if (minutes > 60) {
            minutes -= 60
            hour++
        } else if (minutes == 60) {
            hour++
            return hour.toString() + "00"
        }
        return hour.toString() + minutes.toString()
    }

    private fun getVansUsed(areaVans: MutableList<VanModel>): String {
        val result = StringBuilder()
        for (i in 0..(areaVans.size - 1)) {
            if (areaVans[i].vanStatus) {
                result.append(areaVans[i].vanId).append(" ")
            }
        }
        return result.toString()
    }

    private fun getScheduleWorking(): String {
        val result = "Chưa cài đặt lịch tưới"

        if (model.schedule == null || model.schedule.isEmpty())
            return result

        val count = "0${model.schedule.size}"
        when (count) {
            EnumHelper.ScheduleDays.ONE_DAY.key -> {
                return StringUtil.getScheduleOneDay(model.schedule[0])
            }
            EnumHelper.ScheduleDays.TWO_DAY.key -> {
                return StringUtil.getScheduleTwoDay(model.schedule[0], model.schedule[1])
            }
            EnumHelper.ScheduleDays.THREE_DAY.key -> {
                return StringUtil.getScheduleThreeDay(model.schedule[0], model.schedule[1], model.schedule[2])
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

    // check only van ON
    fun getVanId(): String {

        var areaVanss: MutableList<VanModel> = mutableListOf()
        areaVanss = model.areaVans

        //Check only On
        for (i in 0..(areaVanss.size - 1)) {
            if (areaVanss[i].vanStatus) {
                return areaVanss[i].vanId
            }
        }
        return "01"
    }
}