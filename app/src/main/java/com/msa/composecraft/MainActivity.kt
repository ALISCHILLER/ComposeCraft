package com.msa.composecraft

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.msa.composecraft.animComponent.animatedButtonShowcase.AnimatedButtonShowcase
import com.msa.composecraft.animComponent.crossfadeImageSlider.CrossfadeImageSlider
import com.msa.composecraft.ui.theme.ComposeCraftTheme

class MainActivity : ComponentActivity() {

    var speechInput = mutableStateOf("")
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

    fun askSpeechInput(context: Context) {
        val language = "fa-IR"
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Toast.makeText(context, "Speech not Available", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language)
            intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk")
            startActivityForResult(intent, 102)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            speechInput.value = result?.get(0).toString()
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