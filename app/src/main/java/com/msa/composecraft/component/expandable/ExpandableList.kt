package com.msa.composecraft.component.expandable

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// کلاس داده‌ای برای نگهداری اطلاعات هر گروه در لیست قابل گسترش
data class ExpandableItem(
    val title: String, // عنوان گروه
    val subItems: List<String>, // لیست آیتم‌های زیرمجموعه
    val icon: ImageVector? = null // آیکون سفارشی برای هدر (اختیاری)
)

// کامپوننت اصلی برای نمایش لیست قابل گسترش
@Composable
fun ExpandableList(
    items: List<ExpandableItem>, // لیست گروه‌ها و آیتم‌های زیرمجموعه
    modifier: Modifier = Modifier, // مودیفایر برای سفارشی‌سازی اندازه و موقعیت
    headerColor: Color = MaterialTheme.colorScheme.primary, // رنگ پس‌زمینه هدر
    headerTextColor: Color = MaterialTheme.colorScheme.onPrimary, // رنگ متن هدر
    subItemColor: Color = MaterialTheme.colorScheme.surfaceVariant, // رنگ پس‌زمینه آیتم‌های زیرمجموعه
    subItemTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant, // رنگ متن آیتم‌های زیرمجموعه
    isAccordion: Boolean = false, // حالت آکاردئونی (فقط یک گروه باز باشد)
    onSubItemClick: (String) -> Unit = {} // عملیات کلیک روی آیتم‌های زیرمجموعه
) {
    // حالت برای نگهداری ایندکس گروهی که در حال حاضر باز است
    var expandedGroupId by remember { mutableStateOf<Int?>(null) }

    // استفاده از LazyColumn برای نمایش لیست گروه‌ها
    LazyColumn(modifier = modifier) {
        // تبدیل لیست به لیستی از IndexedValue و نمایش هر گروه
        items(items.withIndex().toList(), key = { it.index }) { (index, item) ->
            ExpandableGroup(
                item = item,
                isExpanded = if (isAccordion) index == expandedGroupId else null,
                onExpandChange = { expanded ->
                    expandedGroupId = if (expanded) index else null
                },
                headerColor = headerColor,
                headerTextColor = headerTextColor,
                subItemColor = subItemColor,
                subItemTextColor = subItemTextColor,
                onSubItemClick = onSubItemClick
            )
        }
    }
}

