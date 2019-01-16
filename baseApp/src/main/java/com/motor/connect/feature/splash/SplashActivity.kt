package com.motor.connect.feature.splash

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.SplashActivityBinding
import com.motor.connect.base.view.BaseActivity
import com.motor.connect.feature.data.MainActivity
import com.motor.connect.utils.PermissionUtils
import io.reactivex.annotations.NonNull
import java.util.*

class SplashActivity : BaseActivity() {

    val PERMISSIONS_REQUEST = 123

    lateinit var needPermissions: MutableList<String>

//    private var viewModel: SplashViewModel()


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: SplashActivityBinding = DataBindingUtil.setContentView(this, R.layout.splash_activity)
//        binding.viewModel = viewModel

//        viewModel.startUpdates()

        //Check Permission
        verifyAppPermission()


        grantPermissions()

    }

    private fun verifyAppPermission() {
        needPermissions = ArrayList()

        if (!PermissionUtils.isGranted(this,
                        Manifest.permission.SEND_SMS)) {
            needPermissions.add(Manifest.permission.SEND_SMS)
        }
        if (!PermissionUtils.isGranted(this,
                        Manifest.permission.READ_SMS)) {
            needPermissions.add(Manifest.permission.READ_SMS)
        }
        if (!PermissionUtils.isGranted(this,
                        Manifest.permission.RECEIVE_SMS)) {
            needPermissions.add(Manifest.permission.RECEIVE_SMS)
        }
    }

    private fun checkPermission(sendSms: String): Boolean {

        val checkpermission = ContextCompat.checkSelfPermission(this, sendSms)
        return checkpermission == PackageManager.PERMISSION_GRANTED
    }

    fun grantPermissions() {
        ActivityCompat.requestPermissions(this,
                needPermissions.toTypedArray(), PERMISSIONS_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Toast.makeText(this, "=== permission  Accept ====", Toast.LENGTH_LONG).show()
                MainActivity.show(this)
            }
        }
    }
}
