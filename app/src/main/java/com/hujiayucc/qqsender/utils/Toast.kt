package com.hujiayucc.qqsender.utils

import android.annotation.SuppressLint
import android.widget.Toast
import com.hujiayucc.qqsender.Applications.Companion.context

object Toast {
    private const val LONG = 1
    private const val SHORT = 0

    @SuppressLint("WrongConstant")
    fun Long(message: String?) {
        Toast.makeText(context, message, LONG).show()
    }

    @SuppressLint("WrongConstant")
    fun Short(message: String?) {
        Toast.makeText(context, message, SHORT).show()
    }
}