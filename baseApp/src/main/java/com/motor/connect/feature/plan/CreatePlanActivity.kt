package com.motor.connect.feature.plan

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseActivity_View
import com.feature.area.R
import com.feature.area.databinding.CreatePlanViewBinding
import kotlinx.android.synthetic.main.create_plan_view.*
import java.text.SimpleDateFormat
import java.util.*


class CreatePlanActivity : BaseActivity_View<CreatePlanViewBinding, CreatePlanViewModel>(), CreatePlanView, View.OnClickListener {

    override fun viewLoaded() {

    }

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, CreatePlanActivity::class.java))
        }
    }

    var dateResult: String = ""

    override fun createViewModel(): CreatePlanViewModel {
        val viewModel = CreatePlanViewModel(this, BaseModel())
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: CreatePlanViewModel): CreatePlanViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.create_plan_view)
        mBinding.viewModel = mViewModel

        setUpActionBar()

        val btnTime = findViewById<Button>(R.id.btn_time)
        btnTime.setOnClickListener(this)

        return mBinding
    }

    override fun setUpActionBar() {
        //Implement later
    }

    override fun actionRight() {
        super.onBackPressed()
    }

    override fun actionLeft() {
        super.onBackPressed()
    }

    override fun typeYourIdea() {

        var items: Array<String> = resources.getStringArray(R.array.dialog_single_choice_array)
        var selected: Int = 0

        AlertDialog.Builder(this)
                .setTitle("Choose idea")
                .setSingleChoiceItems(items, selected) { dialogInterface, i ->

                    showUnderConstruction("AlertDialog   ${items[selected]}")
                }
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .show()
    }

    override fun setTimeDuration() {
        showUnderConstruction("timeDuration")

    }

    //Need to custom getTime
    override fun onClick(p0: View?) {
        val cal = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)

            dateResult = SimpleDateFormat("yyyy:MM:dd").format(cal.time)
            setDateTime(dateResult)
        }
        DatePickerDialog(this, datePicker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
    }

    fun setDateTime(dateResult: String) {
        val cal = Calendar.getInstance()
        val timeSetListener = OnTimeSetListener { timePicker, hour, minute ->

            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            btn_time.text = dateResult + SimpleDateFormat(" HH:mm").format(cal.time)
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    override fun inviteFriends() {

        //open contact list
    }

}