package com.motor.connect.base.view

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import com.motor.connect.base.BaseViewModel

abstract class BaseFragment<Binding : ViewDataBinding, ViewModel : BaseViewModel<*, *>> : Fragment
(), IBaseView {

    lateinit var mBinding: Binding

    lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = createDataBinding()
        mViewModel = createViewModel()
    }

    abstract fun createViewModel(): ViewModel

    abstract fun createDataBinding(): Binding

    override fun onDestroy() {
        mBinding.unbind()
        mViewModel.destroy()
        super.onDestroy()
    }
}
