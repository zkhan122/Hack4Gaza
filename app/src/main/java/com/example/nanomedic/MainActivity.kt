package com.example.nanomedic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.nanomedic.ui.theme.NanoMedicTheme

// 'MainActivity' class is the main entry point to the entire application.
// 'MainActivity' is the name given by default to the first screen that loads when a user launches your app.
// 'ComponentActivity' provides all the basic functionality a screen needs (like handling user input, managing its lifecycle, etc.).
class MainActivity : ComponentActivity() {
    // 'onCreate' is called when the screen is created. This is like a constructor for a class.
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // 'setContent' renders the below UI inside this screen.
        setContent {
            NanoMedicTheme {
                // 'Scaffold' is a pre-built layout component from the Material Design library. It gives you a standard screen structure, with slots for things like a top bar, a bottom bar, a floating action button, and the main content area.

                // A 'Modifier' is an object that you pass to a component to change its appearance or behavior (size, padding, background color, click listeners, etc.).
                // Example, 'Modifier' is like the style or className attribute in HTML/JSX. 'Modifier.fillMaxSize()' is the equivalent of setting CSS width: 100%; height: 100%;. We're telling the Scaffold to take up the entire available screen space.

                // 'innerPadding' calculates the height around the content.
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }

                Navigation()
            }
        }
    }
}