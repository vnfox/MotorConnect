package com.motor.connect.feature.notification

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.NotificationViewBinding
import com.motor.connect.base.BaseModel
import com.motor.connect.base.view.BaseViewActivity
import com.motor.connect.feature.model.SmsModel
import kotlinx.android.synthetic.main.notification_view.*


class NotificationActivity : BaseViewActivity<NotificationViewBinding, NotificationViewModel>(), NotificationView {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, NotificationActivity::class.java))
        }
    }

    private val viewModel = NotificationViewModel(this, BaseModel())
    private var adapter: SMSRecieversAdapter? = null


    override fun createViewModel(): NotificationViewModel {
        viewModel.mView = this
        return viewModel
    }

    override fun createDataBinding(mViewModel: NotificationViewModel): NotificationViewBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.notification_view)
        mBinding.viewModel = mViewModel


        adapter = SMSRecieversAdapter { smsModel ->
            Toast.makeText(this, "=== Item Click  ====  " + smsModel.contactName,
                    Toast.LENGTH_LONG).show()
        }

        recyclerView_sms_receivers.adapter = adapter
        viewModel.initViewModel()
        viewModel.initData(this)

        val onClose = findViewById<ImageView>(R.id.action_left)
        onClose?.setOnClickListener {
            actionLeft()
        }
        return mBinding
    }

    override fun updateUI(smsReceivers: MutableList<SmsModel>) {
        hideLoadingView()
        adapter?.setData(smsReceivers)
        recyclerView_sms_receivers.adapter?.notifyDataSetChanged()

        recyclerView_sms_receivers.visibility = View.VISIBLE
        txt_empty.visibility = View.GONE
    }

    override fun showEmptyView() {
        recyclerView_sms_receivers.visibility = View.GONE
        txt_empty.visibility = View.VISIBLE
    }
}