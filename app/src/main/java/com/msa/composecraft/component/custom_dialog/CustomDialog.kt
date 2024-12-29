package com.msa.composecraft.component.custom_dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.msa.composecraft.R

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit, // عملیات هنگام بسته شدن دیالوگ
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    elevation: Dp = 24.dp, // سایه دیالوگ
    shape: Shape = RoundedCornerShape(16.dp), // شکل دیالوگ
    properties: DialogProperties = DialogProperties(), // ویژگی‌های دیالوگ
    title: @Composable (() -> Unit)? = null, // عنوان دیالوگ
    content: @Composable ColumnScope.() -> Unit, // محتوای اصلی دیالوگ
    buttons: @Composable RowScope.() -> Unit = {} // دکمه‌های پایین دیالوگ
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Surface(
            modifier = modifier,
            color = backgroundColor,
            contentColor = contentColor,
            shape = shape,
            shadowElevation = elevation
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // عنوان دیالوگ
                if (title != null) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        title()
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // محتوای اصلی دیالوگ
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    content = content
                )

                // دکمه‌های پایین دیالوگ
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End,
                    content = buttons
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCustomDialog() {
    MaterialTheme {

        Column(modifier = Modifier.fillMaxSize()) {
            CustomDialog(
                onDismissRequest = { /* Handle dismiss */ },
                content = {
                    Text(
                        text = "این یک دیالوگ ساده است.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomDialog2() {
    MaterialTheme {

        Column(modifier = Modifier.fillMaxSize()) {

            CustomDialog(
                onDismissRequest = { /* Handle dismiss */ },
                title = {
                    Text(
                        text = "عنوان دیالوگ",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                content = {
                    Text(
                        text = "این یک دیالوگ سفارشی است که می‌تواند هر نوع محتوایی را نمایش دهد.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                buttons = {
                    Button(
                        onClick = { /* Handle cancel */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("لغو")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { /* Handle confirm */ }
                    ) {
                        Text("تأیید")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomDialog3() {
    MaterialTheme {

        Column(modifier = Modifier.fillMaxSize()) {
            CustomDialog(
                onDismissRequest = { /* Handle dismiss */ },
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.not_load_image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "این یک دیالوگ با تصویر است.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}
