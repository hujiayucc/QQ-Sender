package com.hujiayucc.qqsender.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.databinding.ActivitySplashBinding
import com.hujiayucc.qqsender.utils.Bot.login
import com.hujiayucc.qqsender.utils.Const.Companion.settings

/** 开始界面 */
@SuppressLint("CustomSplashScreen", "UseCompatLoadingForDrawables")
class SplashActivity : AppCompatActivity() {
    private var auto_login = false
    private var protocols = 0
    private lateinit var binding: ActivitySplashBinding
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        initView()
        auto_login = settings!!.getBoolean("autoLogin", false)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (auto_login) {
                val qq = settings!!.getLong("qq", 0)
                val password = settings!!.getString("password", "")
                protocols = settings!!.getInt("protocol", 0)
                login(this@SplashActivity, qq, password!!, protocols)
            } else {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)
    }

    private fun initView() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = binding.splashLogo
        imageView.background = getDrawable(R.mipmap.splash)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) return false
        return super.onKeyDown(keyCode, event)
    }
}