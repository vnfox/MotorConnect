package com.motor.connect.base.view

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.widget.Toast
import com.motor.connect.base.BaseViewModel

abstract class BaseActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel<*, *>> :
        RootActivity(), IBaseView {

    lateinit var mBinding: Binding

    lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = createViewModel()
        mBinding = createDataBinding(mViewModel)
    }

    abstract fun createViewModel(): ViewModel

    abstract fun createDataBinding(mViewModel: ViewModel): Binding

    override fun onDestroy() {
        mBinding.unbind()
        mViewModel.destroy()
        super.onDestroy()
    }

    override fun showLoadingView() {
        // TODO("not implemented")
    }

    override fun hideLoadingView() {
        // TODO("not implemented")
    }

    fun showUnderConstruction(methodName: String) {
        Toast.makeText(this, "=== $methodName ====", Toast.LENGTH_SHORT).show()
    }
}