// کامپوننت برای نمایش یک گروه قابل گسترش
@Composable
fun ExpandableGroup(
    item: ExpandableItem, // اطلاعات گروه
    isExpanded: Boolean?, // مشخص می‌کند که آیا گروه باز است یا خیر
    onExpandChange: (Boolean) -> Unit, // عملیات تغییر حالت باز و بسته شدن
    headerColor: Color, // رنگ پس‌زمینه هدر
    headerTextColor: Color, // رنگ متن هدر
    subItemColor: Color, // رنگ پس‌زمینه آیتم‌های زیرمجموعه
    subItemTextColor: Color, // رنگ متن آیتم‌های زیرمجموعه
    onSubItemClick: (String) -> Unit // عملیات کلیک روی آیتم‌های زیرمجموعه
) {
    // حالت برای نگهداری وضعیت باز یا بسته بودن گروه
    var isGroupExpanded by remember { mutableStateOf(isExpanded ?: false) }

    // اثر جانبی برای به‌روزرسانی وضعیت باز و بسته شدن گروه
    LaunchedEffect(isExpanded) {
        if (isExpanded != null) {
            isGroupExpanded = isExpanded
        }
    }

    // ستون اصلی برای نمایش گروه و آیتم‌های زیرمجموعه
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 300)) // انیمیشن برای تغییر اندازه
    ) {
        // هدر گروه
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isGroupExpanded = !isGroupExpanded
                    onExpandChange(isGroupExpanded)
                }
                .background(headerColor)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ردیف برای نمایش آیکون و عنوان
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (item.icon != null) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = headerTextColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = item.title,
                    color = headerTextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            // آیکون باز و بسته شدن
            Icon(
                imageVector = if (isGroupExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isGroupExpanded) "Collapse" else "Expand",
                tint = headerTextColor
            )
        }

        // نمایش آیتم‌های زیرمجموعه اگر گروه باز باشد
        if (isGroupExpanded) {
            Column(modifier = Modifier.fillMaxWidth()) {
                item.subItems.forEach { subItem ->
                    Text(
                        text = subItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSubItemClick(subItem) }
                            .background(subItemColor)
                            .padding(16.dp),
                        color = subItemTextColor,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

// پیش‌نمایش اول: لیست قابل گسترش با حالت آکاردئونی
@Preview(showBackground = true)
@Composable
fun PreviewExpandableList() {
    val items = listOf(
        ExpandableItem(
            title = "گروه ۱",
            subItems = listOf("آیتم ۱-۱", "آیتم ۱-۲", "آیتم ۱-۳"),
            icon = Icons.Default.ExpandMore
        ),
        ExpandableItem(
            title = "گروه ۲",
            subItems = listOf("آیتم ۲-۱", "آیتم ۲-۲")
        ),
        ExpandableItem(
            title = "گروه ۳",
            subItems = listOf("آیتم ۳-۱", "آیتم ۳-۲", "آیتم ۳-۳", "آیتم ۳-۴")
        )
    )

    MaterialTheme {
        ExpandableList(
            items = items,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            isAccordion = true
        )
    }
}

// پیش‌نمایش دوم: لیست قابل گسترش با رنگ‌های سفارشی
@Preview(showBackground = true)
@Composable
fun PreviewExpandableList2() {
    val items = listOf(
        ExpandableItem(
            title = "گروه ۱",
            subItems = listOf("آیتم ۱-۱", "آیتم ۱-۲")
        ),
        ExpandableItem(
            title = "گروه ۲",
            subItems = listOf("آیتم ۲-۱", "آیتم ۲-۲", "آیتم ۲-۳")
        )
    )

    ExpandableList(
        items = items,
        headerColor = Color.Blue,
        headerTextColor = Color.White,
        subItemColor = Color.LightGray,
        subItemTextColor = Color.Black
    )
}

// پیش‌نمایش سوم: لیست قابل گسترش با حالت آکاردئونی
@Preview(showBackground = true, name = "Accordion ExpandableList")
@Composable
fun PreviewAccordionExpandableList() {
    val items = listOf(
        ExpandableItem(
            title = "گروه ۱",
            subItems = listOf("آیتم ۱-۱", "آیتم ۱-۲", "آیتم ۱-۳"),
            icon = Icons.Default.ExpandMore
        ),
        ExpandableItem(
            title = "گروه ۲",
            subItems = listOf("آیتم ۲-۱", "آیتم ۲-۲")
        ),
        ExpandableItem(
            title = "گروه ۳",
            subItems = listOf("آیتم ۳-۱", "آیتم ۳-۲", "آیتم ۳-۳", "آیتم ۳-۴")
        )
    )

    MaterialTheme {
        ExpandableList(
            items = items,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            isAccordion = true
        )
    }
}

// پیش‌نمایش چهارم: لیست قابل گسترش با رنگ‌های سفارشی
@Preview(showBackground = true, name = "Custom Colors ExpandableList")
@Composable
fun PreviewCustomColorsExpandableList() {
    val items = listOf(
        ExpandableItem(
            title = "گروه ۱",
            subItems = listOf("آیتم ۱-۱", "آیتم ۱-۲", "آیتم ۱-۳"),
            icon = Icons.Default.ExpandMore
        ),
        ExpandableItem(
            title = "گروه ۲",
            subItems = listOf("آیتم ۲-۱", "آیتم ۲-۲")
        ),
        ExpandableItem(
            title = "گروه ۳",
            subItems = listOf("آیتم ۳-۱", "آیتم ۳-۲", "آیتم ۳-۳", "آیتم ۳-۴")
        )
    )

    MaterialTheme {
        ExpandableList(
            items = items,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            headerColor = Color.Blue,
            headerTextColor = Color.White,
            subItemColor = Color.LightGray,
            subItemTextColor = Color.Black
        )
    }
}