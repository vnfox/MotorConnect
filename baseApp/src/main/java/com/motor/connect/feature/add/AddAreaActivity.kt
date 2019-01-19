package com.motor.connect.feature.add

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.feature.area.R
import com.feature.area.databinding.AddAreaViewBinding
import com.motor.connect.base.view.BaseActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.VanModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk


class AddAreaActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddAreaActivity::class.java))
        }
    }

    private val viewModel = AddAreaViewModel()

    private var areaName: EditText? = null
    private var areaPhone: EditText? = null
    private var areaDetail: EditText? = null
    private var areaVan: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: AddAreaViewBinding = DataBindingUtil.setContentView(this, R.layout.add_area_view)

        binding.viewModel = viewModel

        viewModel.startUpdates()

        areaName = findViewById(R.id.input_name)
        areaPhone = findViewById(R.id.input_phone)
        areaDetail = findViewById(R.id.input_detail)
        areaVan = findViewById(R.id.txt_van)


        val onClose = findViewById<ImageView>(R.id.action_left)
        onClose?.setOnClickListener(this)

        val onImage = findViewById<ImageView>(R.id.img_wall)
        onImage?.setOnClickListener(this)

        val onSave = findViewById<Button>(R.id.btn_save)
        onSave?.setOnClickListener(this)

        val onVanSelect = findViewById<TextView>(R.id.txt_van)
        onVanSelect?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.action_left -> {
                actionLeft()
            }
            R.id.img_wall -> {
                AddAreaActivity.show(this)
            }
            R.id.txt_van -> {
                selectVanView()
            }
            R.id.btn_save -> {
                saveDataShref()
            }
        }
    }

    private fun saveDataShref() {
        val dataModel = AreaModel()
        dataModel.areaName = areaName?.text.toString()
        dataModel.areaPhone = areaPhone?.text.toString()
        dataModel.areaDetails = areaDetail?.text.toString()
        dataModel.areaVans = getAreaVans(areaVan?.text.toString())
        dataModel.areaId = areaPhone?.text.toString()

        //Save data
        var areaModels: MutableList<AreaModel> = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        areaModels.add(dataModel)
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, areaModels)

        shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, true)
        actionLeft()
    }

    private fun getAreaVans(vanSelected: String): List<VanModel>? {

        var areaVans: MutableList<VanModel> = mutableListOf()
        val van = VanModel()
        var numVan = vanSelected.substring(0, 1).toInt()

        for (i in 0..numVan) {
            van.vanId = "0$i"
            van.vanStatus = false

            areaVans.add(van)
        }
        return areaVans
    }

    private fun selectVanView() {
        var items = resources.getStringArray(R.array.number_van_choice)
        AlertDialog.Builder(this)
                .setTitle("So Van Su Dung")
                .setSingleChoiceItems(items, 0) { _, i ->

                    areaVan?.text = items[i].toString()
                }
                .setPositiveButton("Chon", null)
                .setNegativeButton("Huy", null)
                .show()
    }
}