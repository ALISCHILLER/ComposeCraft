package com.msa.composecraft.component.radioGroupC

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msa.composecraft.ui.theme.Typography

/**
 * این کامپوننت یک گروه رادیویی به صورت ردیفی (Row) ایجاد می‌کند.
 * @param options لیستی از گزینه‌هایی که باید نمایش داده شوند.
 * @param selectedOption گزینه‌ای که در حال حاضر انتخاب شده است.
 * @param onOptionSelected تابعی که هنگام انتخاب یک گزینه فراخوانی می‌شود.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 */
@Composable
fun RadioGroupRow(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // تنظیم رنگ‌های دکمه‌های رادیویی
    val radioColors = RadioButtonDefaults.colors(
        selectedColor = MaterialTheme.colorScheme.primary, // رنگ دایره انتخاب شده
        unselectedColor = MaterialTheme.colorScheme.onSurface // رنگ دایره غیرانتخاب شده
    )

    // ایجاد یک ردیف برای نمایش گزینه‌ها
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        options.forEach { option ->
            // هر گزینه به صورت یک ردیف شامل دکمه رادیویی و متن نمایش داده می‌شود
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                // دکمه رادیویی
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    colors = radioColors
                )
                // متن گزینه
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

/**
 * این کامپوننت یک گروه رادیویی به صورت ستونی (Column) ایجاد می‌کند.
 * @param options لیستی از گزینه‌هایی که باید نمایش داده شوند.
 * @param selectedOption گزینه‌ای که در حال حاضر انتخاب شده است.
 * @param onOptionSelected تابعی که هنگام انتخاب یک گزینه فراخوانی می‌شود.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 */
@Composable
fun RadioGroupColumn(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // تنظیم رنگ‌های دکمه‌های رادیویی
    val radioColors = RadioButtonDefaults.colors(
        selectedColor = MaterialTheme.colorScheme.primary, // رنگ دایره انتخاب شده
        unselectedColor = MaterialTheme.colorScheme.onSurface // رنگ دایره غیرانتخاب شده
    )

    // ایجاد یک ستون برای نمایش گزینه‌ها
    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        options.forEach { option ->
            // هر گزینه به صورت یک ردیف شامل دکمه رادیویی و متن نمایش داده می‌شود
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                // دکمه رادیویی
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    colors = radioColors
                )
                // متن گزینه
                Text(
                    text = option,
                    style = Typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

/**
 * Preview برای کامپوننت RadioGroupRow.
 */
@Preview(showBackground = true)
@Composable
private fun RadioGroupRowPreview() {
    RadioGroupRow(
        options = listOf("گزینه ۱", "گزینه ۲", "گزینه ۳"),
        selectedOption = "گزینه ۱",
        onOptionSelected = { selected -> println("$selected انتخاب شد") },
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Preview برای کامپوننت RadioGroupColumn.
 */
@Preview(showBackground = true)
@Composable
private fun RadioGroupColumnPreview() {
    RadioGroupColumn(
        options = listOf("گزینه ۱", "گزینه ۲", "گزینه ۳"),
        selectedOption = "گزینه ۱",
        onOptionSelected = { selected -> println("$selected انتخاب شد") },
        modifier = Modifier.fillMaxWidth()
    )
}