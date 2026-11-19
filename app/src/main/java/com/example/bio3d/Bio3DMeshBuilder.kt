package com.example.bio3d

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class MeshPart(
    val partId: String,
    val vertices: FloatArray, // [px, py, pz, nx, ny, nz, ...]
    val indices: ShortArray,
    val r: Float,
    val g: Float,
    val b: Float,
    val isOuterShell: Boolean = false,
    val baseAlpha: Float = 1.0f,
    val shininess: Float = 48.0f
)

object Bio3DMeshBuilder {

    fun buildModel(type: BioModelType): List<MeshPart> = when (type) {
        BioModelType.EUKARYOTIC_CELL -> buildEukaryoticCell()
        BioModelType.PHOSPHOLIPID_BILAYER -> buildPhospholipidBilayer()
        BioModelType.MITOCHONDRION -> buildMitochondrion()
        BioModelType.CHLOROPLAST -> buildChloroplast()
        BioModelType.DNA_HELIX -> buildDnaHelix()
        BioModelType.BIOSYSTEM_LEVELS -> buildBiosystemLevels()
    }

    private fun buildEukaryoticCell(): List<MeshPart> {
        val parts = mutableListOf<MeshPart>()

        // 1. Central Nucleolus + Chromatin inside Nucleus
        parts += createEllipsoid("nucleus", 0f, 0.05f, 0f, 0.16f, 0.16f, 0.16f, 22, 28, 0.98f, 0.78f, 0.25f, shininess = 64f)
        val chromatin = mutableListOf<FloatArray>()
        for (i in 0..40) {
            val t = i / 40f * 4f * PI.toFloat()
            chromatin += floatArrayOf(
                0.22f * cos(t) * sin(t * 0.6f),
                0.05f + 0.22f * cos(t * 0.7f),
                0.22f * sin(t)
            )
        }
        parts += createTube("nucleus", chromatin, 0.022f, 10, 0.85f, 0.62f, 1.0f)

        // Inner nuclear envelope (translucent when xray is high, semi-opaque normally)
        parts += createEllipsoid("nucleus", 0f, 0.05f, 0f, 0.44f, 0.42f, 0.44f, 28, 36, 0.65f, 0.48f, 0.98f, isOuterShell = true, baseAlpha = 0.68f, shininess = 72f)

        // 2. Endoplasmic Reticulum folds wrapping around the nucleus
        for (layer in 0..2) {
            val rad = 0.56f + layer * 0.12f
            val erPts = mutableListOf<FloatArray>()
            for (i in 0..28) {
                val a = -1.1f + (i / 28f) * 2.5f
                val wave = 0.05f * sin(i * 0.9f)
                erPts += floatArrayOf(
                    rad * cos(a),
                    -0.12f + layer * 0.13f + wave,
                    rad * sin(a)
                )
            }
            parts += createTube("er_golgi", erPts, 0.042f, 12, 0.22f, 0.74f, 0.97f, shininess = 56f)
        }

        // Golgi Apparatus stacked cisternae on opposite side
        for (stack in 0..3) {
            val gPts = mutableListOf<FloatArray>()
            val baseR = 0.62f + stack * 0.085f
            for (i in 0..20) {
                val a = 2.0f + (i / 20f) * 1.45f
                gPts += floatArrayOf(
                    baseR * cos(a),
                    -0.25f + stack * 0.06f,
                    baseR * sin(a)
                )
            }
            parts += createTube("er_golgi", gPts, 0.036f, 12, 0.18f, 0.90f, 0.84f, shininess = 60f)
        }
        // Golgi vesicles
        parts += createEllipsoid("er_golgi", -0.78f, 0.08f, 0.28f, 0.06f, 0.06f, 0.06f, 14, 18, 0.28f, 0.95f, 0.88f)
        parts += createEllipsoid("er_golgi", -0.70f, -0.18f, 0.42f, 0.055f, 0.055f, 0.055f, 14, 18, 0.28f, 0.95f, 0.88f)

        // 3. Mitochondria (3D capsules at varied orientations)
        val mitoConfigs = listOf(
            floatArrayOf(0.68f, -0.45f, 0.35f, 0.24f, 0.13f, 0.13f),
            floatArrayOf(-0.62f, 0.52f, -0.32f, 0.14f, 0.23f, 0.14f),
            floatArrayOf(0.25f, 0.62f, 0.52f, 0.22f, 0.12f, 0.13f)
        )
        mitoConfigs.forEach { cfg ->
            parts += createEllipsoid("mitochondria", cfg[0], cfg[1], cfg[2], cfg[3], cfg[4], cfg[5], 20, 26, 0.98f, 0.72f, 0.15f, shininess = 64f)
        }

        // 4. Centrosome (perpendicular centriole pair) + Lysosomes
        parts += createCylinder("centrosome", -0.38f, -0.52f, 0.48f, -0.38f, -0.30f, 0.48f, 0.055f, 0.055f, 16, 0.98f, 0.44f, 0.52f)
        parts += createCylinder("centrosome", -0.49f, -0.41f, 0.58f, -0.27f, -0.41f, 0.58f, 0.055f, 0.055f, 16, 0.98f, 0.44f, 0.52f)
        parts += createEllipsoid("centrosome", 0.55f, 0.38f, -0.55f, 0.11f, 0.11f, 0.11f, 16, 20, 0.96f, 0.42f, 0.55f)
        parts += createEllipsoid("centrosome", -0.18f, -0.75f, -0.35f, 0.10f, 0.10f, 0.10f, 16, 20, 0.96f, 0.42f, 0.55f)

        // 5. Outer Plasma Membrane (Translucent Glass Shell)
        parts += createEllipsoid("membrane", 0f, 0f, 0f, 1.32f, 1.24f, 1.32f, 34, 44, 0.18f, 0.90f, 0.84f, isOuterShell = true, baseAlpha = 0.38f, shininess = 96f)

        return parts
    }

