package com.motor.connect.feature.notification

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.Toast
import com.feature.area.R
import com.feature.area.databinding.NotificationViewBinding
import com.motor.connect.base.view.BaseActivity
import com.motor.connect.feature.model.SmsModel
import kotlinx.android.synthetic.main.notification_view.*


class NotificationActivity : BaseActivity() {

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, NotificationActivity::class.java))
        }
    }

    private var dataMessage: MutableList<SmsModel> = mutableListOf()

    private val viewModel = NotificationViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: NotificationViewBinding = DataBindingUtil.setContentView(this, R.layout.notification_view)

        binding.viewModel = viewModel

        val adapter = SMSRecieversAdapter { smsModel ->
            Toast.makeText(this, "=== Item Click  ====  " + smsModel.contactName,
                    Toast.LENGTH_LONG).show()
        }

        recyclerView_sms_receivers.adapter = adapter
        binding.viewModel = viewModel

        viewModel.startUpdates(getSMSRecievers())


        val onClose = findViewById<ImageView>(R.id.action_left)
        onClose?.setOnClickListener {
            actionLeft()
        }
    }

    fun getSMSRecievers(): MutableList<SmsModel> {
        val sms: MutableList<SmsModel> = mutableListOf()
        val uriSMSURI = Uri.parse("content://sms/inbox")
        val cur = contentResolver.query(uriSMSURI, null, null, null, null)

        while (cur != null && cur.moveToNext()) {
            val address = cur.getString(cur.getColumnIndex("address"))
            val body = cur.getString(cur.getColumnIndexOrThrow("body"))
            val type = cur.getString(cur.getColumnIndexOrThrow("type"))
            val contact = getContactbyPhoneNumber(this, address)
            val timestamp = cur.getString(cur.getColumnIndexOrThrow("date"))

//            sms.add("Number: $address Contact: $contact Type: $type .Message: $body")
            val smsModel = SmsModel()
            smsModel.contactName = contact
            smsModel.phoneNumber = address
            smsModel.smsType = type
            smsModel.messageContent = body
            smsModel.date = timestamp

            sms.add(smsModel)

            //Get 30 sms lasted
            if (sms.size == 30) {
                return sms
            }
        }
        cur?.close()
        return sms
    }

    private fun getContactbyPhoneNumber(c: Context, phoneNumber: String): String {

        try {
            val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
            val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
            val cursor = c.contentResolver.query(uri, projection, null, null, null)
            if (cursor == null) {
                return phoneNumber
            } else {
                var name = phoneNumber
                try {

                    if (cursor.moveToFirst()) {
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
                    }

                } finally {
                    cursor.close()
                }

                return name
            }
        } finally {
            return phoneNumber
        }
    }
}