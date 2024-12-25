package com.msa.composecraft.component.editText

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun RoundedIconTextField(
    value: String, // مقدار فعلی فیلد متنی
    onValueChange: (String) -> Unit, // تابعی که هنگام تغییر مقدار فیلد فراخوانی می‌شود
    label: String, // برچسب فیلد
    icon: ImageVector, // آیکن مورد نظر برای فیلد (برای فیلدهایی که پسورد نیستند)
    isPassword: Boolean = false, // مشخص می‌کند که این فیلد برای پسورد است یا نه
    modifier: Modifier = Modifier, // امکان تنظیم ویژگی‌های ظاهری فیلد
    typeEnabled: Boolean = false, // اگر true باشد، فیلد تنهاخواندنی می‌شود
    borderColor: Color = Color.Gray, // رنگ حاشیه فیلد
    focusedBorderColor: Color = Color.Blue, // رنگ حاشیه در زمان فوکوس
    shape: Shape = RoundedCornerShape(26.dp), // شکل فیلد (حالت گوشه گرد)
    backgroundColor: Color = Color.White // رنگ پس‌زمینه فیلد
) {
    // وضعیت نمایش یا مخفی کردن پسورد
    var passwordVisibility by remember { mutableStateOf(!isPassword) }

    // Column برای سازماندهی بهتر فیلد متنی و آیکن
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value, // مقدار ورودی
            onValueChange = onValueChange, // تغییر مقدار ورودی
            label = { Text(text = label) }, // برچسب فیلد
            singleLine = true, // فقط یک خط ورودی
            trailingIcon = {
                // اگر فیلد مربوط به پسورد باشد، یک دکمه برای تغییر نمایش پسورد اضافه می‌شود
                if (isPassword) {
                    // دکمه برای نمایش/مخفی کردن پسورد
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                } else {
                    // در غیر این صورت، آیکن عمومی نمایش داده می‌شود
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                }
            },
            // اگر پسورد است، از PasswordVisualTransformation برای مخفی کردن متن استفاده می‌شود
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            // تنظیمات صفحه‌کلید برای حالت "تمام شدن"
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // صفحه‌کلید به‌صورت "تمام شدن" تنظیم می‌شود
            ),
            // تنظیمات مربوط به انجام عمل در هنگام پایان و انتقال به فیلد بعدی
            keyboardActions = KeyboardActions(onDone = { /* می‌توانید عملیات خاصی انجام دهید */ }),
            modifier = Modifier.fillMaxWidth(), // فیلد به طور کامل عرض را اشغال می‌کند
            shape = shape, // تنظیم شکل گوشه‌ها
            readOnly = typeEnabled, // اگر `typeEnabled` برابر true باشد، فیلد فقط خواندنی است
            colors = OutlinedTextFieldDefaults.colors(
                // رنگ حاشیه هنگام فوکوس و زمانی که فیلد فعال نیست
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = borderColor,
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRoundedIconTextField() {
    var phoneNumber by remember { mutableStateOf("1234567890") }
    var password by remember { mutableStateOf("password123") }
    var username by remember { mutableStateOf("username") }

    Column(modifier = Modifier.padding(16.dp)) {
        // فیلد شماره تلفن
        RoundedIconTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = "Phone Number",
            icon = Icons.Default.Phone,
            isPassword = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        // فیلد پسورد
        RoundedIconTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            icon = Icons.Default.Lock,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        // فیلد نام کاربری
        RoundedIconTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username",
            icon = Icons.Default.AccountCircle,
            isPassword = false
        )
    }


}
