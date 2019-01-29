package com.motor.connect.feature.details

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.design.widget.BottomSheetDialog
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
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.setting.area.SettingAreaScheduleActivity
import com.motor.connect.feature.setting.van.SettingAreaVanActivity
import com.motor.connect.utils.EnumHelper
import com.motor.connect.utils.EnumHelper.*
import com.motor.connect.utils.StringUtil
import com.motor.connect.utils.StringUtil.*
import kotlinx.android.synthetic.main.detail_view.*


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

    override fun viewMotorInfo(model: AreaModel) {
        txt_area_name.text = model.areaName
        txt_area_phone.text = model.areaPhone
        txt_area_van.text = "So van " + model.areaVans.size.toString()


        //03 0601 030 1100 060 1600 090    <01> repeat
        //"Lịch tuới: ngày tưới " + StringUtil.getCountWorkingDay(model.areaSchedule) + "lần"

        txt_area_scheduler.text = getScheduleWorking(model.areaSchedule)


    }

    override fun viewMotorWorking() {
        // get data done => show
        //showUnderConstruction("update info viewMotorWorking")
    }

    private fun getScheduleWorking(value: String): String {
        val result = "chua co lich tuoi"

        if (value.isEmpty())
            return result

        val count = StringUtil.getCountWorkingDay(value)
        when (count) {
            ScheduleDays.ONE_DAY.key -> {

                return getScheduleWorkingOneDay(value)
            }
            ScheduleDays.TWO_DAY.key -> {
                return getScheduleWorkingTwoDay(value)
            }
            ScheduleDays.THREE_DAY.key -> {
                return getScheduleWorkingThreeDay(value)
            }
        }
        return result!!
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
