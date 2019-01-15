package com.motor.connect.service

import android.content.Context
import com.aykuttasil.callrecord.CallRecord
import com.aykuttasil.callrecord.receiver.CallRecordReceiver
import java.io.File
import java.util.*

class AppCallReceiver(callRecord: CallRecord) : CallRecordReceiver(callRecord) {

    private var lastCallPhoneNumber: String? = null

    private var lastCallTime: Long? = 0L

    private var lastCallDuration: Int? = 0

    override fun onIncomingCallEnded(context: Context?, number: String?, start: Date?, end: Date?) {
        super.onIncomingCallEnded(context, number, start, end)
        onNewCallEnd(number, start, end)
    }

    override fun onOutgoingCallEnded(context: Context?, number: String?, start: Date?, end: Date?) {
        super.onOutgoingCallEnded(context, number, start, end)
        onNewCallEnd(number, start, end)
    }


    override fun onRecordingFinished(context: Context?, callRecord: CallRecord?, audioFile: File?) {
        super.onRecordingFinished(context, callRecord, audioFile)
        audioFile?.let {
            lastCallPhoneNumber?.let {
                handleNewRecordFinished(context, audioFile, lastCallPhoneNumber!!, lastCallTime!!,
                        lastCallDuration!!)
            }
        }
    }

    private fun onNewCallEnd(number: String?, start: Date?, end: Date?) {
        lastCallPhoneNumber = number
        lastCallTime = start?.time
        lastCallDuration = start?.let { (start.time - end!!.time).toInt() }
    }

    private fun handleNewRecordFinished(context: Context?, file: File, phoneNumber: String, time: Long, duration: Int) =
            null
}
