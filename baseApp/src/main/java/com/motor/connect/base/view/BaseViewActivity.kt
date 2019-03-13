package com.motor.connect.base.view

import android.app.ProgressDialog
import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.Handler
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
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

    var telephonyManager: TelephonyManager? = null
    lateinit var mBinding: Binding

    lateinit var mViewModel: ViewModel
    private var progressDialog: ProgressDialog? = null
    lateinit var toolbar: Toolbar
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = createViewModel()
        mBinding = createDataBinding(mViewModel)
        Hawk.init(this).build()
        progressDialog = ProgressDialog(this)

        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
    }

    abstract fun createViewModel(): ViewModel

    abstract fun createDataBinding(mViewModel: ViewModel): Binding

    override fun onResume() {
        super.onResume()
        telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onStop() {
        super.onStop()
        telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
    }

    override fun onDestroy() {
        mBinding.unbind()
        mViewModel.destroy()
        super.onDestroy()
    }

    override fun showLoadingView(message: String) {
        progressDialog?.setMessage(message)
        progressDialog?.setCanceledOnTouchOutside(false)
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

    private var phoneStateListener: PhoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            super.onCallStateChanged(state, incomingNumber)

            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> {
                    Log.d("hqdat", ">>>> CALL_STATE_IDLE >>>>>")
                }
                TelephonyManager.CALL_STATE_RINGING -> {
                    Log.d("hqdat", ">>>> CALL_STATE_RINGING >>>>>")
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    Log.d("hqdat", ">>>> CALL_STATE_OFFHOOK >>>>>")
                }
            }
        }
    }
}
