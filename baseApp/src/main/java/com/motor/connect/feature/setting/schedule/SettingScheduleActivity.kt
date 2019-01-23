package com.motor.connect.feature.setting.schedule

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import com.feature.area.R
import com.feature.area.databinding.SettingScheduleActivityBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.add.AddAreaActivity
import com.motor.connect.feature.details.AreaDetailActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.utils.MotorConstants
import kotlinx.android.synthetic.main.activity_main.*


class SettingScheduleActivity : BaseViewActivity<SettingScheduleActivityBinding, SettingScheduleViewModel>(), SettingScheduleView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingScheduleActivity::class.java))
        }
    }

    private val viewModel = SettingScheduleViewModel(this, BaseModel())
    private var adapter: SettingScheduleAdapter? = null


    override fun createViewModel(): SettingScheduleViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingScheduleViewModel): SettingScheduleActivityBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_schedule_activity)
        mBinding.viewModel = mViewModel

        //Adapter item click
        adapter = SettingScheduleAdapter { areaModel, position ->

            //            Toast.makeText(this, "=== Item Click  ====  $position    " + areaModel.areaName,
//                    Toast.LENGTH_LONG).show()

            val intent = Intent(this, AreaDetailActivity::class.java)

            this.startActivity(intent)
        }

        recyclerView.adapter = adapter
        val isUser = shef?.getFirstUserPref(MotorConstants.FIRST_USED)
        viewModel.initData(isUser)

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

    fun openEmptyScreen(v: View) {
        AddAreaActivity.show(this)
    }
}
