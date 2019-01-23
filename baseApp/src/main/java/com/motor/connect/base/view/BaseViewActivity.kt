package com.motor.connect.base.view

import android.app.ProgressDialog
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.widget.Toast
import com.motor.connect.base.BaseViewModel
import com.orhanobut.hawk.Hawk

@Suppress("DEPRECATION")
abstract class BaseViewActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel<*, *>> :
        RootActivity(), IBaseView {

    lateinit var mBinding: Binding

    lateinit var mViewModel: ViewModel
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = createViewModel()
        mBinding = createDataBinding(mViewModel)
        Hawk.init(this).build()
        progressDialog = ProgressDialog(this)
    }

    abstract fun createViewModel(): ViewModel

    abstract fun createDataBinding(mViewModel: ViewModel): Binding

    override fun onDestroy() {
        mBinding.unbind()
        mViewModel.destroy()
        super.onDestroy()
    }

    fun actionLeft() {
        onBackPressed()
    }

    override fun showLoadingView() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog?.show()
    }

    override fun hideLoadingView() {
        progressDialog?.dismiss()
    }

    fun showUnderConstruction(methodName: String) {
        Toast.makeText(this, "=== $methodName ====", Toast.LENGTH_SHORT).show()
    }
}
