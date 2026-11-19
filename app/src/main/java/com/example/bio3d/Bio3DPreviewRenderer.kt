package com.example.bio3d

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Generates glossy, Phong + Fresnel 3D-shaded offscreen renders for hero cards and 3D atlas previews
 * completely offline with zero internet dependency.
 */
object Bio3DPreviewRenderer {
    private val cache = ConcurrentHashMap<String, ImageBitmap>()

    private data class SphereObj(
        val x: Float, val y: Float, val z: Float, val r: Float,
        val cr: Float, val cg: Float, val cb: Float,
        val alpha: Float = 1f,
        val shininess: Float = 42f
    )

    suspend fun getPreviewBitmap(type: BioModelType, width: Int = 360, height: Int = 220): ImageBitmap {
        val key = "${type.id}_${width}x${height}"
        cache[key]?.let { return it }

        val bmp = withContext(Dispatchers.Default) {
            renderScene(type, width, height).asImageBitmap()
        }
        cache[key] = bmp
        return bmp
    }

    private fun renderScene(type: BioModelType, w: Int, h: Int): Bitmap {
        val pixels = IntArray(w * h)
        val spheres = buildSpheresFor(type)

        // Sort spheres far-to-near (larger z is closer in our view space or smaller z? Let's use camera at z = +4 looking toward 0)
        val sorted = spheres.sortedBy { it.z }

        // Key light direction (normalized)
        val lx = 0.48f; val ly = 0.65f; val lz = 0.59f

        val aspect = w.toFloat() / h.toFloat()
        for (py in 0 until h) {
            val ny = 1f - 2f * (py + 0.5f) / h.toFloat()
            for (px in 0 until w) {
                val nx = (2f * (px + 0.5f) / w.toFloat() - 1f) * aspect

                // Ambient lab radial glow background
                val dist2 = nx * nx * 0.45f + ny * ny * 0.6f
                val bgGlow = exp(-dist2 * 2.2f)
                var accR = 0.028f + bgGlow * 0.045f
                var accG = 0.052f + bgGlow * 0.135f
                var accB = 0.082f + bgGlow * 0.165f

                // Ray-sphere tracing in painter order for translucent layering
                for (s in sorted) {
                    val dx = nx - s.x
                    val dy = ny - s.y
                    val d2 = dx * dx + dy * dy
                    val r2 = s.r * s.r
                    if (d2 <= r2) {
                        val nz = sqrt(max(0f, r2 - d2)) / s.r
                        val snx = dx / s.r
                        val sny = dy / s.r

                        // Blinn-Phong + Fresnel rim
                        val diff = max(0f, snx * lx + sny * ly + nz * lz)
                        val hx = lx * 0.5f
                        val hy = ly * 0.5f
                        val hz = (lz + 1f) * 0.5f
                        val hLen = sqrt(hx * hx + hy * hy + hz * hz)
                        val specDot = max(0f, (snx * hx + sny * hy + nz * hz) / hLen)
                        val spec = specDot.pow(s.shininess) * 0.72f
                        val fresnel = (1f - nz).pow(2.5f)

                        val shadeR = s.cr * (0.24f + diff * 0.85f) + spec + fresnel * 0.35f
                        val shadeG = s.cg * (0.24f + diff * 0.85f) + spec + fresnel * 0.55f
                        val shadeB = s.cb * (0.24f + diff * 0.85f) + spec + fresnel * 0.62f

                        val effAlpha = (s.alpha + fresnel * (1f - s.alpha) * 0.65f).coerceIn(0.08f, 1f)
                        accR = accR * (1f - effAlpha) + shadeR * effAlpha
                        accG = accG * (1f - effAlpha) + shadeG * effAlpha
                        accB = accB * (1f - effAlpha) + shadeB * effAlpha
                    }
                }

                // Subtle DNA / wave scanline accent for extra lab polish
                val wave = 0.012f * sin(nx * 8f + ny * 4f) * exp(-abs(ny) * 2.5f)
                val ir = ((accR + wave).coerceIn(0f, 1f) * 255f).toInt()
                val ig = ((accG + wave * 2f).coerceIn(0f, 1f) * 255f).toInt()
                val ib = ((accB + wave * 2f).coerceIn(0f, 1f) * 255f).toInt()
                pixels[py * w + px] = (0xFF shl 24) or (ir shl 16) or (ig shl 8) or ib
            }
        }
        return Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888)
    }

    private fun buildSpheresFor(type: BioModelType): List<SphereObj> = when (type) {
        BioModelType.EUKARYOTIC_CELL -> listOf(
            SphereObj(0.55f, 0.28f, -0.3f, 0.16f, 0.98f, 0.75f, 0.15f), // mitochondrion
            SphereObj(-0.56f, -0.22f, -0.2f, 0.15f, 0.98f, 0.75f, 0.15f),
            SphereObj(-0.32f, 0.42f, 0.1f, 0.12f, 0.22f, 0.85f, 0.95f), // ER/Golgi
            SphereObj(0.36f, -0.38f, 0.2f, 0.11f, 0.96f, 0.42f, 0.55f), // lysosome
            SphereObj(0f, 0f, 0.15f, 0.34f, 0.65f, 0.48f, 0.98f, alpha = 0.78f), // nucleus
            SphereObj(0f, 0.02f, 0.28f, 0.13f, 0.98f, 0.80f, 0.25f), // nucleolus
            SphereObj(0f, 0f, 0.5f, 0.86f, 0.18f, 0.90f, 0.84f, alpha = 0.32f, shininess = 90f) // outer glass cell
        )

        BioModelType.PHOSPHOLIPID_BILAYER -> buildList {
            for (i in -4..4) {
                val x = i * 0.28f
                val yOff = 0.06f * sin(x * 3f)
                if (abs(x) < 0.25f) continue
                add(SphereObj(x, 0.38f + yOff, 0.2f, 0.12f, 0.18f, 0.90f, 0.84f))
                add(SphereObj(x, -0.38f + yOff, 0.2f, 0.12f, 0.18f, 0.90f, 0.84f))
                add(SphereObj(x, 0.14f + yOff, 0.05f, 0.065f, 0.98f, 0.75f, 0.15f))
                add(SphereObj(x, -0.14f + yOff, 0.05f, 0.065f, 0.98f, 0.75f, 0.15f))
            }
            add(SphereObj(0f, 0f, 0.3f, 0.28f, 0.65f, 0.50f, 0.98f, alpha = 0.92f))
            add(SphereObj(0.24f, 0.54f, 0.35f, 0.08f, 0.20f, 0.85f, 0.60f))
            add(SphereObj(0.35f, 0.70f, 0.38f, 0.07f, 0.20f, 0.85f, 0.60f))
        }

        BioModelType.MITOCHONDRION -> buildList {
            for (i in -3..3) {
                val x = i * 0.26f
                val y = if (i % 2 == 0) 0.14f else -0.14f
                add(SphereObj(x, y, 0.15f, 0.16f, 0.96f, 0.42f, 0.55f))
                add(SphereObj(x * 0.85f, -y * 0.5f, 0.25f, 0.07f, 0.98f, 0.78f, 0.15f))
            }
            add(SphereObj(-0.6f, 0f, 0.45f, 0.66f, 0.18f, 0.90f, 0.84f, alpha = 0.30f, shininess = 90f))
            add(SphereObj(0f, 0f, 0.48f, 0.72f, 0.18f, 0.90f, 0.84f, alpha = 0.28f, shininess = 90f))
            add(SphereObj(0.6f, 0f, 0.45f, 0.66f, 0.18f, 0.90f, 0.84f, alpha = 0.30f, shininess = 90f))
        }

        BioModelType.CHLOROPLAST -> buildList {
            val cols = listOf(-0.55f, -0.18f, 0.18f, 0.55f)
            cols.forEach { cx ->
                for (row in -1..1) {
                    add(SphereObj(cx, row * 0.16f, 0.2f, 0.14f, 0.18f, 0.86f, 0.56f))
                }
            }
            add(SphereObj(0f, 0.28f, 0.25f, 0.10f, 0.98f, 0.78f, 0.18f))
            add(SphereObj(-0.35f, 0f, 0.46f, 0.74f, 0.20f, 0.88f, 0.75f, alpha = 0.28f, shininess = 90f))
            add(SphereObj(0.35f, 0f, 0.46f, 0.74f, 0.20f, 0.88f, 0.75f, alpha = 0.28f, shininess = 90f))
        }

        BioModelType.DNA_HELIX -> buildList {
            for (i in 0..16) {
                val t = i / 16f
                val x = -1.15f + t * 2.3f
                val a = t * 2.4f * PI.toFloat()
                val y1 = 0.42f * sin(a)
                val z1 = 0.35f * cos(a)
                val y2 = -y1
                val z2 = -z1
                add(SphereObj(x, y1, z1 + 0.2f, 0.11f, 0.18f, 0.90f, 0.84f))
                add(SphereObj(x, y2, z2 + 0.2f, 0.11f, 0.25f, 0.68f, 0.98f))
                if (i % 2 == 0) {
                    add(SphereObj(x, 0f, (z1 + z2) * 0.5f + 0.2f, 0.08f, 0.98f, 0.75f, 0.16f))
                }
            }
        }

        BioModelType.BIOSYSTEM_LEVELS -> listOf(
            SphereObj(0f, 0f, 0.65f, 0.86f, 0.48f, 0.62f, 0.98f, alpha = 0.24f, shininess = 92f),
            SphereObj(0f, 0f, 0.45f, 0.62f, 0.18f, 0.90f, 0.84f, alpha = 0.34f, shininess = 80f),
            SphereObj(0f, 0f, 0.25f, 0.40f, 0.20f, 0.83f, 0.60f, alpha = 0.56f),
            SphereObj(0f, 0f, 0.10f, 0.20f, 0.98f, 0.75f, 0.15f, alpha = 1f),
            SphereObj(-0.58f, 0.45f, 0.5f, 0.09f, 0.20f, 0.90f, 0.85f),
            SphereObj(0.62f, -0.38f, 0.5f, 0.09f, 0.98f, 0.75f, 0.15f)
        )
    }
}
