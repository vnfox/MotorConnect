package com.motor.connect.feature.details

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.feature.area.R
import com.feature.area.databinding.DetailViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.setting.area.SettingAreaScheduleActivity
import com.motor.connect.feature.setting.van.SettingAreaVanActivity


class AreaDetailActivity : BaseViewActivity<DetailViewBinding, AreaDetailViewModel>(), AreaDetailView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AreaDetailActivity::class.java))
        }
    }

    private val viewModel = AreaDetailViewModel(this, BaseModel())

    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null

    private var bottomSheetDialog: BottomSheetDialog? = null

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
        toolbar.setNavigationOnClickListener {

            actionLeft()
        }

        //Setup info
        viewModel.initViewModel()

        //Setup CollapsingToolbarLayout View
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        collapsingToolbarLayout?.title = "Details View"

        return mBinding
    }


    override fun viewLoaded() {
        loadBackdrop()
    }

    override fun viewMotorWorking() {
        showUnderConstruction("update info viewMotorWorking")
    }

    override fun viewMotorInfo() {
        showUnderConstruction("update info  viewMotorInfo")
    }

    private fun loadBackdrop() {
        val imageView = findViewById<ImageView>(R.id.backdrop)
        //Todo Will get list Backdrop
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).apply(RequestOptions.centerCropTransform()).into(imageView)
    }


    fun selectSettingView(v: View) {
        bottomSheetDialog = BottomSheetDialog(this)
        val sheetView = this.layoutInflater.inflate(R.layout.dialog_bottom_sheet_view, null)
        bottomSheetDialog?.setContentView(sheetView)
        bottomSheetDialog?.show()
    }

    fun setupScheduleArea(v: View) {
        bottomSheetDialog?.dismiss()
        SettingAreaScheduleActivity.show(this)
    }

    fun setupVanUsedArea(v: View) {
        bottomSheetDialog?.dismiss()
        SettingAreaVanActivity.show(this)
    }

    fun scheduleStopArea(v: View) {
        showUnderConstruction("scheduleStopArea")
        bottomSheetDialog?.dismiss()
    }
}
