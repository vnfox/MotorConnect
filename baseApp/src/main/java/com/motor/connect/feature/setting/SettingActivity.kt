package com.motor.connect.feature.setting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.SettingViewBinding
import com.motor.connect.base.view.BaseActivity
import com.motor.connect.utils.PermissionUtils
import io.reactivex.annotations.NonNull
import java.util.*


class SettingActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    lateinit var needPermissions: MutableList<String>


    private val viewModel = SettingViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: SettingViewBinding = DataBindingUtil.setContentView(this, R.layout.setting_view)

        binding.viewModel = viewModel

        viewModel.startUpdates()

        verifyAppPermission()


        val onClose = findViewById<ImageView>(R.id.action_left)
        onClose?.setOnClickListener {
            actionLeft()
        }


        val onSetting = findViewById<TextView>(R.id.txt_area)
        onSetting?.setOnClickListener {
            Toast.makeText(this, "=== Send SMS  ====", Toast.LENGTH_LONG).show()

            val smsNumber = "0947818171"
            val smsText = "Send sms test app putExtra"

            val uri = Uri.parse("smsto:$smsNumber")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", smsText)
            startActivity(intent)
        }

        val onConfig = findViewById<TextView>(R.id.txt_config)
        onConfig?.setOnClickListener {

            if (checkPermission(Manifest.permission.SEND_SMS)) {
                Toast.makeText(this, "=== permission  Accept ====", Toast.LENGTH_LONG).show()
                val smsNumber = "0947818171"
                val smsText = "Send sms test app SmsManager"

                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(smsNumber, null, smsText, null, null)
            } else {
                Toast.makeText(this, "=== permission  Denied ====", Toast.LENGTH_LONG).show()
            }
        }

        val onNote = findViewById<TextView>(R.id.txt_note)
        onNote?.setOnClickListener {
            grantPermissions()
        }

        val onHowUse = findViewById<TextView>(R.id.txt_how_use)
        onHowUse?.setOnClickListener {

        }


        val onHelp = findViewById<TextView>(R.id.txt_help)
        onHelp?.setOnClickListener {

            getSMS()
            Log.d("hqdat", "== SMS content:   " + getSMS()[0])
        }
    }

    fun getSMS(): List<String> {
        val sms = ArrayList<String>()
        val uriSMSURI = Uri.parse("content://sms/inbox")
        val cur = contentResolver.query(uriSMSURI, null, null, null, null)

        while (cur != null && cur.moveToNext()) {
            val address = cur.getString(cur.getColumnIndex("address"))
            val body = cur.getString(cur.getColumnIndexOrThrow("body"))
            sms.add("Number: $address .Message: $body")
        }

        cur?.close()
        return sms
    }

    private fun verifyAppPermission() {
        needPermissions = ArrayList()

        if (!PermissionUtils.isGranted(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
            needPermissions.add(Manifest.permission.SEND_SMS)
        }
    }

    private fun checkPermission(sendSms: String): Boolean {

        val checkpermission = ContextCompat.checkSelfPermission(this, sendSms)
        return checkpermission == PackageManager.PERMISSION_GRANTED
    }

    fun grantPermissions() {
        ActivityCompat.requestPermissions(this,
                needPermissions.toTypedArray(), 101)
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "=== permission  Accept ====", Toast.LENGTH_LONG).show()
                val smsNumber = "0947818171"
                val smsText = "Send sms test app SmsManager"

                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(smsNumber, null, smsText, null, null)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.action_left -> {

            }
            R.id.txt_area -> {/* you can omit the braces if there is only a single expression */
            }
        }
    }
}