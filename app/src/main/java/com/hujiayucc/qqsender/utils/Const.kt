package com.hujiayucc.qqsender.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.*
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.hujiayucc.qqsender.Applications.Companion.context
import com.hujiayucc.qqsender.R
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.utils.BotConfiguration
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
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

        /** 启动时间 yyyy-MM-dd HH:mm:ss */
        val time2: String = formats.format(Date())

        /** 机器人 */
        var bot: Bot? = null

        /** TAG */
        var TAG: String = context!!.packageName
        var settings: SharedPreferences? = null
        var editor: SharedPreferences.Editor? = null
        var qqlist: ArrayList<Friend> = ArrayList()
        var grouplist: ArrayList<Group> = ArrayList()
        var page = 0

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

        /** 获取网络图片 */
        fun getURLImage(url: String): Bitmap? {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            try {
                val imgUrl = URL(url)
                val conn: HttpURLConnection = imgUrl.openConnection() as HttpURLConnection
                conn.connectTimeout = 6000 //设置超时
                conn.doInput = true
                conn.useCaches = false //不缓存
                conn.connect()
                val inputStream: InputStream = conn.inputStream //获得图片的数据流
                return makeRoundCorner(BitmapFactory.decodeStream(inputStream)) //读取图像数据
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        /** 获取QQ头像地址 */
        fun getQQFace(qq: String): String {
            return "http://q2.qlogo.cn/headimg_dl?dst_uin=${qq}&spec=640"
        }

        /** 获取QQ头像地址 */
        fun getQQFace(qq: Long): String {
            return "http://q2.qlogo.cn/headimg_dl?dst_uin=${qq}&spec=640"
        }

        /** 将Bitmap转为圆形 */
        fun makeRoundCorner(bitmap: Bitmap): Bitmap? {
            val width = bitmap.width
            val height = bitmap.height
            var left = 0
            var top = 0
            var right = width
            var bottom = height
            var roundPx = (height / 2).toFloat()
            if (width > height) {
                left = (width - height) / 2
                top = 0
                right = left + height
                bottom = height
            } else if (height > width) {
                left = 0
                top = (height - width) / 2
                right = width
                bottom = top + width
                roundPx = (width / 2).toFloat()
            }
            val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(left, top, right, bottom)
            val rectF = RectF(rect)
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }
    }
}