    private fun buildPhospholipidBilayer(): List<MeshPart> {
        val parts = mutableListOf<MeshPart>()
        val grid = -2..2
        val spacing = 0.40f

        // 1. Transmembrane Channel Protein Barrel in center (0,0,0)
        for (i in 0 until 6) {
            val angle = (i / 6.0f) * 2f * PI.toFloat()
            val px = 0.24f * cos(angle)
            val pz = 0.24f * sin(angle)
            parts += createEllipsoid("channel_protein", px, 0f, pz, 0.15f, 0.62f, 0.15f, 18, 22, 0.65f, 0.55f, 0.98f, shininess = 70f)
        }

        // 2. Glycoprotein carbohydrate chains branching off extracellular top
        val glycoPts1 = listOf(
            floatArrayOf(0.24f, 0.55f, 0.0f),
            floatArrayOf(0.32f, 0.78f, 0.08f),
            floatArrayOf(0.45f, 0.98f, 0.15f)
        )
        val glycoPts2 = listOf(
            floatArrayOf(0.32f, 0.78f, 0.08f),
            floatArrayOf(0.18f, 0.96f, 0.22f)
        )
        val glycoPts3 = listOf(
            floatArrayOf(-0.80f, 0.44f, 0.40f),
            floatArrayOf(-0.86f, 0.68f, 0.48f),
            floatArrayOf(-0.98f, 0.86f, 0.52f)
        )
        parts += createTube("glycocalyx", glycoPts1, 0.032f, 10, 0.20f, 0.83f, 0.60f)
        parts += createTube("glycocalyx", glycoPts2, 0.028f, 10, 0.20f, 0.83f, 0.60f)
        parts += createTube("glycocalyx", glycoPts3, 0.032f, 10, 0.20f, 0.83f, 0.60f)
        parts += createEllipsoid("glycocalyx", 0.45f, 0.98f, 0.15f, 0.055f, 0.055f, 0.055f, 12, 14, 0.25f, 0.92f, 0.65f)
        parts += createEllipsoid("glycocalyx", 0.18f, 0.96f, 0.22f, 0.055f, 0.055f, 0.055f, 12, 14, 0.25f, 0.92f, 0.65f)
        parts += createEllipsoid("glycocalyx", -0.98f, 0.86f, 0.52f, 0.055f, 0.055f, 0.055f, 12, 14, 0.25f, 0.92f, 0.65f)

        // 3. Phospholipid Heads (top & bottom leaflets) and Hydrophobic Tails
        for (gx in grid) {
            for (gz in grid) {
                val x = gx * spacing
                val z = gz * spacing
                // Skip center where transmembrane channel protein sits
                if (sqrt(x * x + z * z) < 0.38f) continue

                val waveY = 0.04f * sin(x * 2.5f + z * 2.0f)
                val topY = 0.45f + waveY
                val botY = -0.45f + waveY

                // Outer hydrophilic heads (semi-translucent when X-Ray enabled so tails/proteins shine)
                parts += createEllipsoid("heads", x, topY, z, 0.105f, 0.105f, 0.105f, 14, 18, 0.18f, 0.90f, 0.84f, isOuterShell = true, baseAlpha = 0.88f, shininess = 72f)
                parts += createEllipsoid("heads", x, botY, z, 0.105f, 0.105f, 0.105f, 14, 18, 0.18f, 0.90f, 0.84f, isOuterShell = true, baseAlpha = 0.88f, shininess = 72f)

                // Dual wavy hydrophobic fatty acid tails
                parts += createCylinder("tails", x - 0.04f, topY - 0.08f, z, x - 0.06f, 0.05f + waveY, z + 0.02f, 0.024f, 0.020f, 8, 0.98f, 0.75f, 0.14f)
                parts += createCylinder("tails", x + 0.04f, topY - 0.08f, z, x + 0.05f, 0.05f + waveY, z - 0.02f, 0.024f, 0.020f, 8, 0.98f, 0.75f, 0.14f)
                parts += createCylinder("tails", x - 0.04f, botY + 0.08f, z, x - 0.05f, -0.05f + waveY, z - 0.02f, 0.024f, 0.020f, 8, 0.98f, 0.75f, 0.14f)
                parts += createCylinder("tails", x + 0.04f, botY + 0.08f, z, x + 0.06f, -0.05f + waveY, z + 0.02f, 0.024f, 0.020f, 8, 0.98f, 0.75f, 0.14f)
            }
        }
        return parts
    }

