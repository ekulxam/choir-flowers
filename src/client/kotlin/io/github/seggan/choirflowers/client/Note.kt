package io.github.seggan.choirflowers.client

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.nio.ByteBuffer
import javax.sound.sampled.AudioFormat
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

    private val audios = Int2ObjectOpenHashMap<Pair<AudioFormat, ByteBuffer>>()

    init {
        for (octave in 2..5) {
            val midi = midiValue(octave)
            val audioInputStream =
                AudioSystem.getAudioInputStream(Note::class.java.getResource("/notes/choir_$midi.wav"))
            val format = audioInputStream.format
            val buffer = ByteBuffer.wrap(audioInputStream.readAllBytes()).asReadOnlyBuffer()
            audios[octave] = format to buffer
        }
    }

    fun midiValue(octave: Int): Int {
        return (octave - 4) * 12 + 69 + semitoneOffset
    }

    fun getAudioForOctave(octave: Int): Pair<AudioFormat, ByteBuffer> {
        return audios[octave] ?: error("No audio for note $this in octave $octave")
    }

    override fun toString() = name.replace("_SHARP", "#")
}