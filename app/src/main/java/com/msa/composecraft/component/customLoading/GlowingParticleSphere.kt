//package com.msa.composecraft.component.customLoading
//
//
//
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.*
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import kotlin.math.*
//import kotlin.random.Random
//
//data class ParticleSphereGlowingParticleSphere3D(
//    var theta: Float,      // زاویه آزیماوت (۰ تا ۲π)
//    var phi: Float,        // زاویه قطبی (۰ تا π)
//    var baseRadius: Float, // اندازه پایه ذره
//    val color: Color       // رنگ ذره
//)
//
//@Composable
//fun GlowingParticleSphere(
//    modifier: Modifier = Modifier,
//    particleCount: Int = 350,
//    sphereRadiusDp: Float = 140f,
//) {
//    val density = LocalDensity.current
//    val sphereRadiusPx = with(density) { sphereRadiusDp.dp.toPx() }
//
//    val particles = remember {
//        List(particleCount) {
//            ParticleSphere3D(
//                theta = Random.nextFloat() * 2 * PI.toFloat(),
//                phi = acos(1f - 2f * Random.nextFloat()),
//                baseRadius = Random.nextFloat() * 2f + 1f,
//                color = Color.Cyan.copy(alpha = 0.8f)
//            )
//        }
//    }
//
//    val infiniteTransition = rememberInfiniteTransition()
//    val rotationY by infiniteTransition.animateFloat(
//        0f, 360f,
//        animationSpec = infiniteRepeatable(animation = tween(18000, easing = LinearEasing))
//    )
//    val rotationX by infiniteTransition.animateFloat(
//        0f, 360f,
//        animationSpec = infiniteRepeatable(animation = tween(28000, easing = LinearEasing))
//    )
//
//    fun sphericalToCartesian(
//        theta: Float,
//        phi: Float,
//        radius: Float,
//        rotationX: Float,
//        rotationY: Float
//    ): Triple<Float, Float, Float> {
//        val x = radius * sin(phi) * cos(theta)
//        val y = radius * cos(phi)
//        val z = radius * sin(phi) * sin(theta)
//
//        val cosX = cos(rotationX)
//        val sinX = sin(rotationX)
//        val y1 = y * cosX - z * sinX
//        val z1 = y * sinX + z * cosX
//
//        val cosY = cos(rotationY)
//        val sinY = sin(rotationY)
//        val x2 = x * cosY - z1 * sinY
//        val z2 = x * sinY + z1 * cosY
//
//        return Triple(x2, y1, z2)
//    }
//
//    fun calcLightIntensity(x: Float, y: Float, z: Float): Float {
//        val lightDir = Triple(0f, 0f, 1f)
//        val length = sqrt(x * x + y * y + z * z)
//        val dot = (x * lightDir.first + y * lightDir.second + z * lightDir.third) / length
//        return max(0.1f, dot.toFloat())
//    }
//
//    Box(
//        modifier = modifier
//            .size(sphereRadiusDp.dp * 2)
//            .background(Color.Black)
//    ) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val cx = size.width / 2f
//            val cy = size.height / 2f
//
//            drawCircle(Color.DarkGray, radius = sphereRadiusPx, center = Offset(cx, cy))
//
//            val sortedParticles: List<Triple<ParticleSphere3D, Triple<Float, Float, Float>>> =
//                particles.map { p ->
//                    val (x3d, y3d, z3d) = sphericalToCartesian(
//                        p.theta, p.phi, sphereRadiusPx,
//                        Math.toRadians(rotationX.toDouble()).toFloat(),
//                        Math.toRadians(rotationY.toDouble()).toFloat()
//                    )
//                    Triple(p, Triple(x3d, y3d, z3d))
//                }.sortedBy { it.second.third }
//
//            sortedParticles.forEach { (p, pos) ->
//                val (x3d, y3d, z3d) = pos
//
//                val perspective = (z3d / sphereRadiusPx + 1f) / 2f
//                val light = calcLightIntensity(x3d, y3d, z3d)
//
//                val particleRadius = lerpGlowingParticleSphere(p.baseRadius * 0.3f, p.baseRadius * 1.8f, perspective)
//                val alpha = lerp(0.1f, 1f, light) * lerp(0.2f, 1f, perspective)
//
//                val colorWithLight = p.color.copy(alpha = alpha)
//
//                drawCircle(
//                    color = colorWithLight,
//                    center = Offset(cx + x3d, cy + y3d),
//                    radius = particleRadius,
//                    blendMode = BlendMode.Plus
//                )
//            }
//        }
//    }
//}
//
//fun lerpGlowingParticleSphere(start: Float, end: Float, fraction: Float) = start + (end - start) * fraction
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewGlowingParticleSphere() {
//    GlowingParticleSphere(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp)
//    )
//}
