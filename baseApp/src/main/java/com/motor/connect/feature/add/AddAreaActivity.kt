package com.motor.connect.feature.add

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.ImageView
import com.feature.area.R
import com.feature.area.databinding.AddAreaViewBinding
import com.motor.connect.base.view.BaseActivity


class AddAreaActivity : BaseActivity() {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddAreaActivity::class.java))
        }
    }

    private val viewModel = AddAreaViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: AddAreaViewBinding = DataBindingUtil.setContentView(this, R.layout.add_area_view)

        binding.viewModel = viewModel

        viewModel.startUpdates()

        val onClose = findViewById<ImageView>(R.id.action_left)
        onClose?.setOnClickListener {
            actionLeft()
        }
    }
}