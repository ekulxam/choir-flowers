#!/usr/bin/env kotlin

import com.jogamp.common.nio.ByteBufferInputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ThreadLocalRandom
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.math.*

val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100f, 16, 1, 2, 44100f, false)

val detunes = intArrayOf(-12, -7, -3, 0, 2, 6, 11)

fun generateChoirNote(f0: Double, durationSeconds: Double): FloatArray {
    val data = FloatArray((format.sampleRate * durationSeconds).toInt())
    val ts = FloatArray(data.size) { i -> i / format.sampleRate }
    val drifts = DoubleArray(data.size) { i ->
        val t = ts[i]
        1 + 0.0002 * sin(PI * 2 * 0.25 * t)
    }
    for (detune in detunes) {
        val detunedF0 = f0 * 2.0.pow(detune / 1200.0)
        val basePhase = ThreadLocalRandom.current().nextDouble() * PI * 2
        var n = 0
        val maxHarmonic = (format.sampleRate / (2 * detunedF0)).toInt()
        while (++n < maxHarmonic) {
            val harmonic = n * detunedF0
            val amp = formantEnvelope(harmonic) / n
            val phase = basePhase + ThreadLocalRandom.current().nextDouble() * PI * 2
            for (i in data.indices) {
                val freq = harmonic * drifts[i]
                data[i] += (amp * sin(PI * 2 * freq * ts[i] + phase)).toFloat()
            }
        }
    }

    val max = data.maxOf { abs(it) }
    if (max > 0) {
        for (i in data.indices) {
            data[i] /= max
        }
    }

    return data
}

val formants = listOf(
    700 to 120,
    1100 to 150,
    2500 to 200
)

fun formantEnvelope(frequency: Double): Double {
    var amp = 0.0
    for ((formantFreq, bandwidth) in formants) {
        val x = (frequency - formantFreq) / bandwidth
        amp += exp(-0.5 * x * x)
    }
    return amp
}

fun generateLoopableChoirSection(
    f0: Double,
    durationSeconds: Double,
    crossfadeSeconds: Double
): FloatArray {
    val sampleRate = format.sampleRate.toInt()
    val totalSamples = (durationSeconds * sampleRate).toInt()
    val crossfadeSamples = (crossfadeSeconds * sampleRate).toInt()

    // Generate slightly longer section for overlap
    val section = generateChoirNote(f0, durationSeconds + crossfadeSeconds)

    // Apply overlap-add crossfade
    for (i in 0 until crossfadeSamples) {
        val fadeIn = i.toFloat() / crossfadeSamples
        val fadeOut = 1f - fadeIn
        section[i] = section[i] * fadeIn + section[(totalSamples + i).coerceIn(section.indices)] * fadeOut
    }

    // Keep only the first 'durationSeconds' samples
    return section.copyOfRange(0, totalSamples)
}


fun floatToPcm16(input: FloatArray): ByteBuffer {
    val buffer = ByteBuffer.allocate(input.size * 2).order(ByteOrder.LITTLE_ENDIAN)
    for (sample in input) {
        val pcm16 = (sample.coerceIn(-1f, 1f) * Short.MAX_VALUE).toInt().toShort()
        buffer.putShort(pcm16)
    }
    buffer.flip()
    return buffer
}

// BEGIN MAIN

var midi = 36
while (midi <= 83) {
    val f0 = 440.0 * 2.0.pow((midi - 69) / 12.0)
    val note = generateLoopableChoirSection(f0, 1.0, 0.1)
    val pcmData = floatToPcm16(note)
    pcmData.rewind()
    val stream = AudioInputStream(ByteBufferInputStream(pcmData), format, pcmData.limit() / format.frameSize.toLong())
    AudioSystem.write(stream, AudioFileFormat.Type.WAVE, File("src/main/resources/notes/choir_$midi.wav"))
    println("Generated MIDI ${midi++}")
}