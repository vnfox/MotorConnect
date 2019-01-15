package com.motor.connect.feature.notification

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.widget.Toast
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseActivity
import com.feature.area.R
import com.feature.area.databinding.NotificationViewBinding


class NotificationActivity : BaseActivity<NotificationViewBinding, NotificationViewModel>(), NotificationView {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, NotificationActivity::class.java))
        }
    }

    override fun createViewModel(): NotificationViewModel {
        val viewModel = NotificationViewModel(this, BaseModel())
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: NotificationViewModel): NotificationViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.notification_view)
        mBinding.viewModel = mViewModel
        return mBinding
    }

    override fun viewLoaded() {
        Toast.makeText(this, "=== showCalendarSelection ====", Toast.LENGTH_LONG).show()
    }

    override fun actionLeft() {
        super.onBackPressed()
    }
}