package com.example.bio3d

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Bio3DRenderer : GLSurfaceView.Renderer {

    private class GpuMesh(
        val part: MeshPart,
        val vertexBuffer: FloatBuffer,
        val indexBuffer: ShortBuffer,
        val indexCount: Int
    )

    @Volatile var rotationX: Float = 18f
    @Volatile var rotationY: Float = -24f
    @Volatile var zoomDistance: Float = 3.85f
    @Volatile var xrayLevel: Float = 0.65f // 0 = opaque shell, 1 = crystal-clear X-Ray shell
    @Volatile var highlightedPartId: String? = null
    @Volatile var autoRotate: Boolean = true

    private var pendingModelType: BioModelType? = BioModelType.EUKARYOTIC_CELL
    private var gpuMeshes: List<GpuMesh> = emptyList()

    private var program = 0
    private var aPosLoc = 0
    private var aNormLoc = 0
    private var uMvpLoc = 0
    private var uModelLoc = 0
    private var uColorLoc = 0
    private var uAlphaLoc = 0
    private var uHighlightLoc = 0
    private var uShininessLoc = 0
    private var uEyePosLoc = 0

    private val projMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private val tempMatrix = FloatArray(16)

    private var pulsePhase = 0f

    fun setModelType(type: BioModelType) {
        pendingModelType = type
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.027f, 0.051f, 0.078f, 1.0f) // #070D14 Obsidian Lab
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glCullFace(GLES20.GL_BACK)

        val vShader = compileShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER)
        val fShader = compileShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vShader)
            GLES20.glAttachShader(it, fShader)
            GLES20.glLinkProgram(it)
        }

        aPosLoc = GLES20.glGetAttribLocation(program, "aPos")
        aNormLoc = GLES20.glGetAttribLocation(program, "aNormal")
        uMvpLoc = GLES20.glGetUniformLocation(program, "uMVP")
        uModelLoc = GLES20.glGetUniformLocation(program, "uModel")
        uColorLoc = GLES20.glGetUniformLocation(program, "uColor")
        uAlphaLoc = GLES20.glGetUniformLocation(program, "uAlpha")
        uHighlightLoc = GLES20.glGetUniformLocation(program, "uHighlight")
        uShininessLoc = GLES20.glGetUniformLocation(program, "uShininess")
        uEyePosLoc = GLES20.glGetUniformLocation(program, "uEyePos")

        loadPendingModelIfNeeded()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height.coerceAtLeast(1)
        Matrix.perspectiveM(projMatrix, 0, 42f, ratio, 0.3f, 30f)
    }

    override fun onDrawFrame(gl: GL10?) {
        loadPendingModelIfNeeded()

        if (autoRotate) {
            rotationY = (rotationY + 0.35f) % 360f
        }
        pulsePhase = (pulsePhase + 0.07f) % (2f * Math.PI.toFloat())

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glUseProgram(program)

        val camZ = zoomDistance.coerceIn(2.1f, 6.8f)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0.15f, camZ, 0f, 0f, 0f, 0f, 1f, 0f)
        GLES20.glUniform3f(uEyePosLoc, 0f, 0.15f, camZ)

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(modelMatrix, 0, rotationX, 1f, 0f, 0f)
        Matrix.rotateM(modelMatrix, 0, rotationY, 0f, 1f, 0f)

        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, tempMatrix, 0)

        GLES20.glUniformMatrix4fv(uMvpLoc, 1, false, mvpMatrix, 0)
        GLES20.glUniformMatrix4fv(uModelLoc, 1, false, modelMatrix, 0)

        val activeHighlight = highlightedPartId
        val pulse = 0.5f + 0.5f * kotlin.math.sin(pulsePhase)

        // Pass 1: Opaque internal structures
        GLES20.glDisable(GLES20.GL_BLEND)
        GLES20.glDepthMask(true)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        for (mesh in gpuMeshes) {
            if (mesh.part.isOuterShell) continue
            val isHighlighted = activeHighlight != null && mesh.part.partId == activeHighlight
            val dimOthers = activeHighlight != null && !isHighlighted
            val hlStrength = if (isHighlighted) 0.35f + 0.35f * pulse else 0f
            val colorScale = if (dimOthers) 0.42f else 1.0f
            drawMesh(
                mesh = mesh,
                alpha = 1.0f,
                highlight = hlStrength,
                colorScale = colorScale
            )
        }

        // Pass 2: Translucent outer membranes / shells (X-Ray Cutaway)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glDepthMask(false)
        for (mesh in gpuMeshes) {
            if (!mesh.part.isOuterShell) continue
            val isHighlighted = activeHighlight != null && mesh.part.partId == activeHighlight
            // When xrayLevel = 0 -> opaque-ish (0.90), when xrayLevel = 1 -> crystal clear (0.14)
            val shellAlpha = (mesh.part.baseAlpha * (1.25f - xrayLevel * 0.92f))
                .coerceIn(0.12f, 0.92f)
            val finalAlpha = if (isHighlighted) (shellAlpha + 0.28f).coerceAtMost(0.85f) else shellAlpha
            val hlStrength = if (isHighlighted) 0.45f + 0.25f * pulse else 0f
            drawMesh(
                mesh = mesh,
                alpha = finalAlpha,
                highlight = hlStrength,
                colorScale = 1.0f
            )
        }
        GLES20.glDepthMask(true)
        GLES20.glDisable(GLES20.GL_BLEND)
    }

    private fun drawMesh(mesh: GpuMesh, alpha: Float, highlight: Float, colorScale: Float) {
        val p = mesh.part
        GLES20.glUniform3f(uColorLoc, p.r * colorScale, p.g * colorScale, p.b * colorScale)
        GLES20.glUniform1f(uAlphaLoc, alpha)
        GLES20.glUniform1f(uHighlightLoc, highlight)
        GLES20.glUniform1f(uShininessLoc, p.shininess)

        mesh.vertexBuffer.position(0)
        GLES20.glEnableVertexAttribArray(aPosLoc)
        GLES20.glVertexAttribPointer(aPosLoc, 3, GLES20.GL_FLOAT, false, 24, mesh.vertexBuffer)

        mesh.vertexBuffer.position(3)
        GLES20.glEnableVertexAttribArray(aNormLoc)
        GLES20.glVertexAttribPointer(aNormLoc, 3, GLES20.GL_FLOAT, false, 24, mesh.vertexBuffer)

        mesh.indexBuffer.position(0)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.indexCount, GLES20.GL_UNSIGNED_SHORT, mesh.indexBuffer)

        GLES20.glDisableVertexAttribArray(aPosLoc)
        GLES20.glDisableVertexAttribArray(aNormLoc)
    }

    private fun loadPendingModelIfNeeded() {
        val type = pendingModelType ?: return
        pendingModelType = null
        val parts = Bio3DMeshBuilder.buildModel(type)
        gpuMeshes = parts.map { part ->
            val vb = ByteBuffer.allocateDirect(part.vertices.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .apply { put(part.vertices); position(0) }
            val ib = ByteBuffer.allocateDirect(part.indices.size * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .apply { put(part.indices); position(0) }
            GpuMesh(part, vb, ib, part.indices.size)
        }
    }

    private fun compileShader(type: Int, code: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, code)
            GLES20.glCompileShader(shader)
        }
    }

    companion object {
        private const val VERTEX_SHADER = """
            uniform mat4 uMVP;
            uniform mat4 uModel;
            attribute vec3 aPos;
            attribute vec3 aNormal;
            varying vec3 vWorldPos;
            varying vec3 vWorldNormal;
            void main() {
                vec4 wp = uModel * vec4(aPos, 1.0);
                vWorldPos = wp.xyz;
                vWorldNormal = normalize(mat3(uModel) * aNormal);
                gl_Position = uMVP * vec4(aPos, 1.0);
            }
        """

        private const val FRAGMENT_SHADER = """
            precision mediump float;
            uniform vec3 uColor;
            uniform float uAlpha;
            uniform float uHighlight;
            uniform float uShininess;
            uniform vec3 uEyePos;
            varying vec3 vWorldPos;
            varying vec3 vWorldNormal;

            void main() {
                vec3 N = normalize(vWorldNormal);
                vec3 V = normalize(uEyePos - vWorldPos);

                // Key warm directional light + cool fill rim light
                vec3 L1 = normalize(vec3(2.2, 3.0, 2.5));
                vec3 L2 = normalize(vec3(-2.5, -1.5, 1.4));

                float wrapDiff1 = max(dot(N, L1) * 0.72 + 0.28, 0.0);
                float diff2 = max(dot(N, L2), 0.0) * 0.35;

                vec3 H1 = normalize(L1 + V);
                float spec = pow(max(dot(N, H1), 0.0001), uShininess) * 0.65;

                // Fresnel rim glow for glass membrane & bioluminescent depth
                float ndotv = clamp(dot(N, V), 0.0, 1.0);
                float fresnel = pow(max(1.0 - ndotv, 0.0001), 2.6);

                vec3 ambient = uColor * 0.24;
                vec3 diffuse = uColor * (wrapDiff1 * 0.88 + diff2);
                vec3 rimColor = mix(uColor, vec3(0.18, 0.92, 0.88), 0.55) * fresnel * 0.85;
                vec3 hlGlow = vec3(1.0, 0.92, 0.60) * (uHighlight * (0.45 + fresnel * 0.75));

                vec3 finalRgb = ambient + diffuse + vec3(spec) + rimColor + hlGlow;
                float outAlpha = clamp(uAlpha + fresnel * (1.0 - uAlpha) * 0.65, 0.08, 1.0);
                gl_FragColor = vec4(finalRgb, outAlpha);
            }
        """
    }
}
