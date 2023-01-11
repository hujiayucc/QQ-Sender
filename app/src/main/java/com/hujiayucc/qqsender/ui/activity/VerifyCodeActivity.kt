package com.hujiayucc.qqsender.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.utils.LoginSolver.Companion.verificationResult
import com.tuo.customview.VerificationCodeView

class VerifyCodeActivity : AppCompatActivity() {
    lateinit var verificationCodeView: VerificationCodeView
    private lateinit var context: Context

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code)
        context = this
        verificationCodeView = findViewById(R.id.verify)
        verificationCodeView.setInputCompleteListener(object : VerificationCodeView.InputCompleteListener {
            override fun inputComplete() {
                if (verificationCodeView.inputContent.length == 6) {
                    val alertDialog = AlertDialog.Builder(context)
                        .setView(R.layout.dialog_login)
                        .setCancelable(false)
                        .create()
                    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    alertDialog.show()
                    verificationResult.complete(verificationCodeView.inputContent)
                    alertDialog.dismiss()
                    finish()
                }
            }

            override fun deleteContent() {}
        })
    }
}