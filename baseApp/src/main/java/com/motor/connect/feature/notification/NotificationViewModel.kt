package com.motor.connect.feature.notification

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.SmsModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk

class NotificationViewModel(mView: NotificationView?, mModel: BaseModel)
    : BaseViewModel<NotificationView, BaseModel>(mView, mModel) {

    private var dataArea: MutableList<AreaModel> = mutableListOf()
    private var smsReceivers: MutableList<SmsModel> = mutableListOf()

    override fun initViewModel() {
        mView?.showLoadingView()
    }

    fun initData(context: NotificationActivity) {
        smsReceivers = getSMSReceives(context)

        if (smsReceivers.isEmpty()) {
            mView?.hideLoadingView()
            mView?.showEmptyView()
        } else {
            mView?.updateUI(smsReceivers)
        }
    }

    private fun getSMSReceives(context: NotificationActivity): MutableList<SmsModel> {
        val sms: MutableList<SmsModel> = mutableListOf()
        val uriSMSURI = Uri.parse("content://sms/inbox")
        val cur = context.contentResolver.query(uriSMSURI, null, null, null, null)

        while (cur != null && cur.moveToNext()) {
            val address = cur.getString(cur.getColumnIndex("address"))
            val body = cur.getString(cur.getColumnIndexOrThrow("body"))
            val type = cur.getString(cur.getColumnIndexOrThrow("type"))
            val contact = getContactByPhoneNumber(context, address)
            val timestamp = cur.getString(cur.getColumnIndexOrThrow("date"))

            val smsModel = SmsModel()
            smsModel.contactName = contact
            smsModel.phoneNumber = address
            smsModel.smsType = type
            smsModel.messageContent = body
            smsModel.date = timestamp

            if (address.length < 13)
                sms.add(smsModel)

            //Get 30 sms lasted
//            if (sms.size == 50) {
//                return verifyContactArea(sms)
//            }
        }
        cur?.close()
        return verifyContactArea(sms)
    }

    private fun verifyContactArea(smsList: MutableList<SmsModel>): MutableList<SmsModel> {
        val result: MutableList<SmsModel> = mutableListOf()
        dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)

        for (i in 0 until dataArea.size) {
            for (j in 0 until smsList.size) {
                if (smsList[j].phoneNumber.contains(dataArea[i].areaPhone.substring(1, dataArea[i].areaPhone.length))) {
                    result.add(smsList[j])
                }
            }
        }
        return result
    }

    private fun getContactByPhoneNumber(c: Context, phoneNumber: String): String {
        try {
            val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
            val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
            val cursor = c.contentResolver.query(uri, projection, null, null, null)
            return if (cursor == null) {
                phoneNumber
            } else {
                var name = phoneNumber
                cursor.use { cursor ->
                    if (cursor.moveToFirst()) {
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
                    }
                }

                name
            }
        } finally {
            return phoneNumber
        }
    }
}