    private fun buildMitochondrion(): List<MeshPart> {
        val parts = mutableListOf<MeshPart>()

        // 1. Convoluted Inner Membrane (Cristae) labyrinth
        val cristaePts = mutableListOf<FloatArray>()
        val steps = 80
        for (i in 0..steps) {
            val t = i / steps.toFloat()
            val angle = t * 2f * PI.toFloat()
            val cristaeWave = 0.24f * sin(angle * 7f)
            val rx = (0.78f - cristaeWave) * cos(angle)
            val rz = (0.38f - cristaeWave * 0.55f) * sin(angle)
            val ry = 0.08f * sin(angle * 3f)
            cristaePts += floatArrayOf(rx, ry, rz)
        }
        parts += createTube("cristae", cristaePts, 0.085f, 16, 0.96f, 0.42f, 0.55f, shininess = 68f)

        // 2. Golden ATP Synthase Complexes along cristae
        for (i in 0 until 12) {
            val a = (i / 12f) * 2f * PI.toFloat()
            val px = 0.52f * cos(a)
            val pz = 0.24f * sin(a)
            val py = if (i % 2 == 0) 0.11f else -0.11f
            parts += createEllipsoid("atp_synthase", px, py, pz, 0.055f, 0.055f, 0.055f, 12, 14, 0.98f, 0.78f, 0.15f, shininess = 80f)
        }

        // 3. Circular Mitochondrial DNA + Ribosomes in Matrix
        parts += createTorus("mt_dna", 0.12f, 0f, 0f, 0.20f, 0.024f, 32, 12, 0.4f, 0.2f, 0.20f, 0.83f, 0.60f)
        parts += createEllipsoid("mt_dna", -0.35f, 0.06f, 0.05f, 0.045f, 0.045f, 0.045f, 10, 12, 0.20f, 0.88f, 0.65f)
        parts += createEllipsoid("mt_dna", 0.42f, -0.04f, -0.06f, 0.045f, 0.045f, 0.045f, 10, 12, 0.20f, 0.88f, 0.65f)

        // 4. Smooth Outer Membrane Capsule (Translucent shell)
        parts += createEllipsoid("outer_mem", 0f, 0f, 0f, 1.30f, 0.68f, 0.72f, 34, 44, 0.18f, 0.90f, 0.84f, isOuterShell = true, baseAlpha = 0.40f, shininess = 96f)

        return parts
    }

