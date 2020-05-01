package com.xfhy.weather.ui.common

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * @author : xfhy
 * Create time : 2020-04-28 22:17
 * Description : 常用方法
 */

fun Fragment.toast(text: String) {
    context?.let {
        Toast.makeText(it, text, Toast.LENGTH_SHORT).show()
    }
}

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}