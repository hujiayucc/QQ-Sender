package com.hujiayucc.qqsender.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.*
import com.hujiayucc.qqsender.Applications.Companion.context
import com.hujiayucc.qqsender.R
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.utils.BotConfiguration
import java.text.SimpleDateFormat
import java.util.*


/** 常量列表 **/
class Const {
    @SuppressLint("StaticFieldLeak")
    companion object {
        /** 机器人数据目录 */
        var bot_dir: String = context!!.filesDir.path + "/robot/"

        /** 日志目录 */
        var log_dir: String = context!!.filesDir.path + "/logs/"

        @SuppressLint("SimpleDateFormat")
        private val format = SimpleDateFormat("yyyy-MM-dd")

        /** 启动时间 yyyy-MM-dd */
        val time: String = format.format(Date())

        @SuppressLint("SimpleDateFormat")
        private val formats = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        /** 机器人 */
        var bot: Bot? = null

        /** TAG */
        var TAG: String = context!!.packageName
        var settings: SharedPreferences? = null
        var editor: SharedPreferences.Editor? = null
        var qqlist: ArrayList<Friend> = ArrayList()
        var grouplist: ArrayList<Group> = ArrayList()
        var numberlist: ArrayList<NormalMember> = ArrayList()
        val send_type_null = 0
        val send_type_friend = 1
        val send_type_group = 2
        val send_type_group_friend = 3

        /** 登录按钮背景色 */
        val colors = intArrayOf(
            context!!.getColor(R.color.login_background_color1),
            context!!.getColor(R.color.login_background_color2),
            context!!.getColor(R.color.login_background_color3),
            context!!.getColor(R.color.login_background_color4),
            context!!.getColor(R.color.login_background_color5)
        )

        /** 登录协议 */
        val modes = arrayOf(
            BotConfiguration.MiraiProtocol.ANDROID_PHONE.name,
            BotConfiguration.MiraiProtocol.ANDROID_PAD.name,
            BotConfiguration.MiraiProtocol.ANDROID_WATCH.name,
            BotConfiguration.MiraiProtocol.IPAD.name,
            BotConfiguration.MiraiProtocol.MACOS.name
        )

        /** 获取QQ头像地址 */
        fun getQQFace(qq: String): String {
            return "http://q2.qlogo.cn/headimg_dl?dst_uin=${qq}&spec=640"
        }

        /** 获取QQ头像地址 */
        fun getQQFace(qq: Long): String {
            return "http://q2.qlogo.cn/headimg_dl?dst_uin=${qq}&spec=640"
        }
    }
}
