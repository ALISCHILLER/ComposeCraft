package com.msa.composecraft.component.customLoading

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

// مدل مختصات سه‌بعدی
data class Vec3(val x: Float, val y: Float, val z: Float)

// داده ذره روی سطح کره با زاویه‌های کروی و اندازه و رنگ
data class ParticleSphere3D(
    var theta: Float,      // زاویه آزیماوت (۰ تا ۲π)
    var phi: Float,        // زاویه قطبی (۰ تا π)
    var baseRadius: Float, // اندازه پایه ذره
    val color: Color       // رنگ ذره
)

@Composable
fun ParticleSphere(
    modifier: Modifier = Modifier,
    particleCount: Int = 350,
    sphereRadiusDp: Float = 140f,
) {
    val density = LocalDensity.current
    val sphereRadiusPx = with(density) { sphereRadiusDp.dp.toPx() }

    // ایجاد لیست ذرات با توزیع یکنواخت روی سطح کره
    val particles = remember {
        List(particleCount) {
            ParticleSphere3D(
                theta = Random.nextFloat() * 2 * PI.toFloat(),
                phi = acos(1f - 2f * Random.nextFloat()),
                baseRadius = Random.nextFloat() * 2f + 1f,
                color = Color.Cyan.copy(alpha = 0.8f)
            )
        }
    }

    // انیمیشن چرخش بی‌نهایت در محور X و Y
    val infiniteTransition = rememberInfiniteTransition()
    val rotationY by infiniteTransition.animateFloat(
        0f, 360f,
        animationSpec = infiniteRepeatable(animation = tween(18000, easing = LinearEasing))
    )
    val rotationX by infiniteTransition.animateFloat(
        0f, 360f,
        animationSpec = infiniteRepeatable(animation = tween(28000, easing = LinearEasing))
    )

    // تبدیل مختصات کروی به کارتزین با اعمال چرخش حول محورها
    fun sphericalToCartesian(
        theta: Float,
        phi: Float,
        radius: Float,
        rotationX: Float,
        rotationY: Float
    ): Vec3 {
        val x = radius * sin(phi) * cos(theta)
        val y = radius * cos(phi)
        val z = radius * sin(phi) * sin(theta)

        // چرخش حول محور X
        val cosX = cos(rotationX)
        val sinX = sin(rotationX)
        val y1 = y * cosX - z * sinX
        val z1 = y * sinX + z * cosX

        // چرخش حول محور Y
        val cosY = cos(rotationY)
        val sinY = sin(rotationY)
        val x2 = x * cosY - z1 * sinY
        val z2 = x * sinY + z1 * cosY

        return Vec3(x2, y1, z2)
    }

    // محاسبه شدت نور بر اساس زاویه بین نور و نرمال ذره
    fun calcLightIntensity(x: Float, y: Float, z: Float): Float {
        val lightDir = Vec3(0f, 0f, 1f) // نور از سمت جلو (محور Z مثبت)
        val length = sqrt(x * x + y * y + z * z)
        val dot = (x * lightDir.x + y * lightDir.y + z * lightDir.z) / length
        return max(0.1f, dot) // حداقل نور برای ذرات پشت کره
    }

    Box(
        modifier = modifier
            .size(sphereRadiusDp.dp * 2)
            .background(Color.Black)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            // پس‌زمینه کره با رنگ تیره
            drawCircle(Color.DarkGray, radius = sphereRadiusPx, center = Offset(cx, cy))

            // مرتب‌سازی ذرات بر اساس عمق (z) برای رسم صحیح
            val sortedParticles: List<Pair<ParticleSphere3D, Vec3>> =
                particles.map { p ->
                    val pos = sphericalToCartesian(
                        p.theta, p.phi, sphereRadiusPx,
                        Math.toRadians(rotationX.toDouble()).toFloat(),
                        Math.toRadians(rotationY.toDouble()).toFloat()
                    )
                    p to pos
                }.sortedBy { it.second.z }

            // رسم ذرات
            sortedParticles.forEach { (p, pos) ->
                val (x3d, y3d, z3d) = pos

                val perspective = (z3d / sphereRadiusPx + 1f) / 2f
                val light = calcLightIntensity(x3d, y3d, z3d)

                val particleRadius = lerp(p.baseRadius * 0.3f, p.baseRadius * 1.8f, perspective)
                val alpha = lerp(0.1f, 1f, light) * lerp(0.2f, 1f, perspective)

                val colorWithLight = p.color.copy(alpha = alpha)

                drawCircle(
                    color = colorWithLight,
                    center = Offset(cx + x3d, cy + y3d),
                    radius = particleRadius,
                    blendMode = BlendMode.Plus // حالت نئون زیباتر
                )
            }
        }
    }
}

// تابع کمکی برای خطی‌کردن مقدار بین start و end
fun lerp(start: Float, end: Float, fraction: Float) = start + (end - start) * fraction

@Preview(showBackground = true)
@Composable
fun PreviewParticleSphere() {
    ParticleSphere(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    )
}
