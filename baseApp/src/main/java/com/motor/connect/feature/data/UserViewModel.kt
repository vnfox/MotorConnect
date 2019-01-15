package com.motor.connect.feature.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Handler
import com.android.databinding.library.baseAdapters.BR
import com.motor.connect.feature.model.AreaModel
import java.util.*

class UserViewModel : BaseObservable() {

    @get:Bindable
    var dataArea: MutableList<AreaModel> = mutableListOf()
        private set(value) {
            field = value
            notifyPropertyChanged(BR.dataArea)
        }

    @get:Bindable
    var changedPositions: Set<Int> = mutableSetOf()
        private set(value) {
            field = value
            notifyPropertyChanged(BR.changedPositions)
        }

    private val updateInterval = 1000L
    private val updateHandler = Handler()
    private val random = Random()

    private var updateRunnable: Runnable = object : Runnable {
        override fun run() {
            updateList()
            updateHandler.postDelayed(this, updateInterval)
        }
    }

    private fun updateList() {

    }

    fun startUpdates() {
        initList()
        updateHandler.postDelayed(updateRunnable, updateInterval)
    }

    private fun initList() {
        // populate the data from the source, such as the database.
        for (i in 0..20) {
            val dataModel = AreaModel()
            dataModel.setAreaName("Name  " + i.toString())
            dataModel.setAreaPhone("0906 >>  " + i.toString())
            dataModel.setStatus("status  " + i.toString())
            dataModel.setSchedule("Lich tuoi ngay tuoi 3 lan")
            if (i == 3)
                dataModel.setSchedule("jasjfksa lsafj lasfka l asjfaos  ljfas f lfao  po fap ")

            if (i == 4)
                dataModel.setSchedule("")
            dataArea.add(dataModel)
        }
        notifyPropertyChanged(BR.dataArea)
    }

    fun stopUpdates() {
        updateHandler.removeCallbacks(updateRunnable)
    }
}