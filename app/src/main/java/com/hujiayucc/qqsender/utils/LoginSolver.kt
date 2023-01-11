package com.hujiayucc.qqsender.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.hujiayucc.qqsender.ui.activity.VerifyActivity
import com.hujiayucc.qqsender.ui.activity.VerifyCodeActivity
import com.hujiayucc.qqsender.utils.Const.Companion.TAG
import kotlinx.coroutines.CompletableDeferred
import net.mamoe.mirai.Bot
import net.mamoe.mirai.network.RetryLaterException
import net.mamoe.mirai.utils.DeviceVerificationRequests
import net.mamoe.mirai.utils.DeviceVerificationResult
import net.mamoe.mirai.utils.LoginSolver

class LoginSolver(private val context: Context) : LoginSolver() {
    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        return null
    }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String {
        verificationResult = CompletableDeferred()
        verify(url)
        return verificationResult.await()
    }

    override suspend fun onSolveDeviceVerification(
        bot: Bot, requests: DeviceVerificationRequests
    ): DeviceVerificationResult {
        sendVerify()
        verificationResult = CompletableDeferred()
        try {
            requests.sms?.requestSms()
            return requests.sms!!.solved(verificationResult.await())
        } catch (e: RetryLaterException) {
            Log.d(TAG, "验证码：${requests.fallback!!.url}")
            Log.d(TAG, "验证码2：${requests.preferSms}")
            e.printStackTrace()
            onSolveSliderCaptcha(bot, requests.fallback!!.url)
            return requests.fallback!!.solved()
        }
    }

    private fun verify(url: String) {
        val intent = Intent(context, VerifyActivity::class.java)
        intent.data = Uri.parse(url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun sendVerify() {
        val intent = Intent(context, VerifyCodeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override val isSliderCaptchaSupported: Boolean get() = true

    companion object {
        lateinit var verificationResult: CompletableDeferred<String>
    }
}