package com.motor.connect.feature.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.feature.area.R
import com.feature.area.databinding.HomeViewBinding
import com.motor.connect.base.view.BaseActivity


class HomeActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    private val viewModel = HomeViewModel()
    private var circularProgress: CircularProgressIndicator? = null
    private var seekbar: SeekBar? = null


    private var mProgress: ProgressBar? = null

    private val timeTotal: Int = 1800
    private var pStatus: Int = 0
    private val handler = Handler()
    private lateinit var tv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: HomeViewBinding = DataBindingUtil.setContentView(this, R.layout.home_view)
        binding.viewModel = viewModel

        viewModel.startUpdates()

        showProgressDialog()

        seekbar = findViewById<SeekBar>(R.id.sb_progress)
        seekbar?.setOnSeekBarChangeListener(this)

        circularProgress = findViewById(R.id.circular_progress)
        circularProgress?.maxProgress = timeTotal.toDouble()

        circularProgress?.setProgressTextAdapter(TIME_TEXT_ADAPTER)


        circularProgress?.setOnProgressChangeListener { progress, maxProgress ->

            //            Log.d("PROGRESS", String.format("Current: %1$.0f, max: %2$.0f", progress, maxProgress))

            Log.d("hqdat", "== setOnProgressChangeListener>>>>>>   $progress")
        }


        //Todo Progress
        val res = resources
        val drawable = res.getDrawable(R.drawable.circular)
        mProgress = findViewById<View>(R.id.circularProgressbar) as ProgressBar?
        mProgress?.progress = 0   // Main Progress

        mProgress?.max = timeTotal // Maximum Progress
        mProgress?.secondaryProgress = timeTotal // Secondary Progress
        mProgress?.progressDrawable = drawable
        tv = findViewById<View>(R.id.tv) as TextView


        val onNote = findViewById<Button>(R.id.btn_demo)
        onNote?.setOnClickListener {
            //onRunProgress()


//            onStartThread()

        }

        val onTest = findViewById<Button>(R.id.btn_test)
        onTest.setOnClickListener {
            onDemoFunction()
        }
    }

    private fun onDemoFunction() {
        Thread(Runnable {
            // TODO Auto-generated method stub
            while (pStatus < timeTotal) {
                pStatus += 1

                handler.post {
                    circularProgress?.setCurrentProgress(pStatus.toDouble())
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
//        val hours = (time / 3600).toInt()
        time %= 3600.0

        val minutes = (time / 60).toInt()
        val seconds = (time % 60).toInt()
        val sb = StringBuilder()
//        if (hours < 10) {
//            sb.append(0)
//        }
//        sb.append(hours).append(":")
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

    private fun onStartThread() {
        Thread(Runnable {
            // TODO Auto-generated method stub
            while (pStatus < timeTotal) {
                pStatus += 1

                handler.post {
                    // TODO Auto-generated method stub
                    mProgress?.progress = pStatus
                    tv.text = this.getStringMessage(pStatus)

                    if (pStatus == timeTotal) {
                        tv.text = "Done"
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

    private fun getStringMessage(time: Int): String {

        val minutes = (time / 60)
        val seconds = (time % 60)
        val sb = StringBuilder()
        if (minutes < 10) {
            sb.append(0)
        }
        sb.append(minutes).append(":")
        if (seconds < 10) {
            sb.append(0)
        }
        sb.append(seconds)
        sb.toString()
        return sb.toString()
    }




}