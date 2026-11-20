package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.PreferencesRepository
import com.example.ui.navigation.GanjinehAppRoot
import com.example.ui.theme.GanjinehTheme
import com.example.ui.viewmodel.GanjinehViewModel
import com.example.ui.viewmodel.GanjinehViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        val prefsRepo = PreferencesRepository(applicationContext)
        val factory = GanjinehViewModelFactory(prefsRepo)
        val viewModel = ViewModelProvider(this, factory)[GanjinehViewModel::class.java]

        setContent {
            val progressState by viewModel.progressState.collectAsStateWithLifecycle()
            GanjinehTheme(themeMode = progressState.themeMode) {
                GanjinehAppRoot(viewModel = viewModel)
            }
        }
    }
}
