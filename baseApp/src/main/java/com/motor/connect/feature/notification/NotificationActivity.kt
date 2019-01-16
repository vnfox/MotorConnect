package com.motor.connect.feature.notification

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.ImageView
import com.feature.area.R
import com.feature.area.databinding.NotificationViewBinding
import com.motor.connect.base.view.BaseActivity


class NotificationActivity : BaseActivity() {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, NotificationActivity::class.java))
        }
    }

    private val viewModel = NotificationViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: NotificationViewBinding = DataBindingUtil.setContentView(this, R.layout.notification_view)

        binding.viewModel = viewModel

        viewModel.startUpdates()

        val onClose = findViewById<ImageView>(R.id.action_left)
        onClose?.setOnClickListener {
            actionLeft()
        }
    }
}