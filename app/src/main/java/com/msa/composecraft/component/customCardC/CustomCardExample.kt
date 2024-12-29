package com.msa.composecraft.component.customCardC

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class CardType {
    FILLED, ELEVATED, OUTLINED
}

@Composable
fun CustomCardExample(
    modifier: Modifier = Modifier,
    cardType: CardType = CardType.FILLED,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    elevation: Dp = 4.dp,
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = modifier
        .fillMaxWidth()
        .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })

    when (cardType) {
        CardType.FILLED -> {
            Card(
                modifier = cardModifier,
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                ),
                shape = shape,
                elevation = CardDefaults.cardElevation(defaultElevation = elevation)
            ) {
                Column(content = content)
            }
        }
        CardType.ELEVATED -> {
            ElevatedCard(
                modifier = cardModifier,
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                ),
                shape = shape,
                elevation = CardDefaults.cardElevation(defaultElevation = elevation)
            ) {
                Column(content = content)
            }
        }
        CardType.OUTLINED -> {
            OutlinedCard(
                modifier = cardModifier,
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                ),
                shape = shape,
                border = border ?: BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(content = content)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomCardExample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Filled Card
        CustomCardExample(
            cardType = CardType.FILLED,
            backgroundColor = Color.LightGray,
            contentColor = Color.DarkGray,
            onClick = { /* Handle click */ }
        ) {
            Text(
                text = "Filled Card",
                modifier = Modifier.padding(16.dp)
            )
        }

        // Elevated Card
        CustomCardExample(
            cardType = CardType.ELEVATED,
            elevation = 8.dp,
            onClick = { /* Handle click */ }
        ) {
            Text(
                text = "Elevated Card",
                modifier = Modifier.padding(16.dp)
            )
        }

        // Outlined Card
        CustomCardExample(
            cardType = CardType.OUTLINED,
            border = BorderStroke(2.dp, Color.Red),
            onClick = { /* Handle click */ }
        ) {
            Text(
                text = "Outlined Card",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}