    private fun buildChloroplast(): List<MeshPart> {
        val parts = mutableListOf<MeshPart>()

        // 1. Grana stacks (thylakoid discs stacked vertically)
        val stackCenters = listOf(
            floatArrayOf(-0.55f, 0f, 0.18f),
            floatArrayOf(-0.12f, 0.05f, -0.22f),
            floatArrayOf(0.28f, -0.04f, 0.20f),
            floatArrayOf(0.62f, 0.02f, -0.10f)
        )
        stackCenters.forEach { c ->
            for (d in 0 until 5) {
                val dy = -0.20f + d * 0.10f
                parts += createEllipsoid("grana", c[0], c[1] + dy, c[2], 0.18f, 0.036f, 0.18f, 14, 22, 0.16f, 0.84f, 0.56f, shininess = 62f)
            }
        }

        // 2. Stroma Lamellae bridges & Plastid DNA + Starch granule
        for (i in 0 until stackCenters.size - 1) {
            val a = stackCenters[i]
            val b = stackCenters[i + 1]
            parts += createCylinder("stroma_lamellae", a[0], a[1], a[2], b[0], b[1], b[2], 0.025f, 0.025f, 10, 0.98f, 0.75f, 0.14f)
        }
        parts += createTorus("stroma_lamellae", -0.30f, -0.12f, -0.15f, 0.14f, 0.02f, 24, 10, 0.8f, 0.3f, 0.98f, 0.80f, 0.25f)
        parts += createEllipsoid("stroma_lamellae", 0.10f, 0.18f, 0.05f, 0.12f, 0.08f, 0.10f, 14, 18, 0.92f, 0.94f, 0.96f)

        // 3. Double outer envelope (Translucent lens capsule)
        parts += createEllipsoid("envelope", 0f, 0f, 0f, 1.34f, 0.62f, 0.82f, 34, 44, 0.20f, 0.88f, 0.75f, isOuterShell = true, baseAlpha = 0.36f, shininess = 96f)
        parts += createEllipsoid("envelope", 0f, 0f, 0f, 1.24f, 0.54f, 0.74f, 30, 38, 0.18f, 0.78f, 0.85f, isOuterShell = true, baseAlpha = 0.28f, shininess = 88f)

        return parts
    }

