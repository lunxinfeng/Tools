package com.lxf.tools.util

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(applicationContext, msg, duration).show()

fun isEmpty(s: String?) = TextUtils.isEmpty(s)