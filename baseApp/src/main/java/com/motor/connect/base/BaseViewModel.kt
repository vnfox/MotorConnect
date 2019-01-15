package com.motor.connect.base

import android.arch.lifecycle.ViewModel
import com.motor.connect.base.view.IBaseView

open abstract class BaseViewModel<V : IBaseView, M : BaseModel>(var mView: V?, var mModel: M) : ViewModel() {

    abstract fun initViewModel()

    fun destroy() {
        mModel.destroy()
        mView = null
    }
}