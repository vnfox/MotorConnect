package com.motor.connect.feature.data

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.ActivityMainBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.add.AddAreaActivity
import com.motor.connect.feature.details.AreaDetailActivity
import com.motor.connect.feature.home.HomeActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.notification.NotificationActivity
import com.motor.connect.feature.plan.CreatePlanActivity
import com.motor.connect.feature.setting.SettingActivity
import com.motor.connect.utils.MotorConstants
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseViewActivity<ActivityMainBinding, UserViewModel>(), MainAreaView, View.OnClickListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val viewModel = UserViewModel(this, BaseModel())
    private var adapter: UserAdapter? = null


    override fun createViewModel(): UserViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: UserViewModel): ActivityMainBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = mViewModel

        //Adapter item click
        adapter = UserAdapter { areaModel, position ->

//            Toast.makeText(this, "=== Item Click  ====  $position    " + areaModel.areaName,
//                    Toast.LENGTH_LONG).show()

            val intent = Intent(this, AreaDetailActivity::class.java)

            this.startActivity(intent)
        }

        recyclerView.adapter = adapter
        val isUser = shef?.getFirstUserPref(MotorConstants.FIRST_USED)
        viewModel.initData(isUser)


        // Button
        val actionHome = findViewById<TextView>(R.id.btn_home)
        actionHome.setOnClickListener(this)
        val actionAdd = findViewById<TextView>(R.id.btn_add)
        actionAdd?.setOnClickListener(this)
        val actionNotify = findViewById<TextView>(R.id.btn_notify)
        actionNotify?.setOnClickListener(this)
        val actionSetting = findViewById<TextView>(R.id.btn_setting)
        actionSetting?.setOnClickListener(this)
        val emptyView = findViewById<TextView>(R.id.txt_empty)
        emptyView?.setOnClickListener(this)

        return mBinding
    }

    override fun showEmptyView() {
        recyclerView.visibility = View.GONE
        txt_empty.visibility = View.VISIBLE

    }

    override fun updateUI(dataArea: MutableList<AreaModel>) {
        recyclerView.visibility = View.VISIBLE
        txt_empty.visibility = View.GONE
        adapter?.setData(dataArea)
        recyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onResume() {
        super.onResume()
        if (shef!!.getTriggerData(MotorConstants.KEY_TRIGGER_DATA)) {
            viewModel.updateData()
            shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, false)
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
//                CreatePlanActivity.show(this)
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
            R.id.txt_empty -> {
                AddAreaActivity.show(this)
            }
        }
    }
}
