package com.motor.connect.base.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.orhanobut.hawk.Hawk

abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Hawk.init(applicationContext).build()
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