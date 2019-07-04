package com.motor.connect.feature.setting.help

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.motor.connect.R
import com.motor.connect.databinding.HelpFeedbackViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import kotlinx.android.synthetic.main.how_use_view.*


class HelpFeedbackActivity : BaseViewActivity<HelpFeedbackViewBinding, HelpFeedbackViewModel>(), HelpFeedbackView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, HelpFeedbackActivity::class.java))
        }
    }

    private val viewModel = HelpFeedbackViewModel(this, BaseModel())

    override fun createViewModel(): HelpFeedbackViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: HelpFeedbackViewModel): HelpFeedbackViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.help_feedback_view)
        mBinding.viewModel = mViewModel

        viewModel.initViewModel()
        return mBinding
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun viewLoaded(url: String) {
        webView.webViewClient
        webView.loadUrl(url)
    }

    fun gotoPreviousScreen(view: View) {
        actionLeft()
    }
}