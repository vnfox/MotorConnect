package com.motor.connect.service

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import com.aykuttasil.callrecord.CallRecord
import java.io.File


class CallRecordManager {

    companion object {

        private const val ROOT_FOLDER_NAME = "CarServiceRecorder"

        fun startService(context: Context) {
            val callRecord = CallRecord.Builder(context)
                    .setRecordFileName(genFileName())
                    .setRecordDirName(ROOT_FOLDER_NAME)
                    .setRecordDirPath(getSaveDirectory())
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    .setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
                    .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                    .setShowSeed(true)
                    .build()
            callRecord.changeReceiver(AppCallReceiver(callRecord))
            callRecord.startCallRecordService()
        }

        private fun genFileName(): String {
            return "rec"
        }

        private fun getSaveDirectory(): String {
            // TODO: handle when external storage not ready
            if (checkExternalStorage()) return ""
            val rootFolder = File(Environment.getExternalStorageDirectory(), ROOT_FOLDER_NAME)
            if (rootFolder.exists()) rootFolder.mkdirs()
            return rootFolder.absolutePath
        }

        private fun checkExternalStorage() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}