    private fun buildDnaHelix(): List<MeshPart> {
        val parts = mutableListOf<MeshPart>()
        val pts1 = mutableListOf<FloatArray>()
        val pts2 = mutableListOf<FloatArray>()
        val numPts = 64
        val height = 2.3f
        val radius = 0.54f
        val turns = 1.65f

        for (i in 0..numPts) {
            val t = i / numPts.toFloat()
            val y = -height / 2f + t * height
            val angle = t * turns * 2f * PI.toFloat()
            pts1 += floatArrayOf(radius * cos(angle), y, radius * sin(angle))
            pts2 += floatArrayOf(radius * cos(angle + PI.toFloat()), y, radius * sin(angle + PI.toFloat()))
        }
        // Sugar-phosphate backbones
        parts += createTube("backbone", pts1, 0.068f, 14, 0.18f, 0.90f, 0.84f, shininess = 80f)
        parts += createTube("backbone", pts2, 0.068f, 14, 0.22f, 0.68f, 0.98f, shininess = 80f)

        // Base pair rungs (14 pairs)
        val rungs = 14
        for (i in 1 until rungs) {
            val t = i / rungs.toFloat()
            val y = -height / 2f + t * height
            val angle = t * turns * 2f * PI.toFloat()
            val x1 = radius * cos(angle)
            val z1 = radius * sin(angle)
            val x2 = -x1
            val z2 = -z1

            val isAT = i % 2 == 0
            if (isAT) {
                parts += createCylinder("at_pairs", x1, y, z1, 0f, y, 0f, 0.036f, 0.036f, 10, 0.98f, 0.75f, 0.14f)
                parts += createCylinder("at_pairs", 0f, y, 0f, x2, y, z2, 0.036f, 0.036f, 10, 0.18f, 0.90f, 0.84f)
                parts += createEllipsoid("at_pairs", 0f, y, 0f, 0.055f, 0.055f, 0.055f, 10, 12, 1f, 0.92f, 0.5f)
            } else {
                parts += createCylinder("gc_pairs", x1, y, z1, 0f, y, 0f, 0.036f, 0.036f, 10, 0.20f, 0.83f, 0.60f)
                parts += createCylinder("gc_pairs", 0f, y, 0f, x2, y, z2, 0.036f, 0.036f, 10, 0.98f, 0.44f, 0.52f)
                parts += createEllipsoid("gc_pairs", 0f, y, 0f, 0.055f, 0.055f, 0.055f, 10, 12, 0.65f, 0.95f, 0.78f)
            }
        }
        return parts
    }

    private fun buildBiosystemLevels(): List<MeshPart> {
        val parts = mutableListOf<MeshPart>()

        // 1. Core Molecular -> Cell (Gold Glowing Core + Orbiting Atoms)
        parts += createEllipsoid("molecular_cell", 0f, 0f, 0f, 0.28f, 0.28f, 0.28f, 22, 28, 0.98f, 0.75f, 0.15f, shininess = 85f)
        for (i in 0 until 4) {
            val a = i * (PI.toFloat() / 2f)
            parts += createEllipsoid("molecular_cell", 0.42f * cos(a), 0.10f * sin(a * 2f), 0.42f * sin(a), 0.07f, 0.07f, 0.07f, 12, 14, 0.98f, 0.88f, 0.40f)
        }

        // 2. Tissue / Organ / System (Emerald Ring + Organ Nodes)
        parts += createTorus("tissue_organ", 0f, 0f, 0f, 0.62f, 0.045f, 40, 14, 0.55f, 0.2f, 0.20f, 0.83f, 0.60f)
        parts += createEllipsoid("tissue_organ", 0.62f, 0f, 0f, 0.10f, 0.10f, 0.10f, 14, 18, 0.24f, 0.90f, 0.64f)
        parts += createEllipsoid("tissue_organ", -0.62f, 0f, 0f, 0.10f, 0.10f, 0.10f, 14, 18, 0.24f, 0.90f, 0.64f)

        // 3. Organism / Population / Community (Cyan Orbital Ring + Organism Spheres)
        parts += createTorus("population_community", 0f, 0f, 0f, 0.92f, 0.045f, 44, 14, -0.5f, 0.4f, 0.18f, 0.90f, 0.84f)
        for (i in 0 until 3) {
            val a = i * (2f * PI.toFloat() / 3f)
            parts += createEllipsoid("population_community", 0.92f * cos(a), 0.22f * sin(a), 0.78f * sin(a), 0.11f, 0.11f, 0.11f, 14, 18, 0.22f, 0.92f, 0.90f)
        }

        // 4. Ecosystem & Biosphere (Violet Outer Ring + Translucent Biosphere Glass Shell)
        parts += createTorus("ecosystem_biosphere", 0f, 0f, 0f, 1.18f, 0.042f, 48, 14, 0.25f, -0.6f, 0.65f, 0.55f, 0.98f)
        parts += createEllipsoid("ecosystem_biosphere", 0f, 0f, 0f, 1.32f, 1.32f, 1.32f, 34, 44, 0.42f, 0.65f, 0.98f, isOuterShell = true, baseAlpha = 0.32f, shininess = 96f)

        return parts
    }

