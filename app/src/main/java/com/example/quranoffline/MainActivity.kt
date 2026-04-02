package com.example.quranoffline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.quranoffline.ui.theme.QuranOfflineTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: android.content.Context) {
        val config = newBase.resources.configuration
        val locales = config.locales
        if (!locales.isEmpty) {
            java.util.Locale.setDefault(locales[0])
        }
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QuranOfflineTheme {
                QuranApp()
            }
        }
    }
}