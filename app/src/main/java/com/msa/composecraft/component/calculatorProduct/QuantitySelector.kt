package com.msa.composecraft.component.calculatorProduct

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msa.composecraft.ui.theme.ComposeCraftTheme

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun QuantitySelector(
    units: List<String>, // لیست واحدهای کالا (مثلاً ["عدد", "کیلوگرم", "لیتر"])
    onUnitSelected: (String) -> Unit, // callback برای انتخاب واحد
    onQuantityChanged: (Int) -> Unit, // callback برای تغییر تعداد
    modifier: Modifier = Modifier,
    minQuantity: Int = 1, // حداقل تعداد مجاز
    maxQuantity: Int = 100 // حداکثر تعداد مجاز
) {
    var selectedUnit by remember { mutableStateOf(units[0]) }
    var quantity by remember { mutableStateOf(minQuantity) }
    var isError by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // انتخاب واحد کالا
        UnitSelector(
            units = units,
            selectedUnit = selectedUnit,
            onUnitSelected = {
                selectedUnit = it
                onUnitSelected(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // افزایش و کاهش تعداد
        QuantityController(
            quantity = quantity,
            onQuantityChanged = { newQuantity ->
                if (newQuantity in minQuantity..maxQuantity) {
                    quantity = newQuantity
                    onQuantityChanged(newQuantity)
                    isError = false
                } else {
                    isError = true
                }
            },
            isError = isError,
            minQuantity = minQuantity,
            maxQuantity = maxQuantity
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitSelector(
    units: List<String>,
    selectedUnit: String,
    onUnitSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "واحد کالا",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            units.forEach { unit ->
                FilterChip(
                    selected = unit == selectedUnit,
                    onClick = { onUnitSelected(unit) },
                    label = { Text(text = unit) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuantityController(
    quantity: Int,
    onQuantityChanged: (Int) -> Unit,
    isError: Boolean,
    minQuantity: Int,
    maxQuantity: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // دکمه کاهش تعداد
        IconButton(
            onClick = { onQuantityChanged(quantity - 1) },
            modifier = Modifier.size(48.dp),
            enabled = quantity > minQuantity
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "کاهش تعداد"
            )
        }

        // نمایش تعداد
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = quantity.toString(),
                onValueChange = {
                    val newQuantity = it.toIntOrNull() ?: minQuantity
                    onQuantityChanged(newQuantity)
                },
                modifier = Modifier.width(100.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isError,
                shape = RoundedCornerShape(12.dp)
            )

            // نمایش پیام خطا (در صورت وجود)
            AnimatedVisibility(
                visible = isError,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                Text(
                    text = "تعداد باید بین $minQuantity و $maxQuantity باشد",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // دکمه افزایش تعداد
        IconButton(
            onClick = { onQuantityChanged(quantity + 1) },
            modifier = Modifier.size(48.dp),
            enabled = quantity < maxQuantity
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "افزایش تعداد"
            )
        }
    }
}


@Composable
fun QuantityScreen(modifier: Modifier = Modifier) {
    ComposeCraftTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val units = listOf("عدد", "کیلوگرم", "لیتر")
            QuantitySelector(
                units = units,
                onUnitSelected = { unit ->
                    println("واحد انتخاب شده: $unit")
                },
                onQuantityChanged = { quantity ->
                    println("تعداد جدید: $quantity")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                minQuantity = 1,
                maxQuantity = 10
            )
        }
    }
}

@Preview
@Composable
private fun QuantityPreview() {
    QuantityScreen()
}