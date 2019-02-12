package com.motor.connect.base.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.motor.connect.utils.SharePrefUtil


@SuppressLint("Registered")
open class RootActivity : AppCompatActivity() {
    var shef: SharePrefUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shef = SharePrefUtil(this)
    }
}
