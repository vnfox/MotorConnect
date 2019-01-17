package com.motor.connect.feature.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.feature.area.R
import com.feature.area.databinding.HomeViewBinding
import com.motor.connect.base.view.BaseActivity
import java.util.*
import com.orhanobut.hawk.Hawk.count
import android.databinding.adapters.TextViewBindingAdapter.setText


class HomeActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }


    private val viewModel = HomeViewModel()
    private var circularProgress: CircularProgressIndicator? = null
    private var seekbar: SeekBar? = null
    private var countDownTimer: CountDownTimer? = null

    private val timeTotal: Long? = 10000
    private var timeStep: Int? = 0

    private val timer: Timer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: HomeViewBinding = DataBindingUtil.setContentView(this, R.layout.home_view)
        binding.viewModel = viewModel

        viewModel.startUpdates()

        seekbar = findViewById<SeekBar>(R.id.sb_progress)
        seekbar?.setOnSeekBarChangeListener(this)

        circularProgress = findViewById(R.id.circular_progress)
        circularProgress?.setMaxProgress(10000.0)

        circularProgress?.setProgressTextAdapter(TIME_TEXT_ADAPTER)


        val onNote = findViewById<Button>(R.id.btn_demo)
        onNote?.setOnClickListener {
            onRunProgress()
        }
    }

    fun onRunProgress() {

//       circularProgress?.setCurrentProgress(i.toDouble())

        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {

                }
            }
        }, 1000, 1000)

    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.sb_progress -> {
                circularProgress?.setCurrentProgress(progress.toDouble())
                Log.d("hqdat", "== Progress Change >>>>>>  $progress")
            }
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    private val TIME_TEXT_ADAPTER = CircularProgressIndicator.ProgressTextAdapter { time ->
        var time = time
        val hours = (time / 3600).toInt()
        time %= 3600.0
        val minutes = (time / 60).toInt()
        val seconds = (time % 60).toInt()
        val sb = StringBuilder()
        if (hours < 10) {
            sb.append(0)
        }
        sb.append(hours).append(":")
        if (minutes < 10) {
            sb.append(0)
        }
        sb.append(minutes).append(":")
        if (seconds < 10) {
            sb.append(0)
        }
        sb.append(seconds)
        sb.toString()
    }
}