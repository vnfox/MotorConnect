package com.motor.connect.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class BaseModel {

    private var compositeDisposable = CompositeDisposable()

    fun subscribe(disposable: Disposable) = compositeDisposable.add(disposable)

    fun destroy() {
        compositeDisposable.dispose()
    }
}