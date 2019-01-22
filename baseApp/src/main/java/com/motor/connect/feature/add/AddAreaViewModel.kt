package com.motor.connect.feature.add

import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class AddAreaViewModel(mView: AddAreaView?, mModel: BaseModel)
    : BaseViewModel<AddAreaView, BaseModel>(mView, mModel) {

    override fun initViewModel() {
    }

    fun showProgressView() {
        mView?.showLoadingView()
    }

    fun hideProgressView() {
        mView?.hideLoadingView()
    }

    fun saveDataArea(isFirstUsed: Boolean?, dataModel: AreaModel) {

        var areaModels: MutableList<AreaModel> = mutableListOf()
        if (!isFirstUsed!!) {
            areaModels = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        }
        areaModels.add(dataModel)
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)

        mView?.goBackMainScreen()
    }
}