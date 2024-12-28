
@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package com.msa.composecraft.component.searchbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.msa.composecraft.MainActivity
import com.msa.composecraft.R
import com.msa.composecraft.ui.theme.*


/**
 * کامپوننت پیشرفته DockedSearch برای نمایش نوار جستجو با قابلیت‌های سفارشی.
 *
 * امکانات:
 * 1. **پشتیبانی از ورودی گفتار**: از طریق کلاس MainActivity، کاربر می‌تواند متن جستجو را از طریق میکروفون وارد کند.
 * 2. **حالت پویا**: مدیریت وضعیت‌های جستجو (فعال/غیرفعال) و متن ورودی.
 * 3. **طراحی مدرن**: استفاده از رنگ‌ها و گوشه‌های گرد.
 * 4. **پشتیبانی از حالت راست به چپ (RTL)**: نمایش مناسب در زبان‌های راست‌چین.
 *
 * @param onQueryChange فراخوانی زمانی که متن جستجو تغییر کند.
 */
@Composable
fun DockedSearch(
    onQueryChange: (String) -> Unit,
) {
    // دسترسی به context برای استفاده از MainActivity و مدیریت ورودی گفتار
    val context = LocalContext.current
    val speechContext = context as MainActivity

    // مدیریت وضعیت‌های محلی
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    // به‌روزرسانی متن جستجو در صورت دریافت ورودی گفتار
    if (speechContext.speechInput.value.isNotEmpty()) {
        text = speechContext.speechInput.value
        active = true
        onQueryChange(text)
        speechContext.speechInput.value = "" // پاک کردن ورودی گفتار
    }

    DockedSearchBar(
        colors = SearchBarDefaults.colors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 8.dp)
            .fillMaxWidth()
            .border(width = 1.dp, color = barcolor, shape = RoundedCornerShape(10.dp))
            .semantics { traversalIndex = -1f }, // بهینه‌سازی دسترسی‌پذیری
        shape = RoundedCornerShape(10.dp),
        query = text,
        onQueryChange = {
            text = it
            onQueryChange(it)
        },
        onSearch = { /* امکان تعریف عملیات جستجو */ },
        active = active,
        onActiveChange = { active = it },
        placeholder = {
            Text(
                text = stringResource(id = R.string.title_search),
                color = barcolorlow
            )
        },
        leadingIcon = { SearchIcon() },
        trailingIcon = { TrailingIcons(text, active, onQueryChange) }
    ) {
        // محتوای اضافی (در صورت نیاز می‌توان گسترش داد)
    }
}

/**
 * آیکون جستجو برای نمایش در سمت چپ نوار جستجو.
 */
@Composable
private fun SearchIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_search),
        contentDescription = "Search",
        tint = barcolorlow
    )
}

/**
 * آیکون‌های سمت راست نوار جستجو شامل دکمه میکروفون و بستن.
 *
 * @param text متن فعلی جستجو
 * @param active وضعیت فعال بودن نوار جستجو
 * @param onQueryChange فراخوانی برای به‌روزرسانی متن
 */
@Composable
private fun TrailingIcons(
    text: String,
    active: Boolean,
    onQueryChange: (String) -> Unit
) {
    val context = LocalContext.current as MainActivity

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalDivider(
            modifier = Modifier.padding(vertical = 7.dp),
            color = barcolor, thickness = 2.dp
        )
        IconButton(onClick = { context.askSpeechInput(context) }) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_microphone),
                contentDescription = "Mic",
                tint = barcolorlow
            )
        }
        if (active) {
            IconButton(onClick = {
                if (text.isNotEmpty()) {
                    onQueryChange("")
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = barcolorlow
                )
            }
        }
    }
}

/**
 * پیش‌نمایش طراحی نوار جستجو در حالت راست به چپ (RTL).
 */
@Preview
@Composable
fun DockedSearchPreview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        DockedSearch(onQueryChange = {"dsf"})
    }
}
