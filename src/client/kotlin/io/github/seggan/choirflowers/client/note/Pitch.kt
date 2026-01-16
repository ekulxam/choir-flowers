package io.github.seggan.choirflowers.client.note

import io.github.seggan.choirflowers.client.FORMAT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioSystem

data class Pitch(val note: Note, val octave: Int) {

    val midiValue = (octave - 4) * 12 + 69 + note.semitonesFromA

    val audio: ShortArray
        get() = audios.getOrPut(this) {
            val audioInputStream = AudioSystem.getAudioInputStream(Note::class.java.getResource("/notes/choir_$midiValue.wav"))
            val format = audioInputStream.format
            if (!format.matches(FORMAT)) {
                error("Unexpected audio format for note $this$octave (MIDI $midiValue): expected $FORMAT, got $format")
            }
            val buffer = ByteBuffer.wrap(audioInputStream.readAllBytes())
            buffer.order(if (FORMAT.isBigEndian) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN)
            val shortBuffer = buffer.asShortBuffer()
            val shortArray = ShortArray(shortBuffer.limit())
            shortBuffer.get(shortArray)
            shortArray
        }

    init {
        require(octave in 2..5) { "Octave must be between 2 and 5, got $octave" }
    }

    override fun toString() = note.toString() + octave

    companion object {
        private val audios = mutableMapOf<Pitch, ShortArray>()
    }
}
