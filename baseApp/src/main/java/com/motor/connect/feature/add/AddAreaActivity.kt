package com.motor.connect.feature.add

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.feature.area.R
import com.feature.area.databinding.AddAreaViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.motor.connect.utils.StringUtils


class AddAreaActivity : BaseViewActivity<AddAreaViewBinding, AddAreaViewModel>(), AddAreaView {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddAreaActivity::class.java))
        }
    }

    private val timeTotal: Int = 10
    private var pStatus: Int = 0
    private val handler = Handler()

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

    fun selectBackdrop(view: View?) {
        showUnderConstruction()
    }

    fun onSelectVanUsed(view: View?) {
        selectVanView()

    }

    fun onSaveData(view: View?) {
        if (StringUtils.isNullOrEmpty(areaName?.text.toString())) {
            areaName?.error = getString(R.string.add_input_name_empty)
            return
        }
        if (StringUtils.isNullOrEmpty(areaPhone?.text.toString())) {
            areaPhone?.error = getString(R.string.add_input_phone_empty)
            return
        }

        prepareDate()
    }

    override fun goBackMainScreen() {
        shef!!.setFirstUserPref(MotorConstants.FIRST_USED, false)
        shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, true)
        backToMainScreen()
    }

    private fun prepareDate() {
        viewModel.showProgressView()
        val dataModel = AreaModel()
        dataModel.areaName = areaName?.text.toString()
        dataModel.areaPhone = areaPhone?.text.toString()
        dataModel.areaDetails = areaDetail?.text.toString()
        dataModel.areaVans = getAreaVans(areaVan?.text.toString())
        dataModel.areaId = areaPhone?.text.toString()

        val isFirstUsed = shef?.getFirstUserPref(MotorConstants.FIRST_USED)
        viewModel.saveDataArea(isFirstUsed, dataModel)
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
                .setTitle(getString(R.string.add_number_van_used))
                .setSingleChoiceItems(items, 0) { _, i ->

                    areaVan?.text = items[i].toString()
                }
                .setPositiveButton(getString(R.string.btn_chon), null)
                .setNegativeButton(getString(R.string.btn_huy), null)
                .show()
    }

    private fun backToMainScreen() {
        Thread(Runnable {
            while (pStatus < timeTotal) {
                pStatus += 1
                handler.post {

                    if (pStatus == timeTotal) {
                        viewModel.hideProgressView()
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