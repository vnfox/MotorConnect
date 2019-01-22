package com.motor.connect.base.view

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.widget.Toast
import com.motor.connect.base.BaseViewModel
import com.orhanobut.hawk.Hawk

abstract class BaseActivity_View<Binding : ViewDataBinding, ViewModel : BaseViewModel<*, *>> :
        RootActivity(), IBaseView {

    lateinit var mBinding: Binding

    lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = createViewModel()
        mBinding = createDataBinding(mViewModel)
        Hawk.init(this).build()
    }

    abstract fun createViewModel(): ViewModel

    abstract fun createDataBinding(mViewModel: ViewModel): Binding

    override fun onDestroy() {
        mBinding.unbind()
        mViewModel.destroy()
        super.onDestroy()
    }

    override fun showLoadingView() {

    }

    override fun hideLoadingView() {

    }

    fun showUnderConstruction(methodName: String) {
        Toast.makeText(this, "=== $methodName ====", Toast.LENGTH_SHORT).show()
    }
}
