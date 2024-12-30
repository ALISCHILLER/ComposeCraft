package com.msa.composecraft.component.otp

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.msa.composecraft.R

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OtpScreen() {
    val context = LocalContext.current
    var otpValue by remember { mutableStateOf("") }
    var isOtpFilled by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // دریافت خودکار کد OTP از پیام‌های SMS
    OtpReceiverEffect(
        context = context,
        onOtpReceived = { otp ->
            otpValue = otp
            if (otpValue.length == 6) {
                keyboardController?.hide()
                isOtpFilled = true
            }
        }
    )

    // درخواست فوکوس و نمایش کیبورد به صورت خودکار
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // تنظیم رنگ نوار وضعیت
    (LocalView.current.context as Activity).window.statusBarColor = Color.White.toArgb()

    // UI صفحه‌ی ورود OTP
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Enter One Time Password") },
                navigationIcon = {

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.DarkGray
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { /* Handle OTP verification */ },
                enabled = isOtpFilled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Continue")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please verify your phone number with the OTP we sent to (***)***-2193.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OtpInputField(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .focusRequester(focusRequester),
                otpText = otpValue,
                onOtpModified = { value, otpFilled ->
                    otpValue = value
                    isOtpFilled = otpFilled
                    if (otpFilled) {
                        keyboardController?.hide()
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OtpReceiverEffect(
    context: Context,
    onOtpReceived: (String) -> Unit
) {
    val otpReceiver = remember { OTPReceiver() }

    LaunchedEffect(Unit) {
        startSMSRetrieverClient(context)
        otpReceiver.init(object : OTPReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                otp?.let { onOtpReceived(it) }
                try {
                    context.unregisterReceiver(otpReceiver)
                } catch (e: IllegalArgumentException) {
                    Log.e("OTPReceiverEffect", "Error in unregistering receiver: ${e.message}")
                }
            }

            override fun onOTPTimeOut() {
                Log.e("OTPReceiverEffect", "Timeout")
            }
        })
        try {
            context.registerReceiver(
                otpReceiver,
                IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
                Context.RECEIVER_EXPORTED
            )
        } catch (e: IllegalArgumentException) {
            Log.e("OTPReceiverEffect", "Error in registering receiver: ${e.message}")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                context.unregisterReceiver(otpReceiver)
            } catch (e: IllegalArgumentException) {
                Log.e("OTPReceiverEffect", "Error in unregistering receiver: ${e.message}")
            }
        }
    }
}