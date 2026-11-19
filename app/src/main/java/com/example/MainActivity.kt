package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import com.example.audio.PianoAudioEngine
import com.example.data.local.AppDatabase
import com.example.data.local.SettingsDataStore
import com.example.data.remote.QuestionUpdateService
import com.example.data.repository.BiologyRepository
import com.example.ui.navigation.ZistNegarAppRoot
import com.example.ui.theme.ZistNegarTheme
import com.example.ui.viewmodel.BiologyViewModel
import com.example.ui.viewmodel.BiologyViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var audioEngine: PianoAudioEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        val db = AppDatabase.getInstance(applicationContext)
        val settingsStore = SettingsDataStore(applicationContext)
        val updateService = QuestionUpdateService(applicationContext)
        val repository = BiologyRepository(db.biologyDao(), settingsStore, updateService)
        audioEngine = PianoAudioEngine()

        val factory = BiologyViewModelFactory(repository, audioEngine)
        val viewModel = ViewModelProvider(this, factory)[BiologyViewModel::class.java]

        audioEngine.start()
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> audioEngine.pauseForLifecycle()
                    Lifecycle.Event.ON_RESUME -> audioEngine.resumeForLifecycle()
                    else -> Unit
                }
            }
        )

        setContent {
            ZistNegarTheme {
                ZistNegarAppRoot(viewModel = viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::audioEngine.isInitialized) {
            audioEngine.release()
        }
    }
}
