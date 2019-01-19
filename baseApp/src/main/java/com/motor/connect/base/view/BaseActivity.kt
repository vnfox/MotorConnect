package com.motor.connect.base.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.motor.connect.utils.SharePrefUtil
import com.orhanobut.hawk.Hawk

abstract class BaseActivity : AppCompatActivity() {

    public var shef: SharePrefUtil? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Hawk.init(this).build()
        this.shef = SharePrefUtil(this)
    }

    fun showUnderConstruction(methodName: String) {
        Toast.makeText(this, "=== $methodName ====", Toast.LENGTH_SHORT).show()
    }

    fun actionLeft() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}