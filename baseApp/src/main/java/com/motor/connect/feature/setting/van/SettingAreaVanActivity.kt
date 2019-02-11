package com.motor.connect.feature.setting.van

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.feature.area.R
import com.feature.area.databinding.SettingAreaVanViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.model.VanModel
import kotlinx.android.synthetic.main.setting_area_van_view.*
import java.lang.StringBuilder


class SettingAreaVanActivity : BaseViewActivity<SettingAreaVanViewBinding, SettingAreaVanViewModel>(), SettingAreaVanView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingAreaVanActivity::class.java))
        }
    }

    private val viewModel = SettingAreaVanViewModel(this, BaseModel())
    private var adapter: SettingAreaVanAdapter? = null

    override fun createViewModel(): SettingAreaVanViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: SettingAreaVanViewModel): SettingAreaVanViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_area_van_view)
        mBinding.viewModel = mViewModel

        //Adapter item click
        adapter = SettingAreaVanAdapter { areaModel, position ->

            //Update UI  => save data
            showUnderConstruction("setupVanUsed $position")
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 1)


        viewModel.initViewModel()

        return mBinding
    }

    override fun viewLoaded(areaVans: MutableList<VanModel>) {
        adapter?.setData(areaVans)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun actionClose(v: View) {
        actionLeft()
    }

    fun setupVanUsed(v: View) {
        showUnderConstruction("setupVanUsed")
        var countItem = adapter?.itemCount
        var listVans = adapter?.getDataView()
        var stringVans = StringBuilder()


        for (i in 0 ..listVans!!.size-1){
            if(listVans!![i].vanStatus){
                stringVans.append(listVans!![i].vanId).append(" ")
            }
        }

        Log.d("hqdat",">>>  String  " + stringVans.toString().trim())

        //Send SMS
    }
}