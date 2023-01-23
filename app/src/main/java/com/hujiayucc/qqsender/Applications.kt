package com.hujiayucc.qqsender

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.hujiayucc.qqsender.utils.Const.Companion.bot_dir
import com.hujiayucc.qqsender.utils.Const.Companion.editor
import com.hujiayucc.qqsender.utils.Const.Companion.log_dir
import com.hujiayucc.qqsender.utils.Const.Companion.settings
import java.io.File

class Applications : Application() {
    override fun onCreate() {
        super.onCreate()
        init()
        settings = getSharedPreferences("setting", 0)
        editor = settings!!.edit()
    }

    /** 初始化 **/
    private fun init() {
        context = applicationContext
        if (!File(bot_dir).exists()) File(bot_dir).mkdirs()
        if (!File(log_dir).exists()) File(log_dir).mkdirs()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }
}