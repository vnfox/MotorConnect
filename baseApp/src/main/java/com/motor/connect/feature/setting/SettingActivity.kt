package com.motor.connect.feature.setting

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import com.feature.area.R
import com.feature.area.databinding.SettingViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.setting.schedule.SettingScheduleActivity


class SettingActivity : BaseViewActivity<SettingViewBinding, SettingViewModel>(), SettingView {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    private val viewModel = SettingViewModel(this, BaseModel())

    override fun createViewModel(): SettingViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingViewModel): SettingViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_view)
        mBinding.viewModel = mViewModel

        return mBinding
    }

    fun gotoPreviousScreen(v: View) {
        actionLeft()
    }

    fun openSettingScheduler(v: View) {
        SettingScheduleActivity.show(this)
    }

    fun openConfigScreen(v: View) {
        showUnderConstruction("Config system")
    }

    fun openNotedScreen(v: View) {
        showUnderConstruction("Note")
    }

    fun openHowToUseScreen(v: View) {
//        HowToUseActivity.show(this)
        showUnderConstruction()
    }

    fun openHelpFeedbackScreen(v: View) {
//        HelpFeedbackActivity.show(this)
        showUnderConstruction()
    }
}