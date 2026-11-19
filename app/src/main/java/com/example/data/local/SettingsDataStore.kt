package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "zist_negar_settings")

data class UserSettings(
    val pianoEnabled: Boolean = true,
    val sfxEnabled: Boolean = true,
    val audioVolume: Float = 0.55f,
    val updateJsonUrl: String = "https://example.com/zist10-ch1-pack.json",
    val packVersion: Int = 1,
    val lastSyncTimestamp: Long = 0L,
    val languageCode: String = "fa"
)

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val PIANO_ENABLED = booleanPreferencesKey("piano_enabled")
        val SFX_ENABLED = booleanPreferencesKey("sfx_enabled")
        val AUDIO_VOLUME = floatPreferencesKey("audio_volume")
        val UPDATE_JSON_URL = stringPreferencesKey("update_json_url")
        val PACK_VERSION = intPreferencesKey("pack_version")
        val LAST_SYNC_TS = longPreferencesKey("last_sync_ts")
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
    }

    val settingsFlow: Flow<UserSettings> = context.settingsDataStore.data.map { prefs ->
        UserSettings(
            pianoEnabled = prefs[Keys.PIANO_ENABLED] ?: true,
            sfxEnabled = prefs[Keys.SFX_ENABLED] ?: true,
            audioVolume = prefs[Keys.AUDIO_VOLUME] ?: 0.55f,
            updateJsonUrl = prefs[Keys.UPDATE_JSON_URL]
                ?: "https://example.com/zist10-ch1-pack.json",
            packVersion = prefs[Keys.PACK_VERSION] ?: 1,
            lastSyncTimestamp = prefs[Keys.LAST_SYNC_TS] ?: 0L,
            languageCode = prefs[Keys.LANGUAGE_CODE] ?: "fa"
        )
    }

    suspend fun setPianoEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.PIANO_ENABLED] = enabled }
    }

    suspend fun setSfxEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.SFX_ENABLED] = enabled }
    }

    suspend fun setAudioVolume(volume: Float) {
        context.settingsDataStore.edit { it[Keys.AUDIO_VOLUME] = volume.coerceIn(0f, 1f) }
    }

    suspend fun setUpdateJsonUrl(url: String) {
        context.settingsDataStore.edit { it[Keys.UPDATE_JSON_URL] = url.trim() }
    }

    suspend fun recordPackUpdate(version: Int, timestamp: Long) {
        context.settingsDataStore.edit {
            it[Keys.PACK_VERSION] = version
            it[Keys.LAST_SYNC_TS] = timestamp
        }
    }

    suspend fun setLanguageCode(code: String) {
        context.settingsDataStore.edit { it[Keys.LANGUAGE_CODE] = code }
    }
}
