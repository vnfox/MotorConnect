package com.motor.connect.utils.extensions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

fun FragmentActivity.backPrevFragment() {
    supportFragmentManager.popBackStackImmediate()
}


fun FragmentActivity.showFragment(containerId: Int,
                                  fragmentClass: Class<Fragment>,
                                  data: Bundle? = null,
                                  backStack: Boolean = false) {
    val fragment = fragmentClass.getConstructor().newInstance()
    fragment.arguments = data
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(containerId, fragment)
    if (backStack) transaction.addToBackStack(null) else transaction.disallowAddToBackStack()
    transaction.commit()
}