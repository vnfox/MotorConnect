package com.motor.connect.feature.add

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.motor.connect.R
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.databinding.AddAreaViewBinding
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants


class AddAreaActivity : BaseViewActivity<AddAreaViewBinding, AddAreaViewModel>(), AddAreaView {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddAreaActivity::class.java))
        }
    }

    private var pStatus: Int = 0
    private val viewModel = AddAreaViewModel(this, BaseModel())

    private var areaName: EditText? = null
    private var areaPhone: EditText? = null
    private var areaDetail: EditText? = null
    private var areaVan: TextView? = null

    override fun createViewModel(): AddAreaViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: AddAreaViewModel): AddAreaViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.add_area_view)
        mBinding.viewModel = viewModel

        areaName = findViewById(R.id.input_name)
        areaPhone = findViewById(R.id.input_phone)
        areaDetail = findViewById(R.id.input_detail)
        areaVan = findViewById(R.id.txt_van)

        return mBinding
    }

    fun actionClose(view: View?) {
        actionLeft()
    }

    fun onSelectVanUsed(view: View?) {
        selectVanView()
    }

    fun onSaveData(view: View?) {
        if (areaName?.text.toString().isNullOrEmpty()) {
            areaName?.error = getString(R.string.add_input_name_empty)
            return
        }
        if (areaPhone?.text.toString().isNullOrEmpty()) {
            areaPhone?.error = getString(R.string.add_input_phone_empty)
            return
        }
        prepareData()
        backToMainScreen()
    }

    override fun goBackMainScreen() {
        shef!!.setFirstUserPref(MotorConstants.FIRST_USED, false)
        shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, true)
        backToMainScreen()
    }

    private fun prepareData() {
        showLoadingView(getString(R.string.sms_loading))
        val dataModel = AreaModel()
        dataModel.areaName = areaName?.text.toString()
        dataModel.areaPhone = areaPhone?.text.toString()
        dataModel.areaDetails = areaDetail?.text.toString()
        dataModel.areaVans = getAreaVans(areaVan?.text.toString())

        val isFirstUsed = shef?.getFirstUserPref(MotorConstants.FIRST_USED)
        viewModel.saveDataArea(isFirstUsed, dataModel)
    }

    //Default all Van open
    private fun getAreaVans(vanSelected: String): List<VanModel>? {
        var areaVans: MutableList<VanModel> = mutableListOf()
        var numVan = vanSelected.substring(0, 1).toInt()

        for (i in 1..numVan) {
            val van = VanModel()
            van.vanId = "0$i"
            van.vanStatus = true
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

    private fun backToMainScreen() {
        Thread(Runnable {
            while (pStatus < MotorConstants.TIME_PROGRESS) {
                pStatus += 1
                handler.post {
                    if (pStatus == MotorConstants.TIME_PROGRESS) {
                        hideLoadingView()
                        actionLeft()
                    }
                }
                try {
                    // Sleep for 200 milliseconds.
                    // Just to display the progress slowly
                    Thread.sleep(100) //thread will take approx 3 seconds to finish
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }
}