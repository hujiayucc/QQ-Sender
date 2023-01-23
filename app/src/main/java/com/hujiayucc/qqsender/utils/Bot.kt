package com.hujiayucc.qqsender.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.hujiayucc.qqsender.Applications.Companion.context
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.ui.activity.LoginActivity
import com.hujiayucc.qqsender.ui.activity.MainActivity
import com.hujiayucc.qqsender.utils.Const.Companion.bot
import com.hujiayucc.qqsender.utils.Const.Companion.editor
import kotlinx.coroutines.launch
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.network.LoginFailedException
import net.mamoe.mirai.utils.BotConfiguration
import java.io.File

object Bot {

    /**
     * 登录QQ
     * @param activity 窗体
     * @param qq QQ号
     * @param password 密码
     * @param auto_login 自动登录
     * @param protocols 登录协议
     */
    fun login(
        activity: Activity,
        qq: Long,
        password: String,
        auto_login: Boolean,
        protocols: Int,
    ) {
        editor!!.putLong("qq", qq)
        editor!!.putString("password", password)
        editor!!.putBoolean("autoLogin", auto_login)
        editor!!.apply()

        val alertDialog = AlertDialog.Builder(activity)
            .setView(R.layout.dialog_login)
            .setCancelable(false)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        val thread = Thread {
            val time = System.currentTimeMillis() + 30000
            while (true) {
                if (bot?.isOnline == false) return@Thread
                if (System.currentTimeMillis() > time) {
                    alertDialog.dismiss()
                    bot?.close()
                    activity.runOnUiThread {
                        AlertDialog.Builder(activity)
                            .setMessage("登录超时")
                            .setPositiveButton(
                                "关闭"
                            ) { p0, _ -> p0?.dismiss() }.create().show()
                    }
                    return@Thread
                }
                Thread.sleep(500)
            }
        }

        setBot(qq, password, protocols)

        bot!!.launch {
            try {
                thread.start()
                bot!!.login()
                alertDialog.dismiss()
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
                activity.finish()
            } catch (e: LoginFailedException) {
                alertDialog.dismiss()
                activity.runOnUiThread {
                    AlertDialog.Builder(activity)
                        .setTitle("登录失败")
                        .setCancelable(false)
                        .setMessage(e.message)
                        .setPositiveButton(
                            "关闭"
                        ) { p0, _ -> p0?.dismiss() }
                        .create().show()
                }
                e.printStackTrace()
            }
        }
    }

    /**
     * 登录QQ
     * @param activity 活动窗体
     * @param qq QQ号
     * @param password 密码
     * @param protocols 登录协议
     */
    fun login(
        activity: Activity,
        qq: Long,
        password: String,
        protocols: Int,
    ) {
        var status = true
        val alertDialog = AlertDialog.Builder(activity)
            .setView(R.layout.dialog_login)
            .setCancelable(false)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        val thread = Thread {
            val time = System.currentTimeMillis() + 30000
            while (true) {
                if (bot?.isOnline == true) return@Thread
                if (!status) return@Thread
                if (System.currentTimeMillis() > time) {
                    alertDialog.dismiss()
                    bot?.close()
                    activity.runOnUiThread {
                        AlertDialog.Builder(activity)
                            .setMessage("登录超时")
                            .setPositiveButton("关闭") { p0, _ ->
                                p0?.dismiss()
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context!!.startActivity(intent)
                            }.create().show()
                    }
                    return@Thread
                }
                Thread.sleep(500)
            }
        }

        setBot(qq, password, protocols)

        bot!!.launch {
            try {
                thread.start()
                bot!!.login()
                alertDialog.dismiss()
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
                activity.finish()
            } catch (e: LoginFailedException) {
                alertDialog.dismiss()
                status = false
                activity.runOnUiThread {
                    AlertDialog.Builder(activity)
                        .setMessage("登录失败")
                        .setPositiveButton("关闭") { z, _ ->
                            z?.dismiss()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            context!!.startActivity(intent)
                        }.create().show()
                }
                e.printStackTrace()
            }
        }
    }

    private fun setBot(qq: Long, password: String, protocols: Int) {
        bot = BotFactory.newBot(qq, password) {
            val logFile = File("${Const.log_dir}/${qq}")
            val qqFile = File("${Const.bot_dir}/${qq}")
            val cacheFile = File("${qqFile.path}/cache")
            if (!logFile.exists()) logFile.mkdirs()
            if (!qqFile.exists()) qqFile.mkdirs()
            workingDir = qqFile
            cacheDir = cacheFile
            if (!workingDir.exists()) workingDir.mkdirs()
            if (!cacheDir.exists()) cacheDir.mkdirs()
            protocol = getModes(protocols)

            fileBasedDeviceInfo()

            redirectBotLogToDirectory(logFile)
            redirectNetworkLogToDirectory(logFile)
            redirectBotLogToFile(File("${logFile.path}/${Const.time}-bot.log"))
            redirectNetworkLogToFile(File("${logFile.path}/${Const.time}-net.log"))

            loginSolver = LoginSolver(context!!)
            Log.d(Const.TAG, "机器人初始化完成")
        }
    }

    private fun getModes(i: Int): BotConfiguration.MiraiProtocol {
        return when (i) {
            0 -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
            1 -> BotConfiguration.MiraiProtocol.ANDROID_PAD
            2 -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
            3 -> BotConfiguration.MiraiProtocol.IPAD
            4 -> BotConfiguration.MiraiProtocol.MACOS
            else -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
        }
    }
}