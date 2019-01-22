package com.motor.connect.feature.details

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.feature.area.R
import com.feature.area.databinding.DetailViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity

class AreaDetailActivity : BaseViewActivity<DetailViewBinding, AreaDetailViewModel>(), AreaDetailView, View.OnClickListener {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AreaDetailActivity::class.java))
        }
    }

    private val viewModel = AreaDetailViewModel(this, BaseModel())

    override fun createViewModel(): AreaDetailViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: AreaDetailViewModel): DetailViewBinding {

        mBinding = DataBindingUtil.setContentView(this, R.layout.detail_view)
        mBinding.viewModel = mViewModel

        //Setup Account Bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.mipmap.ic_back)
        toolbar.setNavigationOnClickListener { actionLeft() }

        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbar.title = "Details View"

        loadBackdrop()
        return mBinding
    }

    private fun loadBackdrop() {
        val imageView = findViewById<ImageView>(R.id.backdrop)
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).apply(RequestOptions.centerCropTransform()).into(imageView)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.action_left -> {
                actionLeft()
            }
        }
    }
}
