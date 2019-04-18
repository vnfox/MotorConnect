package com.motor.connect.feature.edit

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.feature.area.R
import com.feature.area.databinding.EditAreaViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import kotlinx.android.synthetic.main.edit_area_view.*


class EditAreaActivity : BaseViewActivity<EditAreaViewBinding, EditAreaViewModel>(), EditAreaView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, EditAreaActivity::class.java))
        }
    }

    private val viewModel = EditAreaViewModel(this, BaseModel())

    private var areaName: EditText? = null
    private var areaPhone: EditText? = null
    private var areaDetail: EditText? = null
    private var areaVan: TextView? = null

    override fun createViewModel(): EditAreaViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: EditAreaViewModel): EditAreaViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.edit_area_view)
        mBinding.viewModel = viewModel

        areaName = findViewById(R.id.input_name)
        areaPhone = findViewById(R.id.input_phone)
        areaDetail = findViewById(R.id.input_detail)
        areaVan = findViewById(R.id.txt_van)

        mViewModel.initViewModel()
        return mBinding
    }

    override fun viewLoaded(model: AreaModel?) {

        areaName?.setText(model?.areaName)
        areaPhone?.setText(model?.areaPhone)
        areaDetail?.setText(model?.areaDetails)
        areaVan?.text = (model?.areaVans?.size.toString() + " Van")
    }

    override fun backDetailScreen() {
        // reload data
        shef!!.setUpdateData(MotorConstants.KEY_EDIT_AREA, true)
        actionLeft()
    }

    fun actionClose(view: View?) {
        //Check info
        shef!!.setUpdateData(MotorConstants.KEY_EDIT_AREA, false)
        actionLeft()
    }

    fun selectBackdrop(view: View?) {
        showUnderConstruction("selectBackdrop")
    }

    fun onSelectVanUsed(view: View?) {
        selectVanView()
    }

    fun actionRemove(view: View?) {
        showUnderConstruction()
        var vansUsed = viewModel.getNumberVanUsed() - 1
        if (vansUsed < 1) {
            btn_remove.isEnabled = false
        } else {
            btn_remove.isEnabled = true
            areaVan?.text = "$vansUsed Van"
            viewModel.updateVansUsed(false, vansUsed)
        }
    }

    fun actionAdd(view: View?) {
        showUnderConstruction()
        var vansUsed = viewModel.getNumberVanUsed() + 1
        if (vansUsed >= 8) {
            btn_add.isEnabled = false
        } else {
            btn_add.isEnabled = true
            areaVan?.text = "$vansUsed Van"
            viewModel.updateVansUsed(true, vansUsed)
        }
    }

    fun onSaveData(view: View?) {
        val dataModel = AreaModel()
        dataModel.areaName = areaName?.text.toString()
        dataModel.areaPhone = areaPhone?.text.toString()
        dataModel.areaDetails = areaDetail?.text.toString()
        dataModel.areaVans = getAreaVans(areaVan?.text.toString())
        dataModel.areaId = areaPhone?.text.toString()

        viewModel.updateDataArea(dataModel)
    }

    private fun getAreaVans(vanSelected: String): List<VanModel>? {

        var areaVans: MutableList<VanModel> = mutableListOf()

        var numVan = vanSelected.substring(0, 1).toInt()

        for (i in 1..numVan) {
            val van = VanModel()
            van.vanId = "0$i"
            van.vanStatus = false
            areaVans.add(van)
        }
        return areaVans
    }

    private fun selectVanView() {
        var items = resources.getStringArray(R.array.number_van_choice)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_area_van_use))
                .setSingleChoiceItems(items, 0) { _, i ->

                    areaVan?.text = items[i].toString()
                }
                .setPositiveButton(getString(R.string.btn_chon), null)
                .setNegativeButton(getString(R.string.btn_huy), null)
                .show()
    }
}