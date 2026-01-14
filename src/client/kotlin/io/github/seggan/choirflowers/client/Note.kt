package io.github.seggan.choirflowers.client

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.nio.ByteBuffer
import javax.sound.sampled.AudioSystem

enum class Note(private val semitoneOffset: Int) {
    C(-9),
    C_SHARP(-8),
    D(-7),
    D_SHARP(-6),
    E(-5),
    F(-4),
    F_SHARP(-3),
    G(-2),
    G_SHARP(-1),
    A(0),
    A_SHARP(1),
    B(2);

    private val audios = Int2ObjectOpenHashMap<ByteBuffer>()

    init {
        val samplingRate = FORMAT.sampleRate.toInt()
        for (octave in 2..5) {
            val offsetSeconds = midiValue(octave) - 36 // 36 is MIDI C2
            val start = offsetSeconds * samplingRate * FORMAT.frameSize
            val len = samplingRate * FORMAT.frameSize
            audios[octave] = allNoteAudio.slice(start, len)
            ChoirFlowersClient.LOGGER.info("Loaded note $this$octave")
        }
    }

    fun midiValue(octave: Int): Int {
        return (octave - 4) * 12 + 69 + semitoneOffset
    }

    private val humanName = name.replace("_SHARP", "#")
    override fun toString() = humanName
}

private val FORMAT = AudioSystem.getAudioInputStream(Note::class.java.getResourceAsStream("/choir.wav")).use { it.format }

private val allNoteAudio =
    AudioSystem.getAudioInputStream(Note::class.java.getResourceAsStream("/choir.wav")).readAllBytes()
        .let(ByteBuffer::wrap)