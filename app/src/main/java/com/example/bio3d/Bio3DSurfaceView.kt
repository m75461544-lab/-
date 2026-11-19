package com.example.bio3d

import android.opengl.GLSurfaceView
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun Bio3DSurfaceView(
    modelType: BioModelType,
    xrayLevel: Float,
    highlightedPartId: String?,
    autoRotate: Boolean,
    zoomDistance: Float,
    onZoomChange: (Float) -> Unit,
    resetTrigger: Int = 0,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val renderer = remember { Bio3DRenderer() }
    val glSurfaceView = remember {
        GLSurfaceView(context).apply {
            setEGLContextClientVersion(2)
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            setRenderer(renderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
    }

    LaunchedEffect(modelType) {
        renderer.setModelType(modelType)
    }

    LaunchedEffect(xrayLevel, highlightedPartId, autoRotate, zoomDistance) {
        renderer.xrayLevel = xrayLevel
        renderer.highlightedPartId = highlightedPartId
        renderer.autoRotate = autoRotate
        renderer.zoomDistance = zoomDistance
    }

    LaunchedEffect(resetTrigger) {
        if (resetTrigger > 0) {
            renderer.rotationX = 18f
            renderer.rotationY = -24f
        }
    }

    DisposableEffect(glSurfaceView) {
        glSurfaceView.onResume()
        onDispose {
            glSurfaceView.onPause()
        }
    }

    Box(
        modifier = modifier
            .testTag("bio_3d_surface")
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    if (zoom != 1f) {
                        val nextZoom = (renderer.zoomDistance / zoom).coerceIn(2.2f, 6.2f)
                        renderer.zoomDistance = nextZoom
                        onZoomChange(nextZoom)
                    }
                    renderer.rotationY = (renderer.rotationY + pan.x * 0.42f) % 360f
                    renderer.rotationX = (renderer.rotationX + pan.y * 0.42f).coerceIn(-85f, 85f)
                }
            }
    ) {
        AndroidView(
            factory = {
                glSurfaceView.apply {
                    setOnTouchListener { v, event ->
                        v.parent?.requestDisallowInterceptTouchEvent(true)
                        false
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
