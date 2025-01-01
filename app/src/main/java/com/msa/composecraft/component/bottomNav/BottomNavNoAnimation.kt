@file:OptIn(ExperimentalAnimationApi::class)

// فایل اصلی: تعریف کلاس‌ها و کامپوننت‌های مربوط به Bottom Navigation بدون انیمیشن

package com.msa.composecraft.component.bottomNav

// ایمپورت‌های مورد نیاز از Compose
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import com.msa.composecraft.R
import com.msa.composecraft.ui.theme.Typography
import com.msa.composecraft.ui.theme.barcolorDark

/**
 * تابع پیش‌نمایش برای نمایش Bottom Navigation بدون انیمیشن
 */
@Preview
@Composable
fun BottomNavNoAnimationPreview() {
    Scaffold(
        bottomBar = {
            BottomNavNoAnimation(
                currentRoute = "",
                onClick = {}
            )
        }
    ) {
        it
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.Red)
        ) {}
    }
}

/**
 * کامپوننت اصلی Bottom Navigation بدون انیمیشن
 *
 * @param currentRoute مسیر جاری
 * @param onClick تابع برای هندل کردن کلیک روی هر آیتم
 */
@ExperimentalAnimationApi
@Composable
fun BottomNavNoAnimation(
    currentRoute: String?,
    onClick: (String) -> Unit,
) {
    val screens = listOf(
        Screen("صفحه اصلی", Icons.Filled.Home, Icons.Outlined.Home, Route.HomeScreen.route),
        Screen("گزارش خرید", ImageVector.vectorResource(R.drawable.ic_invoice), ImageVector.vectorResource(R.drawable.ic_invoice), Route.OrderStatusReportScreen.route),
        Screen("سبد", ImageVector.vectorResource(R.drawable.ic_basket), ImageVector.vectorResource(R.drawable.ic_basket), Route.BasketScreen.route),
        Screen("پروفایل", ImageVector.vectorResource(R.drawable.ic_profile), ImageVector.vectorResource(R.drawable.ic_profile), Route.ProfileScreen.route)
    )

    var selectedScreen by remember { mutableStateOf(0) }
    Box(
        Modifier
            .padding(horizontal = 8.dp, vertical = 5.dp)
            .shadow(5.dp, shape = RoundedCornerShape(10.dp))
            .background(color = colors.surface)
            .height(64.dp)
            .fillMaxWidth()
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (screen in screens) {
                val isSelected = screen == screens.find { it.route == currentRoute }
                val animatedWeight by animateFloatAsState(targetValue = if (isSelected) 1.5f else 1f)
                Box(
                    modifier = Modifier.weight(animatedWeight),
                    contentAlignment = Alignment.Center,
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    BottomNavItem(
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            selectedScreen = screens.indexOf(screen)
                            onClick(screen.route)
                        },
                        item = screen,
                        isSelected = isSelected,
                    )
                }
            }
        }
    }
}

/**
 * آیتم‌های Bottom Navigation با انیمیشن
 *
 * @param modifier مودیفایر
 * @param item آیتم مربوطه
 * @param isSelected وضعیت انتخاب شده
 */
@ExperimentalAnimationApi
@Composable
private fun BottomNavItem(
    modifier: Modifier = Modifier,
    item: Screen,
    isSelected: Boolean,
) {
    val animatedHeight by animateDpAsState(targetValue = if (isSelected) 36.dp else 26.dp)
    val animatedElevation by animateDpAsState(targetValue = if (isSelected) 15.dp else 0.dp)
    val animatedAlpha by animateFloatAsState(targetValue = if (isSelected) 1f else .5f)
    val animatedIconSize by animateDpAsState(
        targetValue = if (isSelected) 26.dp else 20.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .height(animatedHeight)
                .shadow(elevation = animatedElevation, shape = RoundedCornerShape(20.dp))
                .background(
                    color = if (isSelected) Color.Red else colors.surface,
                    shape = RoundedCornerShape(22.dp)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                FlipIcon(
                    modifier = Modifier
                        .alpha(animatedAlpha)
                        .size(animatedIconSize),
                    isActive = isSelected,
                    activeIcon = item.activeIcon,
                    inactiveIcon = item.inactiveIcon,
                    contentDescription = ""
                )

                AnimatedVisibility(visible = isSelected) {
                    Text(
                        text = item.title,
                        modifier = Modifier.padding(start = 1.dp, end = 1.dp),
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
 * آیکون قابل چرخش برای Bottom Navigation
 *
 * @param modifier مودیفایر
 * @param isActive وضعیت فعال
 * @param activeIcon آیکون فعال
 * @param inactiveIcon آیکون غیرفعال
 * @param contentDescription توضیحات آیکون
 */
@Composable
fun FlipIcon(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    contentDescription: String,
) {
    val animationRotation by animateFloatAsState(
        targetValue = if (isActive) 180f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )
    Box(
        modifier = modifier.graphicsLayer { rotationY = animationRotation },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            rememberVectorPainter(image = if (animationRotation > 90f) activeIcon else inactiveIcon),
            contentDescription = contentDescription,
            tint = if (isActive) Color.White else barcolorDark
        )
    }
}

/**
 * مدل صفحه
 *
 * @param title عنوان صفحه
 * @param activeIcon آیکون فعال
 * @param inactiveIcon آیکون غیرفعال
 * @param route مسیر صفحه
 */
data class Screen(
    val title: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val route: String,
)

/**
 * مسیرها و صفحات مختلف اپلیکیشن
 */
sealed class Route(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object BACK : Route(route = "back")
    object SplashScreen : Route(route = "splashScreen")
    object LoginScreen : Route(route = "loginScreen")
    object HomeScreen : Route(route = "homeScreen")
    object BasketScreen : Route(route = "BasketScreen")
    object ProfileScreen : Route(route = "profileScreen")
    object OrderStatusReportScreen : Route(route = "orderStatusReportScreen")
}
