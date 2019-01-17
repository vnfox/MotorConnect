package com.motor.connect.feature.model

import android.graphics.Bitmap

class AreaModel {

    private var areaId: String? = null
    private var areaType: String? = null
    private var areaName: String? = null
    private var areaPhone: String? = null
    private var areaStatus: String? = null
    private var schedule: String? = null

    private var imgAvatar: Bitmap? = null

    private var vanNUmber: Int? = null


    fun getAreaId(): String? {
        return areaId
    }

    fun setAreaId(areaId: String?) {
        this.areaId = areaId
    }

    fun getAreaType(): String? {
        return areaType
    }

    fun setAreaType(type: String?) {
        this.areaType = type
    }

    fun getAreaName(): String? {
        return areaName
    }

    fun setAreaName(name: String?) {
        this.areaName = name
    }

    fun getAreaPhone(): String? {
        return areaPhone
    }

    fun setAreaPhone(phone: String?) {
        this.areaPhone = phone
    }

    fun getStatus(): String? {
        return areaStatus
    }

    fun setStatus(status: String?) {
        this.areaStatus = status
    }

    fun getSchedule(): String? {
        return schedule
    }

    fun setSchedule(schedule: String?) {
        this.schedule = schedule
    }

    fun getAvatar(): Bitmap? {
        return imgAvatar
    }

    fun setAvatar(bitmap: Bitmap?) {
        this.imgAvatar = bitmap
    }

    fun getVanNumber(): Int? {
        return vanNUmber
    }

    fun setVanNumber(van: Int) {
        this.vanNUmber = van
    }
}