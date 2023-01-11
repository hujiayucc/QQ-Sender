package com.hujiayucc.qqsender.ui.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.hujiayucc.qqsender.Applications
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.databinding.ActivitySplashBinding
import com.hujiayucc.qqsender.utils.Const
import com.hujiayucc.qqsender.utils.Const.Companion.TAG
import com.hujiayucc.qqsender.utils.Const.Companion.bot
import com.hujiayucc.qqsender.utils.Const.Companion.settings
import kotlinx.coroutines.launch
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.network.LoginFailedException
import net.mamoe.mirai.utils.BotConfiguration
import java.io.File

/** 开始界面 */
@SuppressLint("CustomSplashScreen", "UseCompatLoadingForDrawables")
class SplashActivity : AppCompatActivity() {
    private var auto_login = false
    private var protocols = BotConfiguration.MiraiProtocol.ANDROID_PHONE
    private var mode = 0

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
                mode = settings!!.getInt("protocol", 0)
                when (mode) {
                    0 -> protocols = BotConfiguration.MiraiProtocol.ANDROID_PHONE
                    1 -> protocols = BotConfiguration.MiraiProtocol.ANDROID_PAD
                    2 -> protocols = BotConfiguration.MiraiProtocol.ANDROID_WATCH
                    3 -> protocols = BotConfiguration.MiraiProtocol.IPAD
                    4 -> protocols = BotConfiguration.MiraiProtocol.MACOS
                }
                login(qq, password!!)
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

    private fun login(qq: Long, password: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setView(R.layout.dialog_login)
            .setCancelable(false)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        Thread {
            val time = System.currentTimeMillis() + 30000
            while (true) {
                if (bot?.isOnline == false) return@Thread
                if (System.currentTimeMillis() > time) {
                    alertDialog.dismiss()
                    runOnUiThread({
                        AlertDialog.Builder(this@SplashActivity)
                            .setTitle("登录失败")
                            .setCancelable(false)
                            .setMessage("登录超时，请重新尝试")
                            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0?.dismiss()
                                    val intent = Intent(applicationContext, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            })
                            .create().show()
                    })
                    bot?.close()
                    return@Thread
                }
                Thread.sleep(300)
            }
        }.start()

        bot = BotFactory.newBot(qq, password) {
            val logFile = File("${Const.log_dir}/${qq}")
            val qqFile = File("${Const.bot_dir}/${qq}")
            if (!logFile.exists()) logFile.mkdirs()
            if (!qqFile.exists()) qqFile.mkdirs()
            workingDir = qqFile
            if (workingDir.exists()) workingDir.mkdirs()

            cacheDir = Applications.context!!.cacheDir
            protocol = protocols

            fileBasedDeviceInfo()

            redirectBotLogToDirectory(logFile)
            redirectNetworkLogToDirectory(logFile)
            redirectBotLogToFile(File("${logFile.path}/${Const.time}-bot.log"))
            redirectNetworkLogToFile(File("${logFile.path}/${Const.time}-net.log"))

            loginSolver = com.hujiayucc.qqsender.utils.LoginSolver(applicationContext)
            Log.d(TAG, "机器人初始化完成")
        }

        bot!!.launch {
            try {
                bot!!.login()
                alertDialog.dismiss()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: LoginFailedException) {
                alertDialog.dismiss()
                runOnUiThread({
                    AlertDialog.Builder(this@SplashActivity)
                        .setTitle("登录失败")
                        .setCancelable(false)
                        .setMessage(e.message)
                        .setPositiveButton("关闭", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0?.dismiss()
                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        })
                        .create().show()
                })
                Log.d(TAG, "失败：${e.message}")
                e.printStackTrace()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) return false
        return super.onKeyDown(keyCode, event)
    }
}