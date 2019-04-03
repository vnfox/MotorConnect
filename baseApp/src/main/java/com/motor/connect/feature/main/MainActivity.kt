package com.motor.connect.feature.main

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import com.feature.area.R
import com.feature.area.databinding.ActivityMainBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.add.AddAreaActivity
import com.motor.connect.feature.details.AreaDetailActivity
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.notification.NotificationActivity
import com.motor.connect.feature.setting.SettingActivity
import com.motor.connect.utils.DialogHelper
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseViewActivity<ActivityMainBinding, UserViewModel>(), MainAreaView, DialogHelper.AlertDialogListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val viewModel = UserViewModel(this, BaseModel())
    private var adapter: UserAdapter? = null
    private var isFirst: Boolean = false


    override fun createViewModel(): UserViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: UserViewModel): ActivityMainBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = mViewModel

        //Adapter item click
        adapter = UserAdapter { areaModel, position ->
            Hawk.put(MotorConstants.KEY_POSITION, position)
            //Hawk.put(MotorConstants.KEY_PUT_AREA_DETAIL, areaModel)
            val intent = Intent(this, AreaDetailActivity::class.java)
            this.startActivity(intent)
        }
        recyclerView.adapter = adapter

        val isUser = shef?.getFirstUserPref(MotorConstants.FIRST_USED)
        viewModel.initData(isUser)

        return mBinding
    }

    override fun showEmptyView() {
        isFirst = true
        recyclerView.visibility = View.GONE
        txt_empty.visibility = View.VISIBLE

    }

    override fun updateUI(dataArea: MutableList<AreaModel>) {
        isFirst = false
        recyclerView.visibility = View.VISIBLE
        txt_empty.visibility = View.GONE
        adapter?.setData(dataArea)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        if (shef!!.getTriggerData(MotorConstants.KEY_TRIGGER_DATA) || shef!!.getUpdateData(MotorConstants.KEY_EDIT_AREA)) {
            viewModel.updateData()
            shef!!.setTriggerData(MotorConstants.KEY_TRIGGER_DATA, false)
            shef!!.setUpdateData(MotorConstants.KEY_EDIT_AREA, false)
        }
    }

    override fun onBackPressed() {
        var title = getString(R.string.sms_warning_title)
        var content = getString(R.string.sms_exit_content)

        var alertDialogHelper: DialogHelper?
        alertDialogHelper = DialogHelper(this)
        alertDialogHelper?.showAlertDialog(title, content, getString(R.string.btn_accept), getString(R.string.btn_huy), false)
    }

    fun openEmptyScreen(v: View) {
        AddAreaActivity.show(this)
    }

    fun openAddAreaScreen(v: View) {
        AddAreaActivity.show(this)
    }

    fun openNotificationScreen(v: View) {
        if (!isFirst)
            NotificationActivity.show(this)
    }

    fun openSettingScreen(v: View) {
        SettingActivity.show(this)
    }

    override fun onPositiveClick() {
        this.finish()
    }

    override fun onNegativeClick() {
        //do nothing
    }
}
