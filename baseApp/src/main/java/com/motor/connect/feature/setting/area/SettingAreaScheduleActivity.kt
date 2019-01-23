package com.motor.connect.feature.setting.area

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import com.feature.area.R
import com.feature.area.databinding.SettingAreaScheduleViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity


class SettingAreaScheduleActivity : BaseViewActivity<SettingAreaScheduleViewBinding, SettingAreaScheduleViewModel>(), SettingAreaScheduleView {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingAreaScheduleActivity::class.java))
        }
    }

    lateinit var needPermissions: MutableList<String>

    private val viewModel = SettingAreaScheduleViewModel(this, BaseModel())

    override fun createViewModel(): SettingAreaScheduleViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingAreaScheduleViewModel): SettingAreaScheduleViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_area_schedule_view)
        mBinding.viewModel = mViewModel

        return mBinding
    }

    fun actionClose(v: View) {
        actionLeft()
    }

}