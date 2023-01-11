package com.hujiayucc.qqsender.ui.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.hujiayucc.qqsender.Applications.Companion.context
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.databinding.ActivityLoginBinding
import com.hujiayucc.qqsender.ui.base.CircleTransform
import com.hujiayucc.qqsender.utils.Const
import com.hujiayucc.qqsender.utils.Const.Companion.TAG
import com.hujiayucc.qqsender.utils.Const.Companion.bot
import com.hujiayucc.qqsender.utils.Const.Companion.colors
import com.hujiayucc.qqsender.utils.Const.Companion.getQQFace
import com.hujiayucc.qqsender.utils.Const.Companion.modes
import com.hujiayucc.qqsender.utils.Const.Companion.settings
import com.hujiayucc.qqsender.utils.Toast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.network.LoginFailedException
import net.mamoe.mirai.utils.BotConfiguration
import java.io.File

/** 登录界面 */
class LoginActivity : AppCompatActivity() {
    private var protocols = BotConfiguration.MiraiProtocol.ANDROID_PHONE
    private var mode = 0

    private lateinit var binding: ActivityLoginBinding
    private lateinit var qq_face: ImageView
    private lateinit var qq_edittext: EditText
    private lateinit var pwd_edittext: EditText
    private lateinit var auto_login: CheckBox
    private lateinit var login_protocol: TextView

    private val drawable = GradientDrawable(LEFT_RIGHT, colors)

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        initView()
        setLoginBackground()
    }

    private fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        qq_face = binding.qqface
        qq_edittext = binding.qqId
        pwd_edittext = binding.qqPassword
        auto_login = binding.autoLogin
        login_protocol = binding.protocol

        if (settings!!.getLong("qq", 0).toString().length > 5) {
            qq_edittext.setText("${settings!!.getLong("qq", 0)}")
            Picasso.with(context).load(getQQFace(qq_edittext.text.toString()))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .transform(CircleTransform()).into(qq_face)
        } else {
            Picasso.with(context).load(getQQFace(1661054627))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .transform(CircleTransform()).into(qq_face)
        }
        pwd_edittext.setText(settings!!.getString("password", ""))
        auto_login.isChecked = settings!!.getBoolean("autoLogin", false)
        mode = settings!!.getInt("protocol", 0)
        setModes(mode)

        qq_edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length > 5) {
                    Picasso.with(context).load(getQQFace(qq_edittext.text.toString()))
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .transform(CircleTransform()).into(qq_face)
                } else {
                    Picasso.with(context).load(getQQFace(1661054627))
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .transform(CircleTransform()).into(qq_face)
                }
            }
        })

        binding.btnLogin.setOnClickListener {
            val qq = if (qq_edittext.text.length > 5) qq_edittext.text.toString().toLong() else 0L
            if (qq == 0L) {
                Toast.Long("请输入正确的QQ")
                return@setOnClickListener
            } else if (pwd_edittext.text.length < 8) {
                Toast.Long("密码最少为8位")
                return@setOnClickListener
            }
            login(qq, pwd_edittext.text.toString())
        }

        login_protocol.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("请选择登录协议")
                .setSingleChoiceItems(modes, mode, { dialogInterface, i ->
                    setModes(i)
                    mode = i
                    dialogInterface.dismiss()
                })
                .create().show()
        }
    }

    private fun setModes(i: Int) {
        when (i) {
            0 -> protocols = BotConfiguration.MiraiProtocol.ANDROID_PHONE
            1 -> protocols = BotConfiguration.MiraiProtocol.ANDROID_PAD
            2 -> protocols = BotConfiguration.MiraiProtocol.ANDROID_WATCH
            3 -> protocols = BotConfiguration.MiraiProtocol.IPAD
            4 -> protocols = BotConfiguration.MiraiProtocol.MACOS
        }
        Const.editor!!.putInt("protocol", i)
        Const.editor!!.apply()
    }

    private fun setLoginBackground() {
        drawable.cornerRadius = 200.0F
        drawable.setSize(10, 10)
        drawable.setStroke(0, getColor(R.color.white))
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        binding.btnLogin.background = drawable
    }

    private fun login(qq: Long, password: String) {
        Const.editor!!.putLong("qq", qq)
        Const.editor!!.putString("password", password)
        Const.editor!!.putBoolean("autoLogin", auto_login.isChecked)
        Const.editor!!.apply()

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
                    bot?.close()
                    return@Thread
                }
                Thread.sleep(500)
            }
        }.start()

        bot = BotFactory.newBot(qq, password) {
            val logFile = File("${Const.log_dir}/${qq}")
            val qqFile = File("${Const.bot_dir}/${qq}")
            if (!logFile.exists()) logFile.mkdirs()
            if (!qqFile.exists()) qqFile.mkdirs()
            workingDir = qqFile
            if (workingDir.exists()) workingDir.mkdirs()

            cacheDir = context!!.cacheDir
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
                Log.d(TAG, "失败：${e.message}")
                runOnUiThread({
                    AlertDialog.Builder(this@LoginActivity)
                        .setTitle("登录失败")
                        .setCancelable(false)
                        .setMessage(e.message)
                        .setPositiveButton("关闭", object : OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0?.dismiss()
                            }
                        })
                        .create().show()
                })
                e.printStackTrace()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}