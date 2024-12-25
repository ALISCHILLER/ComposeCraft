package com.msa.composecraft.component.editText

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview

// این کامپوننت برای نمایش یک نوار جستجوی استاندارد است که کاربران می‌توانند متن جستجو را وارد کنند.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SearchBarM3() {

    // وضعیت نگهداری متن جستجو (query) و فعال بودن یا نبودن نوار جستجو
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    // لیست تاریخچه جستجو
    val searchHistory = listOf("Android", "Kotlin", "Compose", "Material Design", "GPT-4")

    // کامپوننت SearchBar برای جستجو
    SearchBar(
        query = query, // مقدار متن جستجو
        onQueryChange = { query = it }, // بروزرسانی متن جستجو در زمان تغییر
        onSearch = { newQuery ->
            println("Performing search on query: $newQuery") // انجام عملیات جستجو
        },
        active = active, // وضعیت فعال بودن نوار جستجو
        onActiveChange = { active = it }, // تغییر وضعیت فعال بودن نوار جستجو
        placeholder = {
            Text(text = "Search") // متن placeholder که در نوار جستجو نمایش داده می‌شود
        },
        leadingIcon = {
            // آیکن جستجو در ابتدای نوار جستجو
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        },
        trailingIcon = {
            // آیکن‌های انتهای نوار جستجو برای فعال یا غیر فعال کردن و همچنین میکروفن
            Row {
                IconButton(onClick = { /* open mic dialog */ }) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Mic" // آیکن میکروفن برای جستجو صوتی
                    )
                }
                // اگر نوار جستجو فعال باشد، امکان پاک کردن جستجو یا غیرفعال کردن آن وجود دارد
                if (active) {
                    IconButton(
                        onClick = { if (query.isNotEmpty()) query = "" else active = false }
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close") // آیکن بستن
                    }
                }
            }
        }
    ) {
        // نمایش تاریخچه جستجو، سه آیتم آخر
        searchHistory.takeLast(3).forEach { item ->
            ListItem(
                modifier = Modifier.clickable { query = item }, // در صورت کلیک، مقدار جستجو به تاریخچه انتخاب شده تغییر می‌کند
                headlineContent = { Text(text = item) }, // نمایش تاریخچه جستجو
                leadingContent = {
                    // آیکن تاریخچه برای هر آیتم
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

// این کامپوننت برای نمایش نوار جستجوی Docked است که در بخش پایین صفحه قرار می‌گیرد.
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DockedSearchBarM3() {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val searchHistory = listOf("Android", "Kotlin", "Compose", "Material Design", "GPT-4")

    // کامپوننت DockedSearchBar برای جستجو
    DockedSearchBar(
        query = query, // مقدار متن جستجو
        onQueryChange = { query = it }, // بروزرسانی متن جستجو در زمان تغییر
        onSearch = { newQuery ->
            println("Performing search on query: $newQuery") // انجام عملیات جستجو
        },
        active = active, // وضعیت فعال بودن نوار جستجو
        onActiveChange = { active = it }, // تغییر وضعیت فعال بودن نوار جستجو
        placeholder = {
            Text(text = "Search") // متن placeholder که در نوار جستجو نمایش داده می‌شود
        },
        leadingIcon = {
            // آیکن جستجو در ابتدای نوار جستجو
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        },
        trailingIcon = {
            // آیکن‌های انتهای نوار جستجو برای فعال یا غیر فعال کردن و همچنین میکروفن
            Row {
                IconButton(onClick = { /* open mic dialog */ }) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Mic" // آیکن میکروفن برای جستجو صوتی
                    )
                }
                if (active) {
                    IconButton(
                        onClick = { if (query.isNotEmpty()) query = "" else active = false }
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close") // آیکن بستن
                    }
                }
            }
        }
    ) {
        // نمایش تاریخچه جستجو، سه آیتم آخر
        searchHistory.takeLast(3).forEach { item ->
            ListItem(
                modifier = Modifier.clickable { query = item }, // در صورت کلیک، مقدار جستجو به تاریخچه انتخاب شده تغییر می‌کند
                headlineContent = { Text(text = item) }, // نمایش تاریخچه جستجو
                leadingContent = {
                    // آیکن تاریخچه برای هر آیتم
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null
                    )
                }
            )
        }
    }
}





//SearchBar(
//modifier = Modifier
//.padding(5.dp)
//.fillMaxWidth()
//.align(Alignment.TopCenter)
//.semantics { traversalIndex = -1f },
//query = text,
//onQueryChange = { text = it },
//onSearch = { active = false },
//active = active,
//onActiveChange = {
//    active = it
//    if (active)
//        viewModel.searchProduct(text)
//},
//placeholder = { Text("محصول مورد نظر را جستجو کنید") },
//colors = SearchBarDefaults.colors(
//containerColor = Color.White
//),
//leadingIcon = {
//    Icon(
//        Icons.Default.Search,
//        contentDescription = null,
//        tint = Color.Red
//    )
//},
//trailingIcon = { Icon(Icons.Default.Mic, contentDescription = null) },
//) {
////                    repeat(4) { idx ->
////                        val resultText = "Suggestion $idx"
////                        ListItem(
////                            headlineContent = { Text(resultText) },
////                            supportingContent = { Text("Additional info") },
////                            leadingContent = {
////                                Icon(
////                                    Icons.Filled.Star,
////                                    contentDescription = null
////                                )
////                            },
////                            modifier = Modifier
////                                .clickable {
////                                    text = resultText
////                                    active = false
////                                }
////                                .fillMaxWidth()
////                                .padding(horizontal = 16.dp, vertical = 4.dp)
////                        )
////                    }
//}