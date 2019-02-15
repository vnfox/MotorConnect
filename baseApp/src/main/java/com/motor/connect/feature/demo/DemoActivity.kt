package com.motor.connect.feature.demo

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.feature.area.R
import com.feature.area.databinding.HomeViewBinding
import com.motor.connect.base.view.BaseActivity
import com.motor.connect.utils.DialogHelper


class DemoActivity : BaseActivity(), DialogHelper.AlertDialogListener {


    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, DemoActivity::class.java))
        }
    }

    var alertDialogHelper: DialogHelper? = null

//    private var alertDialog = AlertDialog.Builder(this)
//    private val dialogListener: DialogListener? = null

    private val viewModel = DemoViewModel()
    private var circularProgress: CircularProgressIndicator? = null

    private val timeTotal: Int = 1800
    private var pStatus: Int = 0
    private val handler = Handler()
    private lateinit var tv: TextView

    private var arrStrings: Array<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: HomeViewBinding = DataBindingUtil.setContentView(this, R.layout.home_view)
        binding.viewModel = viewModel

        viewModel.startUpdates()

//        showProgressDialog()

        circularProgress = findViewById(R.id.circular_progress)
        circularProgress?.maxProgress = timeTotal.toDouble()

        circularProgress?.setProgressTextAdapter(TIME_TEXT_ADAPTER)


        circularProgress?.setOnProgressChangeListener { progress, maxProgress ->

            //            Log.d("PROGRESS", String.format("Current: %1$.0f, max: %2$.0f", progress, maxProgress))

            Log.d("hqdat", "== setOnProgressChangeListener>>>>>>   $progress")
        }

        val onNote = findViewById<Button>(R.id.btn_demo)
        onNote?.setOnClickListener {

            onDemoFunction()

        }
        //4,6,8
        //Dn1234 01 01 0601 030
        //Dn1234 01 03 0601 030 1100 060 1600 090

        //5,7,9
        //De1234 01 01 0601 030 01
        //De1234 01 03 0601 030 1100 060 1600 090 01
        val onTest = findViewById<Button>(R.id.btn_test)
        onTest.setOnClickListener {

            showDialog()
        }
    }

    private fun showDialog() {
        alertDialogHelper = DialogHelper(this)

        alertDialogHelper?.showAlertDialog("Title", "Content Description", "Discard", "Cancel", false)
    }

    override fun onPositiveClick() {
        Log.d("hqdat", "== onPositiveClick>>>>>> ")

    }

    override fun onNegativeClick() {
        Log.d("hqdat", "== onNegativeClick>>>>>> ")
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