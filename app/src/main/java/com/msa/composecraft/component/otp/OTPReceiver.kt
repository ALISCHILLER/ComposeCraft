package com.msa.composecraft.component.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * BroadcastReceiver برای دریافت پیام‌های SMS.
 * این کلاس می‌تواند در AndroidManifest ثبت شود یا در زمان اجرا.
 * باید Intents را بر اساس SmsRetriever.SMS_RETRIEVED_ACTION فیلتر کند.
 */
class OTPReceiver : BroadcastReceiver() {
    private var otpReceiveListener: OTPReceiveListener? = null

    fun init(otpReceiveListener: OTPReceiveListener?) {
        this.otpReceiveListener = otpReceiveListener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
            val extras: Bundle? = intent.extras
            val status: Status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // دریافت محتوای پیام SMS
                    val msg = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    Log.e("OTPReceiver", "SMS Received in OTPReceiver: $msg")

                    // استخراج کد ۶ رقمی از پیام
                    val smsCode = msg.let { "[0-9]{6}".toRegex().find(it) }
                    Log.e("OTPReceiver", "OTP fetched from SMS in OTPReceiver: $smsCode")

                    smsCode?.value?.let { otpReceiveListener?.onOTPReceived(it) }
                }

                CommonStatusCodes.TIMEOUT -> {
                    otpReceiveListener?.onOTPTimeOut()
                }
            }
        }
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
        fun onOTPTimeOut()
    }
}

fun startSMSRetrieverClient(context: Context) {
    val client: SmsRetrieverClient = SmsRetriever.getClient(context)
    val smsRetrieverTask = client.startSmsRetriever()
    smsRetrieverTask.addOnSuccessListener {
        Log.e("OTPReceiver", "startSMSRetrieverClient addOnSuccessListener")
    }
    smsRetrieverTask.addOnFailureListener { e ->
        Log.e("OTPReceiver", "startSMSRetrieverClient addOnFailureListener" + e.stackTrace)
    }
}