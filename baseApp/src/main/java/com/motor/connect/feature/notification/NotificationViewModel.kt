package com.motor.connect.feature.notification

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.SmsModel

class NotificationViewModel(mView: NotificationView?, mModel: BaseModel)
    : BaseViewModel<NotificationView, BaseModel>(mView, mModel) {

    var smsReceivers: MutableList<SmsModel> = mutableListOf()

    override fun initViewModel() {

    }

    fun initData(context: NotificationActivity) {
        smsReceivers = getSMSRecievers(context)

        if (smsReceivers.isEmpty()) {
            mView?.showEmptyView()
        } else {
            mView?.updateUI(smsReceivers)
        }
    }

    fun getSMSRecievers(context: NotificationActivity): MutableList<SmsModel> {
        val sms: MutableList<SmsModel> = mutableListOf()
        val uriSMSURI = Uri.parse("content://sms/inbox")
        val cur = context.contentResolver.query(uriSMSURI, null, null, null, null)

        while (cur != null && cur.moveToNext()) {
            val address = cur.getString(cur.getColumnIndex("address"))
            val body = cur.getString(cur.getColumnIndexOrThrow("body"))
            val type = cur.getString(cur.getColumnIndexOrThrow("type"))
            val contact = getContactByPhoneNumber(context, address)
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

    private fun getContactByPhoneNumber(c: Context, phoneNumber: String): String {

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