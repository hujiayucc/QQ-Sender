package com.hujiayucc.qqsender.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.hujiayucc.qqsender.databinding.ActivityMainBinding
import com.hujiayucc.qqsender.utils.Const.Companion.bot

open class BaseActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val name = bot!!.getFriend(bot!!.id)!!.nick
        if (name.length > 1)
        //就不让你个傻鸟用，气死你
            if (name.contains("莫白") || bot!!.id == 3096135245L)
                throw RuntimeException("禁止某狗白使用该软件")
    }
}