    // --- Parametric Geometry Builders ---

    private fun createEllipsoid(
        partId: String,
        cx: Float, cy: Float, cz: Float,
        rx: Float, ry: Float, rz: Float,
        stacks: Int, slices: Int,
        r: Float, g: Float, b: Float,
        isOuterShell: Boolean = false,
        baseAlpha: Float = 1.0f,
        shininess: Float = 48.0f
    ): MeshPart {
        val verts = ArrayList<Float>((stacks + 1) * (slices + 1) * 6)
        val inds = ArrayList<Short>(stacks * slices * 6)

        for (i in 0..stacks) {
            val v = i / stacks.toFloat()
            val phi = v * PI.toFloat()
            val sinPhi = sin(phi)
            val cosPhi = cos(phi)

            for (j in 0..slices) {
                val u = j / slices.toFloat()
                val theta = u * 2f * PI.toFloat()
                val cosTheta = cos(theta)
                val sinTheta = sin(theta)

                val x = sinPhi * cosTheta
                val y = cosPhi
                val z = sinPhi * sinTheta

                val px = cx + rx * x
                val py = cy + ry * y
                val pz = cz + rz * z

                // Normal for ellipsoid: (x/rx, y/ry, z/rz) normalized
                var nx = x / rx
                var ny = y / ry
                var nz = z / rz
                val len = sqrt(nx * nx + ny * ny + nz * nz).coerceAtLeast(1e-5f)
                nx /= len; ny /= len; nz /= len

                verts.add(px); verts.add(py); verts.add(pz)
                verts.add(nx); verts.add(ny); verts.add(nz)
            }
        }

        val stride = slices + 1
        for (i in 0 until stacks) {
            for (j in 0 until slices) {
                val a = (i * stride + j).toShort()
                val bIdx = ((i + 1) * stride + j).toShort()
                val c = ((i + 1) * stride + j + 1).toShort()
                val d = (i * stride + j + 1).toShort()
                inds.add(a); inds.add(bIdx); inds.add(d)
                inds.add(bIdx); inds.add(c); inds.add(d)
            }
        }

        return MeshPart(partId, verts.toFloatArray(), inds.toShortArray(), r, g, b, isOuterShell, baseAlpha, shininess)
    }

    private fun createTorus(
        partId: String,
        cx: Float, cy: Float, cz: Float,
        majorR: Float, minorR: Float,
        majorSegs: Int, minorSegs: Int,
        tiltX: Float, tiltZ: Float,
        r: Float, g: Float, b: Float,
        shininess: Float = 56f
    ): MeshPart {
        val verts = ArrayList<Float>((majorSegs + 1) * (minorSegs + 1) * 6)
        val inds = ArrayList<Short>(majorSegs * minorSegs * 6)

        val cosX = cos(tiltX); val sinX = sin(tiltX)
        val cosZ = cos(tiltZ); val sinZ = sin(tiltZ)

        fun rotate(x: Float, y: Float, z: Float): FloatArray {
            val y1 = y * cosX - z * sinX
            val z1 = y * sinX + z * cosX
            val x2 = x * cosZ - y1 * sinZ
            val y2 = x * sinZ + y1 * cosZ
            return floatArrayOf(x2, y2, z1)
        }

        for (i in 0..majorSegs) {
            val u = (i / majorSegs.toFloat()) * 2f * PI.toFloat()
            val cosU = cos(u); val sinU = sin(u)
            for (j in 0..minorSegs) {
                val v = (j / minorSegs.toFloat()) * 2f * PI.toFloat()
                val cosV = cos(v); val sinV = sin(v)

                val lx = (majorR + minorR * cosV) * cosU
                val ly = minorR * sinV
                val lz = (majorR + minorR * cosV) * sinU

                val lnx = cosV * cosU
                val lny = sinV
                val lnz = cosV * sinU

                val rp = rotate(lx, ly, lz)
                val rn = rotate(lnx, lny, lnz)

                verts.add(cx + rp[0]); verts.add(cy + rp[1]); verts.add(cz + rp[2])
                verts.add(rn[0]); verts.add(rn[1]); verts.add(rn[2])
            }
        }

        val stride = minorSegs + 1
        for (i in 0 until majorSegs) {
            for (j in 0 until minorSegs) {
                val a = (i * stride + j).toShort()
                val bIdx = ((i + 1) * stride + j).toShort()
                val c = ((i + 1) * stride + j + 1).toShort()
                val d = (i * stride + j + 1).toShort()
                inds.add(a); inds.add(bIdx); inds.add(d)
                inds.add(bIdx); inds.add(c); inds.add(d)
            }
        }

        return MeshPart(partId, verts.toFloatArray(), inds.toShortArray(), r, g, b, false, 1.0f, shininess)
    }

