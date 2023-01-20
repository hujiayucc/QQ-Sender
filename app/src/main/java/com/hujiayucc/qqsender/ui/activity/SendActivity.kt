package com.hujiayucc.qqsender.ui.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.slider.Slider
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.databinding.ActivitySendBinding
import com.hujiayucc.qqsender.utils.Const.Companion.bot
import com.hujiayucc.qqsender.utils.Const.Companion.grouplist
import com.hujiayucc.qqsender.utils.Const.Companion.numberlist
import com.hujiayucc.qqsender.utils.Const.Companion.qqlist
import com.hujiayucc.qqsender.utils.Const.Companion.send_type_friend
import com.hujiayucc.qqsender.utils.Const.Companion.send_type_group
import com.hujiayucc.qqsender.utils.Const.Companion.send_type_group_friend
import com.hujiayucc.qqsender.utils.Const.Companion.send_type_null
import com.hujiayucc.qqsender.utils.Toast
import kotlinx.coroutines.launch
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText

@SuppressLint("SetTextI18n")
class SendActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendBinding
    private lateinit var ms: TextView
    private lateinit var sider: Slider
    private lateinit var message: EditText
    private var yc = 3000
    private var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        type = intent.extras?.getInt("type") ?: 0
        binding = ActivitySendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        ms = binding.ms
        sider = binding.sider
        message = binding.message

        sider.addOnChangeListener(object : Slider.OnChangeListener {
            override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                ms.text = "设置延迟：${value.toInt()}ms"
                yc = value.toInt()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //导入菜单布局
        menuInflater.inflate(R.menu.send, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.send -> {
                if (message.text.isEmpty()) {
                    Toast.Long("请输入发送内容")
                    return false
                }
                val alertDialog = AlertDialog.Builder(this)
                    .setView(R.layout.dialog_sending)
                    .setCancelable(false)
                    .create()
                Thread {
                    runOnUiThread {
                        alertDialog.show()
                    }
                    var int: Int
                    var success = 0
                    var fail = 0
                    when (type) {
                        send_type_null -> finish()

                        send_type_friend -> {
                            int = 0
                            for (friend in qqlist.toList()) {
                                int++
                                try {
                                    val message: Message = PlainText(this@SendActivity.message.text)
                                    bot!!.launch {
                                        friend.sendMessage(message)
                                    }
                                    success++
                                } catch (e: Exception) {
                                    fail++
                                    e.printStackTrace()
                                }
                                if (qqlist.size > 1) Thread.sleep(yc.toLong())
                                if (int >= qqlist.size) {
                                    runOnUiThread {
                                        alertDialog.dismiss()
                                        AlertDialog.Builder(this)
                                            .setTitle("发送完成")
                                            .setMessage("共发送：${qqlist.size}\n成功：$success\n失败：$fail")
                                            .setCancelable(false)
                                            .setPositiveButton("关闭", object : DialogInterface.OnClickListener {
                                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                                    p0?.dismiss()
                                                }
                                            })
                                            .create().show()
                                    }
                                }
                            }
                        }

                        send_type_group -> {
                            int = 0
                            for (group in grouplist.toList()) {
                                int++
                                try {
                                    val message: Message = PlainText(this@SendActivity.message.text)
                                    bot!!.launch {
                                        group.sendMessage(message)
                                    }
                                    success++
                                } catch (e: Exception) {
                                    fail++
                                    e.printStackTrace()
                                }
                                if (grouplist.size > 1) Thread.sleep(yc.toLong())
                                if (int >= qqlist.size) {
                                    runOnUiThread {
                                        alertDialog.dismiss()
                                        AlertDialog.Builder(this)
                                            .setTitle("发送完成")
                                            .setMessage("共发送：${qqlist.size}\n成功：$success\n失败：$fail")
                                            .setCancelable(false)
                                            .setPositiveButton("关闭", object : DialogInterface.OnClickListener {
                                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                                    p0?.dismiss()
                                                }
                                            })
                                            .create().show()
                                    }
                                }
                            }
                        }

                        send_type_group_friend -> {
                            int = 0
                            for (number in numberlist.toList()) {
                                int++
                                try {
                                    val message: Message = PlainText(this@SendActivity.message.text)
                                    bot!!.launch {
                                        number.sendMessage(message)
                                    }
                                    success++
                                } catch (e: Exception) {
                                    fail++
                                    e.printStackTrace()
                                }
                                if (numberlist.size > 1) Thread.sleep(yc.toLong())
                                if (int >= qqlist.size) {
                                    runOnUiThread {
                                        alertDialog.dismiss()
                                        AlertDialog.Builder(this)
                                            .setTitle("发送完成")
                                            .setMessage("共发送：${numberlist.size}\n成功：$success\n失败：$fail")
                                            .setCancelable(false)
                                            .setPositiveButton("关闭", object : DialogInterface.OnClickListener {
                                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                                    p0?.dismiss()
                                                }
                                            })
                                            .create().show()
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }.start()
            }

            else -> {}
        }
        return true
    }
}
