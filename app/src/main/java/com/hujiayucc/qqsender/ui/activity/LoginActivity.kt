package com.hujiayucc.qqsender.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.hujiayucc.qqsender.utils.Bot.login
import com.hujiayucc.qqsender.utils.Const.Companion.colors
import com.hujiayucc.qqsender.utils.Const.Companion.editor
import com.hujiayucc.qqsender.utils.Const.Companion.getQQFace
import com.hujiayucc.qqsender.utils.Const.Companion.modes
import com.hujiayucc.qqsender.utils.Const.Companion.settings
import com.hujiayucc.qqsender.utils.Toast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

/** 登录界面 */
class LoginActivity : AppCompatActivity() {
    private var protocols = 0

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
        protocols = settings!!.getInt("protocol", 0)
        setModes(protocols)

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
            login(
                this@LoginActivity,
                qq, pwd_edittext.text.toString(),
                auto_login.isChecked, protocols
            )
        }

        login_protocol.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("请选择登录协议")
                .setSingleChoiceItems(modes, protocols) { dialogInterface, i ->
                    setModes(i)
                    protocols = i
                    dialogInterface.dismiss()
                }.create().show()
        }
    }

    private fun setModes(i: Int) {
        editor!!.putInt("protocol", i)
        editor!!.apply()
    }

    private fun setLoginBackground() {
        drawable.cornerRadius = 200.0F
        drawable.setSize(10, 10)
        drawable.setStroke(0, getColor(R.color.white))
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        binding.btnLogin.background = drawable
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