package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.ZoomIn
import androidx.compose.material.icons.rounded.ZoomOut
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.bio3d.Bio3DPreviewRenderer
import com.example.bio3d.Bio3DSurfaceView
import com.example.bio3d.BioModelCatalog
import com.example.bio3d.BioModelType
import com.example.bio3d.BioPartInfo
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedMeta
import com.example.ui.i18n.localizedPartsFor
import com.example.ui.theme.AtpGold
import com.example.ui.theme.BioCyan
import com.example.ui.theme.BioCyanContainer
import com.example.ui.theme.ChloroplastEmerald
import com.example.ui.theme.LabBorder
import com.example.ui.theme.LabSurface
import com.example.ui.theme.LabSurfaceElevated
import com.example.ui.theme.ObsidianBg

@Composable
fun Bio3DInspectorCard(
    modelType: BioModelType,
    xrayLevel: Float,
    highlightedPartId: String?,
    autoRotate: Boolean,
    zoomDistance: Float,
    resetCounter: Int,
    onXrayChange: (Float) -> Unit,
    onSelectPart: (String) -> Unit,
    onToggleAutoRotate: () -> Unit,
    onZoomChange: (Float) -> Unit,
    onResetCamera: () -> Unit,
    remoteImageUrl: String? = null,
    viewportHeight: Dp = 250.dp,
    show2DNoteIfPresent: String? = null,
    modifier: Modifier = Modifier
) {
    var showRendered3DIllustration by remember(modelType) { mutableStateOf(false) }
    var isFullscreen by remember { mutableStateOf(false) }

    val strings = LocalAppStrings.current
    val language = LocalAppLanguage.current
    val meta = remember(modelType, language) { modelType.localizedMeta(language) }
    val parts = remember(modelType, language) { BioModelCatalog.localizedPartsFor(modelType, language) }
    val selectedPart: BioPartInfo? = remember(parts, highlightedPartId) {
        parts.firstOrNull { it.id == highlightedPartId }
    }

    val previewBitmap by produceState<ImageBitmap?>(initialValue = null, modelType) {
        value = Bio3DPreviewRenderer.getPreviewBitmap(modelType, 520, 320)
    }

    LabGlassCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp)
    ) {
        // Header row: Model title + Mode switcher (Interactive 3D vs Rendered 3D Illustration)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meta.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = BioCyan
                )
                Text(
                    text = meta.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                onClick = { showRendered3DIllustration = !showRendered3DIllustration },
                shape = RoundedCornerShape(12.dp),
                color = BioCyanContainer,
                border = BorderStroke(1.dp, BioCyan.copy(alpha = 0.45f)),
                modifier = Modifier.testTag("toggle_3d_render_mode")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (showRendered3DIllustration) Icons.Rounded.ViewInAr else Icons.Rounded.Visibility,
                        contentDescription = null,
                        tint = BioCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (showRendered3DIllustration) strings.switchTo3dModel else strings.switchTo3dRender,
                        style = MaterialTheme.typography.labelSmall,
                        color = BioCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3D Viewport Frame
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(viewportHeight)
                .clip(MaterialTheme.shapes.medium)
                .background(ObsidianBg)
        ) {
            if (!showRendered3DIllustration) {
                Bio3DSurfaceView(
                    modelType = modelType,
                    xrayLevel = xrayLevel,
                    highlightedPartId = highlightedPartId,
                    autoRotate = autoRotate,
                    zoomDistance = zoomDistance,
                    onZoomChange = onZoomChange,
                    resetTrigger = resetCounter,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Zoomable3DIllustrationPane(
                    remoteImageUrl = remoteImageUrl,
                    fallbackBitmap = previewBitmap,
                    contentDescription = meta.title
                )
            }

            // Floating Top-Start Hint Pill
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(50),
                color = ObsidianBg.copy(alpha = 0.72f),
                border = BorderStroke(1.dp, LabBorder)
            ) {
                Text(
                    text = if (showRendered3DIllustration) strings.hintPinchZoom else strings.hintDragRotate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            // Floating Control Cluster (Zoom, Auto-Rotate, Reset, Fullscreen)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (!showRendered3DIllustration) {
                    SmallGlassIconButton(
                        icon = Icons.Rounded.ZoomIn,
                        description = strings.descZoomIn,
                        testTag = "btn_3d_zoom_in",
                        onClick = { onZoomChange((zoomDistance - 0.55f).coerceIn(2.2f, 6.2f)) }
                    )
                    SmallGlassIconButton(
                        icon = Icons.Rounded.ZoomOut,
                        description = strings.descZoomOut,
                        testTag = "btn_3d_zoom_out",
                        onClick = { onZoomChange((zoomDistance + 0.55f).coerceIn(2.2f, 6.2f)) }
                    )
                    SmallGlassIconButton(
                        icon = if (autoRotate) Icons.Rounded.PauseCircle else Icons.Rounded.PlayCircle,
                        description = strings.descAutoRotate,
                        testTag = "btn_3d_autorotate",
                        tint = if (autoRotate) BioCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = onToggleAutoRotate
                    )
                    SmallGlassIconButton(
                        icon = Icons.Rounded.CenterFocusStrong,
                        description = strings.descResetAngle,
                        testTag = "btn_3d_reset",
                        onClick = onResetCamera
                    )
                }
                SmallGlassIconButton(
                    icon = Icons.Rounded.Fullscreen,
                    description = strings.descFullscreen3d,
                    testTag = "btn_3d_fullscreen",
                    tint = AtpGold,
                    onClick = { isFullscreen = true }
                )
            }
        }

        // X-Ray Transparency Cutaway Slider (only for live OpenGL 3D mode)
        if (!showRendered3DIllustration) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Layers,
                    contentDescription = null,
                    tint = BioCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = strings.xrayLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${(xrayLevel * 100).toInt()}${strings.percentSymbol}",
                    style = MaterialTheme.typography.labelMedium,
                    color = BioCyan
                )
            }
            Slider(
                value = xrayLevel,
                onValueChange = onXrayChange,
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = BioCyan,
                    activeTrackColor = BioCyan,
                    inactiveTrackColor = LabSurfaceElevated
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("xray_transparency_slider")
            )
        }

        // Internal Parts Selector Chips
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = strings.internalPartsTitle,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(parts, key = { it.id }) { part ->
                val selected = highlightedPartId == part.id
                FilterChip(
                    selected = selected,
                    onClick = { onSelectPart(part.id) },
                    label = {
                        Text(
                            text = part.nameFa,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(part.colorHex))
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(part.colorHex).copy(alpha = 0.22f),
                        selectedLabelColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.testTag("part_chip_${part.id}")
                )
            }
        }

        // Selected Part Anatomical & Konkur Trap Box
        AnimatedVisibility(visible = selectedPart != null) {
            selectedPart?.let { part ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = LabSurfaceElevated,
                    border = BorderStroke(1.dp, Color(part.colorHex).copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = part.nameFa,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(part.colorHex)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = part.roleFa,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = part.konkurTrapFa,
                            style = MaterialTheme.typography.bodySmall,
                            color = AtpGold
                        )
                    }
                }
            }
        }

        if (!show2DNoteIfPresent.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Figure2DExplanationBox(note = show2DNoteIfPresent)
        }
    }

    // Fullscreen 3D Inspection Modal
    if (isFullscreen) {
        var modalReset by remember { mutableIntStateOf(resetCounter) }
        Dialog(
            onDismissRequest = { isFullscreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = ObsidianBg
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = meta.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = BioCyan
                            )
                            Text(
                                text = strings.fullscreenSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = ChloroplastEmerald
                            )
                        }
                        IconButton(
                            onClick = { isFullscreen = false },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = strings.closeFullscreenDesc,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Bio3DSurfaceView(
                            modelType = modelType,
                            xrayLevel = xrayLevel,
                            highlightedPartId = highlightedPartId,
                            autoRotate = autoRotate,
                            zoomDistance = zoomDistance,
                            onZoomChange = onZoomChange,
                            resetTrigger = modalReset,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LabSurface)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${strings.xrayLabel}: ${(xrayLevel * 100).toInt()}${strings.percentSymbol}",
                                style = MaterialTheme.typography.labelMedium,
                                color = BioCyan,
                                modifier = Modifier.weight(1f)
                            )
                            SmallGlassIconButton(
                                icon = Icons.Rounded.CenterFocusStrong,
                                description = strings.resetCameraDesc,
                                testTag = "modal_3d_reset",
                                onClick = {
                                    onResetCamera()
                                    modalReset++
                                }
                            )
                        }
                        Slider(
                            value = xrayLevel,
                            onValueChange = onXrayChange,
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(thumbColor = BioCyan, activeTrackColor = BioCyan)
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(parts, key = { it.id }) { part ->
                                FilterChip(
                                    selected = highlightedPartId == part.id,
                                    onClick = { onSelectPart(part.id) },
                                    label = { Text(part.nameFa, style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Zoomable3DIllustrationPane(
    remoteImageUrl: String?,
    fallbackBitmap: ImageBitmap?,
    contentDescription: String
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val nextScale = (scale * zoom).coerceIn(1f, 3.2f)
                    scale = nextScale
                    if (nextScale > 1f) {
                        offsetX += pan.x
                        offsetY += pan.y
                    } else {
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        val mod = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            )
        if (!remoteImageUrl.isNullOrBlank()) {
            AsyncImage(
                model = remoteImageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = mod
            )
        } else if (fallbackBitmap != null) {
            Image(
                bitmap = fallbackBitmap,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = mod
            )
        }
    }
}

@Composable
private fun SmallGlassIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    testTag: String,
    tint: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = ObsidianBg.copy(alpha = 0.78f),
        border = BorderStroke(1.dp, LabBorder)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(48.dp)
                .testTag(testTag)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
