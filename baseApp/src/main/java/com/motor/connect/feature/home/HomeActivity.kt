package com.motor.connect.feature.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseActivity_View
import com.motor.connect.feature.notification.NotificationActivity
import com.motor.connect.feature.setting.SettingActivity
import com.feature.area.R
import com.feature.area.databinding.HomeViewBinding
import com.motor.connect.feature.data.MainActivity


class HomeActivity : BaseActivity_View<HomeViewBinding, HomeViewModel>(), HomeView {

    override fun viewLoaded() {

    }

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    override fun createViewModel(): HomeViewModel {
        val viewModel = HomeViewModel(this, BaseModel())
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: HomeViewModel): HomeViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.home_view)
        mBinding.viewModel = mViewModel
        return mBinding
    }

    override fun showHomePlanning() {
//        CreatePlanActivity.show(this)
//
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }


    override fun showAddArea() {
        com.motor.connect.feature.add.AddAreaActivity.show(this)
    }


    override fun showNotification() {
        NotificationActivity.show(this)
    }


    override fun showSetting() {
        SettingActivity.show(this)
    }

}