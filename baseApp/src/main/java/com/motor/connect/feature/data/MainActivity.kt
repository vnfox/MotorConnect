package com.motor.connect.feature.data

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.ActivityMainBinding
import com.motor.connect.base.view.BaseActivity
import com.motor.connect.feature.add.AddAreaActivity
import com.motor.connect.feature.home.HomeActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.notification.NotificationActivity
import com.motor.connect.feature.setting.SettingActivity
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.SharePrefUtil
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private var arae: AreaModel? = null

    private val viewModel = UserViewModel()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Hawk.init(this).build()
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Adapter item click
        val adapter = UserAdapter { areaModel ->

            Toast.makeText(this, "=== Item Click  ====  " + areaModel.areaName,
                    Toast.LENGTH_LONG).show()
        }

        recyclerView.adapter = adapter
        binding.viewModel = viewModel

        if (shef!!.getFirstUserPref(MotorConstants.FIRST_USED)) {
            viewModel.startUpdates()
            shef!!.setFirstUserPref(MotorConstants.FIRST_USED, false)
        } else {
            viewModel.updateList()
        }

        // Button
        val actionHome = findViewById<TextView>(R.id.btn_home)
        actionHome.setOnClickListener(this)
        val actionAdd = findViewById<TextView>(R.id.btn_add)
        actionAdd?.setOnClickListener(this)
        val actionNotify = findViewById<TextView>(R.id.btn_notify)
        actionNotify?.setOnClickListener(this)
        val actionSetting = findViewById<TextView>(R.id.btn_setting)
        actionSetting?.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (shef!!.getTriggerData(MotorConstants.KEY_TRIGGER_DATA)) {
            viewModel.updateList()
        }
    }

    override fun onDestroy() {
        viewModel.stopUpdates()
        super.onDestroy()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_home -> {
                HomeActivity.show(this)
            }
            R.id.btn_add -> {
                AddAreaActivity.show(this)
            }
            R.id.btn_notify -> {
                NotificationActivity.show(this)
            }
            R.id.btn_setting -> {
                SettingActivity.show(this)
            }
            else -> {
            }
        }
    }

}
