package com.motor.connect.feature.setting.howuse

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.annotation.RequiresApi
import com.feature.area.R
import com.feature.area.databinding.HowUseViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import kotlinx.android.synthetic.main.how_use_view.*


class HowToUseActivity : BaseViewActivity<HowUseViewBinding, HowToUseViewModel>(), HowToUseView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, HowToUseActivity::class.java))
        }
    }

    private val viewModel = HowToUseViewModel(this, BaseModel())

    override fun createViewModel(): HowToUseViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: HowToUseViewModel): HowUseViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.how_use_view)
        mBinding.viewModel = mViewModel

        viewModel.initViewModel()
        return mBinding
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun viewLoaded(url: String) {
        webView.webViewClient
        webView.loadUrl(url)
    }
}