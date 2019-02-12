package com.motor.connect.base.view

import android.app.ProgressDialog
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import com.motor.connect.base.BaseViewModel
import com.motor.connect.base.view.actionbar.ActionBarView
import com.orhanobut.hawk.Hawk

@Suppress("DEPRECATION")
abstract class BaseViewActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel<*, *>> :
        RootActivity(), ActionBarView {

    override fun actionLeft() {
        onBackPressed()
    }

    lateinit var mBinding: Binding

    lateinit var mViewModel: ViewModel
    private var progressDialog: ProgressDialog? = null
    lateinit var toolbar: Toolbar

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

    fun showUnderConstruction() {
        Toast.makeText(this, "=== Feature will be implement in the future ====", Toast.LENGTH_SHORT).show()
    }
}