    private fun createCylinder(
        partId: String,
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        r1: Float, r2: Float,
        segs: Int,
        r: Float, g: Float, b: Float,
        shininess: Float = 48f
    ): MeshPart {
        return createTube(
            partId = partId,
            points = listOf(floatArrayOf(x1, y1, z1), floatArrayOf(x2, y2, z2)),
            radius = (r1 + r2) * 0.5f,
            segs = segs,
            r = r, g = g, b = b,
            shininess = shininess
        )
    }

    private fun createTube(
        partId: String,
        points: List<FloatArray>,
        radius: Float,
        segs: Int,
        r: Float, g: Float, b: Float,
        shininess: Float = 56f
    ): MeshPart {
        val nPts = points.size
        val verts = ArrayList<Float>(nPts * (segs + 1) * 6 + 12)
        val inds = ArrayList<Short>(nPts * segs * 6)

        for (i in 0 until nPts) {
            val p = points[i]
            val prev = if (i > 0) points[i - 1] else points[i]
            val next = if (i < nPts - 1) points[i + 1] else points[i]

            var tx = next[0] - prev[0]
            var ty = next[1] - prev[1]
            var tz = next[2] - prev[2]
            val tLen = sqrt(tx * tx + ty * ty + tz * tz).coerceAtLeast(1e-5f)
            tx /= tLen; ty /= tLen; tz /= tLen

            // Pick helper up vector not parallel to tangent
            val ux = if (kotlin.math.abs(ty) < 0.9f) 0f else 1f
            val uy = if (kotlin.math.abs(ty) < 0.9f) 1f else 0f
            val uz = 0f

            // binormal = tangent x up
            var bx = ty * uz - tz * uy
            var by = tz * ux - tx * uz
            var bz = tx * uy - ty * ux
            val bLen = sqrt(bx * bx + by * by + bz * bz).coerceAtLeast(1e-5f)
            bx /= bLen; by /= bLen; bz /= bLen

            // normal = binormal x tangent
            val nx0 = by * tz - bz * ty
            val ny0 = bz * tx - bx * tz
            val nz0 = bx * ty - by * tx

            for (j in 0..segs) {
                val a = (j / segs.toFloat()) * 2f * PI.toFloat()
                val cosA = cos(a); val sinA = sin(a)
                val nx = cosA * nx0 + sinA * bx
                val ny = cosA * ny0 + sinA * by
                val nz = cosA * nz0 + sinA * bz

                verts.add(p[0] + radius * nx)
                verts.add(p[1] + radius * ny)
                verts.add(p[2] + radius * nz)
                verts.add(nx); verts.add(ny); verts.add(nz)
            }
        }

        val stride = segs + 1
        for (i in 0 until nPts - 1) {
            for (j in 0 until segs) {
                val a = (i * stride + j).toShort()
                val bIdx = ((i + 1) * stride + j).toShort()
                val c = ((i + 1) * stride + j + 1).toShort()
                val d = (i * stride + j + 1).toShort()
                inds.add(a); inds.add(bIdx); inds.add(d)
                inds.add(bIdx); inds.add(c); inds.add(d)
            }
        }

        return MeshPart(partId, verts.toFloatArray(), inds.toShortArray(), r, g, b, false, 1.0f, shininess)
    }
}
