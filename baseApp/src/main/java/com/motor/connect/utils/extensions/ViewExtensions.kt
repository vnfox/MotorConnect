package com.motor.connect.utils.extensions

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.ColorInt
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun Context.inflateLayout(layoutResId: Int, parent: ViewGroup, attachToRoot: Boolean = false): View = LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)

fun <T : ViewDataBinding> Context.inflateBindingView(layoutResId: Int, parent: ViewGroup?, attachToRoot: Boolean = false): T = DataBindingUtil.inflate<T>(LayoutInflater.from(this), layoutResId, parent, attachToRoot)

fun ImageView.loadImage(url: String) = Glide.with(context).load(url).into(this)

fun View.showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG, f: (Snackbar.() -> Unit) = {}) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun View.showSnackbar(@StringRes message: Int, length: Int = Snackbar.LENGTH_LONG, f: (Snackbar.() -> Unit) = {}) {
    showSnackbar(resources.getString(message), length, f)
}

fun Snackbar.action(action: String, @ColorInt color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}
