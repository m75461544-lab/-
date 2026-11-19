package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.tanh

/**
 * 100% offline polyphonic relaxing piano synthesizer using Android AudioTrack.
 * Generates warm ambient chord arpeggios and gentle piano UI chimes without external audio assets.
 */
class PianoAudioEngine {

    private class Voice(
        val freq: Double,
        var ageSamples: Int = 0,
        val amplitude: Double = 0.26,
        val decayRate: Double = 1.65
    )

    private val sampleRate = 22050
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var playbackJob: Job? = null
    private var audioTrack: AudioTrack? = null

    private val voicesLock = Any()
    private val activeVoices = mutableListOf<Voice>()

    @Volatile var isMusicEnabled: Boolean = true
    @Volatile var isSfxEnabled: Boolean = true
    @Volatile var volume: Float = 0.55f

    private var isPausedForLifecycle = false

    // Calm ambient progression in A minor 9 / F major 7 / C major 9 / G6 (~64 BPM)
    private val ambientArpeggioMidi = intArrayOf(
        // Am9
        57, 64, 67, 71, 72, 76, 71, 64,
        // Fmaj7
        53, 60, 64, 69, 72, 76, 69, 60,
        // Cmaj9
        48, 55, 62, 64, 67, 71, 67, 62,
        // G6/E
        52, 59, 62, 67, 71, 74, 67, 59
    )

    fun start() {
        if (playbackJob?.isActive == true) return
        isPausedForLifecycle = false
        playbackJob = scope.launch {
            val minBuf = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(2048)

            val track = try {
                AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBuf * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
            } catch (_: Throwable) {
                return@launch
            }

            audioTrack = track
            try {
                track.play()
            } catch (_: Throwable) {
                return@launch
            }

            val chunkSize = 512
            val pcm = ShortArray(chunkSize)
            val samplesPerStep = (sampleRate * 0.72).toInt() // ~0.72s per calm arpeggio note
            var sampleCounter = 0
            var stepIndex = 0
            val maxSamplesPerVoice = sampleRate * 4

            while (isActive) {
                if (isPausedForLifecycle || (!isMusicEnabled && activeVoicesEmpty())) {
                    pcm.fill(0)
                    try {
                        track.write(pcm, 0, pcm.size)
                    } catch (_: Throwable) {
                        break
                    }
                    kotlinx.coroutines.delay(80)
                    continue
                }

                for (i in 0 until chunkSize) {
                    if (isMusicEnabled && !isPausedForLifecycle) {
                        if (sampleCounter % samplesPerStep == 0) {
                            val midi = ambientArpeggioMidi[stepIndex % ambientArpeggioMidi.size]
                            val freq = midiToFreq(midi)
                            addVoice(freq, amplitude = 0.22, decayRate = 1.45)
                            // Gentle bass root on first note of each bar
                            if (stepIndex % 8 == 0) {
                                addVoice(midiToFreq(midi - 12), amplitude = 0.16, decayRate = 0.95)
                            }
                            stepIndex++
                        }
                        sampleCounter++
                    }

                    var mix = 0.0
                    synchronized(voicesLock) {
                        val iter = activeVoices.iterator()
                        while (iter.hasNext()) {
                            val v = iter.next()
                            val t = v.ageSamples.toDouble() / sampleRate
                            // Soft attack (16ms) + warm exponential decay
                            val attack = (t / 0.016).coerceAtMost(1.0)
                            val env = attack * exp(-t * v.decayRate)
                            val w = 2.0 * PI * v.freq * t
                            // Warm piano harmonic profile (rich fundamental, gentle upper partials)
                            val wave = sin(w) * 0.72 +
                                sin(w * 2.0) * 0.20 * exp(-t * 1.8) +
                                sin(w * 3.0) * 0.06 * exp(-t * 2.8) +
                                sin(w * 4.0) * 0.02 * exp(-t * 3.5)
                            mix += wave * env * v.amplitude
                            v.ageSamples++
                            if (v.ageSamples > maxSamplesPerVoice || env < 0.002) {
                                iter.remove()
                            }
                        }
                    }

                    val scaled = tanh(mix * volume.toDouble() * 1.45)
                    pcm[i] = (scaled * Short.MAX_VALUE * 0.75).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                try {
                    track.write(pcm, 0, pcm.size)
                } catch (_: Throwable) {
                    break
                }
            }

            try {
                track.pause()
                track.flush()
                track.release()
            } catch (_: Throwable) {
            }
            audioTrack = null
        }
    }

    fun pauseForLifecycle() {
        isPausedForLifecycle = true
    }

    fun resumeForLifecycle() {
        isPausedForLifecycle = false
    }

    fun playCorrectChime() {
        if (!isSfxEnabled) return
        scope.launch {
            // Ascending warm major 9th piano sparkle
            addVoice(midiToFreq(72), 0.26, 2.5)
            kotlinx.coroutines.delay(70)
            addVoice(midiToFreq(76), 0.24, 2.3)
            kotlinx.coroutines.delay(70)
            addVoice(midiToFreq(79), 0.26, 2.0)
        }
    }

    fun playWrongChime() {
        if (!isSfxEnabled) return
        scope.launch {
            // Soft muted descending piano notes
            addVoice(midiToFreq(62), 0.22, 3.2)
            kotlinx.coroutines.delay(90)
            addVoice(midiToFreq(60), 0.20, 2.8)
        }
    }

    fun release() {
        playbackJob?.cancel()
        playbackJob = null
    }

    private fun activeVoicesEmpty(): Boolean = synchronized(voicesLock) { activeVoices.isEmpty() }

    private fun addVoice(freq: Double, amplitude: Double, decayRate: Double) {
        synchronized(voicesLock) {
            if (activeVoices.size >= 12) {
                activeVoices.removeAt(0)
            }
            activeVoices.add(Voice(freq = freq, amplitude = amplitude, decayRate = decayRate))
        }
    }

    private fun midiToFreq(midi: Int): Double = 440.0 * Math.pow(2.0, (midi - 69) / 12.0)
}
