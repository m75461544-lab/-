package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.bio3d.BioModelType
import com.example.ui.components.Bio3DInspectorCard
import com.example.ui.components.LabGlassCard
import com.example.ui.components.LabTopStatusBar
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedMeta
import com.example.ui.theme.BioCyan
import com.example.ui.theme.BioCyanContainer
import com.example.ui.theme.ChloroplastEmerald
import com.example.ui.theme.ObsidianBg
import com.example.ui.viewmodel.BioLab3DState

@Composable
fun BioLab3DScreen(
    labState: BioLab3DState,
    pianoEnabled: Boolean,
    onTogglePiano: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectModel: (BioModelType) -> Unit,
    onXrayChange: (Float) -> Unit,
    onSelectPart: (String) -> Unit,
    onToggleAutoRotate: () -> Unit,
    onZoomChange: (Float) -> Unit,
    onResetCamera: () -> Unit,
    onPracticeQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val language = LocalAppLanguage.current
    val activeMeta = labState.activeModel.localizedMeta(language)

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
                    title = strings.lab3dScreenTitle,
                    pianoEnabled = pianoEnabled,
                    onTogglePiano = onTogglePiano,
                    onSelectLanguage = onSelectLanguage,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Model Switcher
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(BioModelType.entries, key = { it.id }) { model ->
                        val modelMeta = model.localizedMeta(language)
                        FilterChip(
                            selected = labState.activeModel == model,
                            onClick = { onSelectModel(model) },
                            label = {
                                Text(
                                    text = modelMeta.title,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BioCyanContainer,
                                selectedLabelColor = BioCyan
                            ),
                            modifier = Modifier.testTag("lab_model_tab_${model.id}")
                        )
                    }
                }
            }

            // Main Interactive 3D Cutaway Viewer
            item {
                Bio3DInspectorCard(
                    modelType = labState.activeModel,
                    xrayLevel = labState.xrayLevel,
                    highlightedPartId = labState.highlightedPartId,
                    autoRotate = labState.autoRotate,
                    zoomDistance = labState.zoomDistance,
                    resetCounter = labState.resetCounter,
                    onXrayChange = onXrayChange,
                    onSelectPart = onSelectPart,
                    onToggleAutoRotate = onToggleAutoRotate,
                    onZoomChange = onZoomChange,
                    onResetCamera = onResetCamera,
                    viewportHeight = 320.dp,
                    show2DNoteIfPresent = activeMeta.schematic2DNote
                )
            }

            // Detailed Summary Card
            item {
                LabGlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = ChloroplastEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${strings.spatialAnalysisTitle} (${activeMeta.lessonTag})",
                            style = MaterialTheme.typography.titleSmall,
                            color = ChloroplastEmerald
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = activeMeta.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onPracticeQuiz,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BioCyan,
                            contentColor = ObsidianBg
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("lab3d_practice_quiz_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Quiz,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.practiceChapter1Button,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
    }
}
