package com.msa.composecraft.component.customCardC

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomCard2(
    title: String, // عنوان کارت
    description: String, // توضیحات کارت
    imageRes: Int, // منبع تصویر کارت
    onClick: () -> Unit, // عملیات کلیک روی کارت
    onButtonClick: () -> Unit, // عملیات کلیک روی دکمه
    modifier: Modifier = Modifier, // مودیفایر برای سفارشی‌سازی اندازه و موقعیت
    backgroundColor: Color = Color.White, // رنگ پس‌زمینه کارت
    contentColor: Color = Color.Black, // رنگ متن و محتوای کارت
    buttonText: String = "Action" // متن دکمه
) {
    // Surface برای ایجاد یک کارت با سایه و گوشه‌های گرد
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // قابلیت کلیک روی کارت
        shape = RoundedCornerShape(8.dp), // گوشه‌های گرد
        color = backgroundColor, // رنگ پس‌زمینه
        shadowElevation = 4.dp // سایه کارت
    ) {
        Column(
            modifier = Modifier.padding(16.dp) // فاصله داخلی
        ) {
            // تصویر کارت
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.LightGray), // پس‌زمینه تصویر
                contentScale = ContentScale.Crop // نحوه نمایش تصویر
            )
            Spacer(modifier = Modifier.height(16.dp)) // فاصله بین تصویر و عنوان

            // عنوان کارت
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(8.dp)) // فاصله بین عنوان و توضیحات

            // توضیحات کارت
            Text(
                text = description,
                fontSize = 14.sp,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(16.dp)) // فاصله بین توضیحات و دکمه

            // دکمه کارت
            Button(
                onClick = onButtonClick,
                modifier = Modifier.align(Alignment.End), // تراز دکمه به سمت راست
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // رنگ دکمه
                    contentColor = MaterialTheme.colorScheme.onPrimary // رنگ متن دکمه
                )
            ) {
                Text(text = buttonText) // متن دکمه
            }
        }
    }
}

// پیش‌نمایش کامپوننت CustomCard
@Preview(showBackground = true)
@Composable
fun PreviewCustomCard2() {
    CustomCard2(
        title = "عنوان کارت",
        description = "این یک توضیح کوتاه برای کارت است که می‌تواند اطلاعات بیشتری را ارائه دهد.",
        imageRes = android.R.drawable.ic_dialog_info, // تصویر پیش‌فرض
        onClick = { /* عملیات کلیک روی کارت */ },
        onButtonClick = { /* عملیات کلیک روی دکمه */ },
        modifier = Modifier.padding(16.dp)
    )
}