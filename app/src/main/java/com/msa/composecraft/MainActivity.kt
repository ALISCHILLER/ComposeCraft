package com.msa.composecraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.msa.composecraft.animatedButtonShowcase.AnimatedButtonShowcase
import com.msa.composecraft.crossfadeImageSlider.CrossfadeImageSlider
import com.msa.composecraft.ui.theme.ComposeCraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeCraftTheme {
                // VerticalSliderHandle()
                // AnimatedColorTextDisplay()
                // AnimatedButtonShowcase()
                // AnimatedImageCarousel()
                // CrossfadeImageSlider()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeCraftTheme {
        // VerticalSliderHandle()
        // AnimatedColorTextDisplay()
        // AnimatedButtonShowcase()
        // AnimatedImageCarousel()
        // CrossfadeImageSlider()
    }
}