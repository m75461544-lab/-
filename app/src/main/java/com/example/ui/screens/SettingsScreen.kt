package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.local.UserSettings
import com.example.ui.components.BadgeChip
import com.example.ui.components.LabGlassCard
import com.example.ui.components.LabTopStatusBar
import com.example.ui.theme.AtpGold
import com.example.ui.theme.BioCyan
import com.example.ui.theme.ChloroplastEmerald
import com.example.ui.theme.CoralTrap
import com.example.ui.theme.LabSurfaceElevated
import com.example.ui.theme.ObsidianBg
import com.example.ui.viewmodel.SyncUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SettingsScreen(
    settings: UserSettings,
    totalLocalQuestions: Int,
    syncState: SyncUiState,
    onTogglePiano: (Boolean) -> Unit,
    onToggleSfx: (Boolean) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onCheckUpdate: (String) -> Unit,
    onDismissSyncMessage: () -> Unit,
    onClearProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    var urlInput by rememberSaveable(settings.updateJsonUrl) {
        mutableStateOf(settings.updateJsonUrl)
    }
    var showSchemaHint by rememberSaveable { mutableStateOf(false) }
    var showResetConfirm by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                LabTopStatusBar(
                    title = "تنظیمات و بروزرسانی",
                    pianoEnabled = settings.pianoEnabled,
                    onTogglePiano = { onTogglePiano(!settings.pianoEnabled) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Relaxing Piano Audio Controls
            item {
                LabGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.MusicNote,
                            contentDescription = null,
                            tint = BioCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تنظیمات موسیقی آرامش‌بخش پیانو (آفلاین)",
                            style = MaterialTheme.typography.titleMedium,
                            color = BioCyan
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "نوای پیانوی پس‌زمینه",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "تولید زنده و محلی آکوردهای آرام‌بخش پیانو بدون نیاز به اینترنت",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.pianoEnabled,
                            onCheckedChange = onTogglePiano,
                            modifier = Modifier.testTag("switch_piano_music")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "صدای ملایم پاسخ درست و غلط",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Switch(
                            checked = settings.sfxEnabled,
                            onCheckedChange = onToggleSfx,
                            modifier = Modifier.testTag("switch_sfx")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "میزان صدا: ${(settings.audioVolume * 100).toInt()}٪",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = settings.audioVolume,
                        onValueChange = onVolumeChange,
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(thumbColor = BioCyan, activeTrackColor = BioCyan),
                        modifier = Modifier.testTag("slider_audio_volume")
                    )
                }
            }

            // Optional Online JSON Pack Update Card
            item {
                LabGlassCard(
                    borderColor = ChloroplastEmerald.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.CloudDownload,
                                contentDescription = null,
                                tint = ChloroplastEmerald,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "بروزرسانی اختیاری بسته سوالات",
                                style = MaterialTheme.typography.titleMedium,
                                color = ChloroplastEmerald
                            )
                        }
                        BadgeChip(
                            text = "نسخه ${settings.packVersion}",
                            textColor = AtpGold,
                            containerColor = AtpGold.copy(alpha = 0.16f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "تمام امکانات و $totalLocalQuestions سوال فعلی به صورت کاملاً محلی روی گوشی اجرا می‌شوند. فقط در صورت نیاز و اتصال به اینترنت می‌توانید از نشانی JSON دلخواه سوالات جدید دریافت کنید.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (settings.lastSyncTimestamp > 0L) {
                        val fmt = remember(settings.lastSyncTimestamp) {
                            SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale.getDefault())
                                .format(Date(settings.lastSyncTimestamp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "آخرین بروزرسانی موفق: $fmt",
                            style = MaterialTheme.typography.labelSmall,
                            color = BioCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = {
                            urlInput = it
                            onDismissSyncMessage()
                        },
                        label = { Text("نشانی لینک JSON بسته سوالات (HTTPS)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_json_update_url")
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onCheckUpdate(urlInput) },
                        enabled = syncState !is SyncUiState.Loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ChloroplastEmerald,
                            contentColor = ObsidianBg
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_check_json_update")
                    ) {
                        if (syncState is SyncUiState.Loading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = ObsidianBg,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("در حال بررسی و دریافت...")
                        } else {
                            Text(stringResource(R.string.update_button_label))
                        }
                    }

                    AnimatedVisibility(visible = syncState !is SyncUiState.Idle && syncState !is SyncUiState.Loading) {
                        val (msg, accent) = when (syncState) {
                            is SyncUiState.Success -> syncState.message to ChloroplastEmerald
                            is SyncUiState.Offline -> syncState.message to AtpGold
                            is SyncUiState.Error -> syncState.message to CoralTrap
                            else -> "" to BioCyan
                        }
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = accent.copy(alpha = 0.14f),
                            border = BorderStroke(1.dp, accent.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (syncState is SyncUiState.Offline) {
                                    Icon(
                                        imageVector = Icons.Rounded.WifiOff,
                                        contentDescription = null,
                                        tint = accent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = accent
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(
                        onClick = { showSchemaHint = !showSchemaHint },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(if (showSchemaHint) "بستن راهنمای فرمت JSON" else "نمایش نمونه ساختار JSON بسته سوالات")
                    }
                    AnimatedVisibility(visible = showSchemaHint) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = LabSurfaceElevated,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "{\n  \"version\": 2,\n  \"packTitle\": \"بسته تکمیلی زیست دهم\",\n  \"questions\": [\n    {\n      \"id\": \"ch1_l1_extra1\",\n      \"lesson\": 1,\n      \"difficulty\": \"دام کنکوری\",\n      \"questionType\": \"مفهومی\",\n      \"prompt\": \"...\",\n      \"optionA\": \"...\", \"optionB\": \"...\",\n      \"optionC\": \"...\", \"optionD\": \"...\",\n      \"correctIndex\": 0,\n      \"explanation\": \"...\",\n      \"modelType\": \"EUKARYOTIC_CELL\",\n      \"highlightedPartId\": \"nucleus\",\n      \"remoteImageUrl\": null,\n      \"figure2dNote\": null\n    }\n  ]\n}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            // Reset Local Stats
            item {
                LabGlassCard(
                    borderColor = CoralTrap.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "مدیریت سوابق محلی",
                        style = MaterialTheme.typography.titleSmall,
                        color = CoralTrap
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "پاک‌سازی تاریخچه آزمون‌ها و آمار پاسخ‌های ثبت‌شده (بانک سوالات آفلاین حفظ می‌شود).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { showResetConfirm = true },
                        border = BorderStroke(1.dp, CoralTrap.copy(alpha = 0.7f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CoralTrap),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_reset_local_stats")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteSweep,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("پاک‌سازی کارنامه و آمار پاسخ‌ها")
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("پاک‌سازی سوابق") },
            text = { Text("آیا مطمئن هستید که می‌خواهید تاریخچه آزمون‌ها و درصدهای ثبت‌شده پاک شوند؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearProgress()
                        showResetConfirm = false
                    }
                ) {
                    Text("بله، پاک شود", color = CoralTrap)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("انصراف")
                }
            }
        )
    }
}
