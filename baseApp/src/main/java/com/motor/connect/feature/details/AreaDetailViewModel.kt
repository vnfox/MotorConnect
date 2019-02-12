package com.motor.connect.feature.details

import android.util.Log
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.EnumHelper
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.StringUtil
import com.orhanobut.hawk.Hawk
import java.text.SimpleDateFormat
import java.util.*

class AreaDetailViewModel(mView: AreaDetailView?, mModel: BaseModel)
    : BaseViewModel<AreaDetailView, BaseModel>(mView, mModel) {

    override fun initViewModel() {

        val model = Hawk.get<AreaModel>(MotorConstants.KEY_PUT_AREA)

        mView?.showLoadingView()
        //get data
        mView?.viewLoaded()

        updateAreaInfo(model)
        Hawk.put(MotorConstants.KEY_VANS_USED, model.areaVans)
    }

    fun updateMotorWorking(scheduleWorking: String?) {
        val array = StringUtil.getScheduleRunning(scheduleWorking)
        val maxValue = calculateMaxProgress(array[1])

        val currentTime = calculateCurrentProgress(array[0], array[1])
        mView?.viewMotorWorking(array!![0], array[1].replaceFirst("t", "T"), maxValue, currentTime)
    }

    fun updateInfoMotor() {
        val model = Hawk.get<AreaModel>(MotorConstants.KEY_PUT_AREA)
        mView?.updateInfoMotor(model.areaStatus, getVansUsed(model.areaVans),
                StringUtil.getScheduleWorking(model.areaSchedule))
    }

    private fun calculateMaxProgress(time: String): Int {
        val time = time.split(" ")[1]
        return time.toInt() * 60
    }

    private fun calculateCurrentProgress(time: String, minutes: String): Int {
        //get Current time
        val dateFormat = SimpleDateFormat("HH:mm")
        val date = Date()

        val minutes = minutes.split(" ")[1]
        val hour = time.substring(8, 18).replace(" gio ", ":")

        val startTime = hour.replace(":", "").trim().toInt()
        val endTime = getEndTime(hour, minutes).toInt()
        val currentTime = dateFormat.format(date).replace(":", "").toInt()

        Log.d("hqdat", ">>>> startTime  $startTime")
        Log.d("hqdat", ">>>> currentTime  $currentTime")
        if (currentTime > endTime)
            return 0

        return (currentTime - startTime) * 60
    }

    private fun getEndTime(time: String, min: String): String {
        val array = time.split(":")
        var hour = array[0].toInt()
        var minutes = array[1].trim().toInt() + min.toInt()
        if (minutes > 60) {
            minutes -= 60
            hour += 1
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

    private fun updateAreaInfo(model: AreaModel) {
        val schedules = getScheduleWorking(model.areaSchedule)
        mView?.viewAreaInfo(model, schedules)
        mView?.hideLoadingView()
    }

    private fun getScheduleWorking(value: String): String {
        val result = "chua co lich tuoi"

        if (value.isEmpty())
            return result

        val count = StringUtil.getCountWorkingDay(value)
        when (count) {
            EnumHelper.ScheduleDays.ONE_DAY.key -> {

                return StringUtil.getScheduleWorkingOneDay(value)
            }
            EnumHelper.ScheduleDays.TWO_DAY.key -> {
                return StringUtil.getScheduleWorkingTwoDay(value)
            }
            EnumHelper.ScheduleDays.THREE_DAY.key -> {
                return StringUtil.getScheduleWorkingThreeDay(value)
            }
        }
        return result!!
    }
}