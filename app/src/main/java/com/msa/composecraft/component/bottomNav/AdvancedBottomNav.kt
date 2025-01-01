/**
 * @file:OptIn(ExperimentalAnimationApi::class)
 * این فایل شامل یک نوار ناوبری پیشرفته است که با استفاده از Jetpack Compose پیاده‌سازی شده است.
 * نوار ناوبری به کاربران امکان می‌دهد تا بین صفحات مختلف اپلیکیشن به راحتی جابجا شوند.
 * ویژگی‌های این نوار ناوبری شامل انیمیشن‌های جذاب، تغییر رنگ آیتم‌ها هنگام انتخاب، و قابلیت شخصی‌سازی آسان است.
 */

@file:OptIn(ExperimentalAnimationApi::class)

package com.msa.composecraft.component.bottomNav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import com.msa.composecraft.R
import com.msa.composecraft.ui.theme.Typography
import com.msa.composecraft.ui.theme.barcolorDark




/**
 * یک پیش‌نمایش از نوار ناوبری پیشرفته با استفاده از Scaffold که
 * برای نمایش رابط کاربری در حالت طراحی و آزمایش استفاده می‌شود.
 */
@Preview
@Composable
fun AdvancedBottomNavPreview() {
    Scaffold(
        bottomBar = {
            AdvancedBottomNav(
                currentRoute = Route1.HomeScreen.route,
                onClick = {}
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        )
    }
}

/**
 * یک نوار ناوبری پیشرفته که دارای قابلیت‌های انیمیشنی، انتخاب آیتم‌ها و
 * شخصی‌سازی رنگ‌ها و استایل‌ها است.
 *
 * @param currentRoute مسیر فعلی که در حال نمایش است.
 * @param onClick یک تابع که هنگام کلیک بر روی آیتم‌های نوار ناوبری فراخوانی می‌شود.
 * @param modifier تنظیمات اضافی برای ظاهر نوار.
 * @param backgroundColor رنگ پس‌زمینه نوار.
 * @param selectedItemColor رنگ آیتم‌های انتخاب‌شده.
 * @param unselectedItemColor رنگ آیتم‌های غیرانتخاب‌شده.
 * @param elevation ارتفاع سایه‌ی نوار.
 * @param itemPadding فاصله بین آیتم‌های نوار.
 */
@ExperimentalAnimationApi
@Composable
fun AdvancedBottomNav(
    currentRoute: String?,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    selectedItemColor: Color = Color.Red,
    unselectedItemColor: Color = barcolorDark,
    elevation: Dp = 5.dp,
    itemPadding: Dp = 8.dp
) {
    val screens = listOf(
        Screen1("صفحه اصلی", Icons.Filled.Home, Icons.Outlined.Home, Route1.HomeScreen.route),
        Screen1("گزارش خرید", ImageVector.vectorResource(R.drawable.ic_invoice), ImageVector.vectorResource(R.drawable.ic_invoice), Route1.OrderStatusReportScreen.route),
        Screen1("سبد", ImageVector.vectorResource(R.drawable.ic_basket), ImageVector.vectorResource(R.drawable.ic_basket), Route1.BasketScreen.route),
        Screen1("پروفایل", ImageVector.vectorResource(R.drawable.ic_profile), ImageVector.vectorResource(R.drawable.ic_profile), Route1.ProfileScreen.route)
    )

    Box(
        modifier = modifier
            .padding(horizontal = itemPadding, vertical = itemPadding / 2)
            .shadow(elevation, RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .height(64.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            screens.forEach { screen ->
                val isSelected = screen.route == currentRoute
                AdvancedBottomNavItem(
                    item = screen,
                    isSelected = isSelected,
                    selectedItemColor = selectedItemColor,
                    unselectedItemColor = unselectedItemColor,
                    onClick = { onClick(screen.route) }
                )
            }
        }
    }
}

/**
 * آیتم‌های نوار ناوبری با انیمیشن تغییر ارتفاع، سایه و اندازه آیکون.
 *
 * @param item اطلاعات مربوط به آیتم.
 * @param isSelected وضعیت انتخاب شده بودن آیتم.
 * @param selectedItemColor رنگ آیتم انتخاب‌شده.
 * @param unselectedItemColor رنگ آیتم‌های غیرانتخاب‌شده.
 * @param onClick عملکرد هنگام کلیک روی آیتم.
 */
@ExperimentalAnimationApi
@Composable
private fun AdvancedBottomNavItem(
    item: Screen1,
    isSelected: Boolean,
    selectedItemColor: Color,
    unselectedItemColor: Color,
    onClick: () -> Unit
) {
    val animatedHeight by animateDpAsState(if (isSelected) 36.dp else 26.dp)
    val animatedElevation by animateDpAsState(if (isSelected) 15.dp else 0.dp)
    val animatedAlpha by animateFloatAsState(if (isSelected) 1f else 0.5f)
    val animatedIconSize by animateDpAsState(
        if (isSelected) 26.dp else 20.dp,
        spring(stiffness = StiffnessLow, dampingRatio = DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(animatedHeight)
                .shadow(animatedElevation, RoundedCornerShape(20.dp))
                .background(if (isSelected) selectedItemColor else Color.Transparent, RoundedCornerShape(22.dp))
                .padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FlipIcon(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(animatedIconSize)
                        .alpha(animatedAlpha),
                    isActive = isSelected,
                    activeIcon = item.activeIcon,
                    inactiveIcon = item.inactiveIcon,
                    contentDescription = item.title,
                    tint = if (isSelected) Color.White else unselectedItemColor
                )

                AnimatedVisibility(visible = isSelected) {
                    Text(
                        text = item.title,
                        modifier = Modifier.padding(start = 4.dp),
                        maxLines = 1,
                        color = Color.White,
                        style = Typography.labelSmall
                    )
                }
            }
        }
    }
}

/**
 * یک آیکون با انیمیشن چرخش که هنگام انتخاب تغییر می‌کند.
 *
 * @param modifier تنظیمات نمایش آیکون.
 * @param isActive وضعیت انتخاب شده بودن.
 * @param activeIcon آیکون فعال.
 * @param inactiveIcon آیکون غیرفعال.
 * @param contentDescription توضیح محتوا برای دسترس‌پذیری.
 * @param tint رنگ آیکون.
 */
@Composable
fun FlipIcon(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    contentDescription: String,
    tint: Color
) {
    val animationRotation by animateFloatAsState(
        if (isActive) 180f else 0f,
        spring(stiffness = StiffnessLow, dampingRatio = DampingRatioMediumBouncy)
    )
    Box(
        modifier = modifier.graphicsLayer { rotationY = animationRotation },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            rememberVectorPainter(image = if (animationRotation > 90f) activeIcon else inactiveIcon),
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

/**
 * یک داده کلاسی برای نمایش اطلاعات مربوط به هر آیتم نوار ناوبری.
 *
 * @param title عنوان آیتم.
 * @param activeIcon آیکون فعال.
 * @param inactiveIcon آیکون غیرفعال.
 * @param route مسیر مربوط به آیتم.
 */
data class Screen1(
    val title: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val route: String
)

/**
 * مسیرهای مختلف اپلیکیشن که در نوار ناوبری نمایش داده می‌شوند.
 */
sealed class Route1(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object HomeScreen : Route(route = "homeScreen")
    object OrderStatusReportScreen : Route(route = "orderStatusReportScreen")
    object BasketScreen : Route(route = "basketScreen")
    object ProfileScreen : Route(route = "profileScreen")
}
