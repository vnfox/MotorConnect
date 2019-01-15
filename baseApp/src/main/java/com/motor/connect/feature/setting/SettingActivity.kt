package com.motor.connect.feature.setting

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.widget.Toast
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseActivity
import com.feature.area.R
import com.feature.area.databinding.SettingViewBinding


class SettingActivity : BaseActivity<SettingViewBinding, SettingViewModel>(), SettingView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    override fun createViewModel(): SettingViewModel {
        val viewModel = SettingViewModel(this, BaseModel())
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingViewModel): SettingViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_view)
        mBinding.viewModel = mViewModel
        return mBinding
    }

    override fun viewLoaded() {

    }

    override fun actionLeft() {
        super.onBackPressed()
    }

    override fun showSettingArea() {
        Toast.makeText(this, "=== showCalendarSelection ====", Toast.LENGTH_LONG).show()
    }

    override fun showConfigSystem() {
        Toast.makeText(this, "=== showCalendarSelection ====", Toast.LENGTH_LONG).show()
    }

    override fun showReminder() {
        Toast.makeText(this, "=== showCalendarSelection ====", Toast.LENGTH_LONG).show()
    }

    override fun showHowToUse() {
        Toast.makeText(this, "=== showCalendarSelection ====", Toast.LENGTH_LONG).show()
    }

    override fun showHelpFeedback() {
        Toast.makeText(this, "=== showCalendarSelection ====", Toast.LENGTH_LONG).show()
    }

}