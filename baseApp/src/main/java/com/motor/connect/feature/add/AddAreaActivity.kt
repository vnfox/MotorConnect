package com.motor.connect.feature.add

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseActivity
import com.feature.area.R
import com.feature.area.databinding.AddAreaViewBinding


class AddAreaActivity : BaseActivity<AddAreaViewBinding, AddAreaViewModel>(), com.motor.connect.feature.add.AddAreaView {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddAreaActivity::class.java))
        }
    }

    override fun createViewModel(): AddAreaViewModel {
        val viewModel = AddAreaViewModel(this, BaseModel())
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: AddAreaViewModel): AddAreaViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.add_area_view)
        mBinding.viewModel = mViewModel
        return mBinding
    }

    override fun viewLoaded() {

    }

    override fun actionLeft() {
        super.onBackPressed()
    }
}