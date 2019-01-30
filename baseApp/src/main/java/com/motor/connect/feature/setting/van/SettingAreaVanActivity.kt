package com.motor.connect.feature.setting.van

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import com.feature.area.R
import com.feature.area.databinding.SettingAreaVanViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.setting.schedule.SettingScheduleActivity


class SettingAreaVanActivity : BaseViewActivity<SettingAreaVanViewBinding, SettingAreaVanViewModel>(), SettingAreaVanView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingAreaVanActivity::class.java))
        }
    }

    private val viewModel = SettingAreaVanViewModel(this, BaseModel())

    override fun createViewModel(): SettingAreaVanViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingAreaVanViewModel): SettingAreaVanViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_area_van_view)
        mBinding.viewModel = mViewModel

        viewModel.initViewModel()
        return mBinding
    }

    override fun viewLoaded() {

    }

    fun actionClose(v: View) {
        actionLeft()
    }

    fun setupVanUsed(v: View) {
        showUnderConstruction("setupVanUsed")
    }
}